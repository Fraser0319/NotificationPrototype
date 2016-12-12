package com.example.fraser.notaficationprototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        reciveSelection();
    }

    public void reciveSelection(){
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            int emotionButton = (int) extras.get("emotionButton");
            TextView result = (TextView) findViewById(R.id.result);
            result.setText(emotionButton);
        }
    }
}
