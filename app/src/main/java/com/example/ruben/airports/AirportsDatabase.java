package com.example.ruben.airports;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Ruben on 02/10/2015.
 */
public class AirportsDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "airports(2).sqlite";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    public AirportsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Cursor getAirports(){

        db = getReadableDatabase();

        String query = "Select icao as _id, * FROM airports WHERE iso_country = \"JP\"";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }

    public Cursor getFilteredAirports(CharSequence contraint){
        String query = "SELECT icao as _id, * FROM airports WHERE iso_country = \"JP\" AND (icao LIKE '%"+contraint+"%' OR name LIKE '%"+contraint+"%')";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        return c;
    }
}
