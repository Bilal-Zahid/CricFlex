package com.example.cricflex;

/**
 * Created by bilal on 6/21/2016.
 */

public class Player {
    // int id;
    String  email, username,  password, security;

    String bowlingStyle;
    String bowlingArm;
    String DOB;
    String Height;
    String Weight;
    String careerLevel;
    String Gender;
    String Location;

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


    /*
    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return this.id;
    }
    */

    public void setUsername(String username){
        this.username = username;
    }
    public String getusername(){
        return this.username;
    }

    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }

    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }

    public void setSecurity(String security){
        this.security = security;
    }
    public String getSecurity(){
        return this.security;
    }


}
