package com.example.fraser.notaficationprototype;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


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
