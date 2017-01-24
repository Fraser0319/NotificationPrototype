package com.example.fraser.notaficationprototype.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fraser.notaficationprototype.Adapters.CustomPagerAdapter;
import com.example.fraser.notaficationprototype.Fragments.ListIconsFragment;
import com.example.fraser.notaficationprototype.Fragments.SendDataFragment;
import com.example.fraser.notaficationprototype.Fragments.SummaryFragment;
import com.example.fraser.notaficationprototype.Model.NotificationForgroundService;
import com.example.fraser.notaficationprototype.R;


public class MainActivity extends AppCompatActivity  {

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private  ImageButton startNotification;
    private ImageButton endNotification;
    private CustomPagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CustomPagerAdapter cpa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        setUpTabs();
        setUpStartEndListeners();
    }

    private void setUpStartEndListeners(){
        startNotification = (ImageButton) findViewById(R.id.start_notification);
        startNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNotification.setImageResource(R.drawable.play_grey);
                endNotification.setImageResource(R.drawable.play_white);
                Intent service = new Intent(getApplicationContext(), NotificationForgroundService.class);
                service.setAction("startForeground");
                startService(service);
            }
        });

        endNotification = (ImageButton) findViewById(R.id.end_notification);
        endNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endNotification.setImageResource(R.drawable.play_white);
                startNotification.setImageResource(R.drawable.stop_grey);
                Intent intent = new Intent(getApplicationContext(), NotificationForgroundService.class);
                stopService(intent);
                Toast.makeText(getApplicationContext(), "service stopped", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setUpTabs(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpPager(ViewPager viewPager){
        CustomPagerAdapter cpa = new CustomPagerAdapter(getSupportFragmentManager());
        cpa.addFragment(new SummaryFragment(), "Summary");
        cpa.addFragment(new SendDataFragment(), "Send Data");
        cpa.addFragment(new ListIconsFragment(), "Icons");
        viewPager.setAdapter(cpa);
    }

    //persmission method.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

//        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
//        alertBuilder.setCancelable(true);
//        alertBuilder.setTitle("Permission necessary");
//        alertBuilder.setMessage("External storage permission is necessary");
//        alertBuilder.create();
//        alertBuilder.show();

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {

            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }else{

        }
    }


}
