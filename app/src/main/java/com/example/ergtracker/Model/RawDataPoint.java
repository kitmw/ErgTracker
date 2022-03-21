package com.example.ergtracker.Model;



import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RawDataPoint {
    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String userName;
    private double rawTime;
    private double rawDistance;

    public RawDataPoint(String userName, double rawTime, double rawDistance) {
        this.userName = userName;
        this.rawTime = rawTime;
        this.rawDistance = rawDistance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public double getRawTime() {
        return rawTime;
    }

    public void setRawTime(double rawTime) {
        this.rawTime = rawTime;
    }

    public double getRawDistance() {
        return rawDistance;
    }

    public void setRawDistance(double rawDistance) {
        this.rawDistance = rawDistance;
    }
}
