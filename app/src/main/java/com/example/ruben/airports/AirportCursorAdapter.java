package com.example.ruben.airports;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Ruben on 02/10/2015.
 */
public class AirportCursorAdapter extends CursorAdapter {

    public AirportCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {


        View view = LayoutInflater.from(context).inflate(R.layout.listview_row, viewGroup, false);
        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name = (TextView) view.findViewById(R.id.aName);
        TextView icao = (TextView) view.findViewById(R.id.aIcao);

        String outputName = cursor.getString(cursor.getColumnIndex("name"));
        String outputIcao = cursor.getString(cursor.getColumnIndex("icao"));

        name.setText(outputName);
        icao.setText(outputIcao);
    }

}
