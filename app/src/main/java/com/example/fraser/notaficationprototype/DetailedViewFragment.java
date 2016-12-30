package com.example.fraser.notaficationprototype;


import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailedViewFragment extends Fragment{

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private SQLiteOpenHelper authenticationDatabase;

    public DetailedViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myInflatedView = inflater.inflate(R.layout.fragment_detailed_view, container, false);
        getSelectedItemDetails(myInflatedView);
        updateFields(myInflatedView);
        return myInflatedView;
    }

    public void updateFields(View v){
        Button updateButton  = (Button) v.findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long id = getArguments().getLong("id");
                EditText location = (EditText) getView().findViewById(R.id.locInput);
                EditText comment = (EditText) getView().findViewById(R.id.commInput);

                String updateLocAndComm = "UPDATE AUTHENTICATION SET LOCATION = '" + location.getText().toString() + "', COMMENTS = '" + comment.getText().toString() + "' WHERE _id = " + id;
                db.execSQL(updateLocAndComm);
            }
        });
    }


    public void getSelectedItemDetails(View v){
        authenticationDatabase = new DatabaseHelper(getActivity());
        db = authenticationDatabase.getWritableDatabase();

        int device = getArguments().getInt("device");
        int authen = getArguments().getInt("auhen");
        int emotion = getArguments().getInt("emotion");
        String loc = getArguments().getString("location");
        String comms = getArguments().getString("comment");

        TextView deviceName = (TextView) v.findViewById(R.id.deviceName);
        TextView authenName = (TextView) v.findViewById(R.id.authenticationName);
        TextView emotionName = (TextView) v.findViewById(R.id.emotionType);
        EditText location = (EditText) v.findViewById(R.id.locInput);
        EditText comments = (EditText) v.findViewById(R.id.commInput);


        String getDeviceName = "SELECT NAME FROM IMAGE_NAMES WHERE IMAGE_ID = " + device;
        String getAuthenName = "SELECT NAME FROM IMAGE_NAMES WHERE IMAGE_ID = " + authen;
        String getEmotionName = "SELECT NAME FROM IMAGE_NAMES WHERE IMAGE_ID = " + emotion;

        ImageView deviceImage = (ImageView) v.findViewById(R.id.deviceDetailedImage);
        ImageView authenImage = (ImageView) v.findViewById(R.id.authDetailedImage);
        ImageView emoImage = (ImageView) v.findViewById(R.id.emotionDetailedImage);

        Cursor devCursor = db.rawQuery(getDeviceName,null);
        Cursor authenCursor = db.rawQuery(getAuthenName,null);
        Cursor emoCursor = db.rawQuery(getEmotionName,null);


        if(devCursor.moveToFirst() && authenCursor.moveToFirst() && emoCursor.moveToFirst()){
            String devName = devCursor.getString(devCursor.getColumnIndex("NAME"));
            String authName = authenCursor.getString(devCursor.getColumnIndex("NAME"));
            String emoName = emoCursor.getString(devCursor.getColumnIndex("NAME"));

            deviceName.setText(devName);
            authenName.setText(authName);
            emotionName.setText(emoName);
            location.setText(loc);
            comments.setText(comms);

            deviceImage.setImageResource(device);
            authenImage.setImageResource(authen);
            emoImage.setImageResource(emotion);

        }else {
            deviceName.setText("Image Unavailable");
            authenName.setText("Image Unavailable");
            emotionName.setText("Image Unavailable");
            Toast.makeText(getActivity(),"DATABASE UNAVAILABLE",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.close();
    }

}
