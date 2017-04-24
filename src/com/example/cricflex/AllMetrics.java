package com.example.cricflex;

import java.util.ArrayList;

/**
 * Created by bilal on 4/18/2017.
 */

public class AllMetrics {

    String Email;
    String Name;
    String legalBalls;
    String illegalBalls;
    ArrayList<Integer> angleValues = new ArrayList<Integer>();
    ArrayList<Integer> forceValues = new ArrayList<Integer>();
    ArrayList<Integer> armTwistValues = new ArrayList<Integer>();
    ArrayList<Float> actionTimeValues = new ArrayList<Float>();

    public AllMetrics() {
    }

    public AllMetrics(String email, String name, String legalBalls, String illegalBalls, ArrayList<Integer> angleValues, ArrayList<Integer> forceValues, ArrayList<Integer> armTwistValues, ArrayList<Float> actionTimeValues) {
        Email = email;
        Name = name;
        this.legalBalls = legalBalls;
        this.illegalBalls = illegalBalls;
        this.angleValues = angleValues;
        this.forceValues = forceValues;
        this.armTwistValues = armTwistValues;
        this.actionTimeValues = actionTimeValues;
    }
}
