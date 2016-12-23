package com.example.fraser.notaficationprototype;


import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailedViewFragment extends Fragment{

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private SQLiteOpenHelper authenticationDatabase;

    public DetailedViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View myInflatedView = inflater.inflate(R.layout.fragment_detailed_view, container, false);
        getSelectedItemDetails(myInflatedView);
        return myInflatedView;
    }


    public void getSelectedItemDetails(View v){
        authenticationDatabase = new DatabaseHelper(getActivity());
        db = authenticationDatabase.getReadableDatabase();

        int device = getArguments().getInt("device");
        TextView deviceName = (TextView) v.findViewById(R.id.deviceName);

        String getImageName = "SELECT NAME FROM IMAGE_NAMES WHERE IMAGE_ID = " + device;
        Cursor cursor = db.rawQuery(getImageName,null);

        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex("NAME"));
            deviceName.setText(name);
        }else {
            deviceName.setText("Image Unavailable");
            Toast.makeText(getActivity(),"DATABASE UNAVAILABLE",Toast.LENGTH_LONG).show();
        }
    }

    public void onDestroyView(){
        super.onDestroyView();
        db.close();
    }

}
