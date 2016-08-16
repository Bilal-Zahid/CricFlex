package com.example.cricflex;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Asawal on 8/11/2016.
 */
public class ActivityProfile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
    }
}
