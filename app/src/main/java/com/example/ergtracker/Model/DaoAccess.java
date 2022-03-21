package com.example.ergtracker.Model;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DaoAccess {

    @Insert
    Long insertTask(RawDataPoint rawDataPoint);

    @Query("SELECT * FROM RawDataPoint")
    LiveData<List<RawDataPoint>> fetchAllTasks();

    @Delete
    void deleteTask(RawDataPoint rawDataPoint);

    @Query("SELECT * FROM RawDataPoint WHERE userName = :taskId")
    RawDataPoint getTask(String taskId);
}