package com.example.cricflex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityRegister extends Activity {

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().hide();


        setContentView(R.layout.activity_register);
        pattern = Pattern.compile(EMAIL_PATTERN);
/*
        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
*/
        final Button bRegister = (Button) findViewById(R.id.bRegister);
        final Button bLogin = (Button) findViewById(R.id.clogin);
        bLogin.setPaintFlags(bLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // underline button text
/*
        final String namestr = etName.getText().toString();
        final String usernamestr = etUsername.getText().toString();
        final String emailstr = etEmail.getText().toString();
        final String passwordstr = etPassword.getText().toString();
*/
        final Player p = new Player();


        bRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                final EditText etName = (EditText) findViewById(R.id.etName);
                final EditText etUsername = (EditText) findViewById(R.id.etUsername);
                final EditText etEmail = (EditText) findViewById(R.id.etEmail);
                final EditText etPassword = (EditText) findViewById(R.id.etPassword);

//                final String namestr = etName.getText().toString();
                final String usernamestr = etUsername.getText().toString();
                final String emailstr = etEmail.getText().toString();
                final String passwordstr = etPassword.getText().toString();

//                p.setName(namestr);
                if(usernamestr.equals("")||passwordstr.equals("")||emailstr.equals("")){
                    Toast.makeText(ActivityRegister.this, "Empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                matcher = pattern.matcher(emailstr);
                if(!matcher.matches()){
                    Toast.makeText(ActivityRegister.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!helper.searchPassword(usernamestr).equals("not found")){
                    Toast.makeText(ActivityRegister.this, "Username Already Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                p.setUsername(usernamestr);
                p.setEmail(emailstr);
                p.setPassword(passwordstr);

                helper.insertPlayer(p);
                Toast.makeText(ActivityRegister.this, "Registered", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(intent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(ActivityRegister.this, ActivityLogin.class);
                ActivityRegister.this.startActivity(i);
            }
        });


    }
    @Override
    public void onBackPressed() {
        // your code.
        Intent i = new Intent(ActivityRegister.this, ActivityLogin.class);
        ActivityRegister.this.startActivity(i);
    }
}
