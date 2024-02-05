package com.revive.datascraper.calculators;


import com.revive.datascraper.Constants;
import com.revive.datascraper.ExcelModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InterstateDataCalculator implements CalculatorInterface {

    @Override
    public void calculateAndWrite(ArrayList<File> fileArrayList, File fileFinal) throws IOException { //TODO add multiple files uploading functionality

        try {
            File file = fileArrayList.get(0);
            ExcelModel excelModel = new ExcelModel(fileFinal);
            Double iranImportedSum = excelModel.getSumOfColumn(Constants.columnForIranImportedEnergyInInterstateDocument, Constants.rowForIranImportedEnergyInInterstateDocument, file);
            Double iranExportedSum = excelModel.getSumOfColumn(Constants.columnForIranExportedEnergyInInterstateDocument, Constants.rowForIranExportedEnergyInInterstateDocument, file);

            Double georgiaImportedSum = excelModel.getSumOfColumn(Constants.columnForGeorgiaImportedEnergyInInterstateDocument, Constants.rowForGeorgiaImportedEnergyInInterstateDocument, file);
            Double georgiaExportedSum = excelModel.getSumOfColumn(Constants.columnForGeorgiaExportedEnergyInInterstateDocument, Constants.rowForGeorgiaExportedEnergyInInterstateDocument, file);

            int rowForImport = excelModel.getIdSequence(excelModel.readId(file)) + Constants.startCountingRow; //ok

            int columnForIranImportedSum = Constants.columnForIranImportedSum; //column for Iran imported energy in final document
            int columnForIranExportedSum = Constants.columnForIranExportedSum; //column for Iran exported energy in final document
            int columnForGeorgiaImportedSum = Constants.columnForGeorgiaImportedSum; //column for Georgia imported energy in final document
            int columnForGeorgiaExportedSum = Constants.columnForGeorgiaExportedSum; //column for Georgia exported energy in final document

            excelModel.writeCell(columnForIranImportedSum, rowForImport, iranImportedSum, fileFinal);
            excelModel.writeCell(columnForIranExportedSum, rowForImport, iranExportedSum, fileFinal);
            excelModel.writeCell(columnForGeorgiaImportedSum, rowForImport, georgiaImportedSum, fileFinal);
            excelModel.writeCell(columnForGeorgiaExportedSum, rowForImport, georgiaExportedSum, fileFinal);

        }catch (Exception e){
             e.printStackTrace();
        }
    }

}
