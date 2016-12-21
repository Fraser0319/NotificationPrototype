package com.example.fraser.notaficationprototype;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailedViewFragment extends Fragment {


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

        int device = getArguments().getInt("device");
        TextView deviceName = (TextView) v.findViewById(R.id.deviceName);
        deviceName.setText(Integer.toString(device));

    }

}
