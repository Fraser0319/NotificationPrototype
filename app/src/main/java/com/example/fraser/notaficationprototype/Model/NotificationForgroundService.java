package com.example.fraser.notaficationprototype.Model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.fraser.notaficationprototype.Activities.EditAuthenticationActivity;
import com.example.fraser.notaficationprototype.Activities.MainActivity;
import com.example.fraser.notaficationprototype.R;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Fraser on 30/12/2016.
 */

public class NotificationForgroundService extends Service {

    private Notification notification;
    private NotificationManager manager;
    private RemoteViews contentView;
    private int emotionCounter = 0;
    private int authenCounter = 0;
    private int deviceCounter = 0;
    private ArrayList<Integer> emotionList = new ArrayList<Integer>();
    private ArrayList<Integer> authenList = new ArrayList<Integer>();
    private ArrayList<Integer> devList = new ArrayList<Integer>();
    private SQLiteOpenHelper authenticationDatabase;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case "startForeground":
                sendNotification();
                Toast.makeText(this, "service started", Toast.LENGTH_LONG).show();
            case "changeEmotion":
                if (emotionCounter == emotionList.size() - 1) {
                    emotionCounter = 0;
                    updateButton(emotionList, emotionCounter, R.id.emotionButton);
                } else {
                    emotionCounter++;
                    updateButton(emotionList, emotionCounter, R.id.emotionButton);
                }
                break;
            case "changeAuthen":
                if (authenCounter == authenList.size() - 1) {
                    authenCounter = 0;
                    updateButton(authenList, authenCounter, R.id.authenticatorButton);
                } else {
                    authenCounter++;
                    updateButton(authenList, authenCounter, R.id.authenticatorButton);
                }
                break;
            case "changeDevice":
                if (deviceCounter == devList.size() - 1) {
                    deviceCounter = 0;
                    updateButton(devList, deviceCounter, R.id.deviceButton);
                } else {
                    deviceCounter++;
                    updateButton(devList, deviceCounter, R.id.deviceButton);
                }
                break;
            case "confirm":
                Log.e("here", "confirm");
                sendIntent();
                break;
        }
        return START_STICKY;
    }

    public void sendIntent() {
        dbHelper = new DatabaseHelper(this);

        dbHelper.insertAuthentication(db, getCurrentButton(devList, deviceCounter), getCurrentButton(authenList, authenCounter), getCurrentButton(emotionList, emotionCounter), null, null);

        String devName = dbHelper.getImageName(db, getCurrentButton(devList, deviceCounter));
        String authenName = dbHelper.getImageName(db, getCurrentButton(authenList, authenCounter));
        String emoName = dbHelper.getImageName(db, getCurrentButton(emotionList, emotionCounter));

        Intent sendIntent = new Intent("updateList");
        sendIntent.setAction("updateListView");
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.sendBroadcast(sendIntent);

        Log.e("devName", devName);
        Log.e("authenName", authenName);
        Log.e("emoName", emoName);

        if (devName.equals("Other") || authenName.equals("Other") || emoName.equals("Other")) {
            Intent openEditActivity = new Intent(this, EditAuthenticationActivity.class);
            Long id = dbHelper.getMaxID(db);
            Bundle bundle = new Bundle();
            openEditActivity.putExtra("bundle", bundle);
            bundle.putLong("id", id);
            bundle.putInt("target", getCurrentButton(devList, deviceCounter));
            bundle.putInt("authen", getCurrentButton(authenList, authenCounter));
            bundle.putInt("emotion", getCurrentButton(emotionList, emotionCounter));
            openEditActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(openEditActivity);
        }

        Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); // close notification drawer after added
        this.sendBroadcast(intent);

        Toast.makeText(this, "Authentication Added !", Toast.LENGTH_SHORT).show();
    }

    public void setupButtons() {
        Collections.addAll(emotionList, R.drawable.happy, R.drawable.sad, R.drawable.confused, R.drawable.question_mark);
        Collections.addAll(authenList, R.drawable.password, R.drawable.fingerprintscan, R.drawable.mouse_click, R.drawable.hand_gesture, R.drawable.id_card, R.drawable.key, R.drawable.contract,R.drawable.dial,R.drawable.blank_card, R.drawable.ticket,R.drawable.two_step_32x32, R.drawable.question_mark);
        Collections.addAll(devList, R.drawable.suv, R.drawable.metro,R.drawable.cycle, R.drawable.smartphone, R.drawable.mobile_phone, R.drawable.laptop, R.drawable.point_of_service,R.drawable.locked, R.drawable.atm, R.drawable.domain_registration, R.drawable.locker, R.drawable.door, R.drawable.tablet, R.drawable.question_mark);

        updateButton(emotionList, 0, R.id.emotionButton);
        updateButton(authenList, 0, R.id.authenticatorButton);
        updateButton(devList, 0, R.id.deviceButton);
    }

    @SuppressWarnings("deprecation")
    public void sendNotification() {
        authenticationDatabase = new DatabaseHelper(this);
        db = authenticationDatabase.getWritableDatabase();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        contentView = new RemoteViews(this.getPackageName(), R.layout.notification_view);
        contentView.setTextViewText(R.id.title, "title");

        // add intents for each button to rotate through each image.
        Intent changeEmotion = new Intent(this, NotificationForgroundService.class);
        changeEmotion.setAction("changeEmotion");
        PendingIntent pendingChangeEmotion = PendingIntent.getService(this, 0, changeEmotion, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent changeAuthen = new Intent(this, NotificationForgroundService.class);
        changeAuthen.setAction("changeAuthen");
        PendingIntent pendingChangeAuthen = PendingIntent.getService(this, 0, changeAuthen, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent changeDevice = new Intent(this, NotificationForgroundService.class);
        changeDevice.setAction("changeDevice");
        PendingIntent pendingChangeDevice = PendingIntent.getService(this, 0, changeDevice, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent confirmSelection = new Intent(this, NotificationForgroundService.class);
        confirmSelection.setAction("confirm");
        PendingIntent pendingConfirmSelection = PendingIntent.getService(this, 0, confirmSelection, PendingIntent.FLAG_UPDATE_CURRENT);

        // build the notification
        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notebook)
                .setContent(contentView)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setContentTitle("custom View").build();
        // add onclickpending intent listeners for each button
        contentView.setOnClickPendingIntent(R.id.emotionButton, pendingChangeEmotion);
        contentView.setOnClickPendingIntent(R.id.authenticatorButton, pendingChangeAuthen);
        contentView.setOnClickPendingIntent(R.id.deviceButton, pendingChangeDevice);
        contentView.setOnClickPendingIntent(R.id.confirmBtn, pendingConfirmSelection);
        notification.contentView = contentView;
        manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
        setupButtons();
        startForeground(1, notification);
    }

    public int getCurrentButton(ArrayList<Integer> imageList, int counter) {
        return imageList.get(counter);
    }

    @SuppressWarnings("deprecation")
    public boolean updateButton(ArrayList<Integer> imageList, int counter, int buttonID) {
        if (counter < imageList.size()) {
            contentView.setImageViewResource(buttonID, imageList.get(counter));
            notification.contentView = contentView;
            manager.notify(1, notification);
            return true;
        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Toast.makeText(this, "Service Detroyed!", Toast.LENGTH_SHORT).show();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

