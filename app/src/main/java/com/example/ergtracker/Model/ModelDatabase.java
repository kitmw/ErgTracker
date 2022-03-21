package com.example.ergtracker.Model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {RawDataPoint.class}, version = 1, exportSchema = false)
public abstract class ModelDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}
