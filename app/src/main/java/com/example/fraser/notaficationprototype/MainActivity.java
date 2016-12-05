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
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static Notification notification;
    private static NotificationManager manager;
    private static RemoteViews contentView;
    private static int emotionCounter = 0;

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

    public void setupButtons(){
        contentView.setImageViewResource(R.id.emotionButton,R.drawable.happy);
    }

    @SuppressWarnings("deprecation")
    public void sendNotification(){

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        contentView = new RemoteViews(getPackageName(),R.layout.notification_view);
        contentView.setTextViewText(R.id.title, "title");

        // add intents for each button to rotate through each image.
        Intent changeEmotion = new Intent("changeEmotion");
        PendingIntent pendingChangeEmotion = PendingIntent.getBroadcast(this,0,changeEmotion,0);
        // build the notification
        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.diary)
                .setContent(contentView)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setContentTitle("custom View").build();
        // add onclickpending intent listeners for each button
        contentView.setOnClickPendingIntent(R.id.emotionButton,pendingChangeEmotion);
        setupButtons();
        Log.e("here","here");
        notification.contentView = contentView;
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }

    public static class ChangeEmotionRecevicer extends BroadcastReceiver {

        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("reciverAction",intent.getAction());
            Integer [] images = new Integer[]{R.drawable.sad,R.drawable.confused,R.drawable.happy,};
            List<Integer> imageList = Arrays.asList(images);
            if(emotionCounter < imageList.size()){
                imageList.get(emotionCounter);
                contentView.setImageViewResource(R.id.emotionButton,imageList.get(emotionCounter));
                emotionCounter++;
            }else {
                emotionCounter = 0;
            }
            notification.contentView = contentView;
            manager.notify(1,notification);
        }
    }
}
