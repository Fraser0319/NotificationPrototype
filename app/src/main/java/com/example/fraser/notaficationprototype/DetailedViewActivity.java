package com.example.fraser.notaficationprototype;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailedViewActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private SQLiteOpenHelper authenticationDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detailed_view);
        super.onCreate(savedInstanceState);
        getSelectedItemDetails();
        Toolbar toolbar = (Toolbar) findViewById(R.id.detailedViewToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackIntnet = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(goBackIntnet);
            }
        });
    }

    public void updateFields(View v) {

        dbHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("bundle");
        if (extras != null) {
            Long id = extras.getLong("id");
            EditText location = (EditText) findViewById(R.id.locInput);
            EditText comment = (EditText) findViewById(R.id.commInput);
            dbHelper.updateLocationAndComments(db, location.getText().toString(), comment.getText().toString(), id);
            Toast.makeText(getApplicationContext(), "Record Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "cant get id ", Toast.LENGTH_LONG).show();
        }

    }

    public void getSelectedItemDetails() {
        authenticationDatabase = new DatabaseHelper(this);
        db = authenticationDatabase.getWritableDatabase();
        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("bundle");
        if (extras != null) {
            int device = extras.getInt("device");
            int authen = extras.getInt("auhen");
            int emotion = extras.getInt("emotion");
            String loc = extras.getString("location");
            String comms = extras.getString("comment");

            String getDeviceName = "SELECT NAME FROM IMAGE_NAMES WHERE IMAGE_ID = " + device;
            String getAuthenName = "SELECT NAME FROM IMAGE_NAMES WHERE IMAGE_ID = " + authen;
            String getEmotionName = "SELECT NAME FROM IMAGE_NAMES WHERE IMAGE_ID = " + emotion;

            Cursor devCursor = db.rawQuery(getDeviceName, null);
            Cursor authenCursor = db.rawQuery(getAuthenName, null);
            Cursor emoCursor = db.rawQuery(getEmotionName, null);

            if (devCursor.moveToFirst() && authenCursor.moveToFirst() && emoCursor.moveToFirst()) {
                String devName = devCursor.getString(devCursor.getColumnIndex("NAME"));
                String authName = authenCursor.getString(devCursor.getColumnIndex("NAME"));
                String emoName = emoCursor.getString(devCursor.getColumnIndex("NAME"));

                TextView deviceName = (TextView) findViewById(R.id.deviceName);
                TextView authenName = (TextView) findViewById(R.id.authenticationName);
                TextView emotionName = (TextView) findViewById(R.id.emotionType);
                EditText location = (EditText) findViewById(R.id.locInput);
                EditText comments = (EditText) findViewById(R.id.commInput);

                ImageView deviceImage = (ImageView) findViewById(R.id.deviceDetailedImage);
                ImageView authenImage = (ImageView) findViewById(R.id.authDetailedImage);
                ImageView emoImage = (ImageView) findViewById(R.id.emotionDetailedImage);

                deviceName.setText(devName);
                authenName.setText(authName);
                emotionName.setText(emoName);
                location.setText(loc);
                comments.setText(comms);

                deviceImage.setImageResource(device);
                authenImage.setImageResource(authen);
                emoImage.setImageResource(emotion);
            }

        } else {
            Toast.makeText(this, "DATABASE UNAVAILABLE", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

}
