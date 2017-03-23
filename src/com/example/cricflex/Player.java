package com.example.cricflex;

/**
 * Created by bilal on 6/21/2016.
 */

public class Player {
    public Player() {
    }

    //     int id;
    String  email,  password;

//    security;

    String name;

    public Player(String email, String password, String name, String bowlingStyle, String bowlingArm, String DOB, String height, String weight, String careerLevel, String gender, String location, String legalBowls, String illegalBowls, String averageAngle, String longestStreak, String lastBowlAngle) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.bowlingStyle = bowlingStyle;
        this.bowlingArm = bowlingArm;
        this.DOB = DOB;
        Height = height;
        Weight = weight;
        this.careerLevel = careerLevel;
        Gender = gender;
        Location = location;
        this.legalBowls = legalBowls;
        this.illegalBowls = illegalBowls;
        this.averageAngle = averageAngle;
        this.longestStreak = longestStreak;
        this.lastBowlAngle = lastBowlAngle;
    }

    String bowlingStyle;
    String bowlingArm;
    String DOB;
    String Height;
    String Weight;
    String careerLevel;
    String Gender;
    String Location;

    String legalBowls = "0";
    String illegalBowls = "0";
    String averageAngle = "0";
    String longestStreak = "0";
    String lastBowlAngle = "0";

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getLegalBowls() {
        return legalBowls;
    }
    public void setLegalBowls(String legalBowls) {this.legalBowls = legalBowls;}

    public String getAverageAngle() {
        return averageAngle;
    }
    public void setAverageAngle(String averageAngle) {
        this.averageAngle = averageAngle;
    }

    public String getIllegalBowls() {
        return illegalBowls;
    }
    public void setIllegalBowls(String illegalBowls) {
        this.illegalBowls = illegalBowls;
    }

    public String getLongestStreak() {
        return longestStreak;
    }
    public void setLongestStreak(String longestStreak) {
        this.longestStreak = longestStreak;
    }

    public String getLastBowlAngle() { return lastBowlAngle;}
    public void setLastBowlAngle(String lastBowlAngle) { this.lastBowlAngle = lastBowlAngle;}

    public String getBowlingStyle() {
        return bowlingStyle;
    }
    public void setBowlingStyle(String bowlingStyle) {
        this.bowlingStyle = bowlingStyle;
    }

    public String getBowlingArm() {
        return bowlingArm;
    }
    public void setBowlingArm(String bowlingArm) {
        this.bowlingArm = bowlingArm;
    }

    public String getDOB() {
        return DOB;
    }
    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getHeight() {
        return Height;
    }
    public void setHeight(String height) {
        Height = height;
    }

    public String getWeight() {
        return Weight;
    }
    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getCareerLevel() {
        return careerLevel;
    }
    public void setCareerLevel(String careerLevel) {
        this.careerLevel = careerLevel;
    }

    public String getGender() {
        return Gender;
    }
    public void setGender(String gender) {
        Gender = gender;
    }

    public String getLocation() {
        return Location;
    }
    public void setLocation(String location) {
        Location = location;
    }

//    public void setUsername(String email){
//        this.email = email;
//    }
//    public String getUsername(){
//        return this.email;
//    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){return this.email;}

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }

//    public void setSecurity(String security){this.security = security;}
//    public String getSecurity(){
//        return this.security;
//    }


}
