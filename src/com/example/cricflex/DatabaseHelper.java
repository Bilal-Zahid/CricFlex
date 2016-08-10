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

    SQLiteDatabase db;


    private static final String TABLE_CREATE = "create table "+TABLE_NAME+" (id integer primary key not null  , "+
            " email text not null , username text not null, password text not null, security text not null);";



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

        db.insert(TABLE_NAME, null , values);
        db.close();
    }

    public String searchPassword(String username){
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
}
