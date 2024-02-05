package com.revive.datascraper.calculators;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface CalculatorInterface {
    void calculateAndWrite(ArrayList<File> arrayList, File finalFile) throws IOException;
}
