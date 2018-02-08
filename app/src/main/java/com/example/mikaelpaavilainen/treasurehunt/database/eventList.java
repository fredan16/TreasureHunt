package com.example.mikaelpaavilainen.treasurehunt.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.util.Log;

/**
 * Created by mikael.paavilainen on 2018-02-08.
 */

@Entity(tableName = "events")
public class eventList {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "json")
    public String json;

    public int getID(){
        return id;
    }
    public String getJson(){
        return json;
    }

    public void setJson(String Json){
        this.json = Json;
        Log.d("HTTPSJ", "setJson: " + json);
    }
}

