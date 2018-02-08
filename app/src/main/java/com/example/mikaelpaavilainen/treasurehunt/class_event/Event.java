package com.example.mikaelpaavilainen.treasurehunt.class_event;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.mikaelpaavilainen.treasurehunt.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by mikael.paavilainen on 2018-01-23.
 */

public class Event implements View.OnClickListener {

    private boolean showInfo;

    private static LatLng L;
    public static String name;
    private static int level;
    private Boolean showEvent = true;
    private int timerToShow;
    private GoogleMap mMap;
    private ImageView i;
    private Context context;
    private String answer;
    private String problem;

    public boolean collided = false;
    public boolean createdButton = false;

    //circle stuff
    int size;
    int width;
    int strokeColor;
    int fyllColor;
    int Score;
    int Worth;

    String Username;
    String UserHash;
    DrawCircleMap dcm = new DrawCircleMap();//draw circle on map for event



    public void init(double Longitud, double Latitud, String name, int hardship, int timerCount, GoogleMap mMap,int size,int strokeColor,int fyllColor, int width,String answer,String problem,int Worth){

        //init
        L = new LatLng(Longitud,Latitud); //set longitud/lat
        this.name = name; //name of event
        this.level = hardship; // how hard it is to complete
        this.timerToShow = timerCount;
        this.mMap = mMap;
        this.context = context;
        this.answer = answer;
        this.problem = problem;

        this.size = size;
        this.width = width;
        this.fyllColor = fyllColor;
        this.strokeColor = strokeColor;
        this.Score = Score;
        this.Worth = Worth;

        //extra stuff
        this.Username = Username;
        this.UserHash = UserHash;
    }


    public Event(Context current){
        this.context = current;
    }
    public void onDraw(){
        //Text

        //Circle


        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.cross);


        int w = bmp.getWidth();

        int h = bmp.getHeight();

        double imageWitdh = (h);
        Log.d("GMAP", "onDraw: " + R.drawable.cross);
        if (showInfo){
            LatLng newL = new LatLng(L.latitude+(h/2),L.longitude);
            Log.d("GMAP", "onDraw: " + newL + " || " +L);
            mMap.addMarker(new MarkerOptions()
            .position(L)
            .title("Fucker 50 - airplane")
            .snippet("Population: 42m")
            .visible(showEvent));
        }


        //dcm2.drawCircle(L,mMap,size+(size/1852),Color.GREEN,fyllColor,width);
        dcm.drawCircle(L,mMap,size,strokeColor,fyllColor,width);


    }
    public void setPosition(double lat, double lng){
        dcm.setCenter(new LatLng(lat,lng));


    }
    public LatLng getCenter(){
        return dcm.getCenter();
    }
    public double getwidth(){
        double lenght = dcm.getCenter().latitude * 2;
        lenght = lenght/2;
        return lenght;
    }
    public int eventHitbox(){
        return size * 1852;
    }

    @Override
    public void onClick(View v) {

    }
    public Intent startEvent(){
        Intent in = new Intent(context,EventHandler.class);
        in.putExtra("name",this.name);
        String temp1 = String.valueOf(this.level);
        String temp2 = String.valueOf(this.timerToShow);
        String temp3 = String.valueOf(this.answer);
        String temp4 = String.valueOf(this.problem);
        in.putExtra("level",temp1);
        in.putExtra("timer",temp2);
        in.putExtra("answer",temp3);
        in.putExtra("problem",temp4);
        in.putExtra("score",Score);
        in.putExtra("worth",Worth);
        return in;
    }


}
