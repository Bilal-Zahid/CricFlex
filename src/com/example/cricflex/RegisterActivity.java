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

public class RegisterActivity extends Activity {

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
                p.setUsername(usernamestr);
                p.setEmail(emailstr);
                p.setPassword(passwordstr);

                helper.insertPlayer(p);

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
