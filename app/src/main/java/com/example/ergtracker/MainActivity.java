package com.example.ergtracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.ergtracker.Model.ModelRepository;
import com.example.ergtracker.Model.RawDataPoint;
import com.example.ergtracker.Model.User;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LifecycleOwner lifecycleOwner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lifecycleOwner = this;

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void writeUserToDB(View view, User user) {
        new Thread(() -> {
            ModelRepository modelRepository =  new ModelRepository(view.getContext());
            user.getUserData().forEach(dataPoint -> {
                try {
                    RawDataPoint rawDataPoint = dataPoint.getRawDataPoint();
                    modelRepository.insertTask(rawDataPoint.getUserName(),
                            rawDataPoint.getRawTime(), rawDataPoint.getRawDistance());
                } catch (Exception e) {
                    System.out.println("Couldn't write user: " + dataPoint.getRawDataPoint().getUserName());
                    e.printStackTrace();
                }
            });
        }).start();
    }

    // FIX THIS: current db setup means username must be unique identifier. This won't work as each
    // user will have multiple data point so multiple entries in db. Add unique RawDataPoint id
    public void readUserFromDB(View view, String userName) {
        new Thread(() -> {
            ModelRepository modelRepository =  new ModelRepository(view.getContext());
            try{RawDataPoint rawDataPoint = modelRepository.getTask(userName);
            } catch (Exception e) {
                System.out.println("Couldn't read user: " + userName);
                e.printStackTrace();
            }
        }).start();

    }

    public void readAllButtonClick(View view) {
        ModelRepository modelRepository =  new ModelRepository(view.getContext());
        Observer<List<RawDataPoint>> listObserver = dataPoints -> {
            for(RawDataPoint dataPoint : dataPoints) {
                System.out.println("-----------------------");
                System.out.println(dataPoint.getUserName());
            }
        };
        modelRepository.getTasks().observe(lifecycleOwner, listObserver);
    }

}