package com.revive.datascraper.calculators;



import com.revive.datascraper.Constants;
import com.revive.datascraper.ExcelModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PaymentCalculator implements CalculatorInterface {
    private double surplusCount = 0;
    private double surplusPrice = 0;

    private double shortageCount = 0;
    private double shortagePrice = 0;

    String id = null;

    File finalFile = null;

    @Override
    public void calculateAndWrite(ArrayList<File> files, File fileFinal){
        int surplusCountColumn = 20;
        int surplusPriceColumn = 21;
        int shortageCountColumn = 22;
        int shortagePriceColumn = 23;
        this.finalFile = fileFinal;

        ExcelModel excelModel = new ExcelModel(fileFinal);


        for (File file: files) {
            try {
                getData(file);
                int rowForImport = excelModel.getIdSequence(id) + Constants.startCountingRow;

                excelModel.writeCell(surplusCountColumn, rowForImport, surplusCount, fileFinal);
                excelModel.writeCell(surplusPriceColumn, rowForImport, surplusPrice, fileFinal);
                excelModel.writeCell(shortageCountColumn, rowForImport, shortageCount, fileFinal);
                excelModel.writeCell(shortagePriceColumn, rowForImport, shortagePrice, fileFinal);
            }catch (Exception e){
                //e.printStackTrace();
            }
        }
    }

    private void getData(File file){

        ExcelModel excelModel = new ExcelModel(finalFile);

        id = excelModel.getPaymentId(file);

        for (int i = 10; i < 25; i++) {
            try {
                String serviceName = (String) excelModel.readDataFromCell(1, i, file);
                serviceName = serviceName.trim();
                if (serviceName != "" || serviceName != null){
                    if (serviceName.length() == 16){// Ավելցուկի վաճառք
                        String[] parts = serviceName.split(" ");
                        if (parts[0].length() == 9){ // remove Շուկայի oպերատոր
                            surplusCount = (double) excelModel.readDataFromCell(5, i, file);
                            surplusPrice = (double) excelModel.readDataFromCell(6, i, file);
                        }
                    }

                    if (serviceName.length() == 15){// Պակասորդի գնում
                        shortageCount = (double) excelModel.readDataFromCell(5, i, file);
                        shortagePrice = (double) excelModel.readDataFromCell(6, i, file);
                    }
                }else {}
            }catch (Exception e){
//                e.printStackTrace();
            }
        }

        System.out.println();
        System.out.println("ID - " + id);
        System.out.println();
        System.out.println("Ավելցուկի վաճառք - " + surplusCount);
        System.out.println("Ավելցուկի վաճառք gin - " + surplusPrice);
        System.out.println();
        System.out.println("Պակասորդի գնում - " + shortageCount);
        System.out.println("Պակասորդի գնում price - " + shortagePrice);


    }


}
