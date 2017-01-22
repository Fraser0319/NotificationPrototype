package com.example.fraser.notaficationprototype.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.Spinner;

import com.example.fraser.notaficationprototype.R;

/**
 * Created by Fraser on 22/01/2017.
 */

public class CustomSpinnerAdapter extends CursorAdapter {
    private ArrayAdapter<String> arrayAdapter;

    public CustomSpinnerAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Spinner locationSpinner = (Spinner) view.findViewById(R.id.spinnerLocation);

        String locationsdb = cursor.getString(cursor.getColumnIndex("LOCATION"));
        Log.e("Location",locationsdb);
    }
}
