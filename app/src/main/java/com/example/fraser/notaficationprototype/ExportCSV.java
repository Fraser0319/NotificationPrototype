package com.example.fraser.notaficationprototype;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Fraser on 09/01/2017.
 */

public class ExportCSV {

    public File generateCSV(Activity activity) {

        try {
            File file = new File("/sdcard/testCSV.csv");
            Log.e("path", activity.getFilesDir().getPath().toString() + "/testCSV.csv");
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            writer.writeNext(new String[] {"id","Device_Image","Authenticator_Image","Emotion_Image","Location","Comments,","Added_On"});
            writer.writeNext(new String[] {"1","2123443","2344643","6473644","Work","Took 3 Attempts","10/01/2017 14:27:32"});
            writer.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(activity, "Cannot generate CSV File", Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public void HttpSendCSV(){

    }
}
