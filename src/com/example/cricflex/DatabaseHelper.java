package com.example.cricflex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bilal on 6/21/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cricflex.db";
    private static final String TABLE_NAME = "players";
    private static final String COLUMN_ID = "id";

    //private static final String COLUMN_NAME = "name";

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




    SQLiteDatabase db;


    private static final String TABLE_CREATE = "create table "+TABLE_NAME+" (id integer primary key not null  , "+
            " email text not null , username text not null, password text not null, security text not null, " +
            " gender text not null, DOB text not null, location text not null, bowlingstyle text not null, bowlingarm text not null, careerlevel text not null);";



    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS" + TABLE_NAME;
        db.execSQL(query);
        this.onCreate(db);
    }

    public void insertPlayer(Player p){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        String query = "select * from players";
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        cursor.close();
        values.put(COLUMN_ID,count);
        //values.put(COLUMN_NAME , p.getName());

        values.put(COLUMN_EMAIL, p.getEmail());
        values.put(COLUMN_USERNAME, p.getusername());
        values.put(COLUMN_PASSWORD, p.getPassword());
        values.put(COLUMN_SECURITY, p.getSecurity());

        values.put(COLUMN_GENDER, p.getGender());
        values.put(COLUMN_DOB, p.getDOB());
        values.put(COLUMN_LOCATION, p.getLocation());
        values.put(COLUMN_BOWLINGSTYLE, p.getBowlingStyle());
        values.put(COLUMN_BOWLINGARM, p.getBowlingArm());
        values.put(COLUMN_CAREERLEVEL, p.getCareerLevel());


        db.insert(TABLE_NAME, null , values);
        db.close();
    }



    public String getPassword(String username){
        db = this.getReadableDatabase();
        String query = "select username, password from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String uname , pass;
        pass = "not found";
        if(cursor.moveToFirst()){
            do{
                uname = cursor.getString(0);

                if(uname.equals(username)){
                    pass = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return pass;
    }

    public String getEmail(String username){
        db = this.getReadableDatabase();
        String query = "select username, email from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String uname , email;
        email = "not found";
        if(cursor.moveToFirst()){
            do{
                uname = cursor.getString(0);

                if(uname.equals(username)){
                    email = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return email;
    }
    public String getSecurity(String username){
        db = this.getReadableDatabase();
        String query = "select username, security from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String uname , security;
        security = "not found";
        if(cursor.moveToFirst()){
            do{
                uname = cursor.getString(0);

                if(uname.equals(username)){
                    security = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return security;
    }

    public String getGender(String username){
        db = this.getReadableDatabase();
        String query = "select username, gender from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String uname , gender;
        gender = "not found";
        if(cursor.moveToFirst()){
            do{
                uname = cursor.getString(0);

                if(uname.equals(username)){
                    gender = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return gender;
    }

    public String getDOB(String username){
        db = this.getReadableDatabase();
        String query = "select username, DOB from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String uname , DOB;
        DOB = "not found";
        if(cursor.moveToFirst()){
            do{
                uname = cursor.getString(0);

                if(uname.equals(username)){
                    DOB = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return DOB;
    }

    public String getLocation(String username){
        db = this.getReadableDatabase();
        String query = "select username, location from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String uname , location;
        location = "not found";
        if(cursor.moveToFirst()){
            do{
                uname = cursor.getString(0);

                if(uname.equals(username)){
                    location = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return location;
    }

    public String getBowlingStyle(String username){
        db = this.getReadableDatabase();
        String query = "select username, bowlingstyle from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String uname , bowlingStyle;
        bowlingStyle = "not found";
        if(cursor.moveToFirst()){
            do{
                uname = cursor.getString(0);

                if(uname.equals(username)){
                    bowlingStyle = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return bowlingStyle;
    }

    public String getBowlingArm(String username){
        db = this.getReadableDatabase();
        String query = "select username, bowlingarm from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String uname , bowlingArm;
        bowlingArm = "not found";
        if(cursor.moveToFirst()){
            do{
                uname = cursor.getString(0);

                if(uname.equals(username)){
                    bowlingArm = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return bowlingArm;
    }


    public String getCareerLevel(String username){
        db = this.getReadableDatabase();
        String query = "select username, careerlevel from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        String uname , careerLevel;
        careerLevel = "not found";
        if(cursor.moveToFirst()){
            do{
                uname = cursor.getString(0);

                if(uname.equals(username)){
                    careerLevel = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        cursor.close();
        return careerLevel;
    }










    public void changePassword(String username, String newPassword){
        db = this.getReadableDatabase();
//        String query = "select username, security from " + TABLE_NAME;
        String query = "UPDATE "+TABLE_NAME+"  SET password = '"+newPassword+"' WHERE username = '"+username+"'";
        //db.rawQuery(query,null);
        db.execSQL(query);
        //boolean flag = true;
        //return flag;
    }

}
