package com.example.cricflex;

import android.app.Activity;
import android.os.Bundle;



public class test_asawal extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getActionBar().hide();
        setTitle("Profile");
    }
}
