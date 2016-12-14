package com.example.fraser.notaficationprototype;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.id;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;


/**
 * Created by Fraser on 13/12/2016.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {

    private DatabaseHelper mHelper;
    private SQLiteDatabase mWritableDatabase;
    private SQLiteDatabase mReadableDatabase;

    @Before
    public void setUp() throws Exception {
        mHelper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
        mReadableDatabase = mHelper.getReadableDatabase();
        mWritableDatabase = mHelper.getWritableDatabase();
    }

//    @After
//    public void finish() {
//
//    }

    @Test
    public void testPreConditions() {
        assertNotNull(mHelper);
    }

    @Test
    public void testInsertAuthentication() {
        // mHelper.insertAuthentication(mWritableDatabase, R.drawable.smartphone, R.drawable.fingerprintscan, R.drawable.confused, "took ages", "Home");

        ContentValues authenticationValues = new ContentValues();
        authenticationValues.put(mHelper.DEVICE, R.drawable.smartphone);
        authenticationValues.put(mHelper.AUTHEN, R.drawable.fingerprintscan);
        authenticationValues.put(mHelper.EMOTION, R.drawable.confused);
        //authenticationValues.put(mHelper.ADDED_ON, System.currentTimeMillis());
        authenticationValues.put(mHelper.COMMENTS, "took ages");
        authenticationValues.put(mHelper.LOCATION, "Home");
        mWritableDatabase.insert(mHelper.TABLE_NAME, null, authenticationValues);

        ContentValues authenticationValues2 = new ContentValues();
        authenticationValues2.put(mHelper.DEVICE, R.drawable.car);
        authenticationValues2.put(mHelper.AUTHEN, R.drawable.fingerprintscan);
        authenticationValues2.put(mHelper.EMOTION, R.drawable.sad);
        //authenticationValues.put(mHelper.ADDED_ON, System.currentTimeMillis());
        authenticationValues2.put(mHelper.COMMENTS, "fine");
        authenticationValues2.put(mHelper.LOCATION, "work");
        mWritableDatabase.insert(mHelper.TABLE_NAME, null, authenticationValues2);

        // returns -1 if database fails on inserting data otherwise, it will return the row number.
        assertFalse(id == -1);

        String getAllDataQuery = "SELECT * FROM " + mHelper.TABLE_NAME;

        Log.i("getAllDataQuery", getAllDataQuery);

        Cursor cursor = mReadableDatabase.rawQuery(getAllDataQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int deviceId = cursor.getInt(1);
            Log.i("id", "deviceID: " + deviceId);
            //assertEquals(deviceId, R.drawable.smartphone);
            Date c = new Date(cursor.getLong(5));
            SimpleDateFormat ft = new SimpleDateFormat("E dd.MM.yyyy 'at' hh:mm:ss zz");
            Log.i("time", ft.format(c));

        }
        cursor.close();
//        mHelper.close();
//        mReadableDatabase.close();
//        mWritableDatabase.close();
    }

    @Test
    public void getAllRecords() {
        String getAllRows = "SELECT COUNT(*) FROM" + mHelper.TABLE_NAME;

        Cursor cursor = mReadableDatabase.rawQuery(getAllRows, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            Log.i("count:", count + "");
            cursor.close();
//            mHelper.close();
//            mReadableDatabase.close();
        }
    }
}


