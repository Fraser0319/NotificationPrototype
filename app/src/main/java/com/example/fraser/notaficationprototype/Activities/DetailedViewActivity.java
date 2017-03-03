package com.example.fraser.notaficationprototype.Activities;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fraser.notaficationprototype.Model.DatabaseHelper;
import com.example.fraser.notaficationprototype.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailedViewActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private SQLiteOpenHelper authenticationDatabase;
    private Spinner spinner;
    private ArrayAdapter arrayAdapter;
    private EditText location;
    private long id;
    private int target;
    private int authen;
    private int emo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detailed_view);
        super.onCreate(savedInstanceState);
        dbHelper = new DatabaseHelper(this);
        getSelectedItemDetails();
        Toolbar toolbar = (Toolbar) findViewById(R.id.detailedViewToolbar);
        toolbar.setTitle("Detailed View");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackIntnet = new Intent(getApplicationContext(), MainActivity.class);
                goBackIntnet.putExtra("state", getState());
                startActivity(goBackIntnet);
            }
        });
        loadLocationSpinner();
        loadEditButton();
    }

    public boolean getState() {
        Boolean state = getIntent().getExtras().getBoolean("serviceState");
        return state;
    }

    public void loadEditButton() {
        ImageButton editButton = (ImageButton) findViewById(R.id.editRecordBtn);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editAuthenIntent = new Intent(getApplicationContext(), EditAuthenticationActivity.class);
                editAuthenIntent.putExtra("bundle", sendBundle());
                startActivity(editAuthenIntent);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onReumeCalled", "here");

        String getUpdatedRecord = "SELECT * FROM AUTHENTICATION WHERE _id = " + id;
        Cursor cursor = db.rawQuery(getUpdatedRecord, null);

        if (cursor.moveToFirst()) {
            target = cursor.getInt(cursor.getColumnIndex("DEVICE_RESOURCE_ID"));
            authen = cursor.getInt(cursor.getColumnIndex("AUTHENTICATOR_RESOURCE_ID"));
            emo = cursor.getInt(cursor.getColumnIndex("EMOTION_RESOURCE_ID"));


            ImageView deviceImage = (ImageView) findViewById(R.id.deviceDetailedImage);
            ImageView authenImage = (ImageView) findViewById(R.id.authDetailedImage);
            ImageView emoImage = (ImageView) findViewById(R.id.emotionDetailedImage);

            deviceImage.setImageResource(target);
            authenImage.setImageResource(authen);
            emoImage.setImageResource(emo);

            TextView deviceName = (TextView) findViewById(R.id.deviceName);
            TextView authenName = (TextView) findViewById(R.id.authenticationName);
            TextView emotionName = (TextView) findViewById(R.id.emotionType);

            if (target == R.drawable.question_mark) {
                deviceName.setText(dbHelper.getOtherNameFromID(db, "Target", id));
            } else {
                deviceName.setText(dbHelper.getImageName(db, target));
            }

            if (authen == R.drawable.question_mark) {
                authenName.setText(dbHelper.getOtherNameFromID(db, "Authenticator", id));
            } else {
                authenName.setText(dbHelper.getImageName(db, authen));
            }

            if (emo == R.drawable.question_mark) {
                emotionName.setText(dbHelper.getOtherNameFromID(db, "Emotion", id));
            } else {
                emotionName.setText(dbHelper.getImageName(db, emo));
            }
        }
    }

    public Bundle sendBundle() {
        Bundle newBundle = new Bundle();

        newBundle.putLong("id", id);
        newBundle.putInt("target", target);
        newBundle.putInt("authen", authen);
        newBundle.putInt("emotion", emo);

        return newBundle;
    }

    public void loadLocationSpinner() {
        dbHelper = new DatabaseHelper(this);
        location = (EditText) findViewById(R.id.locInput);
        spinner = (Spinner) findViewById(R.id.spinnerLocation);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dbHelper.getLocations(db));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                location.setText(parent.getItemAtPosition(pos).toString());
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(arrayAdapter);
        String location = getLocation();
        if (location != null) {
            spinner.setSelection(arrayAdapter.getPosition(location));
        } else {
            spinner.setSelection(arrayAdapter.getPosition(0));
        }

    }


    public void addLocation(String inputLocation) {
        arrayAdapter.add(inputLocation);
        arrayAdapter.notifyDataSetChanged();
        spinner.setSelection(arrayAdapter.getPosition(inputLocation));
    }

    public String getLocation() {
        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("bundle");
        if (extras != null) {
            id = extras.getLong("id");
            Cursor c = db.rawQuery("SELECT LOCATION FROM AUTHENTICATION WHERE _id = " + id, null);
            if (c.moveToFirst()) {
                String loc = c.getString(c.getColumnIndex("LOCATION"));
                if (loc != null) {
                    Log.e("loc", loc);
                    return loc;
                }
            }
        }
        return null;
    }

    public void updateFields(View v) {

        dbHelper = new DatabaseHelper(this);
        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("bundle");
        if (extras != null) {
            Long id = extras.getLong("id");
            location = (EditText) findViewById(R.id.locInput);
            EditText comment = (EditText) findViewById(R.id.commInput);
            addLocation(location.getText().toString());
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
            int device = extras.getInt("target");
            int authen = extras.getInt("authen");
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
