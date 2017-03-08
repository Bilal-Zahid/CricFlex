package com.example.cricflex;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by bilal on 8/13/2016.
 */
public class SaveSharedPreference {
//    static final String PREF_EMAIL= "email";
    static final String PREF_EMAIL= "email";
    //static final String PREF_USER_NAME= "email";


    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setEmail(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_EMAIL, userName);
        editor.commit();
    }

    public static String getEmail(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_EMAIL, "");
    }


//    public static void setEmail(Context ctx, String email)
//    {
//        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
//        editor.putString(PREF_EMAIL, email);
//        editor.commit();
//    }

//    public static String getEmail(Context ctx)
//    {
//        return getSharedPreferences(ctx).getString(PREF_EMAIL, "");
//    }

    public static void clearEmail(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
}
