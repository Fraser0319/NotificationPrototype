package com.example.fraser.notaficationprototype;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */

public class SummaryFragment extends Fragment {
    private SQLiteDatabase db;
    private Cursor cursor;
    private DatabaseHelper dbHelper;

    public SummaryFragment(){
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View viewInflator = inflater.inflate(R.layout.fragment_summary, container, false);
        SQLiteOpenHelper authenticationDatabase = new DatabaseHelper(getActivity());
        db = authenticationDatabase.getReadableDatabase();
        insertSelectionToDB();
        generateList(viewInflator);
        return viewInflator;
    }

    public void insertSelectionToDB() {
        if(getArguments() != null){
            int emotionButton = getArguments().getInt("emotionButton");
            int authenButton = getArguments().getInt("authenButton");
            int deviceButton = getArguments().getInt("devButton");
            dbHelper.insertAuthentication(db, deviceButton, authenButton, emotionButton, null, null);
        }
    }

    public void generateList(View v) {

        ListView authenList = (ListView) v.findViewById(R.id.authenList);
        try {
            String getAllAuthentications = "SELECT * FROM AUTHENTICATION";
            cursor = db.rawQuery(getAllAuthentications, null);
            CustomCursorAdaptor listAdapter = new CustomCursorAdaptor(getActivity(), cursor);
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
                        bundle.putInt("device", device);
                        bundle.putInt("auhen", authenticator);
                        bundle.putInt("emotion", emotion);
                        bundle.putString("location", location);
                        bundle.putString("comment", comments);
                        Fragment detailedViewFragment = new DetailedViewFragment();
                        detailedViewFragment.setArguments(bundle);

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.activity_main, detailedViewFragment);
                        ft.addToBackStack(null);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();

                    }
                    itemClickCursor.close();
                }
            });
            authenList.setAdapter(listAdapter);
        } catch (SQLiteException e) {
            Toast.makeText(getActivity(), "Database Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.close();
    }
}
