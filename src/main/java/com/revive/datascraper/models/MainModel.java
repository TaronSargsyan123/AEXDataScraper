package com.revive.datascraper.models;

import com.revive.datascraper.calculators.InterstateDataCalculator;
import com.revive.datascraper.calculators.MarketsCalculator;
import com.revive.datascraper.calculators.PaymentCalculator;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

public class MainModel {

    public File calculate(ArrayList<String> messages, ArrayList<File> files) throws IOException, InvalidFormatException {
        File finelFile = null;
        File interstateFile = null;
        File longTermMarketFile = null;
        File longTermMarketReportFile = null;
        ArrayList<File> marketFiles = new ArrayList<>();
        ArrayList<File> paymentFiles = new ArrayList<>();



        for (String message: messages){
            String[] parts = message.split("&");
            String command = parts[0];

            if (Objects.equals(command, "interstate")){
                interstateFile = getFileByName(parts[1], files);
            } else if (Objects.equals(command, "finalBlankFormFile")){
                finelFile = getFileByName(parts[1], files);
            } else if (Objects.equals(command, "longTermMarket")) {
                longTermMarketFile = getFileByName(parts[1], files);
            } else if (Objects.equals(command, "longTermMarketReport")) {
                longTermMarketReportFile = getFileByName(parts[1], files);
            } else if (Objects.equals(command, "marketsMultiple")){

                for (int i = 1; i < parts.length; i++) {
                    File file  = getFileByName(parts[i], files);
                    marketFiles.add(file);
                }

            } else if (Objects.equals(command, "paymentMultiple")){

                for (int i = 1; i < parts.length; i++) {
                    File file  = getFileByName(parts[i], files);
                    paymentFiles.add(file);
                }

            }

        }


        try {
            PaymentCalculator paymentController = new PaymentCalculator();
            paymentController.calculateAndWriteData(paymentFiles, finelFile);
        }catch (Exception e){}

        try {
            MarketsCalculator marketsController = new MarketsCalculator();
            marketsController.calculateAndWriteData(marketFiles, finelFile); // , "C:/Users/taron/Desktop/Template 23_01_2024 00_03_57.xlsx"
        }catch (Exception e){}

        try {
            MarketsCalculator marketsController = new MarketsCalculator();
            marketsController.calculateAndWriteLongLastingShop(longTermMarketReportFile, longTermMarketFile, finelFile);
        }catch (Exception e){}

        try {
            InterstateDataCalculator.calculateAndWriteData(interstateFile, finelFile);
        }catch (Exception e){}



        return finelFile;
    }

    private File getFileByName(String name, ArrayList<File> files){
        File response = null;
        for (File file: files){
            if (file.getName().equals(name)){
                response = file;
            }
        }

        return response;
    }

    public static void clearFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        clearFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
    }


}
