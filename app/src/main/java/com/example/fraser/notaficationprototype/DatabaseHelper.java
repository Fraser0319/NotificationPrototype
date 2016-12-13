package com.example.fraser.notaficationprototype;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Fraser on 13/12/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "AuthenticationDiary";
    private static final int DB_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        updateDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE AUTHENTICATION (_id, INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "DEVICE_RESOURCE_ID INTEGER"
                    + "AUTHENTICATOR_RESOURCE_ID INTEGER"
                    + "EMOTION_RESOURCE_ID INTEGER "
                    + "COMMENTS TEXT"
                    + "ADDED_ON TIMESTAMP NOT NULL DEFAULT current_timestamp"
                    + "LOCATION TEXT");
            insertAuthentication(db, R.drawable.smartphone, R.drawable.fingerprintscan, R.drawable.confused, "took ages", "Home");
        }
    }

    public static void insertAuthentication(SQLiteDatabase db, int deviceID, int authenID, int emotionID, String comments, String location) {

        ContentValues authenticationValues = new ContentValues();
        authenticationValues.put("DEVICE_RESOURCE_ID", deviceID);
        authenticationValues.put("AUTHENTICATION_RESOURCE_ID", authenID);
        authenticationValues.put("EMOTION_RESOURCE_ID", emotionID);
        authenticationValues.put("COMMENTS", comments);
        authenticationValues.put("LOCATION", location);
        db.insert(DB_NAME, null, authenticationValues);
    }

    public void getAllAuthentications() {

    }

    public void getAuthenticationsWithComments() {

    }
}
