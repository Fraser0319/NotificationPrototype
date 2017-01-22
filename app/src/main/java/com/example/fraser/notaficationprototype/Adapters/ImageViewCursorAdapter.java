package com.example.fraser.notaficationprototype.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fraser.notaficationprototype.R;

/**
 * Created by Fraser on 15/01/2017.
 */

public class ImageViewCursorAdapter extends CursorAdapter {

    public ImageViewCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.icon_image_list_view, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ImageView iconImage = (ImageView) view.findViewById(R.id.iconListImage);
        TextView iconName = (TextView) view.findViewById(R.id.iconListName);

        int icon = cursor.getInt(cursor.getColumnIndex("IMAGE_ID"));
        String name = cursor.getString(cursor.getColumnIndex("NAME"));

        iconImage.setImageResource(icon);
        iconName.setText(name);
    }
}
