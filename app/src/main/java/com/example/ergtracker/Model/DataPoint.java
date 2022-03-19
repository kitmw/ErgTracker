package com.example.ergtracker.Model;


import java.time.LocalDateTime;

public class DataPoint implements Comparable<DataPoint> {
    private double rawTime;
    private double rawDistance;
    // a value for what the model thinks the user should achieve over 2km based on recent scores
    // and on the day performance.
    private double scaled2KEstimate;
    private double predicted2KEstimate;
    private LocalDateTime date;

    public DataPoint(double rawTime, double rawDistance, LocalDateTime date) {
        this.rawTime = rawTime;
        this.rawDistance = rawDistance;
        this.scaled2KEstimate = 0; // can only make this estimate with knowledge of other data points
        this.predicted2KEstimate = 0;
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

    public double getScaled2KEstimate() {
        return scaled2KEstimate;
    }

    public void setScaled2KEstimate(double scaled2KEstimate) {
        this.scaled2KEstimate = scaled2KEstimate;
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

    public double getPredicted2KEstimate() {
        return predicted2KEstimate;
    }

    public void setPredicted2KEstimate(double predicted2KEstimate) {
        this.predicted2KEstimate = predicted2KEstimate;
    }
}