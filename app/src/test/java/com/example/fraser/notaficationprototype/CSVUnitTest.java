package com.example.fraser.notaficationprototype;

import com.opencsv.CSVWriter;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Fraser on 07/01/2017.
 */

public class CSVUnitTest {

    private File file;
    private CSVWriter writer;


    /**
     * Writes a csv file in the app folder with two test rows with each value seperated into
     * a seperate cell, will do a test in the database class with this code to see if works the
     * same.
     */
    @Test
    public void generateCSVTest() {
        try {
            file = new File(System.getProperty("user.home"),"testCSV.csv");
            writer = new CSVWriter(new FileWriter(file));
            String[] entries = "first#second".split("#");
            String[] moreEntries = {"first","second","thrid"};
            writer.writeNext(entries);
            writer.writeNext(moreEntries);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
