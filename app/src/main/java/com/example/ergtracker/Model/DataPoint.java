package com.example.ergtracker.Model;

import java.util.Date;

public class DataPoint implements Comparable<DataPoint> {
    private double rawTime;
    private double rawDistance;
    private double TwoKTimeEstimate;
    private Date date;

    public DataPoint(double rawTime, double rawDistance, Date date) {
        this.rawTime = rawTime;
        this.rawDistance = rawDistance;
        this.TwoKTimeEstimate = 0; // can only make this estimate with knowledge of other data points
        this.date = date;
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

    public double getTwoKTimeEstimate() {
        return TwoKTimeEstimate;
    }

    public void setTwoKTimeEstimate(double twoKTimeEstimate) {
        TwoKTimeEstimate = twoKTimeEstimate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int compareTo(DataPoint dataPoint) {
        return this.date.compareTo(dataPoint.date);
    }
}