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


public class MainActivity extends AppCompatActivity {

    private static Notification notification;
    private static NotificationManager manager;
    private static RemoteViews contentView;
    private static int emotionCounter = 0;
    private static int authenCounter = 0;
    private static int deviceCounter = 0;
    private static ArrayList<Integer> emotionList = new ArrayList<Integer>();
    private static ArrayList<Integer> authenList = new ArrayList<Integer>();
    private static ArrayList<Integer> devList = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showSummary();
    }

    public void helloToast(View v) {
        sendNotification();
        Toast t = Toast.makeText(v.getContext(), "yay button click", Toast.LENGTH_LONG);
        t.show();
    }

    public void endNotification(View v){
        manager.cancel(1);
    }

    public void showSummary(){
        Button summarybtn = (Button) findViewById(R.id.summaryButton);
        summarybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewSummary = new Intent(v.getContext(),SummaryActivity.class);
                startActivity(viewSummary);
            }
        });
    }

    public void setupButtons() {
        Collections.addAll(emotionList,R.drawable.happy, R.drawable.sad, R.drawable.confused);
        Collections.addAll(authenList,R.drawable.password, R.drawable.fingerprintscan,R.drawable.cursor,R.drawable.hand_gesture,R.drawable.id_card,R.drawable.key,R.drawable.contract,R.drawable.locked,R.drawable.ticket);
        Collections.addAll(devList, R.drawable.suv, R.drawable.metro, R.drawable.smartphone,R.drawable.mobile_phone,R.drawable.laptop,R.drawable.tramway,R.drawable.point_of_service,R.drawable.buses,R.drawable.atm,R.drawable.browser,R.drawable.locker,R.drawable.cycle);

        ButtonChangeReceiver bcr = new ButtonChangeReceiver();
        bcr.updateButton(emotionList,0,R.id.emotionButton);
        bcr.updateButton(authenList,0,R.id.authenticatorButton);
        bcr.updateButton(devList,0,R.id.deviceButton);
    }

    @SuppressWarnings("deprecation")
    public void sendNotification() {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        contentView = new RemoteViews(getPackageName(), R.layout.notification_view);
        contentView.setTextViewText(R.id.title, "title");

        // add intents for each button to rotate through each image.
        Intent changeEmotion = new Intent("changeEmotion");
        PendingIntent pendingChangeEmotion = PendingIntent.getBroadcast(this, 0, changeEmotion, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent changeAuthen = new Intent("changeAuthen");
        PendingIntent pendingChangeAuthen = PendingIntent.getBroadcast(this, 0, changeAuthen, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent changeDevice = new Intent("changeDevice");
        PendingIntent pendingChangeDevice = PendingIntent.getBroadcast(this, 0, changeDevice, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent confirmSelection = new Intent("confirm");
        PendingIntent pendingConfirmSelection = PendingIntent.getBroadcast(this, 0, confirmSelection, PendingIntent.FLAG_UPDATE_CURRENT);

        // build the notification
        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.diary)
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
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notification);
        setupButtons();
    }

    public static class ButtonChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
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
                    if (authenCounter == authenList.size()-1) {
                        authenCounter = 0;
                        updateButton(authenList, authenCounter, R.id.authenticatorButton);
                    }else {
                        authenCounter++;
                        updateButton(authenList, authenCounter, R.id.authenticatorButton);
                    }
                    break;
                case "changeDevice":
                    if (deviceCounter == devList.size()-1) {
                        deviceCounter = 0;
                        updateButton(devList, deviceCounter, R.id.deviceButton);
                    }else {
                        deviceCounter++;
                        updateButton(devList, deviceCounter, R.id.deviceButton);
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
            intent.putExtra("authenButton",getCurrentButton(authenList,authenCounter));
            intent.putExtra("devButton",getCurrentButton(devList,deviceCounter));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }

        public int getCurrentButton(ArrayList<Integer> imageList, int counter){
            return imageList.get(counter);
        }

    }
}
