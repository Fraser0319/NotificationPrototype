package com.example.fraser.notaficationprototype.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.fraser.notaficationprototype.Model.DatabaseHelper;
import com.example.fraser.notaficationprototype.Model.ExportCSV;
import com.example.fraser.notaficationprototype.R;

import java.util.ArrayList;

public class SendDataActivity extends AppCompatActivity {

    private ExportCSV eCSV;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private SQLiteOpenHelper authenticationDatabase;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_send_data);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detailedViewToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBackIntnet = new Intent(getApplicationContext(), MainActivity.class);
                goBackIntnet.putExtra("state", getState());
                startActivity(goBackIntnet);
            }
        });
        setupSendButtonListeners();
        setUpDB();
        //setUpWebView();
    }

    public boolean getState() {
        Boolean state = getIntent().getExtras().getBoolean("serviceState");
        return state;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public void setUpDB() {
        authenticationDatabase = new DatabaseHelper(this);
        db = authenticationDatabase.getReadableDatabase();
        dbHelper = new DatabaseHelper(this);
    }

//    public void setUpWebView() {
//        webView = (WebView) findViewById(R.id.webView);
//        webView.setWebViewClient(new MyBrowser());
//        String url = "http://54.229.99.58:3000";
//        //String url = "http://google.com";
//        webView.getSettings().setLoadsImagesAutomatically(true);
//        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        webView.loadUrl(url);
//    }
//
//    public void updateWeb(View v){
//        webView.reload();
//    }

    public void setupSendButtonListeners() {
        FloatingActionButton sendButton = (FloatingActionButton) findViewById(R.id.sendDataBtn);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataAlertDialog();
            }
        });

        ImageButton viewButton = (ImageButton) findViewById(R.id
                .viewDataBtn);
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                setUpWebView();
                Log.e("click on doc","here");
            }
        });
    }

//    public void viewCSV(View v) {
//        Button sendButton = (Button) v.findViewById(R.id.viewCSVButton);
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.e("onClick","viewCSV");
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.replace(R.id.actvity_main, new ViewCSVFragment());
//                ft.addToBackStack(null);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                ft.commit();
//            }
//        });
//    }

    public boolean checkValidEmail(CharSequence emailInput) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches();
    }

    public void sendDataAlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you agree to send your recored data ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        EditText inputEmail = (EditText) findViewById(R.id.inputEmailAddress);
                        String email = inputEmail.getText().toString();

                        if (checkValidEmail(email)) {
                            // send data !
                            final Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                            emailIntent.setType("plain/text");
                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Collected Data From Authentication Diary");
                            emailIntent.putExtra(Intent.EXTRA_TEXT, "This the data collected from the study in a CSV file");

                            // send as email attachment
                            ArrayList<Uri> uris = new ArrayList<Uri>();
                            eCSV = new ExportCSV(dbHelper.getAllAuthentications(db));
                            uris.add(Uri.fromFile(eCSV.generateCSV(getApplicationContext())));
                            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                            startActivityForResult(Intent.createChooser(emailIntent, "Sending multiple attachment"), 123);
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You must accept in order to complete the study", Toast.LENGTH_LONG).show();
                    }
                });
        builder.create();
        builder.show();
    }
//
//    private class MyBrowser extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
//    }
}
