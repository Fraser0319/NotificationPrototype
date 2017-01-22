package com.example.fraser.notaficationprototype;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.fraser.notaficationprototype.Model.DatabaseHelper;
import com.opencsv.CSVWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

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
        Long insert_1 = mWritableDatabase.insert(mHelper.TABLE_NAME, null, authenticationValues);
        Log.i("insert_1", insert_1 + "");
        assertTrue(insert_1 != -1);
//        Long insert_2 = mWritableDatabase.insert(mHelper.TABLE_NAME, null, authenticationValues);
//        assertTrue(insert_2 != -1);
//        Log.i("insert_2",insert_2+"");
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
        cursor.moveToFirst();
        int deviceId = cursor.getInt(1);
        Log.i("id", "deviceID: " + deviceId);
        assertFalse(deviceId == R.drawable.sad);
        assertTrue(deviceId == R.drawable.smartphone);

        cursor.close();
    }

    @Test
    public void testGetAllRecords() {

        // checking data exists in the table by checking the row count > 0
        String getAllRows = "SELECT * FROM " + mHelper.TABLE_NAME;
        Cursor cursor = mWritableDatabase.rawQuery(getAllRows, null);
        assertNotNull(cursor);
        int count = cursor.getCount();
        assertTrue(count > 0);
        Log.i("count:", count + "");
        cursor.close();
    }

    @Test
    public void testGetTimeandDate() {

        // testing the output is in the correct format and actually uses the current date and time
        // when executed.
        String getTimeStamp = "SELECT " + mHelper.ADDED_ON + " FROM " + mHelper.TABLE_NAME;
        Cursor cursor = mWritableDatabase.rawQuery(getTimeStamp, null);
        Log.i("query", getTimeStamp);
        assertNotNull(cursor);
        cursor.moveToFirst();
        String time = cursor.getString(0);
        Log.i("time", time + "");

    }

    @Test
    public void testGetAuthenticationsWithComments() {

        String getAllRows = "SELECT * FROM " + mHelper.TABLE_NAME + " WHERE " + mHelper.COMMENTS + " IS NOT NULL";
        Cursor cursor = mWritableDatabase.rawQuery(getAllRows, null);
        assertNotNull(cursor);
        cursor.moveToFirst();
        String comment = cursor.getString(4);
        assertNotNull(comment);
        Log.i("Comment", comment);
        assertNotNull(comment);
        assertTrue(comment.equals("took ages"));
        cursor.close();
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
        mWritableDatabase.delete(mHelper.TABLE_NAME, null, null);
        Cursor cursor2 = mWritableDatabase.rawQuery(getAllRows, null);
        assertNotNull(cursor2);
        int count2 = cursor2.getCount();
        Log.i("count2:", count2 + "");
        assertTrue(count2 == 0);
        cursor2.close();
    }


    // checks if there no comment in a row.
    @Test
    public void checkNullComment() {

        String checkNullComment = "SELECT * FROM " + mHelper.TABLE_NAME + " WHERE " + mHelper.COMMENTS + " is ''";
        Cursor cursor = mWritableDatabase.rawQuery(checkNullComment, null);
        assertNotNull(cursor);
        cursor.moveToFirst();
        String comment = cursor.getString(4);
        Log.i("commentValue", comment);
        assertTrue(comment.isEmpty());
        cursor.close();
    }

    @Test
    public void exportDBToCSV() {

        try {
            File file = new File("/sdcard/dbCSV.csv");
            file.createNewFile();
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            String getAllRows = "SELECT * FROM " + mHelper.TABLE_NAME;
            Cursor cursor = mWritableDatabase.rawQuery(getAllRows, null);
            assertNotNull(cursor);
            cursor.moveToFirst();

            String device = String.valueOf(cursor.getInt(cursor .getColumnIndex("DEVICE_RESOURCE_ID")));
            String authenticator = String.valueOf(cursor .getInt(cursor .getColumnIndex("AUTHENTICATOR_RESOURCE_ID")));
            String emotion = String.valueOf(cursor .getInt(cursor .getColumnIndex("EMOTION_RESOURCE_ID")));
            String timeStsmp = cursor.getString(cursor.getColumnIndex("ADDED_ON"));
            String location = cursor .getString(cursor .getColumnIndex("LOCATION"));
            String comments = cursor .getString(cursor .getColumnIndex("COMMENTS"));

            String[] entries = {device,authenticator,emotion,timeStsmp,location,comments};

            writer.writeNext(cursor.getColumnNames());
            writer.writeNext(entries);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // as the _id is auto incrementing the id continues on for each test so i got what the next id would be
    // and commented out all other tests so i could test the query
    // it will be commenet out for now but it works.
//    @Test
//    public void testUpdateComment(){
//        // update the comment field
//        String updateComment = "UPDATE " + mHelper.TABLE_NAME + " SET " + mHelper.COMMENTS + " = 'YAY IT CHANCGED !!!' WHERE _id = 153";
//        mWritableDatabase.execSQL(updateComment);
//
//        // show its been updated
//        String getAllRows = "SELECT "+ mHelper.COMMENTS + " FROM " + mHelper.TABLE_NAME +  " WHERE _id = 153";
//        Cursor cursor = mWritableDatabase.rawQuery(getAllRows, null);
//        assertNotNull(cursor);
//        cursor.moveToFirst();
//        String comment = cursor.getString(0);
//        Log.i("commentTag",comment);
//        assertEquals("YAY IT CHANCGED !!!",comment);
//        cursor.close();
//    }


}


