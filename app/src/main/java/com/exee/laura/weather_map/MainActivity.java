package com.exee.laura.weather_map;

import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//https://openweathermap.org/city
public class MainActivity extends AppCompatActivity {
    JSONObject data = null;
    String[] array;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner = (Spinner)findViewById(R.id.spinner);
        array=getResources().getStringArray(R.array.City);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.City, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String city=array[(int)id];
                getJSON(""+city);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void getJSON(final String city) {

        new AsyncTask<Void, Void, Void>() {


            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=474038e12108c3d65159e5a9ce0d0903");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");
                    reader.close();

                    data = new JSONObject(json.toString());

                    if(data.getInt("cod") != 200) {
                        System.out.println("Cancelled");
                        return null;
                    }


                } catch (Exception e) {

                    System.out.println("Exception "+ e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void Void) {
                if(data!=null){
                    TextView tv=(TextView)findViewById(R.id.tv);
                    try {
                        JSONObject mainJSONObject = data.getJSONObject("main");
                        JSONObject mainJSONObject2 = data.getJSONObject("wind");
                        JSONObject mainJSONObject3 = data.getJSONObject("coord");
                        double temp = mainJSONObject.getDouble("temp");
                        double wind = mainJSONObject2.getDouble("speed");
                        String coordlon = mainJSONObject3.getString("lon");
                        String coordlat = mainJSONObject3.getString("lat");
                        double tempToCelsius =(temp-273.15);
                        tv.setText("\nTempToCelsius\n"+tempToCelsius +
                                    "\nSpeed "+wind+
                                        "\nCoord lon " +coordlon+
                                "\nCoord lat "+coordlat);
                    } catch (Exception e) {}
                    Log.d("my weather received",data.toString());
                }

            }
        }.execute();

    }
}