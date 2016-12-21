package com.example.fraser.notaficationprototype;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


public class SummaryActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private Cursor cursor;
    private DatabaseHelper dbHelper;
    ListView authenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        SQLiteOpenHelper authenticationDatabase = new DatabaseHelper(this);
        db = authenticationDatabase.getReadableDatabase();
        insertSelectionToDB();
        generateList();
    }

    public void insertSelectionToDB() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int emotionButton = (int) extras.get("emotionButton");
            int authenButton = (int) extras.get("authenButton");
            int deviceButton = (int) extras.get("devButton");
            dbHelper.insertAuthentication(db, deviceButton, authenButton, emotionButton, null, null);
        }
    }

    public void generateList() {

        authenList = (ListView) findViewById(R.id.authenList);
        try {
            String getAllAuthentications = "SELECT * FROM AUTHENTICATION";
            cursor = db.rawQuery(getAllAuthentications, null);
            CustomCursorAdaptor listAdapter = new CustomCursorAdaptor(this, cursor);
            authenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String selectedAuthentication = "SELECT DEVICE_RESOURCE_ID,AUTHENTICATOR_RESOURCE_ID, " +
                            "EMOTION_RESOURCE_ID,LOCATION,COMMENTS FROM AUTHENTICATION WHERE _id = " + id;
                    Cursor itemClickCursor = db.rawQuery(selectedAuthentication,null);
                    if(itemClickCursor.moveToFirst()){

                        int device = itemClickCursor.getInt(itemClickCursor.getColumnIndex("DEVICE_RESOURCE_ID"));
                        int authenticator = itemClickCursor.getInt(itemClickCursor.getColumnIndex("AUTHENTICATOR_RESOURCE_ID"));
                        int emotion = itemClickCursor.getInt(itemClickCursor.getColumnIndex("EMOTION_RESOURCE_ID"));
                        String location = itemClickCursor.getString(itemClickCursor.getColumnIndex("LOCATION"));
                        String comments = itemClickCursor.getString(itemClickCursor.getColumnIndex("COMMENTS"));

                        Bundle bundle = new Bundle();
                        bundle.putInt("device",device);
                        bundle.putInt("auhen",authenticator);
                        bundle.putInt("emotion",emotion);
                        bundle.putString("location",location);
                        bundle.putString("comment",comments);
                        Fragment detailedViewFragment = new DetailedViewFragment();
                        detailedViewFragment.setArguments(bundle);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.activity_summary, detailedViewFragment);
                        ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();

                    }
                    itemClickCursor.close();
                }
            });
            authenList.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
