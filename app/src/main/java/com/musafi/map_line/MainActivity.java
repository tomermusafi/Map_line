package com.musafi.map_line;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.WeightedLatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import java.util.UUID;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gMap;
    Button main_btn_remove;
    TextView main_txt_distance;
    Route route;
    Heat_Map heat_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        main_txt_distance = findViewById(R.id.main_txt_distance);
        main_btn_remove = findViewById(R.id.main_btn_remove);

        main_btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                route.removeRoute();
                heat_map.show();
            }
        });



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        if(route == null) {
            route = new Route(MainActivity.this, googleMap, new Route.Distance_CallBack() {
                @Override
                public void distance(float distance) {
                    main_txt_distance.setText("Distance: "+distance+"m");
                }
            });
        }
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                route.addRoute(latLng, R.color.purple_700,R.drawable.ic_baseline_brightness_1_24,R.drawable.walk_person );
            }
        });

        heat_map = new Heat_Map(generateHeatMapData2(), gMap);
        heat_map.setRadius(50).setColors(new int[]{Color.BLUE,Color.YELLOW, Color.parseColor("#FF0000")}, new float[]{0.2f,0.6f, 1f}).show();

    }

    private ArrayList<WeightedLatLng> generateHeatMapData() {
        ArrayList<WeightedLatLng> data = new ArrayList<>();
        // call our function which gets json data from our asset file
        JSONArray jsonData = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            jsonData = getJsonDataFromAsset("district_data.json");
        }

        // ensure null safety with let call
        double lat = 0,lon = 0,density = 0;
            // loop over each json object
            for (int i = 0; i < jsonData.length(); i++) {
                // parse each json object
                JSONObject entry = null;
                try {
                    entry = jsonData.getJSONObject(i);
                     lat = entry.getDouble("lat");
                     lon = entry.getDouble("lon");
                     density = entry.getDouble("density");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                // optional: remove edge cases like 0 population density values
                if (density != 0.0) {
                    WeightedLatLng weightedLatLng =new WeightedLatLng(new LatLng(lat, lon), density);
                    data.add(weightedLatLng);
                }
            }


        return data;
    }

    private ArrayList<WeightedLatLng> generateHeatMapData2() {
        ArrayList<WeightedLatLng> data = new ArrayList<>();
        // call our function which gets json data from our asset file
        JSONArray jsonData = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            jsonData = getJsonDataFromAsset("data.json");
        }

        // ensure null safety with let call
        double pop = 0;
        LatLng ll = null;
        // loop over each json object
        for (int i = 0; i < jsonData.length(); i++) {
            // parse each json object
            JSONObject entry = null;
            try {
                entry = jsonData.getJSONObject(i);
                ll =func1(entry.getString("location"));
                pop = entry.getDouble("pop");
            } catch (JSONException e) {
                e.printStackTrace();
            }


            // optional: remove edge cases like 0 population density values
            if (pop != 0.0) {
                WeightedLatLng weightedLatLng =new WeightedLatLng(ll, pop);
                data.add(weightedLatLng);
            }
        }


        return data;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private JSONArray getJsonDataFromAsset(String fileName)  {
        try {
            String jsonString = new BufferedReader(
                    new InputStreamReader(getAssets().open(fileName))).lines().collect(Collectors.joining());
            Log.d("pttt","st: "+ jsonString);
            return new JSONArray(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private LatLng func1(String loc){
       String l [] = loc.split("q=")[1].split(",");
       return new LatLng(Float.parseFloat(l[0]),Float.parseFloat(l[1]));
    }

}