package com.example.ergtracker.Model;


import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.time.LocalDateTime;

public class DataPoint implements Comparable<DataPoint> {
    private String userName;
    private double rawTime;
    private double rawDistance;
    // A scaled time is a raw time, scaled to the appropriate distance by the model. A predicted
    // time, is what the model thinks a user should be able to achieve based on recent data. They
    // differ because a user might under or over perform on a particular day.
    private double scaled2KTime;
    private double predicted2KTime;
    private SimpleRegression logRegress;
    private LocalDateTime date;


    public DataPoint(double rawTime, double rawDistance, LocalDateTime date,String userName) {
        this.rawTime = rawTime;
        this.rawDistance = rawDistance;
        this.scaled2KTime = 0; // can only make this estimate with knowledge of other data points
        this.predicted2KTime = 0;
        this.logRegress = new SimpleRegression();
        this.date = date;
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

    public double getScaled2KTime() {
        return scaled2KTime;
    }

    public void setScaled2KTime(double scaled2KTime) {
        this.scaled2KTime = scaled2KTime;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    @Override
    public int compareTo(DataPoint dataPoint) {
        return this.date.compareTo(dataPoint.date);
    }

    public double getPredicted2KTime() {
        return predicted2KTime;
    }

    public void setPredicted2KTime(double predicted2KTime) {
        this.predicted2KTime = predicted2KTime;
    }

    public SimpleRegression getLogRegress() {
        return logRegress;
    }

    public void setLogRegress(SimpleRegression logRegress) {
        this.logRegress = logRegress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}