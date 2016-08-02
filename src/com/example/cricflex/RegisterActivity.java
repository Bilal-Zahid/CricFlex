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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends Activity {

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pattern = Pattern.compile(EMAIL_PATTERN);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //hide action bar

        getActionBar().hide();
        setContentView(R.layout.activity_register);

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
                    Toast.makeText(RegisterActivity.this, "Empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                matcher = pattern.matcher(emailstr);
                if(!matcher.matches()){
                    Toast.makeText(RegisterActivity.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!helper.searchPassword(usernamestr).equals("not found")){
                    Toast.makeText(RegisterActivity.this, "Username Already Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                p.setUsername(usernamestr);
                p.setEmail(emailstr);
                p.setPassword(passwordstr);

                helper.insertPlayer(p);
                Toast.makeText(RegisterActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        bLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(i);
            }
        });


    }
}
