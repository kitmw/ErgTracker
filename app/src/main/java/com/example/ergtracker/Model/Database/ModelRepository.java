package com.example.ergtracker.Model.Database;


import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import com.example.ergtracker.Model.Database.ModelDatabase;
import com.example.ergtracker.Model.RawDataPoint;

import java.util.List;

public class ModelRepository {
    private String DB_NAME = "RoomDB";
    private ModelDatabase modelDatabase;

    public ModelRepository(Context context) {
        modelDatabase = Room.databaseBuilder(context, ModelDatabase.class, DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public void insertTask(final String userName, final double rawTime, final double rawDistance, final String dateString) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                modelDatabase.daoAccess().insertTask(new RawDataPoint(userName,rawTime,rawDistance,dateString));
            }
        });
    }

    public LiveData<List<RawDataPoint>> getTasks() {
        return modelDatabase.daoAccess().fetchAllTasks();
    }

    public RawDataPoint getTask(String id) {
        return modelDatabase.daoAccess().getTask(id);
    }

    public void deleteTask(final String id) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                final RawDataPoint starredDataLiveData = getTask(id);
                if (starredDataLiveData != null) {
                    modelDatabase.daoAccess().deleteTask(starredDataLiveData);
                }
            }
        });
    }

    public void deleteAll() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                modelDatabase.daoAccess().deleteAll();
            }
        });
    }
}
