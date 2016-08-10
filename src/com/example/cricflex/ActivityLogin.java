package com.example.cricflex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityLogin extends Activity {


    DatabaseHelper helper = new DatabaseHelper(this);
    private static Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //hide action bar

        getActionBar().hide();

        setContentView(R.layout.activity_login);



        /*
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);


        final String usernamestr = etUsername.getText().toString();
        final String passwordstr = etPassword.getText().toString();
        */

        Button button_forgot_password = (Button) findViewById(R.id.button_forgot_password);
        button_forgot_password.setOnClickListener(new handleForgotPasswordButton());

        final Button bLogin = (Button) findViewById(R.id.Login);
        bLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                final EditText etUsername = (EditText) findViewById(R.id.etUsername);
                final EditText etPassword = (EditText) findViewById(R.id.etPassword);


                final String usernamestr = etUsername.getText().toString();
                final String passwordstr = etPassword.getText().toString();

                String password = helper.searchPassword(usernamestr);
                //System.out.print("Dpassword: "+ password + "entered password:"+passwordstr);
                //Log.v("Dpassword", password);
                //Log.v("Entered Password", passwordstr);

                if(password.equals(passwordstr) && password!=null && password!=""){
                    //Toast message = Toast.makeText(ActivityLogin.this, "Right Password! ", Toast.LENGTH_SHORT);
                    //CharSequence text1 = password;
                    // text2 = passwordstr;
                    Toast.makeText(ActivityLogin.this, "Signed In" ,
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ActivityLogin.this, ActivityMain.class);
                    i.putExtra("username",usernamestr);
                    i.putExtra("email",helper.getEmail(usernamestr));
                    ActivityLogin.this.startActivity(i);
                }
                else{
                    //Toast message = Toast.makeText(ActivityLogin.this, "Wrong Password! ", Toast.LENGTH_SHORT);
                    Toast.makeText(ActivityLogin.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setPaintFlags(bRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // underline button text
        bRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ActivityLogin.this, ActivityRegister.class);
                ActivityLogin.this.startActivity(i);
            }
        });

    }

    class handleForgotPasswordButton implements View.OnClickListener {
        public void onClick(View v) {

            setContentView(R.layout.activity_forgot_password);

        }
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            this.finishAffinity(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
        /*
        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);


        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(ActivityLogin.this, ActivityRegister.class);
                ActivityLogin.this.startActivity(registerIntent);
            }
        });



    public void onButtonClick(View v) {
        if (v.getId() == R.id.bRegister) {

            Intent i = new Intent(ActivityLogin.this, ActivityRegister.class);
            ActivityLogin.this.startActivity(i);
        }
    }
    */

