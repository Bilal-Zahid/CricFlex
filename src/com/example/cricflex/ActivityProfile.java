package com.example.cricflex;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;

public class ActivityProfile extends AppCompatActivity {


    Button editProfileButton;

//    public ActivityProfile(){}

    FirebaseAuth firebaseAuth;
    DatabaseReference usersDatabaseReference;
    CircleImageView profilePicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("In Profile Activity!!!");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



//        DatabaseHelper helper = new DatabaseHelper(ActivityProfile.this);


        firebaseAuth = FirebaseAuth.getInstance();
        usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Players");;

        System.out.println("Activity mai ara hai : ");

        Log.v("hi","lolxz");

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Profile  object and use the values to update the UI
                // ...


                System.out.println("Datasnapshot mai ara hai : ");
                User playerProfile = new User();

                playerProfile = dataSnapshot.getValue(User.class);


                if(playerProfile==null){
                    System.out.println("Cant fetch data");
                    return;
                }
                TextView pv_name = (TextView)findViewById(R.id.pv_name);
                pv_name.setText(playerProfile.nameOfPerson);

                TextView pv_email = (TextView)findViewById(R.id.pv_username);
                pv_email.setText(playerProfile.emailId);

                TextView pv_bowling_style = (TextView)findViewById(R.id.pv_bowling_style);
                pv_bowling_style.setText(playerProfile.bowlingStyle);

                TextView pv_career_level = (TextView)findViewById(R.id.pv_career_level);
                pv_career_level.setText(playerProfile.careerLevel);


                TextView pv_gender = (TextView)findViewById(R.id.pv_gender);
                pv_gender.setText(playerProfile.gender);

                TextView pv_location = (TextView)findViewById(R.id.pv_location);
                pv_location.setText(playerProfile.location);

                TextView pv_DOB = (TextView)findViewById(R.id.pv_dateofbirth);
                String DOB = playerProfile.DOB;
                pv_DOB.setText(DOB);

                TextView pv_weight = (TextView)findViewById(R.id.pv_weight);
                String weight = playerProfile.weight + " kg";
                pv_weight.setText(weight);

                String str[] = DOB.split("-");
                int day = Integer.parseInt(str[0]);
                int month = Integer.parseInt(str[1]);
                int year = Integer.parseInt(str[2]);

                System.out.println("In Profile Activity 2!!!");


                TextView pv_age = (TextView)findViewById(R.id.pv_age);
                pv_age.setText(getAge(year,month,day));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };





        System.out.println("Commented ");


        DatabaseReference userRef = usersDatabaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());



        userRef.addValueEventListener(userListener);



        setContentView(R.layout.activity_profile);





        String email = SaveSharedPreference.getEmail(ActivityProfile.this);;

        ///// CHECK THIS

//        Bundle bundle = ActivityProfile.this.getArguments();
//        if (bundle != null) {
//            email = bundle.getString("email", "not sent");
//        }


        ///// CHECK THIS




//        TextView pv_name = (TextView)findViewById(R.id.pv_name);
//        pv_name.setText(helper.getName(email));
//
//        TextView pv_username = (TextView)findViewById(R.id.pv_username);
//        pv_username.setText(email);
//
//        TextView pv_bowling_style = (TextView)findViewById(R.id.pv_bowling_style);
//        pv_bowling_style.setText(helper.getBowlingStyle(email));
//
//        TextView pv_career_level = (TextView)findViewById(R.id.pv_career_level);
//        pv_career_level.setText(helper.getCareerLevel(email));
//
//
//        TextView pv_gender = (TextView)findViewById(R.id.pv_gender);
//        pv_gender.setText(helper.getGender(email));
//
//        TextView pv_location = (TextView)findViewById(R.id.pv_location);
//        pv_location.setText(helper.getLocation(email));
//
//        TextView pv_DOB = (TextView)findViewById(R.id.pv_dateofbirth);
//        String DOB = helper.getDOB(email);
//        pv_DOB.setText(DOB);
//
//        TextView pv_weight = (TextView)findViewById(R.id.pv_weight);
//        String weight = helper.getWeight(email) + " kg";
//        pv_weight.setText(weight);






//        String str[] = DOB.split("-");
//        int day = Integer.parseInt(str[0]);
//        int month = Integer.parseInt(str[1]);
//        int year = Integer.parseInt(str[2]);
//
//        System.out.println("In Profile Activity 2!!!");
//
//
//        TextView pv_age = (TextView)findViewById(R.id.pv_age);
//        pv_age.setText(getAge(year,month,day));

        //CircleImageView circleImageView = new CircleImageView(getActivity());
        //circleImageView =




        profilePicture = (CircleImageView) findViewById(R.id.profilepicture);
        //Bitmap bitmapImage1 = helper.getImage(email);

        //circleImageView.setImageBitmap(bitmapImage1);

        Bitmap b = getImageBitmap(ActivityProfile.this,email,"jpeg");
        profilePicture.setImageBitmap(b);



        editProfileButton = (Button) findViewById(R.id.button_edit_profile);

//        editProfileButton.setOnClickListener(new handleEditProfileButton());
        editProfileButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {

                Intent intent = new Intent(ActivityProfile.this, ActivityProfileEdit.class);
                startActivity(intent);
            }
        });
    }


    public Bitmap getImageBitmap(Context context, String name, String extension){
            name=name+"."+extension;
        try{
            FileInputStream fis = context.openFileInput(name);
            Bitmap b = BitmapFactory.decodeStream(fis);
            fis.close();
            return b;
        }
        catch(Exception e){
        }
        return null;
    }


    private String getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        System.out.println("Day today ,"+today.get(Calendar.DAY_OF_YEAR)  +"DOB DAY : " +dob.get(Calendar.DAY_OF_YEAR));
        if (today.get(Calendar.DAY_OF_YEAR) < (dob.get(Calendar.DAY_OF_YEAR)-31)){
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }
}
