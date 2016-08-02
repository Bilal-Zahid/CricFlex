package com.example.cricflex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {


    DatabaseHelper helper = new DatabaseHelper(this);

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
                    //Toast message = Toast.makeText(LoginActivity.this, "Right Password! ", Toast.LENGTH_SHORT);
                    //CharSequence text1 = password;
                    // text2 = passwordstr;
                    Toast.makeText(LoginActivity.this, "Signed In" ,
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.putExtra("username",usernamestr);
                    LoginActivity.this.startActivity(i);
                }
                else{
                    //Toast message = Toast.makeText(LoginActivity.this, "Wrong Password! ", Toast.LENGTH_SHORT);
                    Toast.makeText(LoginActivity.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final Button bRegister = (Button) findViewById(R.id.bRegister);
        bRegister.setPaintFlags(bRegister.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // underline button text
        bRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(i);
            }
        });

    }
}
        /*
        final TextView registerLink = (TextView) findViewById(R.id.tvRegisterHere);


        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });



    public void onButtonClick(View v) {
        if (v.getId() == R.id.bRegister) {

            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(i);
        }
    }
    */

