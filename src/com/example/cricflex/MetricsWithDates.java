package com.example.cricflex;

import java.util.ArrayList;

/**
 * Created by bilal on 4/18/2017.
 */

public class MetricsWithDates {


    public String Email;
    public ArrayList<Integer> angleValues = new ArrayList<Integer>();
    public ArrayList<Integer> forceValues = new ArrayList<Integer>();
    public ArrayList<Integer> armTwistValues = new ArrayList<Integer>();
    public ArrayList<Float> actionTimeValues = new ArrayList<Float>();

    public MetricsWithDates() {
    }

    public MetricsWithDates( String email, ArrayList<Integer> angleValues, ArrayList<Integer> forceValues, ArrayList<Integer> armTwistValues, ArrayList<Float> actionTimeValues) {

        Email = email;
        this.angleValues = angleValues;
        this.forceValues = forceValues;
        this.armTwistValues = armTwistValues;
        this.actionTimeValues = actionTimeValues;
    }

}
