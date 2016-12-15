package com.example.fraser.notaficationprototype;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Fraser on 13/12/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //public for unit testing

    public static final String DB_NAME = "AuthenticationDiary";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "AUTHENTICATION";
    public static final String DEVICE = "DEVICE_RESOURCE_ID";
    public static final String AUTHEN = "_AUTHENTICATOR_RESOURCE_ID";
    public static final String LOCATION = "LOCATION";
    public static final String COMMENTS = "COMMENTS";
    public static final String EMOTION = "EMOTION_RESOURCE_ID";
    public static final String ADDED_ON = "ADDED_ON";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateDatabase(db,oldVersion,newVersion);
    }

    public void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("version",oldVersion+"");
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + DEVICE + " INTEGER, "
                    + AUTHEN + " INTEGER, "
                    + EMOTION + " INTEGER, "
                    + COMMENTS + " TEXT, "
                    + ADDED_ON + " TIMESTAMP NOT NULL DEFAULT current_timestamp, "
                    + LOCATION + " TEXT);");
            //insertAuthentication(db, R.drawable.smartphone, R.drawable.fingerprintscan, R.drawable.confused, "took ages", "Home");
        }else{
            Log.d("other","version");
        }
    }

    public static void insertAuthentication(SQLiteDatabase db, int deviceID, int authenID, int emotionID, String comments, String location) {

        ContentValues authenticationValues = new ContentValues();
        authenticationValues.put("DEVICE_RESOURCE_ID", deviceID);
        authenticationValues.put("AUTHENTICATION_RESOURCE_ID", authenID);
        authenticationValues.put("EMOTION_RESOURCE_ID", emotionID);
        authenticationValues.put("COMMENTS", comments);
        authenticationValues.put("LOCATION", location);
        db.insert(TABLE_NAME, null, authenticationValues);
    }

    public void getAllAuthentications() {

    }

    public void getAuthenticationsWithComments() {

    }
}
