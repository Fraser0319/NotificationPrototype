package com.example.fraser.notaficationprototype;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

public class MainActivity extends AppCompatActivity {

    public static Notification notification;
    public static NotificationManager manager;
    public static RemoteViews contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @SuppressWarnings("deprecation")
    public void sendNotification(View v){

        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0,notificationIntent,0);

        contentView = new RemoteViews(getPackageName(),R.layout.notification_view);
        contentView.setTextViewText(R.id.title, "title");
        // add intents for each button to rotate through each image.

        // build the notification
        notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.diary)
                .setContent(contentView)
                .setAutoCancel(true)
                .setContentIntent(contentIntent)
                .setOngoing(true)
                .setContentTitle("custom View").build();
        // add onclickpending intent listeners for each button
        notification.contentView = contentView;
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1,notification);
    }
}
