package com.example.ergtracker.Model;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class User {
    private String name;
    private ObservableList<DataPoint> userData;

    public User(String name) {
        this.name = name;
        this.userData = new ObservableArrayList<DataPoint>();
    }

    // Estimate a 2km time for all raw data entries. Raw data can be for any distance.
    // This requires several readings (minimum two but ideally more) in order to establish a
    // distance vs time curve. As the total distance increases, the average split for an athlete
    // drops. This means that the total time taken to row that distance increases at a rate that
    // is dependant on the particular athlete and their conditioning.
    // It is assumed that log(distance) is proportional to log(time), i.e. that the effects of the
    // athlete's fitness and endurance on their distance vs time graph can be approximated by a
    // polynomial fit, i.e.: Distance = A*(Time)^b
    // It is expected that b < 1, i.e. that the athlete produces a lower average split on a longer
    // workout.
    public void estimateAll2KTimes(){
        int windowSize = 5;
        Collections.sort(userData); // ensure dataPoints are in chronological order
        if(userData.size()<2){
            return;
        } else if(userData.size()<windowSize){
            // use an all time logTD curve
            estimateSubList2KTimes(userData);
            return;
        } else {
            // treat data as rolling window size 5 so that logTD curve adjusts for user's change over time
            // Windows overlap and so the latter 4 elements will be overwritten by more up to date
            // estimates, unless the window is the last in the list in which case all elements will
            // estimated from the same logTD fit.
            for(int i=0;i<=(userData.size()-(windowSize));i++){
                estimateSubList2KTimes(userData.subList(i,i+windowSize));
            }
        }
    }

    // subList is a list of minimum 2, maximum 5 elements used to calculate a local DTGradient and
    // so an estimate for 2km time that was relevant at that time
    private double estimateSubList2KTimes(List<DataPoint> subList){
        double[][] logData = new double[subList.size()][2];
        // for the purpose of estimating a time for a distance, it would be convenient if time on y axis, distance on x
        int distanceColIndex = 0;
        int timeColIndex = 1;
        for (int i=0; i<subList.size(); i++){
            logData[i][distanceColIndex] = Math.log10(subList.get(i).getRawDistance());
            logData[i][timeColIndex] = Math.log10(subList.get(i).getRawTime());
        }
        SimpleRegression logRegress = new SimpleRegression(true);
        logRegress.addData(logData);
        logRegress.regress();
        double predictedLog2KTime = logRegress.predict(Math.log10(2000));

        for(DataPoint dataPoint:subList){
            double predictedLogTimeForActualDistance = logRegress.predict(Math.log10(dataPoint.getRawDistance()));
            double dataPointLogScaleFactor = predictedLog2KTime/predictedLogTimeForActualDistance;
            double logTimeForActualDistanceScaledTo2K = Math.log10(dataPoint.getRawTime())*dataPointLogScaleFactor;
            dataPoint.setScaled2KEstimate(Math.pow(10,logTimeForActualDistanceScaledTo2K));
            dataPoint.setPredicted2KEstimate(Math.pow(10,predictedLog2KTime));
        }
        return logRegress.getRSquare();
    }

//    private LinkedList<DataPoint> sortDataPoints(LocalDateTime date, List<DataPoint> listOfDataPoints){
//        DataPoint earliestDataPoint = listOfDataPoints.get(0);
//        if (date==null){
//            for(DataPoint dataPoint:listOfDataPoints.subList(1,listOfDataPoints.size())){
//
//            }
//        }
//    }

    public void addDataPoint(double time, double distance, LocalDateTime date){
        userData.add(new DataPoint(time,distance,date));
    }

    public double estimateDistanceForTime(){
        return 0.0;
    }
    public double estimateTimeForDistance(){
        return 0.0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObservableList<DataPoint> getUserData() {
        return userData;
    }

    public void setUserData(ObservableList<DataPoint> userData) {
        this.userData = userData;
    }
}
