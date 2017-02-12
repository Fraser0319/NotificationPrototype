package com.example.fraser.notaficationprototype.Fragments;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.fraser.notaficationprototype.Model.DatabaseHelper;
import com.example.fraser.notaficationprototype.Adapters.ImageViewCursorAdapter;
import com.example.fraser.notaficationprototype.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListIconsFragment extends Fragment {

    SQLiteOpenHelper imageDb;
    SQLiteDatabase db;
    ListView imageList;
    Cursor cursor;
    ImageViewCursorAdapter imageListAdapter;

    public ListIconsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewInflator = inflater.inflate(R.layout.fragment_list_icons, container, false);
        setUpDB(viewInflator);
        return viewInflator;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        db.close();
    }

    public void setUpDB(View view) {
        imageDb = new DatabaseHelper(getActivity());
        db = imageDb.getReadableDatabase();
        String getIcons = "SELECT * FROM IMAGE_NAMES";
        cursor = db.rawQuery(getIcons, null);
        imageListAdapter = new ImageViewCursorAdapter(getActivity(),cursor);
        imageList = (ListView) view.findViewById(R.id.iconListView);
        imageList.setAdapter(imageListAdapter);
    }


}
