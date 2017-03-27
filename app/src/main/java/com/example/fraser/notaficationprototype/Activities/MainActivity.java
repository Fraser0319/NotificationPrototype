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

    /**
     * Part of the activity lifecycle which is the first method called when the activity is strated.
     * here we set up the listeners for all the buttons on the actvity and set up the all the tabs.
     * along with that we get the toggled state of the play and stop button. this is very important
     * if the user has closed the app so the buttons know what state to be in, depending on whether
     * the notifiation service is running or not.
     *
     * @param savedInstanceState
     */

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

    /**
     * This method takes whatever menu item has been selected and starts an intent to move to the
     * send data activity.
     * @param item
     * @return
     */

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

    /**
     * This method is used for setting the toggle of the play and stop buttons, depending on whether
     * the notification service is running or not.
     *
     * @return
     */

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

    /**
     * simple method for setting the listeners for the play and stop buttons for the notification
     * service.
     */
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

    /**
     * method for setting up the tabs on the toolbar. this simple adds the tabs and toolbar to the
     * top of the activity.
     */

    private void setUpTabs() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * the viewpager is passed in which, then uses a custom pager adapter in order to add a fragment
     * to a tab in the screen. this quite a flexable method which allows easy addtions of new tabs.
     *
     * method based on tutorial : http://www.androidhive.info/2015/09/android-material-design-working-with-tabs/
     * @param viewPager
     */

    private void setUpPager(ViewPager viewPager) {
        CustomPagerAdapter cpa = new CustomPagerAdapter(getSupportFragmentManager());
        cpa.addFragment(new SummaryFragment(), "Summary");
        //cpa.addFragment(new ListDBFragment(), "ImageDB");
        cpa.addFragment(new ListIconsFragment(), "Icons");
        viewPager.setAdapter(cpa);
    }

    /**
     * Permission method
     *
     * From android 6.0 in order to gain access to the read and write permissions for operations
     * to main storge of the device, android added this new way of prompting the user to accept
     * allow these permissions before using the application. if they do not allow this, the feature
     * that needs this permission will not be functional.
     *
     * solution found here: http://stackoverflow.com/a/37038313
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

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
