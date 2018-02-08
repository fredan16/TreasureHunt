package com.example.mikaelpaavilainen.treasurehunt.class_event;

import android.util.Log;

import com.example.mikaelpaavilainen.treasurehunt.HTTPREQUEST.HttpRequest;

/**
 * Created by mikael.paavilainen on 2018-01-30.
 */

public class UserPos extends HttpRequest {
    double[] PositionX = new double[10]; //10 asize
    double[] PositionY = new double[10]; //10 asize
    int current = 0;

    public double xSum;
    public double ySum;
    public boolean startCheck;

    public void addPosition(double lat, double lng){
        PositionX[current] = lat;
        PositionY[current] = lng;
        current++;
        //Log.d("GMAP4", "addPosition: current" + current);
        if(current == 10){
            startCheck = true;
            current = 0;
        }
        if (startCheck == true){
            //Log.d("GMAP5", "addPosition: startcheck " + startCheck);
            for (int i = 0;i < 10;i++){
                xSum += PositionX[i];
                ySum += PositionY[i];

            }
            xSum = xSum/10;
            ySum = ySum/10;
            Log.d("GMAP2", "addPosition: xSum " + xSum + " ySum" + ySum);
        }

    }
}


