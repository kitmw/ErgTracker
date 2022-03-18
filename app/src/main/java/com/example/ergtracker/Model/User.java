package com.example.ergtracker.Model;

import android.service.autofill.UserData;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;

import java.util.Collections;
import java.util.Date;
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
            // use an all time logPD curve
            estimateSubList2KTimes(userData);
            return;
        } else {
            // treat data as rolling window size 5 so that logPD curve adjusts for user's change over time
            for(int i=0;i<userData.size()-(windowSize);i++){
                estimateSubList2KTimes(userData.subList(i,i+windowSize));
            }
        }
    }

    // subList is a list of minimum 2, maximum 5 elements used to calculate a local DTGradient and
    // so an estimate for 2km time that was relevant at that time
    private void estimateSubList2KTimes(List subList){

    }

//    private LinkedList<DataPoint> sortDataPoints(Date date, List<DataPoint> listOfDataPoints){
//        DataPoint earliestDataPoint = listOfDataPoints.get(0);
//        if (date==null){
//            for(DataPoint dataPoint:listOfDataPoints.subList(1,listOfDataPoints.size())){
//
//            }
//        }
//    }

    public void addDataPoint(double time, double distance, Date date){
        userData.add(new DataPoint(time,distance,date));
    }

    public double estimateDistanceForTime(){
        return 0.0;
    }
    public double estimateTimeForDistance(){
        return 0.0;
    }
}
