package com.example.cricflex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends Activity {


    DatabaseHelper helper = new DatabaseHelper(this);
    private static Boolean exit = false;
    private static String layoutCheck = "login activity";

    private static EditText fgp_email = null;
    private static String fgpEmail;

    private static EditText fgp_new_password = null;
    private static String fgpNewPassword;

    private static EditText fgp_security = null;
    private static String fgpSecurity;




    AutoCompleteTextView etEmail;
    EditText etPassword;



    private ProgressDialog progressDialog ;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().hide();



        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        //hide action bar

        getActionBar().hide();

        setContentView(R.layout.activity_login);


//        if(firebaseAuth.getCurrentUser()!=null){
//            // profile activity
//
//            Intent i = new Intent(ActivityLogin.this, ActivityMain.class);
//            ActivityLogin.this.startActivity(i);
//        }

//        progressDialog = new ProgressDialog(this);
//        firebaseAuth = FirebaseAuth.getInstance();

        etEmail = (AutoCompleteTextView) findViewById(R.id.lgn_username);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, helper.getAllEmails());

        etEmail.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                etEmail.showDropDown();
                return false;
            }
        });
        etEmail.setAdapter(adapter);
        etPassword = (EditText) findViewById(R.id.lgn_password);
        //To hide the keyboard when user touch anywhere else on the screen

//        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    hideKeyboard(v);
//                }
//            }
//        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        /////////////////////////////////////
        /*
        final EditText etEmail = (EditText) findViewById(R.id.etEmail);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);


        final String usernamestr = etEmail.getText().toString();
        final String passwordstr = etPassword.getText().toString();
        */

        //Button button_forgot_password = (Button) findViewById(R.id.button_forgot_password);
        //button_forgot_password.setOnClickListener(new handleForgotPasswordButton());

        final Button bLogin = (Button) findViewById(R.id.Login);
        bLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){



//                userLogin();
                final String emailstr = etEmail.getText().toString();
                final String passwordstr = etPassword.getText().toString();

                String password = helper.getPassword(emailstr);
                //System.out.print("Dpassword: "+ password + "entered password:"+passwordstr);
                //Log.v("Dpassword", password);
                //Log.v("Entered Password", passwordstr);

                if(password.equals(passwordstr) && password!=null && password!=""){
                    //Toast message = Toast.makeText(ActivityLogin.this, "Right Password! ", Toast.LENGTH_SHORT);
                    //CharSequence text1 = password;
                    // text2 = passwordstr;
                    Toast.makeText(ActivityLogin.this, "Signed In" ,
                            Toast.LENGTH_SHORT).show();

                    SaveSharedPreference.setEmail(ActivityLogin.this,emailstr);
//                    SaveSharedPreference.setEmail(ActivityLogin.this,helper.getEmail(usernamestr));
//
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

    private void userLogin() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
        }


        progressDialog.setMessage("Signing In");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            //logging in

                            finish();
                            Intent i = new Intent(ActivityLogin.this, ActivityMain.class);
                            ActivityLogin.this.startActivity(i);

                        }

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
                fgp_email = (EditText) findViewById(R.id.fgp_username);
                fgpEmail = fgp_email.getText().toString();

                if(helper.getPassword(fgpEmail).equals("not found")){
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

                if(helper.getSecurity(fgpEmail)==null || helper.getSecurity(fgpEmail).equals("not found")){
                    Toast.makeText(ActivityLogin.this, "kaila kalandar", Toast.LENGTH_SHORT).show();
                }

                else if(!helper.getSecurity(fgpEmail).equals(fgpSecurity)){
                    Toast.makeText(ActivityLogin.this, "Answer doesnot match", Toast.LENGTH_SHORT).show();
                    return;
                }

                else if(helper.getSecurity(fgpEmail).equals(fgpSecurity)){
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
                    helper.changePassword(fgpEmail,fgpNewPassword);
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
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
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

