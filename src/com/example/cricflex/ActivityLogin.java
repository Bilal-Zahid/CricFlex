package com.example.cricflex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ActivityLogin extends Activity {


    DatabaseHelper helper = new DatabaseHelper(this);
    private static Boolean exit = false;
    private static String layoutCheck = "login activity";

    private static EditText fgp_username = null;
    private static String fgpUsername;

    private static EditText fgp_new_password = null;
    private static String fgpNewPassword;

    private static EditText fgp_security = null;
    private static String fgpSecurity;
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

        //Button button_forgot_password = (Button) findViewById(R.id.button_forgot_password);
        //button_forgot_password.setOnClickListener(new handleForgotPasswordButton());

        final Button bLogin = (Button) findViewById(R.id.Login);
        bLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                final EditText etUsername = (EditText) findViewById(R.id.lgn_username);
                final EditText etPassword = (EditText) findViewById(R.id.lgn_password);



                //To hide the keyboard when user touch anywhere else on the screen

                etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            hideKeyboard(v);
                        }
                    }
                });
                etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (!hasFocus) {
                            hideKeyboard(v);
                        }
                    }
                });

                /////////////////////////////////////


                final String usernamestr = etUsername.getText().toString();
                final String passwordstr = etPassword.getText().toString();

                String password = helper.getPassword(usernamestr);
                //System.out.print("Dpassword: "+ password + "entered password:"+passwordstr);
                //Log.v("Dpassword", password);
                //Log.v("Entered Password", passwordstr);

                if(password.equals(passwordstr) && password!=null && password!=""){
                    //Toast message = Toast.makeText(ActivityLogin.this, "Right Password! ", Toast.LENGTH_SHORT);
                    //CharSequence text1 = password;
                    // text2 = passwordstr;
                    Toast.makeText(ActivityLogin.this, "Signed In" ,
                            Toast.LENGTH_SHORT).show();

                    SaveSharedPreference.setUserName(ActivityLogin.this,usernamestr);
                    SaveSharedPreference.setEmail(ActivityLogin.this,helper.getEmail(usernamestr));

                    Intent i = new Intent(ActivityLogin.this, ActivityMain.class);
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

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    public void setfgpLayout1(View view) {
        setContentView(R.layout.activity_forgot_password1);
        layoutCheck = "forgot password 1";

        final Button fgpNext = (Button) findViewById(R.id.fgp_next1);
        fgpNext.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                fgp_username = (EditText) findViewById(R.id.fgp_username);
                fgpUsername = fgp_username.getText().toString();

                if(helper.getPassword(fgpUsername).equals("not found")){
                    Toast.makeText(ActivityLogin.this, "Username doesn't exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                setfgpLayout2(null);

            }

        });


    }

    public void setfgpLayout2(View view) {
        setContentView(R.layout.activity_forgot_password2);
        layoutCheck = "forgot password 2";

        final Button fgpNext = (Button) findViewById(R.id.fgp_next2);
        fgpNext.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                fgp_security = (EditText) findViewById(R.id.fgp_security);
                fgpSecurity = fgp_security.getText().toString();

                if(helper.getSecurity(fgpUsername)==null || helper.getSecurity(fgpUsername).equals("not found")){
                    Toast.makeText(ActivityLogin.this, "kaila kalandar", Toast.LENGTH_SHORT).show();
                }

                else if(!helper.getSecurity(fgpUsername).equals(fgpSecurity)){
                    Toast.makeText(ActivityLogin.this, "Answer doesnot match", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(helper.getSecurity(fgpUsername).equals(fgpSecurity)){
                    setfgpLayout3(null);
                }
            }

        });

    }
    public void setfgpLayout3(View view) {
        setContentView(R.layout.activity_forgot_password3);
        layoutCheck = "forgot password 3";
        final Button fgpFinish = (Button) findViewById(R.id.fgp_finish);
        fgpFinish.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                fgp_new_password = (EditText) findViewById(R.id.fgp_new_password);
                fgpNewPassword = fgp_new_password.getText().toString();

                if( fgpNewPassword.equals("")){
                    Toast.makeText(ActivityLogin.this, "Empty Password Not Accepted", Toast.LENGTH_SHORT).show();
                    return;
                }

                else{
                    helper.changePassword(fgpUsername,fgpNewPassword);
                    setLoginLayout(null);
                }
            }

        });

    }
    public void setLoginLayout(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
        layoutCheck = "login activity";
    }

    @Override
    public void onBackPressed() {

        if(layoutCheck.equals("forgot password 1")){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            layoutCheck = "login activity";
            return;
        }
        if(layoutCheck.equals("forgot password 2")){
            //setContentView(R.layout.activity_forgot_password1);
            setfgpLayout1(null);
            //layoutCheck = "forgot password 1";
            return;
        }
        if(layoutCheck.equals("forgot password 3")){
            //setContentView(R.layout.activity_forgot_password1);
            setfgpLayout2(null);
            //layoutCheck = "forgot password 1";
            return;
        }

        if(layoutCheck.equals("login activity")){
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

