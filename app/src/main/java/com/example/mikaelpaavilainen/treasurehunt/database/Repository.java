package com.example.mikaelpaavilainen.treasurehunt.database;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.util.Log;

/**
 * Created by mikael.paavilainen on 2018-02-08.
 */

public class Repository extends Application {
    public static Repository INSTANCE;
    private static final String DATABASE_NAME = "MyDatabase";


    private Database database;

    public static Repository get() {
        return INSTANCE;
    }


    public void onCreate(Context context) {
        Log.d("app", "onCreate: ");
        super.onCreate();

        // create database
        database = Room.databaseBuilder(context, Database.class, DATABASE_NAME).allowMainThreadQueries().build();

        INSTANCE = this;
    }

    public Database getDB() {
        return database;
    }
}
