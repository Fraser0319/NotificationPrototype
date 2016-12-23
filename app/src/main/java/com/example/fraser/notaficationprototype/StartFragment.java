package com.example.fraser.notaficationprototype;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment {

    private static Notification notification;
    private static NotificationManager manager;
    private static RemoteViews contentView;
    private static int emotionCounter = 0;
    private static int authenCounter = 0;
    private static int deviceCounter = 0;
    private static ArrayList<Integer> emotionList = new ArrayList<Integer>();
    private static ArrayList<Integer> authenList = new ArrayList<Integer>();
    private static ArrayList<Integer> devList = new ArrayList<Integer>();
    private static SQLiteOpenHelper authenticationDatabase;
    private static SQLiteDatabase db;
    private static DatabaseHelper dbHelper;


    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewInflator = inflater.inflate(R.layout.fragment_start, container, false);
        endNotification(viewInflator);
        startNotification(viewInflator);
        showSummary(viewInflator);

        return viewInflator;
    }

    public void startNotification(View v) {
        Button startNotification = (Button) v.findViewById(R.id.startButton);
        startNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast t = Toast.makeText(v.getContext(), "yay button click", Toast.LENGTH_LONG);
                t.show();
                sendNotification();
            }
        });

    }

    public void endNotification(View v) {
        Button endNotification = (Button) v.findViewById(R.id.endButton);
        endNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.cancel(1);
            }
        });
    }

    public void showSummary(View v) {
        Button summarybtn = (Button) v.findViewById(R.id.summaryButton);
        summarybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment summaryFragment = new SummaryFragment();

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.activity_main, summaryFragment);
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        });
    }

    public void setupButtons() {
        Collections.addAll(emotionList, R.drawable.happy, R.drawable.sad, R.drawable.confused);
        Collections.addAll(authenList, R.drawable.password, R.drawable.fingerprintscan, R.drawable.cursor, R.drawable.hand_gesture, R.drawable.id_card, R.drawable.key, R.drawable.contract, R.drawable.locked, R.drawable.ticket);
        Collections.addAll(devList, R.drawable.suv, R.drawable.metro, R.drawable.smartphone, R.drawable.mobile_phone, R.drawable.laptop, R.drawable.tramway, R.drawable.point_of_service, R.drawable.buses, R.drawable.atm, R.drawable.browser, R.drawable.locker, R.drawable.cycle);

        ButtonChangeReceiver bcr = new ButtonChangeReceiver();
        bcr.updateButton(emotionList, 0, R.id.emotionButton);
        bcr.updateButton(authenList, 0, R.id.authenticatorButton);
        bcr.updateButton(devList, 0, R.id.deviceButton);
    }

    @SuppressWarnings("deprecation")
    public void sendNotification() {

        authenticationDatabase = new DatabaseHelper(getActivity());
        db = authenticationDatabase.getReadableDatabase();

        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);

        contentView = new RemoteViews(getActivity().getPackageName(), R.layout.notification_view);
        contentView.setTextViewText(R.id.title, "title");

        // add intents for each button to rotate through each image.
        Intent changeEmotion = new Intent("changeEmotion");
        PendingIntent pendingChangeEmotion = PendingIntent.getBroadcast(getActivity(), 0, changeEmotion, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent changeAuthen = new Intent("changeAuthen");
        PendingIntent pendingChangeAuthen = PendingIntent.getBroadcast(getActivity(), 0, changeAuthen, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent changeDevice = new Intent("changeDevice");
        PendingIntent pendingChangeDevice = PendingIntent.getBroadcast(getActivity(), 0, changeDevice, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent confirmSelection = new Intent("confirm");
        PendingIntent pendingConfirmSelection = PendingIntent.getBroadcast(getActivity(), 0, confirmSelection, PendingIntent.FLAG_UPDATE_CURRENT);

        // build the notification
        notification = new NotificationCompat.Builder(getActivity())
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
        manager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
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

        public void sendIntent(Context context) {
            Bundle bundle = new Bundle();
            bundle.putInt("emotionButton", getCurrentButton(emotionList, emotionCounter));
            bundle.putInt("authenButton", getCurrentButton(authenList, authenCounter));
            bundle.putInt("devButton", getCurrentButton(devList, deviceCounter));

//            Fragment summaryFragment = new SummaryFragment();
//            summaryFragment.setArguments(bundle);
//            FragmentTransaction ft =
//            ft.replace(R.id.activity_main, summaryFragment);
//            ft.addToBackStack(null);
//            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            ft.commitAllowingStateLoss();

            dbHelper.insertAuthentication(db,getCurrentButton(devList, deviceCounter),getCurrentButton(authenList, authenCounter), getCurrentButton(emotionList, emotionCounter),null,null);
            Intent intent = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS); // close notification drawer after added
            context.sendBroadcast(intent);

            Toast.makeText(context,"Authentication Added !!",Toast.LENGTH_LONG).show();

        }

        public int getCurrentButton(ArrayList<Integer> imageList, int counter) {
            return imageList.get(counter);
        }

    }

}
