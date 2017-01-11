package com.example.fraser.notaficationprototype;


import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class SendDataFragment extends Fragment {

    ExportCSV eCSV;


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

    public void setupSendButtonListener(View v) {
        Button sendButton = (Button) v.findViewById(R.id.sendData);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataAlertDialog(getView());
            }
        });
    }

    public void sendDataAlertDialog(final View v) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Do you agree to send your recored data ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText inputEmail = (EditText) v.findViewById(R.id.inputEmailAddress);
                        String email = inputEmail.getText().toString();

                        // send data !
                        final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                        emailIntent.setType("plain/text");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Collected Data From Authentication Diary");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "This the data collected from the study in a CSV file");

                        // send as email attachment
                        ArrayList<Uri> uris = new ArrayList<Uri>();
                        eCSV = new ExportCSV();
                        uris.add(Uri.fromFile(eCSV.generateCSV(getActivity())));
                        emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                        startActivityForResult(Intent.createChooser(emailIntent, "Sending multiple attachment"), 123);

                    }
                })
                .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"You must accept in order to complete the study",Toast.LENGTH_LONG).show();
                    }
                });
        builder.create();
        builder.show();
    }

}
