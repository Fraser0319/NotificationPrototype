package com.example.fraser.notaficationprototype;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Fraser on 17/12/2016.
 */

public class CustomCursorAdaptor extends CursorAdapter{


    public CustomCursorAdaptor(Context context, Cursor c) {
        super(context, c, 0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.authentication_list_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView deviceImage = (ImageView) view.findViewById(R.id.deviceImage);
        ImageView authenticationImage = (ImageView) view.findViewById(R.id.authenticationImage);
        ImageView emotionImage = (ImageView) view.findViewById(R.id.emotionImage);

        int device = cursor.getInt(cursor.getColumnIndex("DEVICE_RESOURCE_ID"));
        int authentication = cursor.getInt(cursor.getColumnIndex("AUTHENTICATOR_RESOURCE_ID"));
        int emotion = cursor.getInt(cursor.getColumnIndex("EMOTION_RESOURCE_ID"));

        deviceImage.setImageResource(device);
        authenticationImage.setImageResource(authentication);
        emotionImage.setImageResource(emotion);

        TextView dbID = (TextView) view.findViewById(R.id.dbID);
        String id = cursor.getString(cursor.getColumnIndex("_id"));
        dbID.setText(id);

    }
}
