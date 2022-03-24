package com.example.ergtracker.Model.Database;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.ergtracker.Model.RawDataPoint;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    Long insertTask(RawDataPoint rawDataPoint);

    @Query("SELECT * FROM RawDataPoint")
    LiveData<List<RawDataPoint>> fetchAllTasks();

    @Query("DELETE FROM RawDataPoint")
    void deleteAll();

    @Delete
    void deleteTask(RawDataPoint rawDataPoint);

    @Query("SELECT * FROM RawDataPoint WHERE dataPointID = :taskId")
    RawDataPoint getTask(String taskId);
}