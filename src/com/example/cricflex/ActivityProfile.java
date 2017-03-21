package com.example.cricflex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileInputStream;
import java.util.Calendar;

public class ActivityProfile extends Activity {


    Button editProfileButton;

    public ActivityProfile(){}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DatabaseHelper helper = new DatabaseHelper(ActivityProfile.this);

        setContentView(R.layout.activity_profile);

        String email = "";

        ///// CHECK THIS

//        Bundle bundle = ActivityProfile.this.getArguments();
//        if (bundle != null) {
//            email = bundle.getString("email", "not sent");
//        }


        ///// CHECK THIS


        TextView pv_name = (TextView)findViewById(R.id.pv_name);
        pv_name.setText(helper.getName(email));

        TextView pv_username = (TextView)findViewById(R.id.pv_username);
        pv_username.setText(email);

        TextView pv_bowling_style = (TextView)findViewById(R.id.pv_bowling_style);
        pv_bowling_style.setText(helper.getBowlingStyle(email));

        TextView pv_career_level = (TextView)findViewById(R.id.pv_career_level);
        pv_career_level.setText(helper.getCareerLevel(email));


        TextView pv_gender = (TextView)findViewById(R.id.pv_gender);
        pv_gender.setText(helper.getGender(email));

        TextView pv_location = (TextView)findViewById(R.id.pv_location);
        pv_location.setText(helper.getLocation(email));

        TextView pv_DOB = (TextView)findViewById(R.id.pv_dateofbirth);
        String DOB = helper.getDOB(email);
        pv_DOB.setText(DOB);

        TextView pv_weight = (TextView)findViewById(R.id.pv_weight);
        String weight = helper.getWeight(email) + " kg";
        pv_weight.setText(weight);



        String str[] = DOB.split("-");
        int day = Integer.parseInt(str[0]);
        int month = Integer.parseInt(str[1]);
        int year = Integer.parseInt(str[2]);

        TextView pv_age = (TextView)findViewById(R.id.pv_age);
        pv_age.setText(getAge(year,month,day));

        //CircleImageView circleImageView = new CircleImageView(getActivity());
        //circleImageView =



        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.profilepicture);
        //Bitmap bitmapImage1 = helper.getImage(email);

        //circleImageView.setImageBitmap(bitmapImage1);

        Bitmap b = getImageBitmap(ActivityProfile.this,email,"jpeg");
        circleImageView.setImageBitmap(b);



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
