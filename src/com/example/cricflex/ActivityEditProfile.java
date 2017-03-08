package com.example.cricflex;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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

import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActivityEditProfile extends FragmentActivity implements View.OnClickListener {


    String email;
    DatabaseHelper helper = new DatabaseHelper(this);

    RadioButton rdbMale, rdbFemale;
    RadioGroup rgGender;

    RadioButton rdbLeft, rdbRight;
    RadioGroup rgBowlingArm;

    TextView location_text;
    TextView name;
    CountryPicker picker;
    Country country;
    ImageView country_flag;

    private EditText birthDate;
    private DatePickerDialog birthDatePickerDialog;
    private SimpleDateFormat dateFormatter;

    private Spinner bowlingStylesSpinner;
    private Spinner careerLevelSpinner;

    EditText weightOfPerson;

    String selectedGender;
    String selectedWeight;
    String selectedLocation;
    String selectedDOB;
    String selectedBowlingArm;
    String selectedBowlingStyle;
    String selectedCareerLevel;


    Button saveEditProfile, cancelEditProfile;


    ImageView profilePicture;
    Bitmap bitmapImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().hide();
        findViewsById();

        email = SaveSharedPreference.getEmail(ActivityEditProfile.this);

        name.setText(helper.getName(email));

        selectedGender = helper.getGender(email);


        System.out.println("Gender: " + selectedGender);

        if(selectedGender.equals("male"))
            rgGender.check(R.id.rdbMale);
        else if(selectedGender.equals("female"))
            rgGender.check(R.id.rdbFemale);


        birthDate.setText(helper.getDOB(email).toString());


        selectedBowlingArm =helper.getBowlingArm(email);
        System.out.println("Arm: " + selectedBowlingArm);


        initializeBowlingStylesSpinner(selectedBowlingArm);
        if(selectedBowlingArm.equals("Right")){
            rgBowlingArm.check(R.id.rdbRight);
        }
        else if (selectedBowlingArm.equals("Left")){
            rgBowlingArm.check(R.id.rdbLeft);
        }


        initializeBowlingStylesSpinner(selectedBowlingArm);



        rgBowlingArm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdbLeft:
                        // do operations specific to this selection
                        selectedBowlingArm = "Left";
                        initializeBowlingStylesSpinner(selectedBowlingArm);
                        break;

                    case R.id.rdbRight:
                        // do operations specific to this selection
                        selectedBowlingArm = "Right";
                        initializeBowlingStylesSpinner(selectedBowlingArm);
                        break;
                }
            }
        });
        initializeCareerLevelSpinner();




        bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon_large);

        profilePicture = (ImageView) findViewById(R.id.profilepicture);
        profilePicture.setClickable(true);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 1);
            }

        });

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        setDateTimeField();


        location_text = (TextView) findViewById(R.id.location);
        country_flag = (ImageView) findViewById(R.id.country_flag);


        selectedLocation = helper.getLocation(email);

        picker = CountryPicker.newInstance("Select Country");
        country = picker.getCountryByName(this,selectedLocation);

//        Country necountry;
//        necountry.set
        System.out.println("Code: "+country.getCode());


        weightOfPerson = (EditText) findViewById(R.id.pv_weight);
        weightOfPerson.setText(helper.getWeight(email).toString());


        location_text.setText(country.getName());
//        location_text.setText(se);
        country_flag.setImageResource(country.getFlag());

        location_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });

        country_flag.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                show();
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



        selectedDOB = birthDate.getText().toString();
