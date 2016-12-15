package com.example.fraser.notaficationprototype;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


/**
 * Created by Fraser on 13/12/2016.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {

    private DatabaseHelper mHelper;
    private SQLiteDatabase mWritableDatabase;
    private ContentValues authenticationValues;

    @Before
    public void setUp() throws Exception {
        mHelper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
        mWritableDatabase = mHelper.getWritableDatabase();
        authenticationValues = new ContentValues();
        authenticationValues.put(mHelper.DEVICE, R.drawable.smartphone);
        authenticationValues.put(mHelper.AUTHEN, R.drawable.fingerprintscan);
        authenticationValues.put(mHelper.EMOTION, R.drawable.confused);
        authenticationValues.put(mHelper.COMMENTS, "took ages");
        authenticationValues.put(mHelper.LOCATION, "Home");
        Long id = mWritableDatabase.insert(mHelper.TABLE_NAME, null, authenticationValues);
        assertFalse(id == -1);
        Log.i("2ndid", id + "");
    }

    @After
    public void tearDown() {

        mWritableDatabase.delete(mHelper.TABLE_NAME, null, null);
        mHelper.close();
        mWritableDatabase.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mHelper);
    }

    @Test
    public void testInsertAuthentication() {

        String getAllDataQuery = "SELECT * FROM " + mHelper.TABLE_NAME;
        Log.i("getAllDataQuery", getAllDataQuery);
        Cursor cursor = mWritableDatabase.rawQuery(getAllDataQuery, null);
        assertNotNull(cursor);
        if (cursor != null) {
            cursor.moveToFirst();
            int deviceId = cursor.getInt(1);
            Log.i("id", "deviceID: " + deviceId);
            assertFalse(deviceId == R.drawable.sad);
            assertTrue(deviceId == R.drawable.smartphone);
        }
        cursor.close();
    }

    @Test
    public void testGetAllRecords() {

        String getAllRows = "SELECT COUNT(*) FROM" + mHelper.TABLE_NAME;
        Cursor cursor = mWritableDatabase.rawQuery(getAllRows, null);
        assertNotNull(cursor);
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            assertTrue(count > 0);
            Log.i("count:", count + "");
            cursor.close();
        }
    }

    @SuppressWarnings("deprecation")
    @Test
    public void testGetTimeandDate() {
        String getTimeStamp = "SELECT " + mHelper.ADDED_ON + " FROM " + mHelper.TABLE_NAME;
        Cursor cursor = mWritableDatabase.rawQuery(getTimeStamp, null);
        Log.i("query", getTimeStamp);
        assertNotNull(cursor);
        if (cursor != null) {
            cursor.moveToFirst();
            String time = cursor.getString(0);
            Log.i("time", time + "");
        }
    }

    @Test
    public void testDelete() {

        // checking data exists in the table by checking the row count > 0
        String getAllRows = "SELECT * FROM " + mHelper.TABLE_NAME;
        Cursor cursor = mWritableDatabase.rawQuery(getAllRows, null);
        assertNotNull(cursor);
        int count = cursor.getCount();
        assertTrue(count > 0);
        Log.i("count:", count + "");
        cursor.close();

        // deleting row and proving row count == 0
        mWritableDatabase.delete(mHelper.TABLE_NAME,null,null);
        Cursor cursor2 = mWritableDatabase.rawQuery(getAllRows, null);
        assertNotNull(cursor2);
        int count2 = cursor2.getCount();
        Log.i("count2:", count2 + "");
        assertTrue(count2 == 0);
        cursor2.close();
        
    }
}


