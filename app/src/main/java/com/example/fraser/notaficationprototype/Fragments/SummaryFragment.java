package com.example.fraser.notaficationprototype.Fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fraser.notaficationprototype.Activities.DetailedViewActivity;
import com.example.fraser.notaficationprototype.Adapters.CustomCursorAdaptor;
import com.example.fraser.notaficationprototype.Model.DatabaseHelper;
import com.example.fraser.notaficationprototype.R;

import static java.security.AccessController.getContext;

/**
 * A simple {@link Fragment} subclass.
 */

public class SummaryFragment extends Fragment {
    private SQLiteDatabase db;
    private Cursor cursor;
    private DatabaseHelper dbHelper;
    private static ListView authenList;
    private static CustomCursorAdaptor listAdapter;
    private View viewInflator;
    private SQLiteOpenHelper authenticationDatabase;

    public SummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflator = inflater.inflate(R.layout.fragment_summary, container, false);
        setUpDB();
        generateList(viewInflator);
        Log.e("onCreateCalled", "here");
        return viewInflator;
    }

    public void setUpDB() {
        authenticationDatabase = new DatabaseHelper(getActivity());
        db = authenticationDatabase.getReadableDatabase();
        String getAllAuthentications = "SELECT * FROM AUTHENTICATION";
        cursor = db.rawQuery(getAllAuthentications, null);
        listAdapter = new CustomCursorAdaptor(getActivity(), cursor);
        authenList = (ListView) viewInflator.findViewById(R.id.authenList);
        authenList.setAdapter(listAdapter);
    }
    public void generateList(View v) {

        try {
            authenList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String selectedAuthentication = "SELECT DEVICE_RESOURCE_ID,AUTHENTICATOR_RESOURCE_ID, " +
                            "EMOTION_RESOURCE_ID,LOCATION,COMMENTS FROM AUTHENTICATION WHERE _id = " + id;
                    Cursor itemClickCursor = db.rawQuery(selectedAuthentication, null);
                    if (itemClickCursor.moveToFirst()) {

                        int device = itemClickCursor.getInt(itemClickCursor.getColumnIndex("DEVICE_RESOURCE_ID"));
                        int authenticator = itemClickCursor.getInt(itemClickCursor.getColumnIndex("AUTHENTICATOR_RESOURCE_ID"));
                        int emotion = itemClickCursor.getInt(itemClickCursor.getColumnIndex("EMOTION_RESOURCE_ID"));
                        String location = itemClickCursor.getString(itemClickCursor.getColumnIndex("LOCATION"));
                        String comments = itemClickCursor.getString(itemClickCursor.getColumnIndex("COMMENTS"));

                        Bundle bundle = new Bundle();
                        bundle.putLong("id", id);
                        bundle.putInt("device", device);
                        bundle.putInt("auhen", authenticator);
                        bundle.putInt("emotion", emotion);
                        bundle.putString("location", location);
                        bundle.putString("comment", comments);

                        Intent detailedViewIntent = new Intent(getContext(),DetailedViewActivity.class);
                        detailedViewIntent.putExtra("bundle",bundle);
                        startActivity(detailedViewIntent);
//                        Fragment detailedViewFragment = new DetailedViewActivity();
//                        detailedViewFragment.setArguments(bundle);

//                        FragmentTransaction ft = getFragmentManager().beginTransaction();
//                        ft.replace(R.id.actvity_main, detailedViewFragment);
//                        ft.addToBackStack(null);
//                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                        ft.commit();

                    }
                    itemClickCursor.close();
                }
            });
        } catch (SQLiteException e) {
            Toast.makeText(getActivity(), "Database Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResumeCalled", "here");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("onDestroyCalled", "here");
        db.close();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("onPauseCalled", "here");
    }

    public static class myReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            SQLiteOpenHelper authenticationDatabase2 = new DatabaseHelper(context);
            SQLiteDatabase db2 = authenticationDatabase2.getReadableDatabase();
            String getAllAuthentications = "SELECT * FROM AUTHENTICATION";
            Cursor c = db2.rawQuery(getAllAuthentications, null);
            CustomCursorAdaptor listAdapter2 = new CustomCursorAdaptor(context, c);
            authenList.setAdapter(listAdapter2);
            db2.close();
        }
    }
}
