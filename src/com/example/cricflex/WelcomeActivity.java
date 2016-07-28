package com.example.cricflex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


public class WelcomeActivity extends Activity{
	
boolean doubleBackToExitPressedOnce = false;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        getActionBar().hide();
        findViewById(R.id.enter).setOnClickListener(new handleEnterButton());
        findViewById(R.id.about).setOnClickListener(new handleAboutButton());
        findViewById(R.id.help).setOnClickListener(new handleHelpButton());

}
	@Override
	public void onBackPressed() {
	    
		if (doubleBackToExitPressedOnce) {
	        super.onBackPressed();
	        finish();
	        System.exit(1);
	        return;
	    }

	    this.doubleBackToExitPressedOnce = true;
	    Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).show();

	    new Handler().postDelayed(new Runnable() {

	        @Override
	        public void run() {
	            doubleBackToExitPressedOnce=false;                       
	        }
	    }, 2000);
	} 
	
	 class handleEnterButton implements OnClickListener {
	        public void onClick(View v) {
		    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
		    startActivity(intent);	
		}
	    }
	 class handleHelpButton implements OnClickListener {
	        public void onClick(View v) {
		    Intent intent = new Intent(WelcomeActivity.this, HelpActivity.class);
		    startActivity(intent);	
		}
	    }
	 
	 class handleAboutButton implements OnClickListener {
	        public void onClick(View v) {
		    Intent intent = new Intent(WelcomeActivity.this, AboutActivity.class);
		    startActivity(intent);	
		}
	    }
	
	 

	
}
