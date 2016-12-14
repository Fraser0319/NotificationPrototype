package com.example.fraser.notaficationprototype;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


/**
 * Created by Fraser on 13/12/2016.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseInstrumentedTest {

    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;

    @Before
    public void setUp() throws Exception {
        mHelper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
        mDatabase = mHelper.getReadableDatabase();

    }

    @After
    public void finish() {
        mHelper.close();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mHelper);
    }

    @Test
    public void testInsertAuthentication() {
        mHelper.insertAuthentication(mDatabase, R.drawable.smartphone, R.drawable.fingerprintscan, R.drawable.confused, "took ages", "Home");

        String getAllDataQuery = "SELECT * FROM " + mHelper.TABLE_NAME;

        Log.i("getAllDataQuery", getAllDataQuery);

        Cursor cursor = mDatabase.rawQuery(getAllDataQuery, null);

        if (cursor.moveToFirst()) {

            int deviceId = cursor.getInt(0);
            assertEquals(deviceId, R.drawable.smartphone);
        }
        cursor.close();
        mDatabase.close();
    }

}