//        selectedCountry = country.getName();
        //String selectedGender = gender;

        if(selectedDOB.equals("")){
            final Toast toast = Toast.makeText(ActivityEditProfile.this, "Date Of Birth Not Selected" , Toast.LENGTH_SHORT);
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



        cancelEditProfile.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {

                Intent intent = new Intent(ActivityEditProfile.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }
        });

        saveEditProfile.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {


                if(weightOfPerson.getText().toString().equals("")){
                    Toast.makeText(ActivityEditProfile.this, "Please Enter Weight." , Toast.LENGTH_SHORT).show();
                    return;
                }

                selectedGender = selectedGender;
                selectedWeight = weightOfPerson.getText().toString();
//                selectedLocation = country.getName();
                selectedDOB = birthDate.getText().toString();
                selectedBowlingArm = selectedBowlingArm  ;
                selectedBowlingStyle = bowlingStylesSpinner.getSelectedItem().toString();
                selectedCareerLevel = careerLevelSpinner.getSelectedItem().toString();

                helper.changeGender(email,selectedGender);
                helper.changeWeight(email,selectedWeight);
                helper.changeLocation(email,selectedLocation);
                helper.changeDOB(email,selectedDOB);
                helper.changeBowlingArm(email,selectedBowlingArm);
                helper.changeBowlingStyle(email,selectedBowlingStyle);
                helper.changeCareerLevel(email,selectedCareerLevel);



                Toast.makeText(ActivityEditProfile.this, "Profile Updated" , Toast.LENGTH_SHORT).show();

                saveImage(getApplicationContext(),bitmapImage, email,"jpeg");

                Intent intent = new Intent(ActivityEditProfile.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }
        });




    }

    //end onCreate

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            ImageView profileImage = (ImageView) findViewById(R.id.profilepicture);
            bitmapImage = BitmapFactory.decodeFile(picturePath);
            bitmapImage = Bitmap.createScaledBitmap(bitmapImage, 200, 200, true);

            profileImage.setImageBitmap(bitmapImage);
        }

    }






    private void initializeBowlingStylesSpinner(String arm){

        Context context=getApplicationContext();

        String bowlingStyle = helper.getBowlingStyle(email);
        if(arm.equals("Left"))
        {
            String[] bowlingStylesArray = context.getResources().getStringArray(R.array.bowlingstyle_left_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spiner_item, bowlingStylesArray);
            bowlingStylesSpinner.setAdapter(adapter);


            if(bowlingStyle.equals("Left Arm Fast")){
                bowlingStylesSpinner.setSelection(0);
            }
            else if(bowlingStyle.equals("Left Arm Medium Fast")){
                bowlingStylesSpinner.setSelection(1);
            }
            else if(bowlingStyle.equals("Left Arm Off break")){
                bowlingStylesSpinner.setSelection(2);
            }
            else if(bowlingStyle.equals("Left Arm Leg break")){
                bowlingStylesSpinner.setSelection(3);
            }
            else if(bowlingStyle.equals("Slow Left Arm Orthodox")){
                bowlingStylesSpinner.setSelection(4);
            }
            else if(bowlingStyle.equals("Slow Left Arm Chinaman")){
                bowlingStylesSpinner.setSelection(5);
            }
        }
        else if(arm.equals("Right"))
        {
            String[] bowlingStylesArray = context.getResources().getStringArray(R.array.bowlingstyle_right_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spiner_item, bowlingStylesArray);
            bowlingStylesSpinner.setAdapter(adapter);


            if (bowlingStyle.equals("Right Arm Fast")){
                bowlingStylesSpinner.setSelection(0);
            }
            else if (bowlingStyle.equals("Right Arm Medium Fast")){
                bowlingStylesSpinner.setSelection(1);
            }
            else if (bowlingStyle.equals("Right Arm Off break")){
                bowlingStylesSpinner.setSelection(2);
            }
            else if (bowlingStyle.equals("Right Arm Leg break")){
                bowlingStylesSpinner.setSelection(3);
            }
        }

    }

    private void initializeCareerLevelSpinner(){
        Context context=getApplicationContext();
        String[] careerLevels = context.getResources().getStringArray(R.array.careerlevel_array);
        careerLevelSpinner = (Spinner)findViewById(R.id.careerlevel_spinner);
        //ArrayList<String> careerLevels = new ArrayList( Arrays.asList( R.array.careerlevel_array ) );
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spiner_item, careerLevels);





        careerLevelSpinner.setAdapter(adapter);

        String careerLevel =  helper.getCareerLevel(email);
        if(careerLevel.equals("Club")){
            careerLevelSpinner.setSelection(0);
        }
        else if(careerLevel.equals("National")){
            careerLevelSpinner.setSelection(1);
        }
        else if(careerLevel.equals("International")){
            careerLevelSpinner.setSelection(2);
        }
    }

    private void findViewsById() {

        name = (TextView) findViewById(R.id.stp_name);
        bowlingStylesSpinner = (Spinner)findViewById(R.id.bowlingstyle_spinner);
        birthDate = (EditText) findViewById(R.id.stp_dob);
        birthDate.setInputType(InputType.TYPE_NULL);
        birthDate.requestFocus();

        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rdbMale = (RadioButton) findViewById(R.id.rdbMale);
        rdbFemale = (RadioButton) findViewById(R.id.rdbMale);

        rgBowlingArm = (RadioGroup) findViewById(R.id.rgBowlingArm);
        rdbLeft = (RadioButton) findViewById(R.id.rdbLeft);
        rdbRight = (RadioButton) findViewById(R.id.rdbRight);

        saveEditProfile = (Button) findViewById(R.id.button_edit_profile_save);
        cancelEditProfile = (Button) findViewById(R.id.button_edit_profile_cancel);
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
    public void onClick(View view) {
        if(view == birthDate) {
            birthDatePickerDialog.show();
        }
    }


    public void show(){

        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                // Implement your code here

                location_text.setText(name);
                selectedLocation = name;
                country_flag.setImageResource(flagDrawableResID);
                //location_text.setCompoundDrawablesRelativeWithIntrinsicBounds(flagDrawableResID, 0, 0, 0);
                //location_text.setCompoundDrawablesWithIntrinsicBounds(flagDrawableResID, 0, 0, 0);
                picker.dismiss();
            }
        });
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

    public void saveImage(Context context, Bitmap b,String name,String extension){
        name=name+"."+extension;

//        Bitmap resized = Bitmap.createScaledBitmap(b, 200, 200, true);
//        b = resized;
        FileOutputStream out;
        try {
            out = context.openFileOutput(name, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
