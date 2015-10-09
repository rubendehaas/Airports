package com.example.ruben.airports;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.Locale;

public class MainActivity extends Activity {

    private AirportCursorAdapter airportCursor;
    private AirportsDatabase airportsDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Set the language for the application session
        setLanguage();

        //Set the title of the activity to the current title string
        setTitle(R.string.airport);
        setContentView(R.layout.activity_main);

        //Fill the ListView with data
        populateAirportList();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //Get the SearchView
        SearchView airportSearch = (SearchView) menu.findItem(R.id.action_search).getActionView();

        //On search input call the filterList() method
        //ListView will update on text change
        airportSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                filterList(newText);

                return true;
            }
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //Intent to the activity with the ID equal to the selected element
        switch(id){
            case R.id.action_settings:
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Cursor getAirportCursor(){

        //Set database in this activity
        airportsDatabase = new AirportsDatabase(getApplicationContext());
        Cursor cursor = airportsDatabase.getAirports();

        return cursor;
    }

    public void populateAirportList(){
        ListView airportList = (ListView) findViewById(R.id.listView);

        //Get the cursor from returned from the AirportCursorAdapter
        airportCursor = new AirportCursorAdapter(this, getAirportCursor(),false);

        airportList.setAdapter(airportCursor);
        airportList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //Intent to MapsActivity
                Intent mapIntent = new Intent(MainActivity.this, MapsActivity.class);

                //Get a cursor from airportCursor based on airportList item position
                Cursor airportItem = (Cursor) airportCursor.getItem(i);

                double laT = airportItem.getDouble(airportItem.getColumnIndex("latitude"));
                double longT = airportItem.getDouble(airportItem.getColumnIndex("longitude"));

                //Put extra information in the Intent
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", laT);
                bundle.putDouble("longitude", longT);
                mapIntent.putExtras(bundle);
                startActivity(mapIntent);

                //Start page transition animation
                overridePendingTransition(R.anim.activity_slide_in, R.anim.activity_slide_out);
            }
        });
    }

    public void filterList(String query){

        airportCursor.getFilter().filter(query);

        //Get airports from database based on constraint
        airportCursor.setFilterQueryProvider(new FilterQueryProvider() {

            public Cursor runQuery(CharSequence constraint) {

                Cursor cursor = null;

                if (constraint != null) {

                    cursor = airportsDatabase.getFilteredAirports(constraint);
                }

                return cursor;
            }

        });
    }

    public void setLanguage(){

        //Get the sharedpreferences with a key "lang"
        SharedPreferences prefs = getSharedPreferences("com.example.airports.preference_lang", 0);
        String restoredLanguage = prefs.getString("lang", "");

        //Set the locale with the specified language
        Resources res = getBaseContext().getResources();
        Locale locale = new Locale(restoredLanguage);
        Locale.setDefault(locale);
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = locale;
        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
    }
}
