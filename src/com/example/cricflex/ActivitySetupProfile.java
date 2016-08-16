package com.example.cricflex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Asawal on 8/13/2016.      Chah gayay ho aap
 */
public class ActivitySetupProfile extends Activity implements View.OnClickListener {

    DatabaseHelper helper = new DatabaseHelper(this);
    RadioButton rdbMale, rdbFemale;
    RadioGroup rgGender;

    RadioButton rdbLeft, rdbRight;
    RadioGroup rgBowlingArm;

    String selectedGender ="male";
    String selectedCountry;
    String selectedDOB;
    String selectedBowlingArm = "Left";
    String selectedBowlingStyle = "fast";
    String selectedCareerLevel = "international";
    Context ctx;

    String backCheck = "activity setup profile 1";

    //private TextView location;

    //private String location = "not set";

    private EditText birthDate;
    private DatePickerDialog birthDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private Spinner CountrySpinner;
    private Spinner bowlingStylesSpinner;
    private Spinner careerLevelSpinner;

    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    final Player p = new Player();


//    private ImageView profileImage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_profile_setup1);
        findViewsById();

        initializeCountrySpinner();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateTimeField();

        ImageView profileImage = (ImageView) findViewById(R.id.profilepicture);
        profileImage.setClickable(true);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.i(SystemSettings.APP_TAG + " : " + HomeActivity.class.getName(), "Entered onClick method");
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                //Image imageFromGallery = new;
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdbFemale:
                        // do operations specific to this selection
                        selectedGender = "female";
                        break;

                    case R.id.rdbMale:
                        // do operations specific to this selection
                        selectedGender = "male";
                        break;
                }
            }
        });


        final Button setupProfileNextButton = (Button) findViewById(R.id.stp_next);
        setupProfileNextButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                selectedDOB = birthDate.getText().toString();
                selectedCountry = CountrySpinner.getSelectedItem().toString();
                //String selectedGender = gender;

                if(selectedDOB.equals("")){
                    final Toast toast = Toast.makeText(ActivitySetupProfile.this, "Date Of Birth Not Selected" , Toast.LENGTH_SHORT);
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

                setContentView(R.layout.activity_profile_setup2);
                findViewsById2();

                backCheck = "activity setup profile 2";

                rgBowlingArm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId){
                            case R.id.rdbLeft:
                                // do operations specific to this selection
                                selectedBowlingArm = "Left";
                                break;

                            case R.id.rdbRight:
                                // do operations specific to this selection
                                selectedBowlingArm = "Right";
                                break;
                        }
                    }
                });
                initializeBowlingStylesSpinner();
                initializeCareerLevelSpinner();



                final Button setupProfileDoneButton = (Button) findViewById(R.id.stp_done);
                setupProfileDoneButton.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View view){
                        selectedBowlingStyle = bowlingStylesSpinner.getSelectedItem().toString();
                        selectedCareerLevel = careerLevelSpinner.getSelectedItem().toString();
                        //DataBase Work to do!!!
                        Intent intent = getIntent();
                        String username = intent.getStringExtra("username");
                        String email = intent.getStringExtra("email");
                        String password = intent.getStringExtra("password");
                        String security = intent.getStringExtra("security");
                        String gender = selectedGender;
                        String location = selectedCountry;
                        String DOB = selectedDOB;
                        String bowlingArm = selectedBowlingArm;
                        String bowlingStyle = selectedBowlingStyle;
                        String careerLevel = selectedCareerLevel;

                        System.out.println("username : "+ username);
                        System.out.println("email : "+ email);
                        System.out.println("password : "+ password);
                        System.out.println("security : "+ security);
                        System.out.println("gender : "+ gender);
                        System.out.println("location : "+ location);
                        System.out.println("DOB : "+ DOB);
                        System.out.println("bowlingArm : "+ bowlingArm);
                        System.out.println("bowlingStyle : "+ bowlingStyle);
                        System.out.println("careerLevel : "+ careerLevel);

                        p.setUsername(username);
                        p.setEmail(email);
                        p.setPassword(password);
                        p.setSecurity(security);
                        p.setGender(gender);
                        p.setLocation(location);
                        p.setDOB(DOB);
                        p.setBowlingArm(bowlingArm);
                        p.setBowlingStyle(bowlingStyle);
                        p.setCareerLevel(careerLevel);

                        helper.insertPlayer(p);
                        Toast toast = Toast.makeText(ActivitySetupProfile.this, "Registered Account!" , Toast.LENGTH_SHORT);

                        Intent i = new Intent(ActivitySetupProfile.this, ActivityMain.class);
                        ActivitySetupProfile.this.startActivity(i);


                    }
                });



            }
        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) findViewById(R.id.profilepicture);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

    }



    private void initializeBowlingStylesSpinner(){


        Context context=getApplicationContext();
        String[] bowlingStylesArray = context.getResources().getStringArray(R.array.bowlingstyle_array);
        bowlingStylesSpinner = (Spinner)findViewById(R.id.bowlingstyle_spinner);
        //ArrayList<String> bowlingStyles = new ArrayList( Arrays.asList( R.array.bowlingstyle_array ) );
        //System.out.println(R.array.bowlingstyle_array);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spiner_item, bowlingStylesArray);
        bowlingStylesSpinner.setAdapter(adapter);
    }

    private void initializeCareerLevelSpinner(){
        Context context=getApplicationContext();
        String[] careerLevels = context.getResources().getStringArray(R.array.careerlevel_array);
        careerLevelSpinner = (Spinner)findViewById(R.id.careerlevel_spinner);
        //ArrayList<String> careerLevels = new ArrayList( Arrays.asList( R.array.careerlevel_array ) );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spiner_item, careerLevels);
        careerLevelSpinner.setAdapter(adapter);
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

        CountrySpinner = (Spinner)findViewById(R.id.country_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spiner_item, countries);
        CountrySpinner.setAdapter(adapter);

        // = CountrySpinner.getSelectedItem().toString();
    }

    private void findViewsById() {

        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rdbMale = (RadioButton) findViewById(R.id.rdbMale);
        rdbFemale = (RadioButton) findViewById(R.id.rdbMale);





        birthDate = (EditText) findViewById(R.id.stp_dob);
        birthDate.setInputType(InputType.TYPE_NULL);
        birthDate.requestFocus();

    }

    private void findViewsById2() {

        rgBowlingArm = (RadioGroup) findViewById(R.id.rgBowlingArm);
        rdbLeft = (RadioButton) findViewById(R.id.rdbLeft);
        rdbRight = (RadioButton) findViewById(R.id.rdbRight);



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






    @Override
    public void onBackPressed() {
        if(backCheck.equals("activity setup profile 1")){
            Intent i = new Intent(ActivitySetupProfile.this, ActivityRegister.class);
            ActivitySetupProfile.this.startActivity(i);
            return;
        }

        if(backCheck.equals("activity setup profile 2")){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
            backCheck = "activity setup profile 1";
            return;
        }

    }


    @Override
    public void onClick(View view) {
        if(view == birthDate) {
            birthDatePickerDialog.show();
        }
        //if(view == )
    }
}
