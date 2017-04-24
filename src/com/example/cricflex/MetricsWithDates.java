package com.example.cricflex;

import java.util.ArrayList;

/**
 * Created by bilal on 4/18/2017.
 */

public class MetricsWithDates {

    String Name;
    String Email;
    ArrayList<Integer> angleValues = new ArrayList<Integer>();
    ArrayList<Integer> forceValues = new ArrayList<Integer>();
    ArrayList<Integer> armTwistValues = new ArrayList<Integer>();
    ArrayList<Float> actionTimeValues = new ArrayList<Float>();

    public MetricsWithDates(String name, String email, ArrayList<Integer> angleValues, ArrayList<Integer> forceValues, ArrayList<Integer> armTwistValues, ArrayList<Float> actionTimeValues) {
        Name = name;
        Email = email;
        this.angleValues = angleValues;
        this.forceValues = forceValues;
        this.armTwistValues = armTwistValues;
        this.actionTimeValues = actionTimeValues;
    }

}
