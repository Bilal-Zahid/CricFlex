package com.example.cricflex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Asawal on 8/13/2016.
 */
public class ActivitySetupProfile extends Activity implements View.OnClickListener {

    RadioButton rdbMale, rdbFemale;
    RadioGroup rgGender;
    String gender ="Male";
    Context ctx;



    private EditText birthDate;
    private DatePickerDialog birthDatePickerDialog;
    private SimpleDateFormat dateFormatter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_profile_setup1);
        findViewsById();

        initializeCountrySpinner();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateTimeField();
    }

    private void initializeCountrySpinner(){

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        Spinner CountrySpinner = (Spinner)findViewById(R.id.country_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.country_spiner_item, countries);
        CountrySpinner.setAdapter(adapter);
    }

    private void findViewsById() {

        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rdbMale = (RadioButton) findViewById(R.id.rdbMale);
        rdbFemale = (RadioButton) findViewById(R.id.rdbMale);




        birthDate = (EditText) findViewById(R.id.stp_dob);
        birthDate.setInputType(InputType.TYPE_NULL);
        birthDate.requestFocus();

    }

    private void setDateTimeField() {
        
        birthDate.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        birthDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                birthDate.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void onCheckedChanged(RadioGroup group, int position) {
        // TODO Auto-generated method stub
        switch (position) {
            case R.id.rdbMale:
                gender = "Male";
                System.out.println("Male");
                break;
            case R.id.rdbFemale:
                gender = "Female";
                System.out.println("FeMale");
                break;

            default:
                break;
        }
    }


    @Override
    public void onClick(View view) {
        if(view == birthDate) {
            birthDatePickerDialog.show();
        }
    }
}
