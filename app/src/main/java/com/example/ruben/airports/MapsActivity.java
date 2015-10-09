package com.example.ruben.airports;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Locale;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double aLat;
    private double aLong;
    private ArrayList<Location> locations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Change the layout language
        setLanguage();

        setContentView(R.layout.activity_maps);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle airportBundle = getIntent().getExtras();
        aLat = airportBundle.getDouble("latitude");
        aLong = airportBundle.getDouble("longitude");

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {

        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();

                //Start page transition animation
                overridePendingTransition(R.anim.activity_return_in, R.anim.activity_return_out);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpMap() {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        try {

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            draw(latitude, longitude);


            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("My location"));
            mMap.addMarker(new MarkerOptions().position(new LatLng(aLat, aLong)).title("Airport"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void draw(double cLat, double cLong) {
        mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(cLat, cLong), new LatLng(aLat, aLong))
                .width(5)
                .geodesic(true)
                .color(Color.RED));
    }

    public void setLanguage(){

        SharedPreferences prefs = getSharedPreferences("com.example.airports.preference_lang", 0);
        String restoredLanguage = prefs.getString("lang", "");

        Resources res = getBaseContext().getResources();
        Locale locale = new Locale(restoredLanguage);
        Locale.setDefault(locale);
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = locale;
        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
    }
}
