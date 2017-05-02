package com.example.cricflex;

import java.util.ArrayList;

/**
 * Created by bilal on 4/18/2017.
 */

public class AllMetrics {



    public String Email;
    public int legalBalls;
    public int illegalBalls;
    public ArrayList<Integer> angleValues = new ArrayList<Integer>();
    public ArrayList<Integer> forceValues = new ArrayList<Integer>();
    public ArrayList<Integer> armTwistValues = new ArrayList<Integer>();
    public ArrayList<Float> actionTimeValues = new ArrayList<Float>();

    public AllMetrics() {
    }

    public AllMetrics(String email, int legalBalls, int illegalBalls, ArrayList<Integer> angleValues, ArrayList<Integer> forceValues, ArrayList<Integer> armTwistValues, ArrayList<Float> actionTimeValues) {
        Email = email;
        this.legalBalls = legalBalls;
        this.illegalBalls = illegalBalls;
        this.angleValues = angleValues;
        this.forceValues = forceValues;
        this.armTwistValues = armTwistValues;
        this.actionTimeValues = actionTimeValues;
    }


//    public AllMetrics(String email, int legalBalls, int illegalBalls, ArrayList<Integer> angleValues, ArrayList<Integer> forceValues, ArrayList<Integer> armTwistValues, ArrayList<Float> actionTimeValues) {
//        Email = email;
//        this.legalBalls = legalBalls;
//        this.illegalBalls = illegalBalls;
//        this.angleValues = angleValues;
//        this.forceValues = forceValues;
//        this.armTwistValues = armTwistValues;
//        this.actionTimeValues = actionTimeValues;
//    }
}
