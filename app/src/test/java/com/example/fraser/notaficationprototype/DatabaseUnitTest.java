package com.example.fraser.notaficationprototype;

import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)

/**
 * Created by Fraser on 13/12/2016.
 */

public class DatabaseUnitTest {

    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;

    @Before
    public void setUp() throws Exception {
        mHelper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
        mDatabase = mHelper.getReadableDatabase();

    }

    @After
    public void finish(){
        mHelper.close();
    }

    @Test
    public void testPreConditions(){
        assertNotNull(mHelper);
    }

    @Test
    public void testInsertAuthentication(){
        mHelper.insertAuthentication(mDatabase, R.drawable.smartphone, R.drawable.fingerprintscan, R.drawable.confused, "took ages", "Home");
    }

}
