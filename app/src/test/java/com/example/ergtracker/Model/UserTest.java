package com.example.ergtracker.Model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Date;

public class UserTest {

    @Before
    public void setup(){
        System.out.println("Setting up dummy data...");
        User largeDataSetUser = new User("TestUserBig");
        largeDataSetUser.addDataPoint(80,500,new Date(1647000000000L));
        largeDataSetUser.addDataPoint(89*2,1000,new Date(1647100000000L));
        largeDataSetUser.addDataPoint(95*4,2000,new Date(1647200000000L));
        largeDataSetUser.addDataPoint(1000,5000,new Date(1647300000000L));
        largeDataSetUser.addDataPoint(105*20,20*500,new Date(1647600000000L));
        largeDataSetUser.addDataPoint(89.5*2,1000,new Date(1647500000000L));
        largeDataSetUser.addDataPoint(83,504,new Date(1647470000000L));
        largeDataSetUser.addDataPoint(1100,4900,new Date(1648000000000L));
        largeDataSetUser.addDataPoint(95.1*4,2001,new Date(1647900000000L));
        largeDataSetUser.addDataPoint(89,500,new Date(1647800000000L));

        User smallDataSetUser = new User("TestUserSmall");
        smallDataSetUser.addDataPoint(80,500,new Date(1647000000000L));
        smallDataSetUser.addDataPoint(89*2,1000,new Date(1647000000000L));
        smallDataSetUser.addDataPoint(95*4,2000,new Date(1647000000000L));

        User newUser = new User("TestUserNew");
        newUser.addDataPoint(80,500,new Date(1647000000000L));


    }

    @Test
    public void estimateAll2KTimes() {
//        largeDataSetUser.
    }

    @Test
    public void estimateDistanceForTime() {
    }

    @Test
    public void estimateTimeForDistance() {
    }
}