package com.example.cricflex;

import java.util.ArrayList;

/**
 * Created by bilal on 3/10/2017.
 */

public class PlayerMetrics {


    ArrayList<Integer> angleValues = new ArrayList<Integer>();
    ArrayList<Integer> forceValues = new ArrayList<Integer>();
    ArrayList<Integer> armTwistValues = new ArrayList<Integer>();
    ArrayList<Float> actionTimeValues = new ArrayList<Float>();


    public ArrayList<Integer> getAngleValues() {
        return angleValues;
    }

    public void setAngleValues(ArrayList<Integer> angleValues) {
        this.angleValues = angleValues;
    }



    public ArrayList<Integer> getForceValues() {
        return forceValues;
    }

    public void setForceValues(ArrayList<Integer> forceValues) {
        this.forceValues = forceValues;
    }

    public ArrayList<Integer> getArmTwistValues() {
        return armTwistValues;
    }

    public void setArmTwistValues(ArrayList<Integer> armTwistValues) {
        this.armTwistValues = armTwistValues;
    }

    public ArrayList<Float> getActionTimeValues() {
        return actionTimeValues;
    }

    public void setActionTimeValues(ArrayList<Float> actionTimeValues) {
        this.actionTimeValues = actionTimeValues;
    }






}
