package com.example.cricflex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bilal on 6/21/2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cricflex.db";

    // Table Names
    private static final String PLAYER_TABLE_NAME = "players";
    private static final String IMAGE_TABLE_NAME = "table_image";
    private static final String STATS_TABLE_NAME = "table_stats";

    private static final String ANGLE_TABLE_NAME = "table_angle";
    private static final String FORCE_TABLE_NAME = "table_force";
    private static final String ACTION_TIME_TABLE_NAME = "table_action_time";
    private static final String ARM_TWIST_TABLE_NAME = "table_arm_twist";


    private static final String ANGLE_HISTORY_TABLE_NAME = "angle_history_table";
    private static final String FORCE_HISTORY_TABLE_NAME = "force_history_table";
    private static final String ARM_TWIST_HISTORY_TABLE_NAME = "arm_twist_history_table";
    private static final String ACTION_TIME_HISTORY_TABLE_NAME = "action_time_history_table";




    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_SECURITY = "security";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_DOB = "DOB";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_BOWLINGSTYLE = "bowlingstyle";
    private static final String COLUMN_BOWLINGARM = "bowlingarm";
    private static final String COLUMN_CAREERLEVEL = "careerlevel";
    private static final String COLUMN_WEIGHT = "weight";


    // For Storing Images
    // column names
    private static final String KEY_NAME = "image_name";
    private static final String KEY_IMAGE = "image_data";


    //For Stats
    private static final String COLUMN_LEGAL_BOWLS = "legal_bowls";
    private static final String COLUMN_ILLEGAL_BOWLS = "illegal_bowls";
    private static final String COLUMN_AVERAGE_ANGLE = "average_angle";
    private static final String COLUMN_LONGEST_STREAK = "longest_streak";
    private static final String COLUMN_LAST_BOWL_ANGLE = "last_bowl_angle";

    // For other stats


    //For Angles
    private static final String COLUMN_ANGLE_VALUES = "angle_values";

    private static final String COLUMN_ACTION_TIME = "action_time_values";
    private static final String COLUMN_FORCE = "force_values";
    private static final String COLUMN_ARM_TWIST = "arm_twist_values";

    //For History
    private static final String COLUMN_SESSION_DATE = "session_date";

    SQLiteDatabase db;

    private static final String CREATE_TABLE_PLAYER = "create table " + PLAYER_TABLE_NAME + " (id integer primary key not null  , " +
            "name text not null , email text not null , username text not null, password text not null, security text not null, " +
            " gender text not null, DOB text not null, location text not null, bowlingstyle text not null," +
            " bowlingarm text not null, careerlevel text not null , weight text not null);";


    private static final String CREATE_TABLE_IMAGE = "CREATE TABLE " + IMAGE_TABLE_NAME + "(" +
            KEY_NAME + " TEXT," +
            KEY_IMAGE + " BLOB);";

    private static final String CREATE_TABLE_PLAYER_STATS = "create table " + STATS_TABLE_NAME + " " +
            "(username text not null, legal_bowls text not null, illegal_bowls text not null" +
            ",average_angle text not null, longest_streak text not null, last_bowl_angle text not null);";

    private static final String CREATE_TABLE_PLAYER_ANGLE = "create table " + ANGLE_TABLE_NAME + " " +
            "(username text not null, angle_values text not null);";

    private static final String CREATE_TABLE_PLAYER_FORCE = "create table " + FORCE_TABLE_NAME + " " +
            "(username text not null, force_values text not null);";
    private static final String CREATE_TABLE_PLAYER_ARM_TWIST = "create table " + ARM_TWIST_TABLE_NAME + " " +
            "(username text not null, arm_twist_values text not null);";
    private static final String CREATE_TABLE_PLAYER_ACTION_TIME = "create table " + ACTION_TIME_TABLE_NAME + " " +
            "(username text not null, action_time_values text not null);";

    private static final String CREATE_TABLE_PLAYER_ANGLE_HISTORY = "create table " + ANGLE_HISTORY_TABLE_NAME + " " +
            "(username text not null, angle_values text not null, session_date text not null);";


    private static final String CREATE_TABLE_PLAYER_FORCE_HISTORY = "create table " + FORCE_HISTORY_TABLE_NAME + " " +
            "(username text not null, force_values text not null, session_date text not null);";

    private static final String CREATE_TABLE_PLAYER_ARM_TWIST_HISTORY = "create table " + ARM_TWIST_HISTORY_TABLE_NAME + " " +
            "(username text not null, arm_twist_values text not null, session_date text not null);";

    private static final String CREATE_TABLE_PLAYER_ACTION_TIME_HISTORY = "create table " + ACTION_TIME_HISTORY_TABLE_NAME + " " +
            "(username text not null, action_time_values text not null, session_date text not null);";



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLAYER);
        db.execSQL(CREATE_TABLE_IMAGE);
        db.execSQL(CREATE_TABLE_PLAYER_STATS);
        db.execSQL(CREATE_TABLE_PLAYER_ANGLE);
        db.execSQL(CREATE_TABLE_PLAYER_FORCE);
        db.execSQL(CREATE_TABLE_PLAYER_ACTION_TIME);
        db.execSQL(CREATE_TABLE_PLAYER_ARM_TWIST);
        db.execSQL(CREATE_TABLE_PLAYER_ANGLE_HISTORY);
        db.execSQL(CREATE_TABLE_PLAYER_FORCE_HISTORY);
        db.execSQL(CREATE_TABLE_PLAYER_ARM_TWIST_HISTORY);
        db.execSQL(CREATE_TABLE_PLAYER_ACTION_TIME_HISTORY);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS" + PLAYER_TABLE_NAME;
        db.execSQL("DROP TABLE IF EXISTS " + IMAGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + STATS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ANGLE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FORCE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ARM_TWIST_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ACTION_TIME_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ANGLE_HISTORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ARM_TWIST_HISTORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FORCE_HISTORY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ACTION_TIME_HISTORY_TABLE_NAME);
        db.execSQL(query);
        this.onCreate(db);
    }

    public ArrayList<String> getAllUserNames (){
        ArrayList<String> allUsernames = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = "select username from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                allUsernames.add(cursor.getString(0));

//                if (uname.equals(username)) {
//                    location = cursor.getString(1);
//                    break;
//                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();


        return allUsernames;
    }


    public void insertPlayer(Player p) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from players";
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        values.put(COLUMN_ID, count);
        values.put(COLUMN_NAME , p.getName());

        values.put(COLUMN_EMAIL, p.getEmail());
        values.put(COLUMN_USERNAME, p.getUsername());
        values.put(COLUMN_PASSWORD, p.getPassword());
        values.put(COLUMN_SECURITY, p.getSecurity());

        values.put(COLUMN_GENDER, p.getGender());
        values.put(COLUMN_DOB, p.getDOB());
        values.put(COLUMN_LOCATION, p.getLocation());
        values.put(COLUMN_BOWLINGSTYLE, p.getBowlingStyle());
        values.put(COLUMN_BOWLINGARM, p.getBowlingArm());
        values.put(COLUMN_CAREERLEVEL, p.getCareerLevel());
        values.put(COLUMN_WEIGHT,p.getWeight());


        db.insert(PLAYER_TABLE_NAME, null, values);
        db.close();
    }

    public void insertPlayerStats(Player p) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from " + STATS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();

        //values.put(COLUMN_ID,count);
        //values.put(COLUMN_NAME , playerStats.getName());

        values.put(COLUMN_USERNAME, p.getUsername());
        values.put(COLUMN_LEGAL_BOWLS, p.getLegalBowls());
        values.put(COLUMN_ILLEGAL_BOWLS, p.getIllegalBowls());
        values.put(COLUMN_AVERAGE_ANGLE, p.getAverageAngle());
        values.put(COLUMN_LONGEST_STREAK, p.getLongestStreak());
        values.put(COLUMN_LAST_BOWL_ANGLE, p.getLastBowlAngle());

        db.insert(STATS_TABLE_NAME, null, values);
        db.close();
    }

    public void insertPlayerAngleValues(String username, String angleValues) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from " + ANGLE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        cursor.close();

        //values.put(COLUMN_ID,count);
        //values.put(COLUMN_NAME , playerStats.getName());

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_ANGLE_VALUES, angleValues);

        db.insert(ANGLE_TABLE_NAME, null, values);
        db.close();
    }


    public void insertPlayerForceValues(String username, String forceValues) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from " + FORCE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_FORCE, forceValues);

        db.insert(FORCE_TABLE_NAME, null, values);
        db.close();
    }

    public void insertPlayerArmTwistValues(String username, String armTwistValues) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from " + ARM_TWIST_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_ARM_TWIST, armTwistValues);

        db.insert(ARM_TWIST_TABLE_NAME, null, values);
        db.close();
    }

    public void insertPlayerActionTimeValues(String username, String actionTimeValues) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from " + ACTION_TIME_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.close();

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_ACTION_TIME, actionTimeValues);

        db.insert(ACTION_TIME_TABLE_NAME, null, values);
        db.close();
    }


    public void insertPlayerAngleValuesWithDate(String username, String angleValues , String date) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from " + ANGLE_HISTORY_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        cursor.close();

        //values.put(COLUMN_ID,count);
        //values.put(COLUMN_NAME , playerStats.getName());

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_ANGLE_VALUES, angleValues);
        values.put(COLUMN_SESSION_DATE, date);

        db.insert(ANGLE_HISTORY_TABLE_NAME, null, values);
        db.close();
    }

    public void insertPlayerArmTwistValuesWithDate(String username, String armTwistValues , String date) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from " + ARM_TWIST_HISTORY_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        cursor.close();

        //values.put(COLUMN_ID,count);
        //values.put(COLUMN_NAME , playerStats.getName());

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_ARM_TWIST, armTwistValues);
        values.put(COLUMN_SESSION_DATE, date);

        db.insert(ARM_TWIST_HISTORY_TABLE_NAME, null, values);
        db.close();
    }

    public void insertPlayerForceValuesWithDate(String username, String forceValues , String date) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from " + FORCE_HISTORY_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        cursor.close();

        //values.put(COLUMN_ID,count);
        //values.put(COLUMN_NAME , playerStats.getName());

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_FORCE, forceValues);
        values.put(COLUMN_SESSION_DATE, date);

        db.insert(FORCE_HISTORY_TABLE_NAME, null, values);
        db.close();
    }


    public void insertPlayerActionTimeValuesWithDate(String username, String actionTimeValues , String date) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from " + ACTION_TIME_HISTORY_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        cursor.close();

        //values.put(COLUMN_ID,count);
        //values.put(COLUMN_NAME , playerStats.getName());

        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_ACTION_TIME, actionTimeValues);
        values.put(COLUMN_SESSION_DATE, date);

        db.insert(ACTION_TIME_HISTORY_TABLE_NAME, null, values);
        db.close();
    }








    public String getPassword(String username) {
        db = this.getReadableDatabase();
        String query = "select username, password from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, pass;
        pass = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    pass = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return pass;
    }

    public String getEmail(String username) {
        db = this.getReadableDatabase();
        String query = "select username, email from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, email;
        email = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    email = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return email;
    }

    public String getSecurity(String username) {
        db = this.getReadableDatabase();
        String query = "select username, security from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, security;
        security = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    security = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return security;
    }

    public String getGender(String username) {
        db = this.getReadableDatabase();
        String query = "select username, gender from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, gender;
        gender = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    gender = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return gender;
    }

    public String getName(String username) {
        db = this.getReadableDatabase();
        String query = "select username, name from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, name;
        name = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    name = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return name;
    }


    public String getWeight(String username) {
        db = this.getReadableDatabase();
        String query = "select username, weight from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, weight;
        weight = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    weight = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return weight;
    }



    public String getDOB(String username) {
        db = this.getReadableDatabase();
        String query = "select username, DOB from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, DOB;
        DOB = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    DOB = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return DOB;
    }

    public String getLocation(String username) {
        db = this.getReadableDatabase();
        String query = "select username, location from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, location;
        location = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    location = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return location;
    }


    public String getBowlingStyle(String username) {
        db = this.getReadableDatabase();
        String query = "select username, bowlingstyle from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, bowlingStyle;
        bowlingStyle = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    bowlingStyle = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return bowlingStyle;
    }


    public String getBowlingArm(String username) {
        db = this.getReadableDatabase();
        String query = "select username, bowlingarm from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, bowlingArm;
        bowlingArm = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    bowlingArm = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return bowlingArm;
    }


    public String getCareerLevel(String username) {
        db = this.getReadableDatabase();
        String query = "select username, careerlevel from " + PLAYER_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, careerLevel;
        careerLevel = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    careerLevel = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return careerLevel;
    }


    //Player stats getter and setter
    public String getLegalCount(String username) {
        db = this.getReadableDatabase();
        String query = "select username, legal_bowls from " + STATS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, legalBowls;
        legalBowls = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    legalBowls = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return legalBowls;
    }

    public String getIllegalCount(String username) {
        db = this.getReadableDatabase();
        String query = "select username, illegal_bowls from " + STATS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, illegalBowls;
        illegalBowls = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    illegalBowls = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return illegalBowls;
    }

    public String getAngleValues(String username) {
        db = this.getReadableDatabase();
        String query = "select username, angle_values from " + ANGLE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, angleValues;
        angleValues = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    angleValues = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return angleValues;
    }



    public String getForceValues(String username) {
        db = this.getReadableDatabase();
        String query = "select username, force_values from " + FORCE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, forceValues;
        forceValues = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    forceValues = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return forceValues;
    }


    public String getArmTwistValues(String username) {
        db = this.getReadableDatabase();
        String query = "select username, arm_twist_values from " + ARM_TWIST_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, armTwistValues;
        armTwistValues = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    armTwistValues = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return armTwistValues;
    }


    public String getActionTimeValues(String username) {
        db = this.getReadableDatabase();
        String query = "select username, action_time_values from " + ACTION_TIME_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, actionTimeValues;
        actionTimeValues = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    actionTimeValues = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return actionTimeValues;
    }




    public String getAllAngleValuesOfPlayer(String username) {
        db = this.getReadableDatabase();
        String query = "select username, angle_values, session_date from " + ANGLE_HISTORY_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, angleValues;
        angleValues = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    angleValues = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return angleValues;
    }



    public String getAllAngleValuesWithDate(String username) {
        db = this.getReadableDatabase();
        String query = "select group_concat(angle_values) from " + ANGLE_HISTORY_TABLE_NAME +
                " where username = '"+ username + "'";
        Cursor cursor = db.rawQuery(query, null);
        String uname, angleValues;
        angleValues = "not found";
        if (cursor.moveToFirst()) {

            angleValues = cursor.getString(0);
        }
        angleValues = angleValues.startsWith(",") ? angleValues.substring(1) : angleValues;
        System.out.println("Angle Values in database: " + angleValues);
        System.out.println("Actual Values should be: " + getAngleValues(username));

        cursor.close();
        return angleValues;
    }






    public String getAngleValuesWithDate(String username, String date) {
        db = this.getReadableDatabase();
        String query = "select angle_values from " + ANGLE_HISTORY_TABLE_NAME +
                " where username = '"+ username + "' AND session_date = '" + date +"'";
        Cursor cursor = db.rawQuery(query, null);
        String uname, angleValues;
        angleValues = "not found";
        if (cursor.moveToFirst()) {

            angleValues = cursor.getString(0);
        }
        cursor.close();
        return angleValues;
    }

    public String getArmTwistValuesWithDate(String username, String date) {
        db = this.getReadableDatabase();
        String query = "select arm_twist_values from " + ARM_TWIST_HISTORY_TABLE_NAME +
                " where username = '"+ username + "' AND session_date = '" + date +"'";
        Cursor cursor = db.rawQuery(query, null);
        String uname, armTwistValues;
        armTwistValues = "not found";
        if (cursor.moveToFirst()) {

            armTwistValues = cursor.getString(0);
        }
        cursor.close();
        return armTwistValues;
    }

    public String getForceValuesWithDate(String username, String date) {
        db = this.getReadableDatabase();
        String query = "select force_values from " + FORCE_HISTORY_TABLE_NAME +
                " where username = '"+ username + "' AND session_date = '" + date +"'";
        Cursor cursor = db.rawQuery(query, null);
        String uname, forceValues;
        forceValues = "not found";
        if (cursor.moveToFirst()) {

            forceValues= cursor.getString(0);
        }
        cursor.close();
        return forceValues;
    }
    public String getActionTimeValuesWithDate(String username, String date) {
        db = this.getReadableDatabase();
        String query = "select action_time_values from " + ACTION_TIME_HISTORY_TABLE_NAME +
                " where username = '"+ username + "' AND session_date = '" + date +"'";
        Cursor cursor = db.rawQuery(query, null);
        String uname, actionTimeValues;
        actionTimeValues = "not found";
        if (cursor.moveToFirst()) {

            actionTimeValues = cursor.getString(0);
        }
        cursor.close();
        return actionTimeValues;
    }




    public String getSessionDates(String username) {
        db = this.getReadableDatabase();
        String query = "select username, session_date from " + ANGLE_HISTORY_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, angleValues;
        angleValues = "not found";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    angleValues = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return angleValues;
    }



    public void changePassword(String username, String newPassword) {
        db = this.getReadableDatabase();
//        String query = "select username, security from " + PLAYER_TABLE_NAME;
        String query = "UPDATE " + PLAYER_TABLE_NAME + "  SET password = '" + newPassword + "' WHERE username = '" + username + "'";
        //db.rawQuery(query,null);
        db.execSQL(query);
        //boolean flag = true;
        //return flag;
    }

    public void changeStatAverageAngle(String username, String averageAngle) {
        db = this.getReadableDatabase();
//        String query = "select username, security from " + PLAYER_TABLE_NAME;
        String query = "UPDATE " + STATS_TABLE_NAME + "  SET average_angle = '" + averageAngle + "' WHERE username = '" + username + "'";
        //db.rawQuery(query,null);
        db.execSQL(query);
        //boolean flag = true;
        //return flag;
    }

    public void changeLongestStreak(String username, String longestStreak) {
        db = this.getReadableDatabase();
//        String query = "select username, security from " + PLAYER_TABLE_NAME;
        String query = "UPDATE " + STATS_TABLE_NAME + "  SET average_angle = '" + longestStreak + "' WHERE username = '" + username + "'";
        //db.rawQuery(query,null);
        db.execSQL(query);
        //boolean flag = true;
        //return flag;
    }

    public void changeLastBowlAngle(String username, String lastBowlAngle) {
        db = this.getReadableDatabase();
//        String query = "select username, security from " + PLAYER_TABLE_NAME;
        String query = "UPDATE " + STATS_TABLE_NAME + "  SET average_angle = '" + lastBowlAngle + "' WHERE username = '" + username + "'";
        //db.rawQuery(query,null);
        db.execSQL(query);
        //boolean flag = true;
        //return flag;
    }

    public void changeStatLegalIllegal(String username, String legal, String illegal) {
        db = this.getReadableDatabase();


        //db = this.getReadableDatabase();
        String query = "select username, legal_bowls, illegal_bowls from " + STATS_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, legalBowls, illegalBowls;
        legalBowls = "0";
        illegalBowls = "0";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);

                if (uname.equals(username)) {
                    legalBowls = cursor.getString(1);
                    illegalBowls = cursor.getString(2);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        int prevLegalBowls = 0, prevIllegalBowls = 0;

        if (!legalBowls.equals("0")) {
            prevIllegalBowls = Integer.parseInt(illegalBowls);
            prevLegalBowls = Integer.parseInt(legalBowls);
        }

        int current_legal_balls = Integer.parseInt(legal);
        int current_illegal_balls = Integer.parseInt(illegal);

        int totalLegalBowls = prevLegalBowls + current_legal_balls;
        int totalIllegalBowls = prevIllegalBowls + current_illegal_balls;


        legal = String.valueOf(totalLegalBowls);
        illegal = String.valueOf(totalIllegalBowls);


        String query1 = "UPDATE " + STATS_TABLE_NAME + "  SET legal_bowls = '" + legal + "' WHERE username = '" + username + "'";
        String query2 = "UPDATE " + STATS_TABLE_NAME + "  SET illegal_bowls = '" + illegal + "' WHERE username = '" + username + "'";

        db.execSQL(query1);
        db.execSQL(query2);
        //boolean flag = true;
        //return flag;
    }


    public void changeAngleValues(String username, String currentAngleValues) {
        db = this.getReadableDatabase();
        String totalAngleValues;
        //db = this.getReadableDatabase();
        String query = "select username, angle_values from " + ANGLE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, prevAngleValues = "";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);
                if (uname.equals(username)) {
                    prevAngleValues = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        ArrayList<String> previousArrayListOfAngles = new ArrayList<String>();
        if(!prevAngleValues.equals("")) {
//        getting previous array list from string
            JSONObject json = null;
            try {
                json = new JSONObject(prevAngleValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = json.optJSONArray("angleArray");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        previousArrayListOfAngles.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        getting current array list from string
        ArrayList<String> currentArrayListOfAngles = new ArrayList<String>();
        if(!currentAngleValues.equals("")){
            JSONObject json1 = null;
            try {
                json1 = new JSONObject(currentAngleValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray1 = json1.optJSONArray("angleArray");
            ;
            if (jsonArray1 != null) {
                int len = jsonArray1.length();
                for (int i = 0; i < len; i++) {
                    try {
                        currentArrayListOfAngles.add(jsonArray1.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //Joining the two lists
        if(!prevAngleValues.equals("") || !currentAngleValues.equals("")) {
            List<String> arrayListOfTotalValues = new ArrayList<String>(previousArrayListOfAngles);
            arrayListOfTotalValues.addAll(currentArrayListOfAngles);
            //            converting array into JSON
            JSONObject json2 = new JSONObject();
            try {
                json2.put("angleArray", new JSONArray(arrayListOfTotalValues));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            totalAngleValues = json2.toString();

            String query1 = "UPDATE " + ANGLE_TABLE_NAME + "  SET angle_values = '" + totalAngleValues + "' WHERE username = '" + username + "'";
            db.execSQL(query1);
        }
    }


    public void changeForceValues(String username, String currentForceValues) {
        db = this.getReadableDatabase();
        String totalForceValues;
        //db = this.getReadableDatabase();
        String query = "select username, force_values from " + FORCE_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, prevForceValues = "";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);
                if (uname.equals(username)) {
                    prevForceValues = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        ArrayList<String> previousArrayListOfForce = new ArrayList<String>();
        if(!prevForceValues.equals("")) {
//        getting previous array list from string
            JSONObject json = null;
            try {
                json = new JSONObject(prevForceValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = json.optJSONArray("forceArray");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        previousArrayListOfForce.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        getting current array list from string
        ArrayList<String> currentArrayListOfForce = new ArrayList<String>();
        if(!currentForceValues.equals("")){
            JSONObject json1 = null;
            try {
                json1 = new JSONObject(currentForceValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray1 = json1.optJSONArray("forceArray");
            ;
            if (jsonArray1 != null) {
                int len = jsonArray1.length();
                for (int i = 0; i < len; i++) {
                    try {
                        currentArrayListOfForce.add(jsonArray1.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //Joining the two lists
        if(!prevForceValues.equals("") || !currentForceValues.equals("")) {
            List<String> arrayListOfTotalValues = new ArrayList<String>(previousArrayListOfForce);
            arrayListOfTotalValues.addAll(currentArrayListOfForce);
            //            converting array into JSON
            JSONObject json2 = new JSONObject();
            try {
                json2.put("forceArray", new JSONArray(arrayListOfTotalValues));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            totalForceValues = json2.toString();

            String query1 = "UPDATE " + FORCE_TABLE_NAME + "  SET force_values = '" + totalForceValues + "' WHERE username = '" + username + "'";
            db.execSQL(query1);
        }
    }


    public void changeArmTwistValues(String username, String currentArmTwistValues) {
        db = this.getReadableDatabase();
        String totalArmTwistValues;
        //db = this.getReadableDatabase();
        String query = "select username, arm_twist_values from " + ARM_TWIST_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, prevArmTwistValues = "";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);
                if (uname.equals(username)) {
                    prevArmTwistValues = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        ArrayList<String> previousArrayListOfArmTwist = new ArrayList<String>();
        if(!prevArmTwistValues.equals("")) {
//        getting previous array list from string
            JSONObject json = null;
            try {
                json = new JSONObject(prevArmTwistValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = json.optJSONArray("armTwistArray");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        previousArrayListOfArmTwist.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        getting current array list from string
        ArrayList<String> currentArrayListOfArmTwist = new ArrayList<String>();
        if(!currentArmTwistValues.equals("")){
            JSONObject json1 = null;
            try {
                json1 = new JSONObject(currentArmTwistValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray1 = json1.optJSONArray("armTwistArray");
            ;
            if (jsonArray1 != null) {
                int len = jsonArray1.length();
                for (int i = 0; i < len; i++) {
                    try {
                        currentArrayListOfArmTwist.add(jsonArray1.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //Joining the two lists
        if(!prevArmTwistValues.equals("") || !currentArmTwistValues.equals("")) {
            List<String> arrayListOfTotalValues = new ArrayList<String>(previousArrayListOfArmTwist);
            arrayListOfTotalValues.addAll(currentArrayListOfArmTwist);
            //            converting array into JSON
            JSONObject json2 = new JSONObject();
            try {
                json2.put("armTwistArray", new JSONArray(arrayListOfTotalValues));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            totalArmTwistValues = json2.toString();

            String query1 = "UPDATE " + ARM_TWIST_TABLE_NAME + "  SET arm_twist_values = '" + totalArmTwistValues + "' WHERE username = '" + username + "'";
            db.execSQL(query1);
        }
    }


    public void changeActionTimeValues(String username, String currentActioTimeValues) {
        db = this.getReadableDatabase();
        String totalActionTimeValues;
        //db = this.getReadableDatabase();
        String query = "select username, action_time_values from " + ACTION_TIME_TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        String uname, prevActionTimeValues = "";
        if (cursor.moveToFirst()) {
            do {
                uname = cursor.getString(0);
                if (uname.equals(username)) {
                    prevActionTimeValues = cursor.getString(1);
                    break;
                }
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        ArrayList<String> previousArrayListOfActionTime = new ArrayList<String>();
        if(!prevActionTimeValues.equals("")) {
//        getting previous array list from string
            JSONObject json = null;
            try {
                json = new JSONObject(prevActionTimeValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = json.optJSONArray("actionTimeArray");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        previousArrayListOfActionTime.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
//        getting current array list from string
        ArrayList<String> currentArrayListOfActionTime = new ArrayList<String>();
        if(!currentActioTimeValues.equals("")){
            JSONObject json1 = null;
            try {
                json1 = new JSONObject(currentActioTimeValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray1 = json1.optJSONArray("actionTimeArray");
            ;
            if (jsonArray1 != null) {
                int len = jsonArray1.length();
                for (int i = 0; i < len; i++) {
                    try {
                        currentArrayListOfActionTime.add(jsonArray1.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        //Joining the two lists
        if(!prevActionTimeValues.equals("") || !currentActioTimeValues.equals("")) {
            List<String> arrayListOfTotalValues = new ArrayList<String>(previousArrayListOfActionTime);
            arrayListOfTotalValues.addAll(currentArrayListOfActionTime);
            //            converting array into JSON
            JSONObject json2 = new JSONObject();
            try {
                json2.put("actionTimeArray", new JSONArray(arrayListOfTotalValues));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            totalActionTimeValues = json2.toString();

            String query1 = "UPDATE " + ACTION_TIME_TABLE_NAME + "  SET action_time_values = '" + totalActionTimeValues + "' WHERE username = '" + username + "'";
            db.execSQL(query1);
        }
    }


    public void changeAngleValuesWithDate(String username, String currentAngleValues , String sessionDate) {

        db = this.getReadableDatabase();
        //Converting current angle values string into arraylist
        ArrayList<String> currentArrayListOfAngles1 = new ArrayList<String>();
        JSONObject json2 = null;
        try {
            json2 = new JSONObject(currentAngleValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray2 = json2.optJSONArray("angleArray");
        if (jsonArray2 != null) {
            int len = jsonArray2.length();
            for (int i = 0; i < len; i++) {
                try {
                    currentArrayListOfAngles1.add(jsonArray2.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //now converting array into string
        JSONObject json4 = new JSONObject();
        try {
            json4.put("angleArray", new JSONArray(currentArrayListOfAngles1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String query = "select  angle_values from " + ANGLE_HISTORY_TABLE_NAME + " where session_date = '" + sessionDate
                +"' AND username = '" + username +"'";
        Cursor cursor = db.rawQuery(query, null);
        String  prevAngleValues = "" ;

        if (cursor.moveToFirst()) {
            prevAngleValues = cursor.getString(0);
        }
        if(prevAngleValues.equals("")){
            insertPlayerAngleValuesWithDate(username,currentAngleValues,sessionDate);
        }
        else{
            String totalAngleValues;
            ArrayList<String> previousArrayListOfAngles = new ArrayList<String>();

//        getting previous array list from string
            JSONObject json = null;
            try {
                json = new JSONObject(prevAngleValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = json.optJSONArray("angleArray");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        previousArrayListOfAngles.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
//        getting current array list from string
            ArrayList<String> currentArrayListOfAngles = new ArrayList<String>();
            if(!currentAngleValues.equals("")){
                JSONObject json1 = null;
                try {
                    json1 = new JSONObject(currentAngleValues);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray1 = json1.optJSONArray("angleArray");
                if (jsonArray1 != null) {
                    int len = jsonArray1.length();
                    for (int i = 0; i < len; i++) {
                        try {
                            currentArrayListOfAngles.add(jsonArray1.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            //Joining the two lists
            if(!prevAngleValues.equals("") || !currentAngleValues.equals("")) {
                List<String> arrayListOfTotalValues = new ArrayList<String>(previousArrayListOfAngles);
                arrayListOfTotalValues.addAll(currentArrayListOfAngles);


                //            converting array into JSON

                JSONObject json3 = new JSONObject();
                try {
                    json3.put("angleArray", new JSONArray(arrayListOfTotalValues));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                totalAngleValues = json3.toString();

                String query1 = "UPDATE " + ANGLE_HISTORY_TABLE_NAME + "  SET angle_values = '" + totalAngleValues + "' WHERE username = '" + username + "'" +
                        " AND session_date = '" + sessionDate +"'";
                db.execSQL(query1);
            }
        }
        cursor.close();
    }

    public void changeArmTwistValuesWithDate(String username, String currentArmTwistValues , String sessionDate) {

        db = this.getReadableDatabase();
        //Converting current angle values string into arraylist
        ArrayList<String> currentArrayListOfArmTwist1 = new ArrayList<String>();
        JSONObject json2 = null;
        try {
            json2 = new JSONObject(currentArmTwistValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray2 = json2.optJSONArray("armTwistArray");
        if (jsonArray2 != null) {
            int len = jsonArray2.length();
            for (int i = 0; i < len; i++) {
                try {
                    currentArrayListOfArmTwist1.add(jsonArray2.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //now converting array into string
        JSONObject json4 = new JSONObject();
        try {
            json4.put("armTwistArray", new JSONArray(currentArrayListOfArmTwist1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String query = "select  arm_twist_values from " + ARM_TWIST_HISTORY_TABLE_NAME + " where session_date = '" + sessionDate
                +"' AND username = '" + username +"'";
        Cursor cursor = db.rawQuery(query, null);
        String  prevArmTwistValues = "" ;

        if (cursor.moveToFirst()) {
            prevArmTwistValues = cursor.getString(0);
        }
        if(prevArmTwistValues.equals("")){
            insertPlayerArmTwistValuesWithDate(username,currentArmTwistValues,sessionDate);
        }
        else{
            String totalArmTwistValues;
            ArrayList<String> previousArrayListOfArmTwist = new ArrayList<String>();

//        getting previous array list from string
            JSONObject json = null;
            try {
                json = new JSONObject(prevArmTwistValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = json.optJSONArray("armTwistArray");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        previousArrayListOfArmTwist.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
//        getting current array list from string
            ArrayList<String> currentArrayListOfArmTwist = new ArrayList<String>();
            if(!currentArmTwistValues.equals("")){
                JSONObject json1 = null;
                try {
                    json1 = new JSONObject(currentArmTwistValues);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray1 = json1.optJSONArray("armTwistArray");
                if (jsonArray1 != null) {
                    int len = jsonArray1.length();
                    for (int i = 0; i < len; i++) {
                        try {
                            currentArrayListOfArmTwist.add(jsonArray1.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            //Joining the two lists
            if(!prevArmTwistValues.equals("") || !currentArmTwistValues.equals("")) {
                List<String> arrayListOfTotalValues = new ArrayList<String>(previousArrayListOfArmTwist);
                arrayListOfTotalValues.addAll(currentArrayListOfArmTwist);


                //            converting array into JSON

                JSONObject json3 = new JSONObject();
                try {
                    json3.put("armTwistArray", new JSONArray(arrayListOfTotalValues));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                totalArmTwistValues = json3.toString();

                String query1 = "UPDATE " + ARM_TWIST_HISTORY_TABLE_NAME + "  SET arm_twist_values = '" + totalArmTwistValues + "' WHERE username = '" + username + "'" +
                        " AND session_date = '" + sessionDate +"'";
                db.execSQL(query1);
            }
        }
        cursor.close();
    }



    public void changeForceValuesWithDate(String username, String currentForceValues , String sessionDate) {

        db = this.getReadableDatabase();
        //Converting current angle values string into arraylist
        ArrayList<String> currentArrayListOfForce1 = new ArrayList<String>();
        JSONObject json2 = null;
        try {
            json2 = new JSONObject(currentForceValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray2 = json2.optJSONArray("forceArray");
        if (jsonArray2 != null) {
            int len = jsonArray2.length();
            for (int i = 0; i < len; i++) {
                try {
                    currentArrayListOfForce1.add(jsonArray2.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //now converting array into string
        JSONObject json4 = new JSONObject();
        try {
            json4.put("forceArray", new JSONArray(currentArrayListOfForce1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String query = "select  force_values from " + FORCE_HISTORY_TABLE_NAME + " where session_date = '" + sessionDate
                +"' AND username = '" + username +"'";
        Cursor cursor = db.rawQuery(query, null);
        String  prevForceValues = "" ;

        if (cursor.moveToFirst()) {
            prevForceValues = cursor.getString(0);
        }
        if(prevForceValues.equals("")){
            insertPlayerForceValuesWithDate(username,currentForceValues,sessionDate);
        }
        else{
            String totalForceValues;
            ArrayList<String> previousArrayListOfForce = new ArrayList<String>();

//        getting previous array list from string
            JSONObject json = null;
            try {
                json = new JSONObject(prevForceValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = json.optJSONArray("forceArray");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        previousArrayListOfForce.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
//        getting current array list from string
            ArrayList<String> currentArrayListOfForce = new ArrayList<String>();
            if(!currentForceValues.equals("")){
                JSONObject json1 = null;
                try {
                    json1 = new JSONObject(currentForceValues);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray1 = json1.optJSONArray("forceArray");
                if (jsonArray1 != null) {
                    int len = jsonArray1.length();
                    for (int i = 0; i < len; i++) {
                        try {
                            currentArrayListOfForce.add(jsonArray1.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            //Joining the two lists
            if(!prevForceValues.equals("") || !currentForceValues.equals("")) {
                List<String> arrayListOfTotalValues = new ArrayList<String>(previousArrayListOfForce);
                arrayListOfTotalValues.addAll(currentArrayListOfForce);


                //            converting array into JSON

                JSONObject json3 = new JSONObject();
                try {
                    json3.put("forceArray", new JSONArray(arrayListOfTotalValues));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                totalForceValues = json3.toString();

                String query1 = "UPDATE " + FORCE_HISTORY_TABLE_NAME + "  SET force_values = '" + totalForceValues + "' WHERE username = '" + username + "'" +
                        " AND session_date = '" + sessionDate +"'";
                db.execSQL(query1);
            }
        }
        cursor.close();
    }


    public void changeActionTimeValuesWithDate(String username, String currentActionTimeValues , String sessionDate) {

        db = this.getReadableDatabase();
        //Converting current angle values string into arraylist
        ArrayList<String> currentArrayListOfActionTime1 = new ArrayList<String>();
        JSONObject json2 = null;
        try {
            json2 = new JSONObject(currentActionTimeValues);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray jsonArray2 = json2.optJSONArray("actionTimeArray");
        if (jsonArray2 != null) {
            int len = jsonArray2.length();
            for (int i = 0; i < len; i++) {
                try {
                    currentArrayListOfActionTime1.add(jsonArray2.get(i).toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        //now converting array into string
        JSONObject json4 = new JSONObject();
        try {
            json4.put("actionTimeArray", new JSONArray(currentArrayListOfActionTime1));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String query = "select  action_time_values from " + ACTION_TIME_HISTORY_TABLE_NAME + " where session_date = '" + sessionDate
                +"' AND username = '" + username +"'";
        Cursor cursor = db.rawQuery(query, null);
        String  prevActionTimeValues = "" ;

        if (cursor.moveToFirst()) {
            prevActionTimeValues = cursor.getString(0);
        }
        if(prevActionTimeValues.equals("")){
            insertPlayerActionTimeValuesWithDate(username,currentActionTimeValues,sessionDate);
        }
        else{
            String totalActionTimeValues;
            ArrayList<String> previousArrayListOfActionTime = new ArrayList<String>();

//        getting previous array list from string
            JSONObject json = null;
            try {
                json = new JSONObject(prevActionTimeValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONArray jsonArray = json.optJSONArray("actionTimeArray");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        previousArrayListOfActionTime.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
//        getting current array list from string
            ArrayList<String> currentArrayListOfActionTime = new ArrayList<String>();
            if(!currentActionTimeValues.equals("")){
                JSONObject json1 = null;
                try {
                    json1 = new JSONObject(currentActionTimeValues);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray1 = json1.optJSONArray("actionTimeArray");
                if (jsonArray1 != null) {
                    int len = jsonArray1.length();
                    for (int i = 0; i < len; i++) {
                        try {
                            currentArrayListOfActionTime.add(jsonArray1.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            //Joining the two lists
            if(!prevActionTimeValues.equals("") || !currentActionTimeValues.equals("")) {
                List<String> arrayListOfTotalValues = new ArrayList<String>(previousArrayListOfActionTime);
                arrayListOfTotalValues.addAll(currentArrayListOfActionTime);


                //            converting array into JSON

                JSONObject json3 = new JSONObject();
                try {
                    json3.put("actionTimeArray", new JSONArray(arrayListOfTotalValues));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                totalActionTimeValues = json3.toString();

                String query1 = "UPDATE " + ACTION_TIME_HISTORY_TABLE_NAME + "  SET action_time_values = '" + totalActionTimeValues + "' WHERE username = '" + username + "'" +
                        " AND session_date = '" + sessionDate +"'";
                db.execSQL(query1);
            }
        }
        cursor.close();
    }



}


//    public void addEntry( String name, byte[] image) throws SQLiteException {
//        SQLiteDatabase database = this.getWritableDatabase();
//        ContentValues cv = new  ContentValues();
//        cv.put(KEY_NAME,    name);
//        cv.put(KEY_IMAGE,   image);
//        database.insert(IMAGE_TABLE_NAME, null, cv );
//    }
//
//
//
//    public Bitmap getImage(String username){
//        db = this.getReadableDatabase();
//        String query = "select image_name , image_data  from " + IMAGE_TABLE_NAME;
//        Cursor cursor = db.rawQuery(query,null);
//        String imageName;
//        byte [] imageData;
//        imageData = null;
//
//        Bitmap bitmapImage = null;
//        bitmapImage = BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.profile1);
//
//
//        if(cursor==null){
//            return bitmapImage;
//        }
//
//        if(cursor.moveToFirst()){
//            do{
//                imageName = cursor.getString(0);
//                if(imageName.equals(username)){
//                    imageData = cursor.getBlob(1);
//                    bitmapImage = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//
//                    break;
//                }
//            }
//            while(cursor.moveToNext());
//        }
//        cursor.close();
//        return bitmapImage;
//    }

