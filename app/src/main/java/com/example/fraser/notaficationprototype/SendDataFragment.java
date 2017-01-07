package com.example.fraser.notaficationprototype;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendDataFragment extends Fragment {


    public SendDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewInflator = inflater.inflate(R.layout.fragment_send_data, container, false);
        setupSendButtonListener(viewInflator);
        return viewInflator;
    }

    public void setupSendButtonListener(View v){
        Button sendButton = (Button) v.findViewById(R.id.sendData);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataAlertDialog();
            }
        });
    }

    public void sendDataAlertDialog(){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you agree to send your recored data ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // send data !
                        try {
                            File file = new File("/sdcard/testCSV.csv");
                            Log.e("path",getActivity().getFilesDir().getPath().toString()+"/testCSV.csv");
                            CSVWriter writer = new CSVWriter(new FileWriter(file));
                            String[] entries = "first#second".split("#");
                            String[] moreEntries = {"first","second","thrid"};
                            writer.writeNext(entries);
                            writer.writeNext(moreEntries);
                            writer.close();

                            // send as email attachment
                            final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                            emailIntent.setType("plain/text");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"fraser0319@gmail.com"});
                            emailIntent.putExtra(Intent.EXTRA_EMAIL,"email body");

                            ArrayList<Uri> uris = new ArrayList<Uri>();
                            uris.add(Uri.fromFile(file));
                            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM,uris);
                            startActivityForResult(Intent.createChooser(emailIntent,"Sending multiple attachment"),123);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                })
                .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create();
        builder.show();
    }

}
