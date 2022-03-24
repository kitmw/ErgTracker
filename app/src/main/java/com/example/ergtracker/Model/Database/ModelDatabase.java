package com.example.ergtracker.Model.Database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.ergtracker.Model.RawDataPoint;

@Database(entities = {RawDataPoint.class}, version = 3, exportSchema = false)
public abstract class ModelDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
