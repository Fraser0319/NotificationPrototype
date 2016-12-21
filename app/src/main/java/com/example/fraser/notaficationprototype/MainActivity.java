package com.example.fraser.notaficationprototype;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.activity_main,new StartFragment());
        ft.addToBackStack(null);
        ft.commit();
        //showSummary();
    }

}
