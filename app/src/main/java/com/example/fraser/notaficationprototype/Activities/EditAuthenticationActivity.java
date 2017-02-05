package com.example.fraser.notaficationprototype.Activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fraser.notaficationprototype.Adapters.CustomExpandableListAdapter;
import com.example.fraser.notaficationprototype.Model.DatabaseHelper;
import com.example.fraser.notaficationprototype.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * expandable list code and listeners based on
 * based on http://www.journaldev.com/9942/android-expandablelistview-example-tutorial
 */

public class EditAuthenticationActivity extends AppCompatActivity {

    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListTitle;
    private Map<String, List<String>> expandableListDetail;
    private EditAuthenticationActivity editAuthenticationActivity = this;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private SQLiteOpenHelper authenticationDatabase;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_authentication);

        authenticationDatabase = new DatabaseHelper(this);
        db = authenticationDatabase.getWritableDatabase();
        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detailedViewToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editAuthenticationActivity.onBackPressed();

            }
        });

        setCurrentSelectedItems();
        updateRecord();

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = dbHelper.getExpandableListData(db);
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);


//        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Expanded.",
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        expandableListTitle.get(groupPosition) + " List Collapsed.",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//        });
//
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
                            targetTextView.setText(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition));
                            break;

                        case "Authenticator":
                            ImageView authenImageView = (ImageView) findViewById(R.id.authenIconView);
                            authenImageView.setImageResource(c.getInt(c.getColumnIndex("IMAGE_ID")));
                            TextView authenTextView = (TextView) findViewById(R.id.authenSelectedItem);
                            authenTextView.setText(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition));
                            break;

                        case "Emotion":
                            ImageView emotionImageView = (ImageView) findViewById(R.id.emotionIconView);
                            emotionImageView.setImageResource(c.getInt(c.getColumnIndex("IMAGE_ID")));
                            TextView emotionTextView = (TextView) findViewById(R.id.emotionSelectedItem);
                            emotionTextView.setText(expandableListDetail.get(expandableListTitle.get(groupPosition)).get(childPosition));
                            break;
                    }
                }
                return false;
            }
        });
    }

    public void updateRecord() {

        FloatingActionButton updateRecordBtn = (FloatingActionButton) findViewById(R.id.updateEditBtn);
        updateRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView targetTextView = (TextView) findViewById(R.id.targetItemSelection);
                TextView autheTextView = (TextView) findViewById(R.id.authenSelectedItem);
                TextView emotionTextView = (TextView) findViewById(R.id.emotionSelectedItem);

                dbHelper.alterAuthentication(db, dbHelper.DEVICE, dbHelper.getImageResourceID(db, targetTextView.getText().toString()), id);
                dbHelper.alterAuthentication(db, dbHelper.AUTHEN, dbHelper.getImageResourceID(db, autheTextView.getText().toString()), id);
                dbHelper.alterAuthentication(db, dbHelper.EMOTION, dbHelper.getImageResourceID(db, emotionTextView.getText().toString()), id);
            }
        });
    }


    public void setCurrentSelectedItems() {
        Intent intent = getIntent();
        Bundle extras = intent.getBundleExtra("bundle");

        if (extras != null) {
            id = extras.getLong("id");
            int dev = extras.getInt("device");
            int authen = extras.getInt("auhen");
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

            devText.setText(dbHelper.getImageName(db, dev));
            authenText.setText(dbHelper.getImageName(db, authen));
            emoText.setText(dbHelper.getImageName(db, emo));

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}
