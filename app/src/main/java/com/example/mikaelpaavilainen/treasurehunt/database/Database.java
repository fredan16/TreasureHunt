package com.example.mikaelpaavilainen.treasurehunt.database;

import android.arch.persistence.room.RoomDatabase;

/**
 * Created by mikael.paavilainen on 2018-01-30.
 */

@android.arch.persistence.room.Database(entities = {eventList.class},version = 1)
public abstract class Database extends RoomDatabase{
    public abstract eventListDao eventListDao();

}