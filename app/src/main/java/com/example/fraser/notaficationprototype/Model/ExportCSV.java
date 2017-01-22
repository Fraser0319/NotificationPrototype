package com.example.fraser.notaficationprototype.Model;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Fraser on 09/01/2017.
 */

public class ExportCSV {

    private ArrayList<Authentication> authenList;

    public ExportCSV(ArrayList<Authentication> authenList) {
        this.authenList = authenList;
    }

    public File generateCSV(Activity activity) {
        File file = null;
        try {
            file = new File("/sdcard/testCSV.csv");
            Log.e("path", activity.getFilesDir().getPath().toString() + "/testCSV.csv");
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            for (Authentication s : authenList) {
                writer.writeNext(createCSVLine(s));
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Cannot generate CSV File", Toast.LENGTH_LONG).show();
        }
        return file;
    }

    public String[] createCSVLine(Authentication s) {
        return new String[]{Long.toString(s.id), s.deviceID, s.authenticatorID, s.emotionID, s.timeStamp, s.location, s.comments};
    }
}

