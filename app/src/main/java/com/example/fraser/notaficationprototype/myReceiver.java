package com.example.fraser.notaficationprototype;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by Fraser on 15/01/2017.
 */

public class myReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"it works",Toast.LENGTH_SHORT).show();
        Log.e("onRecive", "here");
    }
}
