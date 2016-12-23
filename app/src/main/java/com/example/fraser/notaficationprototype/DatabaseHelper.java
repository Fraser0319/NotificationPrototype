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
    public static final int DB_VERSION = 2;
    public static final String TABLE_NAME = "AUTHENTICATION";
    public static final String DEVICE = "DEVICE_RESOURCE_ID";
    public static final String AUTHEN = "AUTHENTICATOR_RESOURCE_ID";
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
        updateDatabase(db, oldVersion, newVersion);
    }

    public void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("version", oldVersion + "");
        switch (oldVersion) {
            case 0:
                db.execSQL("CREATE TABLE " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                        + DEVICE + " INTEGER, "
                        + AUTHEN + " INTEGER, "
                        + EMOTION + " INTEGER, "
                        + COMMENTS + " TEXT, "
                        + ADDED_ON + " TIMESTAMP NOT NULL DEFAULT current_timestamp, "
                        + LOCATION + " TEXT);");
                //insertAuthentication(db, R.drawable.car, R.drawable.fingerprintscan, R.drawable.sad, "took ages", "Home");

                db.execSQL("CREATE TABLE IMAGE_NAMES ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "IMAGE_ID INTEGER," +
                        "NAME TEXT);");
                insertImageNames(db, R.drawable.atm, "ATM");
                insertImageNames(db, R.drawable.browser, "Web Browser");
                insertImageNames(db, R.drawable.buses, "Bus");
                insertImageNames(db, R.drawable.suv, "Car");
                insertImageNames(db, R.drawable.confused, "Confused");
                insertImageNames(db, R.drawable.contract, "Signature");
                insertImageNames(db, R.drawable.cursor, "Arrow Click");
                insertImageNames(db, R.drawable.cycle, "Bike");
                insertImageNames(db, R.drawable.fingerprintscan, "Fingerprint Scan");
                insertImageNames(db, R.drawable.hand_gesture, "Hand Gesture");
                insertImageNames(db, R.drawable.happy, "Happy");
                insertImageNames(db, R.drawable.id_card, "ID Card");
                insertImageNames(db, R.drawable.key, "Key");
                insertImageNames(db, R.drawable.laptop, "Laptop");
                insertImageNames(db, R.drawable.locked, "Lock");
                insertImageNames(db, R.drawable.locker, "Locker");
                insertImageNames(db, R.drawable.metro, "Train");
                insertImageNames(db, R.drawable.mobile_phone, "Mobile Payment");
                insertImageNames(db, R.drawable.password, "Password");
                insertImageNames(db, R.drawable.point_of_service, "Chip and Pin");
                insertImageNames(db, R.drawable.smartphone, "Smartphone");
                insertImageNames(db, R.drawable.ticket, "Ticket");
                insertImageNames(db, R.drawable.tramway, "Tram");
                insertImageNames(db, R.drawable.sad, "Sad");

        }
    }

    protected static void insertAuthentication(SQLiteDatabase db, int deviceID, int authenID, int emotionID, String comments, String location) {

        ContentValues authenticationValues = new ContentValues();
        authenticationValues.put(DEVICE, deviceID);
        authenticationValues.put(AUTHEN, authenID);
        authenticationValues.put(EMOTION, emotionID);
        authenticationValues.put(COMMENTS, comments);
        authenticationValues.put(LOCATION, location);
        db.insert(TABLE_NAME, null, authenticationValues);
    }

    private void insertImageNames(SQLiteDatabase db, int imageId, String name) {
        ContentValues imageValues = new ContentValues();
        imageValues.put("IMAGE_ID", imageId);
        imageValues.put("NAME", name);
        db.insert("IMAGE_NAMES", null, imageValues);
    }

    public void getAllAuthentications() {

    }

    public void getAuthenticationsWithComments() {

    }
}
