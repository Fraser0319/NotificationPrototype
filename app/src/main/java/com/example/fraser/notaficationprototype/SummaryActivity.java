package com.example.fraser.notaficationprototype;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;




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
            int authenButton = (int) extras.get("authenButton");
            int deviceButton = (int) extras.get("devButton");
            TextView emo = (TextView) findViewById(R.id.emotion);
            TextView authen = (TextView) findViewById(R.id.authen);
            TextView device = (TextView) findViewById(R.id.device);

            emo.setText(emotionButton);
            authen.setText(authenButton);
            device.setText(deviceButton);
        }
    }
}
