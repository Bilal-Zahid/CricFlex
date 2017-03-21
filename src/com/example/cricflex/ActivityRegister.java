package com.example.cricflex;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityRegister extends Activity{

    private Pattern pattern;
    private Matcher matcher;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";




    DatabaseHelper helper = new DatabaseHelper(this);


    //progress dialog for waiting
    private ProgressDialog progressDialog;

    //Firebase
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_register);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        pattern = Pattern.compile(EMAIL_PATTERN);

        final Button button_register = (Button) findViewById(R.id.button_register);

        button_register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                final EditText reg_name = (EditText) findViewById(R.id.reg_name);
                final EditText reg_email = (EditText) findViewById(R.id.reg_email);
                final EditText reg_password = (EditText) findViewById(R.id.reg_password);

                final String namestr = reg_name.getText().toString();
                final String emailstr = reg_email.getText().toString();
                final String passwordstr = reg_password.getText().toString();

                if(emailstr.equals("")||passwordstr.equals("") || namestr.equals("")){
                    Toast.makeText(ActivityRegister.this, "Empty fields", Toast.LENGTH_SHORT).show();
                    return;
                }


                matcher = pattern.matcher(emailstr);
                if(!matcher.matches()){
                    Toast.makeText(ActivityRegister.this, "Invalid Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!helper.getPassword(emailstr).equals("not found")){
                    Toast.makeText(ActivityRegister.this, "Email Already Exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(namestr.length()>20){
                    final Toast toast = Toast.makeText(ActivityRegister.this, "Name length should be within 20 character" , Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 500);
                    return;
                }
                if(passwordstr.length()<6){
                    final Toast toast = Toast.makeText(ActivityRegister.this, "Password should be atleast 6 characters long" , Toast.LENGTH_SHORT);
                    toast.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            toast.cancel();
                        }
                    }, 1000);
                    return;
                }

//firebase
                progressDialog.setMessage("Registering User...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);

                firebaseAuth.createUserWithEmailAndPassword(emailstr,passwordstr)
                        .addOnCompleteListener(ActivityRegister.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if(task.isSuccessful()){
                                    //user is successfully registered

                                    progressDialog.dismiss();
                                    Toast.makeText(ActivityRegister.this,"You have been successfully registered.",Toast.LENGTH_SHORT).show();




                                    Player player = new Player();
                                    player.setName(namestr);
                                    player.setEmail(emailstr);
                                    player.setPassword(passwordstr);
                                    player.setGender("-");
                                    player.setLocation("-");
                                    player.setDOB("-");
                                    player.setBowlingArm("-");
                                    player.setBowlingStyle("-");
                                    player.setCareerLevel("-");
                                    player.setWeight("-");


                                    player.setLegalBowls("0");
                                    player.setIllegalBowls("0");
                                    player.setAverageAngle("0");
                                    player.setLongestStreak("0");
                                    player.setLastBowlAngle("0");


//                                    saveImage(getApplicationContext(),bitmapImage,email,"jpeg");







//                        userRef.child("name").setValue(player);
//                        userRef.child("name").child("values").setValue(player);
                                    //helper.addEntry(email,dbBitmapUtility.getBytes(bitmapImage));



                                    helper.insertPlayerStats(player);
                                    helper.insertPlayer(player);

                                    helper.insertPlayerAngleValues(player.getEmail(),"");
                                    helper.insertPlayerActionTimeValues(player.getEmail(),"");
                                    helper.insertPlayerForceValues(player.getEmail(),"");
                                    helper.insertPlayerArmTwistValues(player.getEmail(),"");


                                    helper.insertPlayerAngleValuesWithDate(player.getEmail(),"","");
                                    helper.insertPlayerActionTimeValuesWithDate(player.getEmail(),"","");
                                    helper.insertPlayerForceValuesWithDate(player.getEmail(),"","");
                                    helper.insertPlayerArmTwistValuesWithDate(player.getEmail(),"","");





                                    Intent intent = new Intent(ActivityRegister.this, ActivityProfileSetup.class);
                                    intent.putExtra("name",namestr);
//                intent.putExtra("email", usernamestr);
                                    intent.putExtra("email", emailstr);
                                    intent.putExtra("password", passwordstr);
//                intent.putExtra("security", securitystr);
//                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
                                    startActivity(intent);
//                                    Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
//                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    progressDialog.dismiss();

                                    Toast.makeText(ActivityRegister.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(ActivityRegister.this, "Cannot connect to server. Please make sure you have internet connectivity",Toast.LENGTH_LONG).show();

                                }
                            }
                        });


            }
        });


    }
    @Override
    public void onBackPressed() {
        // your code.
        Intent i = new Intent(ActivityRegister.this, ActivityLogin.class);
        ActivityRegister.this.startActivity(i);
        finish();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            View v = getCurrentFocus();
//            if ( v instanceof EditText) {
//                Rect outRect = new Rect();
//                v.getGlobalVisibleRect(outRect);
//                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
//                    v.clearFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//        }
//        return super.dispatchTouchEvent( event );
//    }


}
