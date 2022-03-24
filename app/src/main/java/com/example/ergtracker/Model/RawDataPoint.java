package com.example.ergtracker.Model;



import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RawDataPoint {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int dataPointID = 0;
    private String userName;
    private double rawTime;
    private double rawDistance;
    private String dateString;

    public RawDataPoint(String userName, double rawTime, double rawDistance, String dateString) {
        this.userName = userName;
        this.rawTime = rawTime;
        this.rawDistance = rawDistance;
        this.dateString = dateString;
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

    public int getDataPointID() {
        return dataPointID;
    }

    public void setDataPointID(int dataPointID) {
        this.dataPointID = dataPointID;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
