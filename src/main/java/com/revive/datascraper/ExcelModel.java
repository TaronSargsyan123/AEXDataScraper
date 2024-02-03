package com.revive.datascraper;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class ExcelModel {

    private int idCount = 0;
    private ArrayList<String> idArrayList = new ArrayList<>();

    public ExcelModel(File finalFile){
        readFinalIDs(finalFile);
    }

    public Double getSumOfColumn(int columnIndex, int startRowIndex, File file) throws IOException {
        ArrayList<Double> doubleList = this.readNumericalColumn(columnIndex, startRowIndex, file);
        double sum = 0.0;
        for (Object number : doubleList) {
            System.out.println(number);
            try {
                sum = sum + (double)number;
            }catch (Exception ignored){}
        }
        return sum;
    }

    public void readFinalIDs(File finalFile){
        try {
            //open file
            FileInputStream fileInputStream = new FileInputStream(finalFile);
            Workbook workbook = new XSSFWorkbook(fileInputStream);

            //select first sheet
            Sheet sheet = workbook.getSheetAt(0);

            //select IDs column
            int columnIndex = 1;

            //read rows
            for (int i = 4; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(columnIndex);
                    if (cell != null) {
                        //get value
                        String cellValue = cell.getStringCellValue();
                        //remove all symbols after first space
                        int indexOfSpace = cellValue.indexOf(' ');
                        String resultID = cellValue.substring(0, indexOfSpace);
                        //add to ID array list
                        getIdArrayList().add(resultID);
                        //System.out.println(resultID);
                        //increment IDs count
                        setIdCount(getIdCount()+1);
                    }
                }
            }

            // Close thread
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(getIdCount());
    }

    public String readId(File file){
        String id;
        id = (String) readDataFromCell(4, 3, file);

        int lastIndex = id.lastIndexOf(" ");

        if (lastIndex != -1) {
            id = id.substring(lastIndex + 1);
        }

        return id;
    }

    public int getColumnCount(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fileInputStream);

        Sheet sheet = workbook.getSheetAt(0);
        int maxColumn = 0;
        int lastRowNum = sheet.getLastRowNum();
        for (int rowNum = 0; rowNum <= lastRowNum; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row != null) {
                int lastCellNum = row.getLastCellNum();
                maxColumn = Math.max(maxColumn, lastCellNum);
            }
        }
        return maxColumn;
    }

    public int getRowsCount(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);
        return sheet.getPhysicalNumberOfRows();
    }

    public String getPaymentId(File file){
        String id;
        id = (String) readDataFromCell(2, 8, file);
        id = id.substring(0, 6);
        return id;
    }

    public int getIdSequence(String id){
        int sequence = 0;
        for (String i: idArrayList){
            if (i.length() > 6) {
                String resultString = i.substring(0, 6);
                //System.out.println(resultString);
                if (resultString.equals(id)){
                    break;
                }
            }
            sequence++;
        }
        return sequence;
    }

    public ArrayList<Double> readNumericalColumn(int columnIndex, int startRowIndex, File file) throws IOException {


        ArrayList<Double> tempList = new ArrayList<>();

        //open file
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fileInputStream);

        //select first sheet
        Sheet sheet = workbook.getSheetAt(0);

        for (int i = startRowIndex; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(columnIndex);
                if (cell != null ) {
                    //get value
                    try {
                        Double cellValue = cell.getNumericCellValue();
                        //System.out.println(cellValue);
                        //add to ArrayList
                        tempList.add(cellValue);
                    }catch (Exception ignored){}


                }
            }
        }

        // Close thread
        fileInputStream.close();

        return tempList;
    }

    public Object readDataFromCell(int columnIndex, int rowIndex, File file){
        Object cellValue = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            Workbook workbook = WorkbookFactory.create(fileInputStream);

            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(rowIndex);

            if (row != null) {
                Cell cell = row.getCell(columnIndex);
                if (cell != null) {
                    try {
                        cellValue = cell.getNumericCellValue();
                    }catch (Exception e){
                        cellValue = cell.getStringCellValue();
                    }

                    //System.out.println(cellValue);
                } else {
                    System.out.println("Cell not exist");
                }
            } else {
                System.out.println("Row not exist");
            }
            fileInputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }

        return cellValue;
    }

    public ArrayList<File> readFilesFromFolder(String path){
        final File folder = new File(path);

        ArrayList<File> files = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            String fileName = fileEntry.toString();

            int index = fileName.lastIndexOf('.');
            if(index > 0) {
                String extension = fileName.substring(index + 1);
                if (extension.equals("xls") || extension.equals("xlsx")){
                    //System.out.println(fileEntry.getName());
                    files.add(fileEntry);
                }

            }
        }

        return files;
    }

    public int getRowFromReport(File file, String value) throws IOException, InvalidFormatException {

        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook;
        workbook = WorkbookFactory.create(fileInputStream);


        Sheet sheet = workbook.getSheetAt(0);
        int row = 0;

        for (int i = 5; i < sheet.getPhysicalNumberOfRows(); i++) {

            String rowValue = (String) readDataFromCell(1, i, file);
            String lastSixCharacters = rowValue.substring(rowValue.length() - 6);
//            System.out.println(lastSixCharacters);
            if (Objects.equals(value, lastSixCharacters)){
                row = i;
            }
        }

        return row;
    }

    public void writeCell(int columnIndex, int rowIndex, double value, File file) throws IOException {

        //open file
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(fileInputStream);

        //select first sheet
        Sheet sheet = workbook.getSheetAt(0);

        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            cell = row.createCell(columnIndex);
        }

        // write data
        cell.setCellValue(value);

        fileInputStream.close();

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        // update excel file
        workbook.write(fileOutputStream);
        fileOutputStream.close();

    }

    public int getIdCount() {
        return idCount;
    }

    private void setIdCount(int idCount) {
        this.idCount = idCount;
    }

    public ArrayList<String> getIdArrayList() {
        return idArrayList;
    }

    private void setIdArrayList(ArrayList<String> idArrayList) {
        this.idArrayList = idArrayList;
    }
}
