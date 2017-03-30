package com.example.cricflex;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
//        editor.clear(); //clear all stored data

        getSharedPreferences(ctx).edit().remove(PREF_EMAIL).commit();;
        editor.commit();
    }

    public static void setEmailList(Context ctx, List<String> emailList)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();


//        List<String> list = new ArrayList<String>();

        editor.putString("emailList", TextUtils.join(",", emailList));



        System.out.println("Email List in setshared pref: " + emailList);
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < emailList.size(); i++) {
//            sb.append(emailList.get(i)).append(",");
//        }
//        editor.putString("email_list", sb.toString());
//        editor.putString(PREF_EMAIL, userName);
        editor.commit();
        System.out.println("Email List in setshared pref: " + getEmailList(ctx));


    }

    public static List<String> getEmailList(Context ctx)
    {



        String serialized = getSharedPreferences(ctx).getString("emailList", null);

        List<String> list = new ArrayList<>();

        if(serialized!=null)
             list = Arrays.asList(TextUtils.split(serialized, ","));

        System.out.println("nai hoga print: " + serialized);

//        emailList.addAll(Arrays.asList(getSharedPreferences(ctx).getString("email_list", "").split(",")));
//        System.out.println("Email List in get sharedpref: " + getSharedPreferences(ctx).getString("email_list", ""));

        return list;
    }


}
