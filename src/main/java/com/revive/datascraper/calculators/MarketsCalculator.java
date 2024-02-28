package com.revive.datascraper.calculators;

import com.revive.datascraper.Constants;
import com.revive.datascraper.ExcelModel;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MarketsCalculator  {

    public void calculateAndWrite(ArrayList<File> files, File fileFinal, File DBMReportFile) throws IOException {
        ExcelModel excelModel = new ExcelModel(fileFinal);


        for (File file : files) {

            String id = excelModel.readId(file);
            int rowForImport = excelModel.getIdSequence(id) + Constants.startCountingRow;

            int columnCount = excelModel.getColumnCount(file);
            int maxColumnForCheck = columnCount - 5;
            int row = 11;
            int startCountingRow = 13;

            double dayBeforeMarketSurplusSum = 0;
            double regulatedContractMarketSurplusSum = 0;
            double unregulatedMarketSurplusSum = 0;
            double unregulatedInterstateMarketSurplusSum = 0;

            double dayBeforeMarketShortageSum = 0;
            double regulatedContractMarketShortageSum = 0;
            double unregulatedMarketShortageSum = 0;
            double unregulatedInterstateMarketShortageSum = 0;

            int DBMReportFileDateColumn = 1;
            int DBMReportFileTimeColumn = 3;
            int DBMReportFilePriceColumn = 7;

            int marketsFileDateColumn = 1;
            int marketsTimeColumn = 3;

            double surplusPriceSum = 0;
            double shortagePriceSum = 0;

            boolean flagDBM = false;


//            System.out.println(maxColumnForCheck);

            if (columnCount > 8) {

                for (int i = 4; i < maxColumnForCheck; i = i + 2) {

//                    System.out.println(i);

                    String cell = (String) excelModel.readDataFromCell(i, row, file);
//                    System.out.println(cell);
                    String[] parts = cell.split(" ");

                    if (cell.length() == 13 && parts[0].length() == 4) { //ԿՈՒՊ Բաղադրիչ
                        regulatedContractMarketSurplusSum = excelModel.getSumOfColumn(i, startCountingRow, file);
                        regulatedContractMarketShortageSum = excelModel.getSumOfColumn(i + 1, startCountingRow, file);
                    }

                    if (cell.length() == 3) { //ՕԱՇ
                        dayBeforeMarketSurplusSum = excelModel.getSumOfColumn(i, startCountingRow, file);
                        dayBeforeMarketShortageSum = excelModel.getSumOfColumn(i + 1, startCountingRow, file);

                        if (flagDBM) {

                            for (int j = startCountingRow; j < excelModel.getRowsCount(file); j++) {
                                String date = (String) excelModel.readDataFromCell(marketsFileDateColumn, j, file);
                                String time = (String) excelModel.readDataFromCell(marketsTimeColumn, j, file);

                                String dateReport = (String) excelModel.readDataFromCell(DBMReportFileDateColumn, j - 4, DBMReportFile);
                                String timeReport = (String) excelModel.readDataFromCell(DBMReportFileTimeColumn, j - 4, DBMReportFile);

                                double price = 0;

                                double DBMSurplus = (double) excelModel.readDataFromCell(i, j, file);
                                double DBMShortage = (double) excelModel.readDataFromCell(i + 1, j, file);

                                if (Objects.equals(timeReport, time) && Objects.equals(dateReport, date)) {
                                    try {
                                        price = (double) excelModel.readDataFromCell(DBMReportFilePriceColumn, j - 4, DBMReportFile);
//                                        System.out.println("Price: " + price);
//                                        surplusPriceSum += DBMSurplus * price;
//                                        shortagePriceSum += DBMShortage * price;
//                                        System.out.println("Surplus Price Sum: " + surplusPriceSum);
//                                        System.out.println("Shortage Price Sum: " + shortagePriceSum);
                                    } catch (Exception ignored) {
                                    }

                                }

                            }

                        }

                    }

                    if (cell.length() == 17) { //ՉՈՒՊ տեղական բաղ․
                        unregulatedMarketSurplusSum = excelModel.getSumOfColumn(i, startCountingRow, file);
                        unregulatedMarketShortageSum = excelModel.getSumOfColumn(i + 1, startCountingRow, file);
                    }

                    if (cell.length() == 20) { //ՉՈՒՊ միջպետական բաղ․
                        unregulatedInterstateMarketSurplusSum = excelModel.getSumOfColumn(i, startCountingRow, file);
                        unregulatedInterstateMarketShortageSum = excelModel.getSumOfColumn(i + 1, startCountingRow, file);
                    }


                }

            }

            excelModel.writeCell(14, rowForImport, dayBeforeMarketSurplusSum, fileFinal);
            excelModel.writeCell(16, rowForImport, dayBeforeMarketShortageSum, fileFinal);

            excelModel.writeCell(10, rowForImport, regulatedContractMarketSurplusSum, fileFinal);
            excelModel.writeCell(12, rowForImport, regulatedContractMarketShortageSum, fileFinal);

            excelModel.writeCell(18, rowForImport, unregulatedMarketSurplusSum + unregulatedInterstateMarketSurplusSum, fileFinal);
            excelModel.writeCell(19, rowForImport, unregulatedMarketShortageSum + unregulatedInterstateMarketShortageSum, fileFinal);

//            System.out.println();
//            System.out.println("ՕԱՇ Բաղադրիչ gnvac - " + dayBeforeMarketSurplusSum);
//            System.out.println("ՕԱՇ Բաղադրիչ vacharvac - " + dayBeforeMarketShortageSum);
//            System.out.println();
//            System.out.println("ԿՈՒՊ Բաղադրիչ gnvac - " + regulatedContractMarketSurplusSum);
//            System.out.println("ԿՈՒՊ Բաղադրիչ vacharvac - " + regulatedContractMarketShortageSum);
//            System.out.println();
//            System.out.println("ՉՈՒՊ Բաղադրիչ gnvac - " + unregulatedMarketSurplusSum + unregulatedInterstateMarketSurplusSum);
//            System.out.println("ՉՈՒՊ Բաղադրիչ vacharvac - " + unregulatedMarketShortageSum + unregulatedInterstateMarketShortageSum);
//            System.out.println();

        }



    }

    public void calculateAndWriteLongLastingShop(File fileForTariff, File initialFile, File fileFinal) throws IOException, InvalidFormatException {
//        ExcelModel excelModel = new ExcelModel(fileFinal);//210004
//        //String id = excelModel.readId(initialFile);
//        int row = excelModel.getRowFromReport(fileForTariff, "210004");//id
//        double value = (double) excelModel.readDataFromCell(2, row, fileForTariff);
//        double sum = excelModel.getSumOfColumn(4, 13, initialFile);
//
////        System.out.println(value);
////        System.out.println(sum);
//
//
//        int rowForImport = excelModel.getIdSequence("210004") + Constants.startCountingRow;
//
//        excelModel.writeCell(8, rowForImport, sum, fileFinal);
//        excelModel.writeCell(9, rowForImport, sum * value, fileFinal);

        ExcelModel excelModel = new ExcelModel(fileFinal);
        String id = excelModel.readId(initialFile);
        int row = excelModel.getRowFromReport(fileForTariff, "210004");
        double value = (double) excelModel.readDataFromCell(2, row, fileForTariff);
        double sum = excelModel.getSumOfColumn(4, 13, initialFile);

        int rowForImport = excelModel.getIdSequence("210004") + Constants.startCountingRow;

        excelModel.writeCell(8, rowForImport, sum, fileFinal);
        excelModel.writeCell(9, rowForImport, sum * value, fileFinal);


    }

    public void calculateAndWriteRCMPurchasedPrice (ArrayList<File> files, File reportFile, File fileFinal) throws IOException, InvalidFormatException {
        ExcelModel excelModel = new ExcelModel(fileFinal);

        String nuclearPowerPlantID = "220237";
        String hydroPowerStationID = "220235";

        double nuclearPowerPlantTariff =  excelModel.getRowDataFromReport(reportFile, nuclearPowerPlantID);
        double hydroPowerStationTariff =  excelModel.getRowDataFromReport(reportFile, hydroPowerStationID);

        double nuclearPowerPlantSum = 0;
        double hydroPowerStationSum = 0;

        for (File file: files) {


            String id = excelModel.getMarketPriceId(file);
            int columnCount = excelModel.getColumnCount(file);

            for (int i = 0; i < columnCount; i++) {

                try {
                    String cell = (String) excelModel.readDataFromCell(i, 15, file);


                    if (cell.length() == 89){
                        nuclearPowerPlantSum = excelModel.getSumOfColumn(i, 16, file);
                    }else if (cell.length() == 69){
                        hydroPowerStationSum = excelModel.getSumOfColumn(i, 16, file);
                    }
                }catch (Exception exception){}

            }
            int rowForImport = excelModel.getIdSequence(id) + Constants.startCountingRow;

            double finalValue = nuclearPowerPlantSum * nuclearPowerPlantTariff + hydroPowerStationSum * hydroPowerStationTariff;

            excelModel.writeCell(11, rowForImport, finalValue, fileFinal);



        }

    }




}
