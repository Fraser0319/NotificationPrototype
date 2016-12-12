package com.example.fraser.notaficationprototype;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.media.CamcorderProfile.get;

public class MainActivity extends AppCompatActivity {

    private static Notification notification;
    private static NotificationManager manager;
    private static RemoteViews contentView;
    private static int emotionCounter = 0;
    private static int authenCounter = 0;
    private static int deviceCounter = 0;
    //private static ArrayList<Integer> sentList = new ArrayList<Integer>();
    private static ArrayList<Integer> emotionList = new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void helloToast(View v) {
        sendNotification();
        Toast t = Toast.makeText(v.getContext(), "yay button click", Toast.LENGTH_LONG);
        t.show();
    }

    public void setupButtons() {
        Collections.addAll(emotionList,R.drawable.happy, R.drawable.sad, R.drawable.confused );

        contentView.setImageViewResource(R.id.emotionButton, emotionList.get(0));
//        contentView.setImageViewResource(R.id.authenticatorButton, R.drawable.fingerprintscan);
//        contentView.setImageViewResource(R.id.deviceButton, R.drawable.smartphone);
    }

    @SuppressWarnings("deprecation")
    public void sendNotification() {



        List<Integer> authenList = new ArrayList<Integer>();
        Collections.addAll(authenList, R.drawable.sad, R.drawable.confused, R.drawable.happy);

        List<Integer> devList = new ArrayList<Integer>();
        Collections.addAll(devList, R.drawable.car, R.drawable.metro, R.drawable.smartphone);


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        contentView = new RemoteViews(getPackageName(), R.layout.notification_view);
        contentView.setTextViewText(R.id.title, "title");

        // add intents for each button to rotate through each image.
        Intent changeEmotion = new Intent("changeEmotion");
        //changeEmotion.putIntegerArrayListExtra("emoImgList", (ArrayList<Integer>) emotionList);
        PendingIntent pendingChangeEmotion = PendingIntent.getBroadcast(this, 0, changeEmotion, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent changeAuthen = new Intent("changeAuthen");
        changeAuthen.putIntegerArrayListExtra("authenImgList", (ArrayList<Integer>) authenList);
        PendingIntent pendingChangeAuthen = PendingIntent.getBroadcast(this, 0, changeAuthen, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent changeDevice = new Intent("changeDevice");
        changeDevice.putIntegerArrayListExtra("devImgList", (ArrayList<Integer>) devList);
        PendingIntent pendingChangeDevice = PendingIntent.getBroadcast(this, 0, changeDevice, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent confirmSelection = new Intent("confirm");
        PendingIntent pendingConfirmSelection = PendingIntent.getBroadcast(this, 0, confirmSelection, PendingIntent.FLAG_UPDATE_CURRENT);



        // build the notification
        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.diary)
                .setContent(contentView)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setContentTitle("custom View").build();
        // add onclickpending intent listeners for each button
        contentView.setOnClickPendingIntent(R.id.emotionButton, pendingChangeEmotion);
        contentView.setOnClickPendingIntent(R.id.authenticatorButton, pendingChangeAuthen);
        contentView.setOnClickPendingIntent(R.id.deviceButton, pendingChangeDevice);
        contentView.setOnClickPendingIntent(R.id.confirmBtn, pendingConfirmSelection);
        setupButtons();
        Log.e("here", "here");
        notification.contentView = contentView;
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
    }

    public static class ChangeEmotionRecevicer extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
           // Log.e("size",sentList.size()+"");
            Log.e("intent",intent.getAction());
            switch (intent.getAction()) {
                case "changeEmotion":

                   // Log.e("size",sentList.size()+"");
                    if (updateButton(emotionList, emotionCounter, R.id.emotionButton)) {
                        emotionCounter++;
                    } else {
                        emotionCounter = 0;
                    }
                    break;
                case "changeAuthentication":
                    ArrayList<Integer> authenImgList = intent.getIntegerArrayListExtra("authenImgList");
                    if (updateButton(authenImgList, authenCounter, R.id.authenticatorButton)) {
                        authenCounter++;
                    } else {
                        authenCounter = 0;
                    }
                    break;
                case "changeDevice":
                    ArrayList<Integer> devImgList = intent.getIntegerArrayListExtra("devImgList");
                    if (updateButton(devImgList, deviceCounter, R.id.deviceButton)) {
                        deviceCounter++;
                    } else {
                        deviceCounter = 0;
                    }
                    break;
                case "confirm":
                    Log.e("here","confirm");
                    sendIntent(context);
                    break;

            }
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

        public void sendIntent(Context context){

            Intent intent = new Intent(context,SummaryActivity.class);
            intent.putExtra("emotionButton",getCurrentButton(emotionList,emotionCounter));
//            intent.putExtra("authenButton",getCurrentButton(sentList,authenCounter));
//            intent.putExtra("devButton",getCurrentButton(sentList,deviceCounter));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        public int getCurrentButton(ArrayList<Integer> imageList, int counter){
            Log.e("counter",counter+"");
            return imageList.get(counter);
        }

    }
}
