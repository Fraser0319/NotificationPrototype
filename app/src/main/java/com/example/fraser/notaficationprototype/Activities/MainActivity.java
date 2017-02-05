package com.example.fraser.notaficationprototype.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fraser.notaficationprototype.Adapters.CustomPagerAdapter;
import com.example.fraser.notaficationprototype.Fragments.ListIconsFragment;
import com.example.fraser.notaficationprototype.Fragments.SummaryFragment;
import com.example.fraser.notaficationprototype.Model.NotificationForgroundService;
import com.example.fraser.notaficationprototype.R;


public class MainActivity extends AppCompatActivity {

    // Storage Permissions variables
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ImageButton startNotification;
    private ImageButton endNotification;
    private CustomPagerAdapter pagerAdapter;
    private ViewPager mViewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CustomPagerAdapter cpa;
    private static boolean serviceState;


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        setUpTabs();
        setUpStartEndListeners();
        if (getIntent().getExtras() != null) {
            serviceState = getIntent().getExtras().getBoolean("state");
        }
        getServiceState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.sendDataMenuItem) {
            Intent sendDataIntent = new Intent(this, SendDataActivity.class);
            sendDataIntent.putExtra("serviceState", serviceState);
            startActivity(sendDataIntent);
            Log.e("Menu Item Clicked", "send data clicked");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("serviceState", serviceState);
    }

    public boolean getServiceState() {
        SharedPreferences sharedPrefs = getSharedPreferences("com.example.fraser.notaficationprototype.Activities", MODE_PRIVATE);
        Log.e("service_state", serviceState + "");
        if (sharedPrefs.getBoolean("service_status", serviceState)) {
            startNotification.setEnabled(false);
            endNotification.setEnabled(true);
            endNotification.setAlpha((float) 1);
            startNotification.setAlpha((float) 0.2);
            return true;
        } else {
            endNotification.setEnabled(false);
            startNotification.setEnabled(true);
            startNotification.setAlpha((float) 1);
            endNotification.setAlpha((float) 0.2);
            return false;
        }
    }

    private void setUpStartEndListeners() {
        startNotification = (ImageButton) findViewById(R.id.start_notification);
        startNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceState = true;
                SharedPreferences.Editor editor = getSharedPreferences("com.example.fraser.notaficationprototype.Activities", MODE_PRIVATE).edit();
                editor.putBoolean("service_status", serviceState);
                editor.commit();

                getServiceState();
                Intent service = new Intent(getApplicationContext(), NotificationForgroundService.class);
                service.setAction("startForeground");
                startService(service);
            }
        });

        endNotification = (ImageButton) findViewById(R.id.end_notification);
        endNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceState = false;
                SharedPreferences.Editor editor = getSharedPreferences("com.example.fraser.notaficationprototype.Activities", MODE_PRIVATE).edit();
                editor.putBoolean("service_status", serviceState);
                editor.commit();

                getServiceState();
                Intent intent = new Intent(getApplicationContext(), NotificationForgroundService.class);
                stopService(intent);
                Toast.makeText(getApplicationContext(), "service stopped", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setUpTabs() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setUpPager(ViewPager viewPager) {
        CustomPagerAdapter cpa = new CustomPagerAdapter(getSupportFragmentManager());
        cpa.addFragment(new SummaryFragment(), "Summary");
        // cpa.addFragment(new SendDataFragment(), "Send Data");
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
        }
    }
}
