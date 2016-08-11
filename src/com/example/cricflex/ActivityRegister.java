package com.example.cricflex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        final EditText reg_username = (EditText) findViewById(R.id.reg_username);
        final EditText reg_email = (EditText) findViewById(R.id.reg_email);
        final EditText reg_password = (EditText) findViewById(R.id.reg_password);
*/
        final Button button_register = (Button) findViewById(R.id.button_register);
        //final Button bLogin = (Button) findViewById(R.id.clogin);
        //bLogin.setPaintFlags(bLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); // underline button text
/*
        final String namestr = etName.getText().toString();
        final String usernamestr = reg_username.getText().toString();
        final String emailstr = reg_email.getText().toString();
        final String passwordstr = reg_password.getText().toString();
*/
        final Player p = new Player();


        button_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                final EditText etName = (EditText) findViewById(R.id.etName);
                final EditText reg_username = (EditText) findViewById(R.id.reg_username);
                final EditText reg_email = (EditText) findViewById(R.id.reg_email);
                final EditText reg_password = (EditText) findViewById(R.id.reg_password);
                final EditText reg_security = (EditText) findViewById(R.id.reg_security);

//                final String namestr = etName.getText().toString();
                final String usernamestr = reg_username.getText().toString();
                final String emailstr = reg_email.getText().toString();
                final String passwordstr = reg_password.getText().toString();
                final String securitystr = reg_security.getText().toString();
//                p.setName(namestr);
                if(usernamestr.equals("")||passwordstr.equals("")||emailstr.equals("")||securitystr.equals("")){
                    Toast.makeText(ActivityRegister.this, "Empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                matcher = pattern.matcher(emailstr);
                if(!matcher.matches()){
                    Toast.makeText(ActivityRegister.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!helper.getPassword(usernamestr).equals("not found")){
                    Toast.makeText(ActivityRegister.this, "Username Already Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                p.setUsername(usernamestr);
                p.setEmail(emailstr);
                p.setPassword(passwordstr);
                p.setSecurity(securitystr);
                helper.insertPlayer(p);

                Toast.makeText(ActivityRegister.this, "Successfully Registered Account", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                startActivity(intent);
            }
        });

//        bLogin.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent i = new Intent(ActivityRegister.this, ActivityLogin.class);
//                ActivityRegister.this.startActivity(i);
//            }
//        });


    }
    @Override
    public void onBackPressed() {
        // your code.
        Intent i = new Intent(ActivityRegister.this, ActivityLogin.class);
        ActivityRegister.this.startActivity(i);
    }
}
