package com.example.ergtracker.Model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

public class UserTest {

    private User largeDataSetUser;
    private User smallDataSetUser;
    private User newUser;

    @Before
    public void setup(){
        System.out.println("Setting up dummy data...");
        largeDataSetUser = new User("TestUserBig");
        largeDataSetUser.addDataPoint(80,500, LocalDateTime.of(2020,12,1,0,0));
        largeDataSetUser.addDataPoint(89*2,1000, LocalDateTime.of(2020,11,1,0,0));
        largeDataSetUser.addDataPoint(95*4,2000, LocalDateTime.of(2020,9,1,0,0));
        largeDataSetUser.addDataPoint(1000,5000, LocalDateTime.of(2020,4,1,0,0));
        largeDataSetUser.addDataPoint(105*20,20*500, LocalDateTime.of(2020,1,1,0,0));
        largeDataSetUser.addDataPoint(89.5*2,1000, LocalDateTime.of(2020,1,1,0,0));
        largeDataSetUser.addDataPoint(83,504, LocalDateTime.of(2020,2,1,0,0));
        largeDataSetUser.addDataPoint(1100,4900, LocalDateTime.of(2020,5,1,0,0));
        largeDataSetUser.addDataPoint(95.1*4,2001, LocalDateTime.of(2020,8,1,0,0));
        largeDataSetUser.addDataPoint(89,500, LocalDateTime.of(2020,1,12,0,0));

        smallDataSetUser = new User("TestUserSmall");
        smallDataSetUser.addDataPoint(80,500, LocalDateTime.of(2021,1,1,0,0));
        smallDataSetUser.addDataPoint(89*2,1000, LocalDateTime.of(2022,1,1,0,0));
        smallDataSetUser.addDataPoint(95*4,2000, LocalDateTime.of(2020,1,1,0,0));

        newUser = new User("TestUserNew");
        newUser.addDataPoint(80,500, LocalDateTime.of(2020,1,1,0,0));


    }

    @Test
    public void estimateAll2KTimes_dateOrder() {
        largeDataSetUser.estimateAll2KTimes();
        List<DataPoint> largeDataList = largeDataSetUser.getUserData();
        LocalDateTime previousDate = largeDataList.get(0).getDate();
        for(DataPoint dataPoint : largeDataList.subList(1,largeDataList.size())){
            if(dataPoint.getDate().isBefore(previousDate)){
                fail("Datapoints in UserData are not in chronological order.");
            }
        }
    }

    @Test
    public void estimateAll2KTimes_2KEstimate(){
        largeDataSetUser.estimateAll2KTimes();
        for(DataPoint dataPoint : largeDataSetUser.getUserData()){
            System.out.println("Estimate for 2km time: " + dataPoint.getScaled2KTime() + "s");
            // FIX THIS
            // currently no test here
            // to test this need to generate dummy data from a known curve and check that the 2K Estimate is correct
        }
        fail("No test here yet");
    }

    @Test
    public void estimateDForTOrTForD() {
        largeDataSetUser.estimateAll2KTimes();
        try {
            System.out.println("Estimated 1000m time is: " + largeDataSetUser.estimateDForTOrTForD(1000, "TForD") + "s");
            System.out.println("Estimated 30 minute distance is: " + largeDataSetUser.estimateDForTOrTForD(30*60, "DForT") + "m");
        } catch (Exception e) {
            e.printStackTrace();
            fail("Threw unexpected exception");
        }
        DataPoint dataPoint = largeDataSetUser.getUserData().get(largeDataSetUser.getUserData().size()-1);
        // FIX THIS
        // currently no test here
        // to test this need to generate dummy data from a known curve and check that the estimates are correct
        fail("No test here yet");
    }

}