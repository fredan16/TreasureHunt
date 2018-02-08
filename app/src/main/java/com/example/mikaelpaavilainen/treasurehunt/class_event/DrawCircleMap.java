package com.example.mikaelpaavilainen.treasurehunt.class_event;

import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by mikael.paavilainen on 2018-01-23.
 */

public class DrawCircleMap {

    Circle circle;
    CircleOptions circleOptions = new CircleOptions();
    public void drawCircle(LatLng point, GoogleMap mMap,int size,int strokeColor,int fyllColor,int width){
        Log.d("GMAP", "drawCircle: " + point);
        // Instantiating CircleOptions to draw a circle around the marker


        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(size);

        // Border color of the circle
        circleOptions.strokeColor(strokeColor);

        // Fill color of the circle
        circleOptions.fillColor(fyllColor);

        // Border width of the circle
        circleOptions.strokeWidth(width);

        // Adding the circle to the GoogleMap
        Log.d("GMAP", "drawCircle: Added Circle");
        circle = mMap.addCircle(circleOptions);

    }
    public void setCenter( LatLng L){
        circle.setCenter(L);
        Log.d("GMAP", "setCenter: new center");
    }
    public LatLng getCenter(){
        return circle.getCenter();
    }
}
