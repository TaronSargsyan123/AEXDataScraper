package com.revive.datascraper;

public class Constants {
//    Օր առաջ շուկա - Day before market (DBM)
//    Կարգավորվող Պայմանագրային շուկա - Regulated Contract Market (RCM)
//    Չկարգավորվող շուկա - Unregulated market (UM)
//    Երկար ժամկետի շուկա - Long term market (LTM)



    public static int startCountingRow = 4; //row from which to start counting in final document

    public static int columnForIranImportedEnergyInInterstateDocument = 12;
    public static int rowForIranImportedEnergyInInterstateDocument = 13;
    public static int columnForIranExportedEnergyInInterstateDocument = 13;
    public static int rowForIranExportedEnergyInInterstateDocument = 13;

    public static int columnForGeorgiaImportedEnergyInInterstateDocument = 14;
    public static int rowForGeorgiaImportedEnergyInInterstateDocument = 13;
    public static int columnForGeorgiaExportedEnergyInInterstateDocument = 15;
    public static int rowForGeorgiaExportedEnergyInInterstateDocument = 13;

    public static int  columnForIranImportedSum = 24; //column for Iran imported energy in final document
    public static int columnForIranExportedSum = 25; //column for Iran exported energy in final document
    public static int columnForGeorgiaImportedSum = 26; //column for Georgia imported energy in final document
    public static int columnForGeorgiaExportedSum = 27; //column for Georgia exported energy in final document




}
