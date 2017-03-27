package com.example.fraser.notaficationprototype.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fraser.notaficationprototype.Adapters.CustomExpandableListAdapter;
import com.example.fraser.notaficationprototype.Model.DatabaseHelper;
import com.example.fraser.notaficationprototype.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * expandable list code and listeners based on
 * based on http://www.journaldev.com/9942/android-expandablelistview-example-tutorial
 */

public class EditAuthenticationActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private LinkedHashMap<String, List<String>> expandableListDetail;
    private EditAuthenticationActivity editAuthenticationActivity = this;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private SQLiteOpenHelper authenticationDatabase;
    private long id;
    private EditText tarEdit;
    private EditText authenEdit;
    private EditText emoEdit;
    private int tarImage;
    private int authenImage;
    private int emoImage;
    private TextView targetTextView;
    private TextView autheTextView;
    private TextView emotionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_authentication);

        authenticationDatabase = new DatabaseHelper(this);
        db = authenticationDatabase.getWritableDatabase();
        dbHelper = new DatabaseHelper(this);
        //initTextAndImageFields();

        Toolbar toolbar = (Toolbar) findViewById(R.id.detailedViewToolbar);
        toolbar.setTitle("Edit Authentication");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //editAuthenticationActivity.onBackPressed();
                initTextAndImageFields();
                if(checkForBlankEntry(emoImage,authenImage,tarImage)){
                    Intent backIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(backIntent);
                }
            }
        });
        updateRecord();
        setCurrentSelectedItems();
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = dbHelper.getExpandableListData(db);
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {

                String query = "SELECT IMAGE_ID FROM IMAGE_NAMES WHERE NAME = ?";
                Cursor c = db.rawQuery(query, new String[]{expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition)});

                if (c.moveToFirst()) {
                    switch (expandableListTitle.get(groupPosition)) {
                        case "Target":
                            ImageView targetImageView = (ImageView) findViewById(R.id.targetIconView);
                            targetImageView.setImageResource(c.getInt(c.getColumnIndex("IMAGE_ID")));
                            TextView targetTextView = (TextView) findViewById(R.id.targetItemSelection);
                            tarEdit.setVisibility(View.GONE);
                            targetTextView.setVisibility(View.VISIBLE);
                            targetTextView.setText(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition));
                            expandableListView.collapseGroup(groupPosition);
                            break;

                        case "Authenticator":
                            ImageView authenImageView = (ImageView) findViewById(R.id.authenIconView);
                            authenImageView.setImageResource(c.getInt(c.getColumnIndex("IMAGE_ID")));
                            TextView authenTextView = (TextView) findViewById(R.id.authenSelectedItem);
                            authenEdit.setVisibility(View.GONE);
                            authenTextView.setVisibility(View.VISIBLE);
                            authenTextView.setText(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition));
                            expandableListView.collapseGroup(groupPosition);
                            break;

                        case "Emotion":
                            ImageView emotionImageView = (ImageView) findViewById(R.id.emotionIconView);
                            emotionImageView.setImageResource(c.getInt(c.getColumnIndex("IMAGE_ID")));
                            TextView emotionTextView = (TextView) findViewById(R.id.emotionSelectedItem);
                            emoEdit.setVisibility(View.GONE);
                            emotionTextView.setVisibility(View.VISIBLE);
                            emotionTextView.setText(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition));
                            expandableListView.collapseGroup(groupPosition);
                            break;
                    }
                }
                return false;
            }
        });
    }


    public void initTextAndImageFields(){

        targetTextView = (TextView) findViewById(R.id.targetItemSelection);
        autheTextView = (TextView) findViewById(R.id.authenSelectedItem);
        emotionTextView = (TextView) findViewById(R.id.emotionSelectedItem);

        tarImage = dbHelper.getImageResourceID(db, targetTextView.getText().toString());
        authenImage = dbHelper.getImageResourceID(db, autheTextView.getText().toString());
        emoImage = dbHelper.getImageResourceID(db, emotionTextView.getText().toString());
    }

    public boolean checkForBlankEntry(int emoImage, int authenImage, int tarImage) {

        if (emoEdit.getText().toString().isEmpty() && emoImage == 0 || authenEdit.getText().toString().isEmpty() && authenImage == 0 ||
                tarEdit.getText().toString().isEmpty() && tarImage == 0) {
            Toast.makeText(this, "Please enter a value for 'other' entry", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.
                INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }


    public void updateRecord() {

        FloatingActionButton updateRecordBtn = (FloatingActionButton) findViewById(R.id.updateEditBtn);
        updateRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                targetTextView = (TextView) findViewById(R.id.targetItemSelection);
                autheTextView = (TextView) findViewById(R.id.authenSelectedItem);
                emotionTextView = (TextView) findViewById(R.id.emotionSelectedItem);

                tarImage = dbHelper.getImageResourceID(db, targetTextView.getText().toString());
                authenImage = dbHelper.getImageResourceID(db, autheTextView.getText().toString());
                emoImage = dbHelper.getImageResourceID(db, emotionTextView.getText().toString());

                //  when you edit and there is an exsisting field from an 'other' it will load in as
                //  an edit text field and when you update it will see it as an edit text field and think
                //  its a new entry so this is why it causes the duplicates, so i need to make it so
                //  it loads in as a textView field.

                if (checkForBlankEntry(emoImage, authenImage, tarImage)) {
                    if (tarImage == 0) {
                        dbHelper.alterAuthentication(db, dbHelper.DEVICE, R.drawable.question_mark, id);
                        dbHelper.insertImageNames(db, R.drawable.question_mark, tarEdit.getText().toString(), "Target");
                        dbHelper.insertOtherEntry(db, tarEdit.getText().toString(), "Target", id);
                        Log.e("insertCalled", "");
                    } else {
                        dbHelper.alterAuthentication(db, dbHelper.DEVICE, tarImage, id);

                        if (tarImage == R.drawable.question_mark) {
                            int value = dbHelper.updateImageNames(db, targetTextView.getText().toString(), "Target", id);
                            if (value == 0) {
                                dbHelper.insertOtherEntry(db, targetTextView.getText().toString(), "Target", id);
                                Log.e("tarTextView: ", targetTextView.getText().toString());
                            }
                        }
                    }

                    if (authenImage == 0) {
                        dbHelper.alterAuthentication(db, dbHelper.AUTHEN, R.drawable.question_mark, id);
                        dbHelper.insertImageNames(db, R.drawable.question_mark, authenEdit.getText().toString(), "Authenticator");
                        dbHelper.insertOtherEntry(db, authenEdit.getText().toString(), "Authenticator", id);
                    } else {
                        dbHelper.alterAuthentication(db, dbHelper.AUTHEN, authenImage, id);

                        if (authenImage == R.drawable.question_mark) {
                            int value = dbHelper.updateImageNames(db, autheTextView.getText().toString(), "Authenticator", id);
                            if (value == 0) {
                                dbHelper.insertOtherEntry(db, autheTextView.getText().toString(), "Authenticator", id);
                                Log.e("tarTextView: ", autheTextView.getText().toString());
                            }
                        }
                    }

                    if (emoImage == 0) {
                        Log.e("emoImageOnClick", "here");
                        dbHelper.alterAuthentication(db, dbHelper.EMOTION, R.drawable.question_mark, id);
                        dbHelper.insertImageNames(db, R.drawable.question_mark, emoEdit.getText().toString(), "Emotion");
                        dbHelper.insertOtherEntry(db, emoEdit.getText().toString(), "Emotion", id);
                    } else {
                        dbHelper.alterAuthentication(db, dbHelper.EMOTION, emoImage, id);

                        Log.e("emoImage", emoImage + "");

                        if (emoImage == R.drawable.question_mark) {
                            Log.e("emoImage", emoImage + "");
                            int value = dbHelper.updateImageNames(db, emotionTextView.getText().toString(), "Emotion", id);
                            if (value == 0) {
                                dbHelper.insertOtherEntry(db, emotionTextView.getText().toString(), "Emotion", id);
                                Log.e("tarTextView: ", emotionTextView.getText().toString());
                            }
                        }
                    }
                    Toast.makeText(getApplicationContext(), "Record Updated", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void setCurrentSelectedItems() {
        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("bundle");

        if (extras != null) {
            id = extras.getLong("id");
            int dev = extras.getInt("target");
            int authen = extras.getInt("authen");
            int emo = extras.getInt("emotion");

            ImageView devImage = (ImageView) findViewById(R.id.targetIconView);
            ImageView authenImage = (ImageView) findViewById(R.id.authenIconView);
            ImageView emoImage = (ImageView) findViewById(R.id.emotionIconView);

            devImage.setImageResource(dev);
            authenImage.setImageResource(authen);
            emoImage.setImageResource(emo);

            TextView devText = (TextView) findViewById(R.id.targetItemSelection);
            TextView authenText = (TextView) findViewById(R.id.authenSelectedItem);
            TextView emoText = (TextView) findViewById(R.id.emotionSelectedItem);

            String devName = dbHelper.getImageName(db, dev);
            String authenName = dbHelper.getImageName(db, authen);
            String emoName = dbHelper.getImageName(db, emo);

            checkForOtherOption(devName, authenName, emoName);

            Log.e("devName", devName);

            if (dev == R.drawable.question_mark) {
                String name = dbHelper.getOtherNameFromID(db, "Target", id);
                if (name.equals("")) {
                    devText.setVisibility(View.GONE);
                    tarEdit.setVisibility(View.VISIBLE);
                    tarEdit.setText(name);
                } else {
                    devText.setVisibility(View.VISIBLE);
                    tarEdit.setVisibility(View.GONE);
                    devText.setText(name);
                }
            } else {
                devText.setText(devName);
            }

            if (authen == R.drawable.question_mark) {
                String name = dbHelper.getOtherNameFromID(db, "Authenticator", id);
                if (name.equals("")) {
                    authenText.setVisibility(View.GONE);
                    authenEdit.setVisibility(View.VISIBLE);
                    authenEdit.setText(name);
                } else {
                    authenText.setVisibility(View.VISIBLE);
                    authenEdit.setVisibility(View.GONE);
                    authenText.setText(name);
                }
            } else {
                authenText.setText(authenName);
            }

            if (emo == R.drawable.question_mark) {
                String name = dbHelper.getOtherNameFromID(db, "Emotion", id);
                if (name.equals("")) {
                    emoText.setVisibility(View.GONE);
                    emoEdit.setVisibility(View.VISIBLE);
                    emoEdit.setText(name);
                } else {
                    emoText.setVisibility(View.VISIBLE);
                    emoEdit.setVisibility(View.GONE);
                    emoText.setText(name);
                }
            } else {
                emoText.setText(emoName);
            }
        }
    }

    public void checkForOtherOption(String dev, String authen, String emo) {

        tarEdit = (EditText) findViewById(R.id.addOtherTarget);
        authenEdit = (EditText) findViewById(R.id.addOtherAuthen);
        emoEdit = (EditText) findViewById(R.id.addOtherEmo);

        if (dev.equals("Other")) {
            TextView devText = (TextView) findViewById(R.id.targetItemSelection);
            devText.setVisibility(View.GONE);
            tarEdit.setVisibility(View.VISIBLE);

        }
        if (authen.equals("Other")) {
            TextView authenText = (TextView) findViewById(R.id.authenSelectedItem);
            authenText.setVisibility(View.GONE);
            authenEdit.setVisibility(View.VISIBLE);
        }
        if (emo.equals("Other")) {
            TextView emoText = (TextView) findViewById(R.id.emotionSelectedItem);
            emoText.setVisibility(View.GONE);
            emoEdit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
