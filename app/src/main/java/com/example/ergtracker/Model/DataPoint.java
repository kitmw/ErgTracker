package com.example.ergtracker.Model;

import android.content.Context;

import com.example.ergtracker.R;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DataPoint implements Comparable<DataPoint> {
    private RawDataPoint rawDataPoint;
    private SimpleRegression logRegress;
    private LocalDate date;
    // A scaled time is a raw time, scaled to the appropriate distance by the model. A predicted
    // time, is what the model thinks a user should be able to achieve based on recent data. They
    // differ because a user might under or over perform on a particular day.
    private double scaled2KTime;
    private double predicted2KTime;



    public DataPoint(double rawTime, double rawDistance, LocalDate date,String userName) {
        String dateString = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.rawDataPoint = new RawDataPoint(userName,rawTime,rawDistance,dateString);
        this.scaled2KTime = 0; // can only make this estimate with knowledge of other data points
        this.predicted2KTime = 0;
        this.logRegress = new SimpleRegression();
        this.date = date;
    }

    public double getRawTime() {
        return rawDataPoint.getRawTime();
    }

    public void setRawTime(double rawTime) {
        this.rawDataPoint.setRawTime(rawTime);
    }

    public double getRawDistance() {
        return rawDataPoint.getRawDistance();
    }

    public void setRawDistance(double rawDistance) {
        this.rawDataPoint.setRawDistance(rawDistance);
    }

    public double getScaled2KTime() {
        return scaled2KTime;
    }

    public void setScaled2KTime(double scaled2KTime) {
        this.scaled2KTime = scaled2KTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
        return this.rawDataPoint.getUserName();
    }

    public void setUserName(String userName) {
        this.rawDataPoint.setUserName(userName);
    }


}