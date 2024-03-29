package com.example.fraser.notaficationprototype.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.fraser.notaficationprototype.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Fraser on 13/12/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //public for unit testing

    public static final String DB_NAME = "AuthenticationDiary";
    public static final int DB_VERSION = 1;
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
        Log.i("TableVersion", oldVersion + "");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DEVICE + " INTEGER, "
                + AUTHEN + " INTEGER, "
                + EMOTION + " INTEGER, "
                + COMMENTS + " TEXT, "
                + ADDED_ON + " TIMESTAMP NOT NULL DEFAULT current_timestamp, "
                + LOCATION + " TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS IMAGE_NAMES ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "IMAGE_ID INTEGER," +
                "NAME TEXT," +
                "CATEGORY TEXT);");

        populateImageDB(db);


        db.execSQL("CREATE TABLE IF NOT EXISTS CUSTOM_ENTRIES ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "NAME TEXT," +
                "CATEGORY TEXT," +
                "AUTHEN_ID INTEGER);");
    }

    public long getMaxID(SQLiteDatabase db) {
        String getMaxID = "SELECT MAX(_id) FROM AUTHENTICATION";
        Cursor c = db.rawQuery(getMaxID, null);
        Long id = null;
        if (c.moveToFirst()) {
            id = c.getLong(0);
        }
        c.close();
        return id;
    }

    public void populateImageDB(SQLiteDatabase db) {

        insertImageNames(db, 0, "Icons", "Header");
        insertImageNames(db, R.drawable.play_32, "Start Notification", "Button");
        insertImageNames(db, R.drawable.stop_32, "End Notification", "Button");
        insertImageNames(db, R.drawable.chat, "Comment Present", "Icon");
        insertImageNames(db, R.drawable.chat_empty, "Comment Not Present", "Icon");
        insertImageNames(db, R.drawable.location, "Location Present", "Icon");
        insertImageNames(db, R.drawable.location_empty, "Location Not Present", "Icon");

        insertImageNames(db, 0, "Emotion", "Header");
        insertImageNames(db, R.drawable.happy, "Happy", "Emotion");
        insertImageNames(db, R.drawable.sad, "Sad", "Emotion");
        insertImageNames(db, R.drawable.confused, "Confused", "Emotion");

        insertImageNames(db, 0, "Target Devices", "Header");
        insertImageNames(db, R.drawable.atm, "ATM", "Target");
        insertImageNames(db, R.drawable.domain_registration, "Browser", "Target");
        insertImageNames(db, R.drawable.suv, "Car", "Target");
        insertImageNames(db, R.drawable.smartphone, "Smartphone", "Target");
        insertImageNames(db, R.drawable.laptop, "Laptop", "Target");
        insertImageNames(db, R.drawable.locked, "Lock", "Target");
        insertImageNames(db, R.drawable.mobile_phone, "Mobile Payment", "Target");
        insertImageNames(db, R.drawable.locker, "Locker", "Target");
        insertImageNames(db, R.drawable.door, "Door", "Target");
        insertImageNames(db, R.drawable.tablet, "Tablet", "Target");
        insertImageNames(db, R.drawable.cycle, "Bike", "Target");
        insertImageNames(db, R.drawable.metro, "Public Transport", "Target");

        insertImageNames(db, 0, "Authenticators", "Header");
        insertImageNames(db, R.drawable.contract, "Signature", "Authenticator");
        insertImageNames(db, R.drawable.mouse_click, "Mouse Click", "Authenticator");
        insertImageNames(db, R.drawable.fingerprintscan, "Fingerprint", "Authenticator");
        insertImageNames(db, R.drawable.hand_gesture, "Hand Gesture", "Authenticator");
        insertImageNames(db, R.drawable.id_card, "ID Card", "Authenticator");
        insertImageNames(db, R.drawable.key, "Key", "Authenticator");
        insertImageNames(db, R.drawable.dial, "Key Pad", "Authenticator");
        insertImageNames(db, R.drawable.password, "Password", "Authenticator");
        insertImageNames(db, R.drawable.ticket, "Ticket", "Authenticator");
        insertImageNames(db, R.drawable.blank_card, "Card", "Authenticator");
        insertImageNames(db, R.drawable.point_of_service, "Chip and Pin", "Authenticator");
        insertImageNames(db, R.drawable.two_step_32x32, "Two Factor Authentication", "Authenticator");

        insertImageNames(db, 0, "Other Items", "Header");
        insertImageNames(db, R.drawable.question_mark, "Other", "Other");
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

    public void insertOtherEntry(SQLiteDatabase db, String name, String category, long authenID) {
        ContentValues imageValues = new ContentValues();
        imageValues.put("NAME", name);
        imageValues.put("CATEGORY", category);
        imageValues.put("AUTHEN_ID", authenID);
        db.insertWithOnConflict("CUSTOM_ENTRIES", null, imageValues, SQLiteDatabase.CONFLICT_REPLACE);
        // db.insert("CUSTOM_ENTRIES", null, imageValues);
    }

    public void insertImageNames(SQLiteDatabase db, int imageId, String name, String category) {
        ContentValues imageValues = new ContentValues();
        imageValues.put("IMAGE_ID", imageId);
        imageValues.put("NAME", name);
        imageValues.put("CATEGORY", category);
        db.insert("IMAGE_NAMES", null, imageValues);
    }

    public void updateLocationAndComments(SQLiteDatabase db, String loc, String comm, long id) {
        ContentValues values = new ContentValues();
        values.put(COMMENTS, comm);
        values.put(LOCATION, loc);
        String where = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        db.update(TABLE_NAME, values, where, whereArgs);
    }

    public ArrayList<Authentication> getAllAuthentications(SQLiteDatabase db) {
        String getALlQuery = "SELECT * FROM " + TABLE_NAME + ";";
        Cursor c = db.rawQuery(getALlQuery, null);
        Authentication authen = new Authentication();
        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex("_id"));
            int deviceID = c.getInt(c.getColumnIndex("DEVICE_RESOURCE_ID"));
            int authenticator = c.getInt(c.getColumnIndex("AUTHENTICATOR_RESOURCE_ID"));
            int emotion = c.getInt(c.getColumnIndex("EMOTION_RESOURCE_ID"));
            String timeStamp = c.getString(c.getColumnIndex("ADDED_ON"));
            String location = c.getString(c.getColumnIndex("LOCATION"));
            String comments = c.getString(c.getColumnIndex("COMMENTS"));

            String deviceName = getImageName(db, deviceID);
            String authenName = getImageName(db, authenticator);
            String emoName = getImageName(db, emotion);

            if (deviceName.equals("Other")) {
                deviceName = getOtherNameFromID(db, "Target", id);
            }

            if (authenName.equals("Other")) {
                authenName = getOtherNameFromID(db, "Authenticator", id);
            }

            if (emoName.equals("Other")) {
                emoName = getOtherNameFromID(db, "Emotion", id);
            }

            Authentication a = new Authentication(id, deviceName, authenName, emoName, timeStamp, location, comments);
            authen.getAuthenList().add(a);
        }
        c.close();
        return authen.getAuthenList();
    }

    public List<String> getLocations(SQLiteDatabase db) {
        List<String> locations = new ArrayList<>();
        Collections.addAll(locations, "Select Location", "Other", "Home", "Work", "Shop", "Travel");
        String getLocations = "SELECT DISTINCT LOCATION FROM AUTHENTICATION WHERE LOCATION NOT NULL";
        Cursor c = db.rawQuery(getLocations, null);

        while (c.moveToNext()) {
            String loc = c.getString(c.getColumnIndex(LOCATION));
            //Log.e("location", loc);
            if (!locations.contains(loc) && !loc.equals("")) {
                locations.add(loc);
            }
        }
        c.close();
        return locations;
    }

    public String getImageName(SQLiteDatabase db, int resourceID) {
        String name = "";
        String getImageNameQuery = "SELECT NAME FROM IMAGE_NAMES WHERE IMAGE_ID = " + resourceID;
        Cursor c = db.rawQuery(getImageNameQuery, null);
        if (c.moveToFirst()) {
            name = c.getString(c.getColumnIndex("NAME"));
            Log.e("getImageName: ", name);
        }
        c.close();
        return name;
    }

    public int getImageResourceID(SQLiteDatabase db, String name) {
        int imageID = 0;
        String getImageNameQuery = "SELECT DISTINCT IMAGE_ID FROM IMAGE_NAMES WHERE NAME = ?";
        Cursor c = db.rawQuery(getImageNameQuery, new String[]{name});
        if (c.moveToFirst()) {
            imageID = c.getInt(c.getColumnIndex("IMAGE_ID"));
        }
        c.close();
        return imageID;
    }

    public void alterAuthentication(SQLiteDatabase db, String columnName, int imageID, long _id) {
        ContentValues values = new ContentValues();
        values.put(columnName, imageID);
        String where = "_id=?";
        String[] whereArgs = new String[]{String.valueOf(_id)};
        db.update(TABLE_NAME, values, where, whereArgs);
    }

    public LinkedHashMap<String, List<String>> getExpandableListData(SQLiteDatabase db) {

        LinkedHashMap<String, List<String>> expandableListDataMap = new LinkedHashMap<>();
        expandableListDataMap.put("Target", getIconsFromCategory(db, "Target"));
        expandableListDataMap.put("Authenticator", getIconsFromCategory(db, "Authenticator"));
        expandableListDataMap.put("Emotion", getIconsFromCategory(db, "Emotion"));
        Log.e("listMap",expandableListDataMap.toString());
        return expandableListDataMap;
    }

    protected List<String> getIconsFromCategory(SQLiteDatabase db, String category) {
        List<String> data = new ArrayList<>();
        String query = "SELECT DISTINCT NAME FROM IMAGE_NAMES WHERE CATEGORY = ?";
        Cursor c = db.rawQuery(query, new String[]{category});
        while (c.moveToNext()) {
            data.add(c.getString(c.getColumnIndex("NAME")));
        }
        c.close();
        return data;
    }

    public String getOtherNameFromID(SQLiteDatabase db, String category, long authenID) {

        String name = "";
        String getName = "SELECT DISTINCT NAME FROM CUSTOM_ENTRIES WHERE AUTHEN_ID = " + authenID + " AND " +
                "CATEGORY = ? ";
        Cursor c = db.rawQuery(getName, new String[]{category});
        if (c.moveToLast()) {
            name = c.getString(c.getColumnIndex("NAME"));
            Log.e("getOtherNameFromID: ", name);
        }
        c.close();
        return name;
    }

    public int updateImageNames(SQLiteDatabase db, String name, String category, long authenID) {

        ContentValues values = new ContentValues();
        values.put("NAME", name);
        String where = "AUTHEN_ID=? AND CATEGORY=?";
        String[] whereArgs = new String[]{String.valueOf(authenID), category};
        int returnValue = db.update("CUSTOM_ENTRIES", values, where, whereArgs);
        Log.e("updateReturnValue", returnValue + "");
        return returnValue;

    }
}
