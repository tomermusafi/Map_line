package com.musafi.map_line;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.heatmaps.WeightedLatLng;

import java.util.ArrayList;

public class Heat_Map {
    private Gradient gradient;
    private HeatmapTileProvider heatMapProvider;
    private int radius;
    private float maxIntensity;
    private float opacity;
    ArrayList<WeightedLatLng> data;
    GoogleMap googleMap;
    TileOverlay tileOverlay;

    public Heat_Map(ArrayList<WeightedLatLng> data, GoogleMap googleMap){
        this.data = data;
        this.googleMap = googleMap;
        HeatmapTileProvider heatMapProvider = new HeatmapTileProvider.Builder()
                .weightedData(this.data)
                .build();
        this.heatMapProvider = heatMapProvider;
    }

    /**
     * Both of the array should be at the same size.
     * @param colors of the heat map. the first is the lower and the last is the higher
     * @param startPoints should be order from the smallest to the biggest value.
     * @return Heat_Map object with gradient
     */
    public Heat_Map setColors(int[] colors,float[] startPoints) {
        Gradient gradient = new Gradient(colors, startPoints);
        this.gradient = gradient;
        heatMapProvider.setGradient(this.gradient);
        return this;
    }

    public Heat_Map setRadius(int radius) {
        this.radius = radius;
        heatMapProvider.setRadius(this.radius);
        return this;
    }

    /**
     * Set the maximum intensity
     * @param maxIntensity
     * @return Heat_Map with max intensity
     */
    public Heat_Map setMaxIntensity(float maxIntensity) {
        this.maxIntensity = maxIntensity;
        heatMapProvider.setMaxIntensity(this.maxIntensity);
        return this;
    }

    /**
     * Set the opacity, should be between 0 - 1
     * @param opacity
     * @return Heat_Map with opacity
     */
    public Heat_Map setOpacity(float opacity) {
        this.opacity = opacity;
        heatMapProvider.setOpacity(this.opacity);
        return this;
    }

    /**
     * Load the data into the heat map
     * @param data array of WeightedLatLng
     * @return Heat_Map with data
     */
    public Heat_Map setData(ArrayList<WeightedLatLng> data) {
        this.data = data;
        heatMapProvider.setWeightedData(this.data);
        return this;
    }

    /**
     * Remove the heat map from the map
     */
    public void remove(){
        tileOverlay.remove();
    }

    /**
     * Shows the heat map on the map
     */
    public void show(){
        if(tileOverlay != null)
            remove();
        tileOverlay = this.googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(heatMapProvider));

    }
}
