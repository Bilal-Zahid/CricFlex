package com.example.cricflex;



import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;


public class ActivityAbout extends Activity {
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_about);
	}

}
