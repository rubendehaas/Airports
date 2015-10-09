package com.example.ruben.airports;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Locale;

public class SettingsActivity extends Activity {
    private int spinnerInit = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //Back button
        getActionBar().setDisplayHomeAsUpEnabled(true);

        //Load spinner with a selection of languages
        languageSpinner();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void languageSpinner(){
        Spinner langSpinner = (Spinner) findViewById(R.id.spinner);
        langSpinner.setSelected(false);

        langSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                spinnerInit = spinnerInit+1;

                if(spinnerInit>1) {
                    SharedPreferences prefs = getSharedPreferences("com.example.airports.preference_lang", 0);

                    String[] langArray = getResources().getStringArray(R.array.lang_key);
                    String langKey = langArray[parent.getSelectedItemPosition()];

                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("lang", langKey);
                    editor.apply();



                    //Restart application to initialize the language
                    Intent i = getBaseContext().getPackageManager()
                            .getLaunchIntentForPackage(getBaseContext().getPackageName());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    System.exit(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
