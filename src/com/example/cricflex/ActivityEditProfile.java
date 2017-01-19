package com.example.cricflex;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;

import java.util.Calendar;

public class ActivityEditProfile extends AppCompatActivity {

    RadioButton rdbMale, rdbFemale;
    RadioGroup rgGender;

    String selectedGender ="male";
    String selectedCountry;

    String selectedBowlingStyle = "fast";
    String selectedCareerLevel = "international";
    String weight = "80";

    TextView location_text;
    CountryPicker picker;
    Country country;


    Drawable drawable;
    ImageView country_flag;

    private Spinner bowlingStylesSpinner;
    private Spinner careerLevelSpinner;


    EditText weightOfPerson;

    ImageView profileImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        initializeAllViews();


        location_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountry();
            }
        });

        country_flag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showCountry();
            }
        });

        profileImage.setClickable(true);

    }


    public void initializeAllViews(){

        location_text = (TextView) findViewById(R.id.location);
        country_flag = (ImageView) findViewById(R.id.country_flag);

        weightOfPerson = (EditText) findViewById(R.id.pv_weight);

//        location_text.setText(country.getName());
//        country_flag.setImageResource(country.getFlag());



        profileImage = (ImageView) findViewById(R.id.profilepicture);


    }

    public void showCountry(){

        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here
                location_text.setText(name);
                country_flag.setImageResource(flagDrawableResID);
                picker.dismiss();
            }
        });
    }


}
