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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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


    
    // database items
//    DatabaseHelper helper = new DatabaseHelper(this);
    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference usersDatabaseReference;
    List<String> emailList = new ArrayList<>();
    String emailString;
    
    
    // layout items
    AutoCompleteTextView emailEditText;
    EditText passwordEditText;
    Button loginButton;
    private ProgressDialog progressDialog ;

    private static Boolean exit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        super.onCreate(savedInstanceState);

        // full screen view
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Hide the status bar.
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        // setting the layout
        setContentView(R.layout.activity_login);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        usersDatabaseReference = databaseReference.child("Users");

        
//        initialing layout items
        emailEditText = (AutoCompleteTextView) findViewById(R.id.lgn_email);
        passwordEditText = (EditText) findViewById(R.id.lgn_password);
        loginButton = (Button) findViewById(R.id.Login);

//        setting layout items
        emailEditText.setThreshold(1);

        System.out.println("Aisay hi print" + SaveSharedPreference.getEmail(ActivityLogin.this) );
        emailList = SaveSharedPreference.getEmailList(ActivityLogin.this);

        System.out.println("Email List in login: " + SaveSharedPreference.getEmailList(ActivityLogin.this));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.dropdown_item, emailList);


        emailEditText.setAdapter(adapter);

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                // ...
//                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator ?<List<String>>() {};
//                List messages = dataSnapshot.getValue(t);
//                if( messages == null ) {
//                    System.out.println("No email" + dataSnapshot.getValue());
//             }
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



        passwordEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    hideSoftKeyboard(ActivityLogin.this);
                    loginButton.performClick();

                    return true;
                }
                return false;
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){


                emailString = emailEditText.getText().toString();
//                final String passwordstr = passwordEditText.getText().toString();

//                String password = helper.getPassword(emailString);
                //System.out.print("Dpassword: "+ password + "entered password:"+passwordstr);
                //Log.v("Dpassword", password);
                //Log.v("Entered Password", passwordstr);

                userLogin();
//                if(password.equals(passwordstr) && password!=null && password!=""){
//                    //Toast message = Toast.makeText(ActivityLogin.this, "Right Password! ", Toast.LENGTH_SHORT);
//                    //CharSequence text1 = password;
//                    // text2 = passwordstr;
//
//
//
//                }
//                else{
//                    //Toast message = Toast.makeText(ActivityLogin.this, "Wrong Password! ", Toast.LENGTH_SHORT);
//                    Toast.makeText(ActivityLogin.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
//                }
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

        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();





        if(TextUtils.isEmpty(email) || email.equals("")){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password ) || password.equals("")){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
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


                            SaveSharedPreference.setEmail(ActivityLogin.this,emailString);
//                    SaveSharedPreference.semailEditText(ActivityLogin.this,helper.gemailEditText(usernamestr));
//


                            FirebaseUser user = firebaseAuth.getCurrentUser();

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
//                            player.setPassword(helper.getPassword(email));
//                            player.setHeight("6");


                            databaseReference.child("Players").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.exists()){
                                        System.out.println("Data doesn't exists but it is here");
                                        Intent i = new Intent(ActivityLogin.this, ActivityMain.class);
                                        ActivityLogin.this.startActivity(i);
                                        finish();

                                    }
                                    else {
                                        Intent i = new Intent(ActivityLogin.this, ActivityProfileSetup.class);

                                        i.putExtra("email", email);
                                        i.putExtra("password", password);
                                        ActivityLogin.this.startActivity(i);
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });




//                            usersDatabaseReference.child("Users").child(user.getUid()).child("Name").setValue(player.getName());
//                            usersDatabaseReference.child("Users").child(user.getUid()).child("Gender").setValue(player.getGender());
//                            usersDatabaseReference.child("Users").child(user.getUid()).child("Email").setValue(player.gemailEditText());
//                            usersDatabaseReference.child("Users").child(user.getUid()).child("Weight").setValue(player.getWeight());
//                            usersDatabaseReference.child("Users").child(user.getUid()).child("Location").setValue(player.getLocation());
//                            usersDatabaseReference.child("Users").child(user.getUid()).child("DOB").setValue(player.getDOB());
//                            usersDatabaseReference.child("Users").child(user.getUid()).child("Bowling Arm").setValue(player.getBowlingArm());
//                            usersDatabaseReference.child("Users").child(user.getUid()).child("Bowling Style").setValue(player.getBowlingStyle());
//                            usersDatabaseReference.child("Users").child(user.getUid()).child("Career Level").setValue(player.getCareerLevel());
//                            usersDatabaseReference.child("Users").child(user.getUid()).child("Device Id").setValue(FirebaseInstanceID.getToken());
//                            usersDatabaseReference.child("Users").child(user.getUid()).child("name").setValue(player.getName());
//                            usersDatabaseReference.child("Users").child(user.getUid()).child("name").setValue(player.getName());

//                            Player player = new Player();
//
//                            player.setName(helper.getName(email));
//                            player.setGender(helper.getGender(email));
//                            player.semailEditText(email);
//                            player.setWeight(helper.getWeight(email));
//                            player.setLocation(helper.getLocation(email));
//                            player.setDOB(helper.getDOB(email));
//                            player.setBowlingArm(helper.getBowlingArm(email));
//                            player.setBowlingStyle(helper.getBowlingStyle(email));
//                            player.setCareerLevel(helper.getCareerLevel(email));



//                            System.out.println(
//                                    "name: " + player.getName() +
//                                    "gender: " + player.getGender() +
//                                    "email: " + player.getEmail() +
//                                    "weight: " + player.getWeight() +
//                                    "location: " + player.getLocation() +
//                                    "bowling Arm: " + player.getBowlingArm() +
//                                    "bowling Style: " + player.getBowlingStyle() +
//                                    "career level: " + player.getCareerLevel()
//                            );






//
//                            usersDatabaseReference = FirebaseDatabase.getInstance().getReference();
//                            usersDatabaseReference.child("Users").child(user.getUid()).setValue(player);








//                            Intent i = new Intent(ActivityLogin.this, ActivityMain.class);
//                            ActivityLogin.this.startActivity(i);
                            Toast.makeText(ActivityLogin.this, "Signed In" ,
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressDialog.dismiss();

                            Toast.makeText(ActivityLogin.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });

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

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
