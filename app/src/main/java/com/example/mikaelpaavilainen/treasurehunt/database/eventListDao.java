package com.example.mikaelpaavilainen.treasurehunt.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by mikael.paavilainen on 2018-02-08.
 */

@SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
@Dao
public interface eventListDao {
    @Query("SELECT * FROM events")
    List<eventList> getAll();


    @Insert
    public void insert(eventList events);

    @Query("UPDATE events SET id=1")
    public void resetID();

    @Query("DELETE FROM events")
    public void deleteAll();

    @Query("SELECT Count(*) From events")
    public int countEvents();
}