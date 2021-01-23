package com.musafi.map_line;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Route {

    /**
     * Provide the total distance of the route
     */
    public interface Distance_CallBack{
        void distance(float distance);
    }
    Distance_CallBack distance_callBack;
    GoogleMap gMap;
    Polyline polyline;
    Marker start, end;
    Context context;
    List<LatLng> latLngList;
    List<Marker>markerList;
    float distance;

    public Route(Context context, GoogleMap gMap, Distance_CallBack distance_callBack){
        latLngList = new ArrayList<>();
        markerList = new ArrayList<>();
        polyline = null;
        this.gMap = gMap;
        this.distance_callBack = distance_callBack;
        this.context = context;
    }

    /**
     * Create a route by the given locations
     * @param latLng is the given location
     * @param color of the route
     * @param icon_start is the icon of the first location
     * @param icon_end is the icon of the last location
     */
    public void addRoute(LatLng latLng, int color, int icon_start, int icon_end){
        latLngList.add(latLng);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
        Marker marker = gMap.addMarker(markerOptions);
        for(int i = 1; i < markerList.size(); i++)
            markerList.get(i).remove();

        if(start != null){
            end = marker;
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(context,icon_end)));
        }
        if(start == null){
            start = marker;
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(getBitmapFromVectorDrawable(context,icon_start)));

        }

        if(polyline != null)
            polyline.remove();
        PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).clickable(true);
        polyline = gMap.addPolyline(polylineOptions);
        polyline.setColor(color);

        if(latLngList.size() > 1) {
            float dis = calculate_distance(latLngList.get(latLngList.size() - 2), latLng);
            distance += dis;
            distance_callBack.distance(distance);
        }
        latLngList.add(latLng);
        markerList.add(marker);
    }

    /**
     * Remove the route from the map
     */
    public void removeRoute(){
        if(polyline!=null)
            polyline.remove();
        start = null;
        end = null;
        latLngList.clear();
        for(Marker marker : markerList)
            marker.remove();
        markerList.clear();
        distance = 0;
        distance_callBack.distance(distance);

    }

    private Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private float calculate_distance(LatLng a, LatLng b){
        UUID uuidA = UUID.randomUUID();
        Location locationA = new Location("location: "+ uuidA);
        locationA.setLatitude(a.latitude);
        locationA.setLongitude(a.longitude);
        UUID uuidB = UUID.randomUUID();
        Location locationB = new Location("location: "+ uuidB);
        locationB.setLatitude(b.latitude);
        locationB.setLongitude(b.longitude);
        return locationA.distanceTo(locationB);
    }
}
