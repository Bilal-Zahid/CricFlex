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
import android.support.percent.PercentRelativeLayout;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


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

    List<String> emailList = new ArrayList<>();
    String emailstr;


    private DatabaseReference databaseReference;

    private DatabaseReference usersDatabaseReference;


    private ProgressDialog progressDialog ;
    FirebaseAuth firebaseAuth;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.







        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_login);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        usersDatabaseReference = databaseReference.child("Users");






        etEmail = (AutoCompleteTextView) findViewById(R.id.lgn_email);
        etEmail.setThreshold(1);

        System.out.println("Aisay hi print" + SaveSharedPreference.getEmail(ActivityLogin.this) );
        emailList = SaveSharedPreference.getEmailList(ActivityLogin.this);

        System.out.println("Email List in login: " + SaveSharedPreference.getEmailList(ActivityLogin.this));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, emailList);


        etEmail.setAdapter(adapter);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                // ...
//                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator ?<List<String>>() {};
//                List messages = dataSnapshot.getValue(t);
//                if( messages == null ) {
//                    System.out.println("No email" + dataSnapshot.getValue());
//                }
//                else {
//                    System.out.println("The first email is: " + messages.get(0) );
//                }

//                collectEmails((Map<String,Object>) dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
//

        usersDatabaseReference.addValueEventListener(userListener);

//        if(firebaseAuth.getCurrentUser()!=null){
//            // profile activity
//
//            Intent i = new Intent(ActivityLogin.this, ActivityMain.class);
//            ActivityLogin.this.startActivity(i);
//        }

        progressDialog = new ProgressDialog(this);
//        firebaseAuth = FirebaseAuth.getInstance();


        etPassword = (EditText) findViewById(R.id.lgn_password);

        final Button bLogin = (Button) findViewById(R.id.Login);
        bLogin.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){


                emailstr = etEmail.getText().toString();
                final String passwordstr = etPassword.getText().toString();

                String password = helper.getPassword(emailstr);
                //System.out.print("Dpassword: "+ password + "entered password:"+passwordstr);
                //Log.v("Dpassword", password);
                //Log.v("Entered Password", passwordstr);

                if(password.equals(passwordstr) && password!=null && password!=""){
                    //Toast message = Toast.makeText(ActivityLogin.this, "Right Password! ", Toast.LENGTH_SHORT);
                    //CharSequence text1 = password;
                    // text2 = passwordstr;

                    userLogin();

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
        System.out.println("List of all emails123: " +emailList);
    }

//    private void collectEmails(Map<String, Object> users) {
//
//
//        emailList = new ArrayList<>();
//
//        //iterate through each user, ignoring their UID
//        for (Map.Entry<String, Object> entry : users.entrySet()){
//
//            //Get user map
//            Map singleUser = (Map) entry.getValue();
//            //Get phone field and append to list
//            emailList.add((String) singleUser.get("Email"));
//        }
//
//
//


//        System.out.println(emailList.toString());
//    }

    private void userLogin() {

        final String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();





        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
        }


        progressDialog.setMessage("Signing In");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {




                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            //logging in





//                            System.out.println("Email: " + email);
                            Toast.makeText(ActivityLogin.this, "Signed In" ,
                                    Toast.LENGTH_SHORT).show();

                            SaveSharedPreference.setEmail(ActivityLogin.this,emailstr);
//                    SaveSharedPreference.setEmail(ActivityLogin.this,helper.getEmail(usernamestr));
//


                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            Player player = new Player();

                            player.setName(helper.getName(email));
                            player.setGender(helper.getGender(email));
                            player.setEmail(email);
                            player.setWeight(helper.getWeight(email));
                            player.setLocation(helper.getLocation(email));
                            player.setDOB(helper.getDOB(email));
                            player.setBowlingArm(helper.getBowlingArm(email));
                            player.setBowlingStyle(helper.getBowlingStyle(email));
                            player.setCareerLevel(helper.getCareerLevel(email));
                            player.setPassword(helper.getPassword(email));
                            player.setHeight("6");


                            databaseReference.child("Users").child(user.getUid()).child("Name").setValue(player.getName());
                            databaseReference.child("Users").child(user.getUid()).child("Gender").setValue(player.getGender());
                            databaseReference.child("Users").child(user.getUid()).child("Email").setValue(player.getEmail());
                            databaseReference.child("Users").child(user.getUid()).child("Weight").setValue(player.getWeight());
                            databaseReference.child("Users").child(user.getUid()).child("Location").setValue(player.getLocation());
                            databaseReference.child("Users").child(user.getUid()).child("DOB").setValue(player.getDOB());
                            databaseReference.child("Users").child(user.getUid()).child("Bowling Arm").setValue(player.getBowlingArm());
                            databaseReference.child("Users").child(user.getUid()).child("Bowling Style").setValue(player.getBowlingStyle());
                            databaseReference.child("Users").child(user.getUid()).child("Career Level").setValue(player.getCareerLevel());
//                            databaseReference.child("Users").child(user.getUid()).child("Device Id").setValue(FirebaseInstanceID.getToken());
//                            databaseReference.child("Users").child(user.getUid()).child("name").setValue(player.getName());
//                            databaseReference.child("Users").child(user.getUid()).child("name").setValue(player.getName());

//                            Player player = new Player();
//
//                            player.setName(helper.getName(email));
//                            player.setGender(helper.getGender(email));
//                            player.setEmail(email);
//                            player.setWeight(helper.getWeight(email));
//                            player.setLocation(helper.getLocation(email));
//                            player.setDOB(helper.getDOB(email));
//                            player.setBowlingArm(helper.getBowlingArm(email));
//                            player.setBowlingStyle(helper.getBowlingStyle(email));
//                            player.setCareerLevel(helper.getCareerLevel(email));



                            System.out.println(
                                    "name: " + player.getName() +
                                    "gender: " + player.getGender() +
                                    "email: " + player.getEmail() +
                                    "weight: " + player.getWeight() +
                                    "location: " + player.getLocation() +
                                    "bowling Arm: " + player.getBowlingArm() +
                                    "bowling Style: " + player.getBowlingStyle() +
                                    "career level: " + player.getCareerLevel()
                            );






//
//                            databaseReference = FirebaseDatabase.getInstance().getReference();
//                            databaseReference.child("Users").child(user.getUid()).setValue(player);








                            Intent i = new Intent(ActivityLogin.this, ActivityMain.class);
                            ActivityLogin.this.startActivity(i);
                            finish();

                        }

                    }
                });

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
                    Toast.makeText(ActivityLogin.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
            if ( !(v instanceof EditText)) {
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if ( !(getCurrentFocus() instanceof EditText)) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return true;
    }
}
