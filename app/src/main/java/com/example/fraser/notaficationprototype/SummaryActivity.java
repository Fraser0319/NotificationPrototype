package com.example.fraser.notaficationprototype;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;


public class SummaryActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        generateList();
        //reciveSelection();
    }

//    public void reciveSelection() {
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            int emotionButton = (int) extras.get("emotionButton");
//            int authenButton = (int) extras.get("authenButton");
//            int deviceButton = (int) extras.get("devButton");
//            TextView emo = (TextView) findViewById(R.id.emotion);
//            TextView authen = (TextView) findViewById(R.id.authen);
//            TextView device = (TextView) findViewById(R.id.device);
//
//            emo.setText(emotionButton);
//            authen.setText(authenButton);
//            device.setText(deviceButton);
//        }
//    }

    public void generateList() {

        ListView authenList = (ListView) findViewById(R.id.authenList);

        try {

            SQLiteOpenHelper authenticationDatabase = new DatabaseHelper(this);
            db = authenticationDatabase.getReadableDatabase();
            String getAllAuthentications = "SELECT * FROM AUTHENTICATION";
            cursor = db.rawQuery(getAllAuthentications, null);
            CustomCursorAdaptor listAdapter = new CustomCursorAdaptor(this,cursor);
            authenList.setAdapter(listAdapter);

        } catch (SQLiteException e) {
            Toast.makeText(this,"Database Unavailable",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
