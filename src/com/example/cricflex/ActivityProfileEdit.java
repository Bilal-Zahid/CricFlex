package com.example.cricflex;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class ActivityProfileEdit extends FragmentActivity {


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


    String bowlingStyleFromFirebase;

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


    //firebase things
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog ;
    FirebaseAuth firebaseAuth;

    User playerProfile = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setDateTimeField();

        findViewsById();

        email = SaveSharedPreference.getEmail(ActivityProfileEdit.this);


        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);


        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Profile  object and use the values to update the UI
                // ...





                playerProfile = dataSnapshot.getValue(User.class);



                System.out.println("Datasnapshot mai ara hai : " + playerProfile.bowlingArm );
                if(playerProfile==null){
                    System.out.println("Cant fetch data");
                    return;
                }

                selectedGender = playerProfile.gender;

                name.setText(playerProfile.nameOfPerson);
                initializeBowlingStylesSpinner(playerProfile.bowlingArm,playerProfile.bowlingStyle);

                if(playerProfile.gender.equals("male"))
                    rgGender.check(R.id.rdbMale);
                else if(playerProfile.gender.equals("female"))
                    rgGender.check(R.id.rdbFemale);

                if(playerProfile.bowlingArm.equals("Right")){
                    rgBowlingArm.check(R.id.rdbRight);
                }
                else if (playerProfile.bowlingArm.equals("Left")){
                    rgBowlingArm.check(R.id.rdbLeft);
                }

                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

                birthDate.setText(playerProfile.DOB);
                birthDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        birthDatePickerDialog.show();
                    }
                });





                initializeCareerLevelSpinner(playerProfile.careerLevel);




                selectedBowlingArm = playerProfile.bowlingArm;
                rgBowlingArm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
                {
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch(checkedId){
                            case R.id.rdbLeft:
                                // do operations specific to this selection
                                selectedBowlingArm = "Left";
                                initializeBowlingStylesSpinner(selectedBowlingArm,playerProfile.bowlingStyle);
                                break;

                            case R.id.rdbRight:
                                // do operations specific to this selection
                                selectedBowlingArm = "Right";
                                initializeBowlingStylesSpinner(selectedBowlingArm,playerProfile.bowlingStyle);
                                break;
                        }
                    }
                });


                weightOfPerson.setText(playerProfile.weight);



                location_text = (TextView) findViewById(R.id.location);
                country_flag = (ImageView) findViewById(R.id.country_flag);



                picker = CountryPicker.newInstance("Select Country");
                country = picker.getCountryByName(ActivityProfileEdit.this,playerProfile.location);


                location_text.setText(country.getName());
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

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        DatabaseReference usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Players").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        usersDatabaseReference.addValueEventListener(userListener);


//        email = SaveSharedPreference.getEmail(ActivityProfileEdit.this);

//        name.setText(helper.getName(email));

//        selectedGender = helper.getGender(email);


//        System.out.println("Gender: " + selectedGender);

//        if(selectedGender.equals("male"))
//            rgGender.check(R.id.rdbMale);
//        else if(selectedGender.equals("female"))
//            rgGender.check(R.id.rdbFemale);




//        selectedBowlingArm =helper.getBowlingArm(email);
//        System.out.println("Arm: " + selectedBowlingArm);


//        initializeBowlingStylesSpinner(selectedBowlingArm);
//        if(selectedBowlingArm.equals("Right")){
//            rgBowlingArm.check(R.id.rdbRight);
//        }
//        else if (selectedBowlingArm.equals("Left")){
//            rgBowlingArm.check(R.id.rdbLeft);
//        }


//        initializeBowlingStylesSpinner(selectedBowlingArm);




//        initializeCareerLevelSpinner(helper.getCareerLevel(email));




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






//        Country necountry;
//        necountry.set
//        System.out.println("Code: "+country.getCode());


//        weightOfPerson = (EditText) findViewById(R.id.pv_weight);
//        weightOfPerson.setText(helper.getWeight(email).toString());




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



//        selectedDOB = birthDate.getText().toString();
////        selectedCountry = country.getName();
//        //String selectedGender = gender;
//
//        if(selectedDOB.equals("")){
//            final Toast toast = Toast.makeText(ActivityProfileEdit.this, "Date Of Birth Not Selected" , Toast.LENGTH_SHORT);
//            toast.show();
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    toast.cancel();
//                }
//            }, 500);
//            return;
//        }



        cancelEditProfile.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {

                Intent intent = new Intent(ActivityProfileEdit.this, ActivityMain.class);
                startActivity(intent);
                finish();
            }
        });

        saveEditProfile.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {


                if(weightOfPerson.getText().toString().equals("")){
                    Toast.makeText(ActivityProfileEdit.this, "Please Enter Weight." , Toast.LENGTH_SHORT).show();
                    return;
                }



                selectedGender = selectedGender;
                selectedWeight = weightOfPerson.getText().toString();
                selectedLocation = country.getName();
                selectedDOB = birthDate.getText().toString();
                selectedBowlingArm = selectedBowlingArm  ;
                selectedBowlingStyle = bowlingStylesSpinner.getSelectedItem().toString();
                selectedCareerLevel = careerLevelSpinner.getSelectedItem().toString();
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();



                System.out.println("Email: " + email + " Bowling arm: " + selectedBowlingArm) ;


                User playerEditedProfile = new User (selectedBowlingArm,selectedBowlingStyle,selectedCareerLevel,
                        selectedDOB,SaveSharedPreference.getEmail(ActivityProfileEdit.this),selectedGender,selectedLocation,name.getText().toString(),selectedWeight,
                        FirebaseAuth.getInstance().getCurrentUser().getUid());



                String str[] = selectedDOB.split("-");
                int day = Integer.parseInt(str[0]);
                int month = Integer.parseInt(str[1]);
                int year = Integer.parseInt(str[2]);


                if(getAge(year,month,day)<10){
                    Toast.makeText(ActivityProfileEdit.this, "Player's age should be atleast 10" +selectedDOB , Toast.LENGTH_SHORT).show();
                    return;
                }



                helper.changeGender(email,selectedGender);
                helper.changeWeight(email,selectedWeight);
                helper.changeLocation(email,selectedLocation);
                helper.changeDOB(email,selectedDOB);
                helper.changeBowlingArm(email,selectedBowlingArm);
                helper.changeBowlingStyle(email,selectedBowlingStyle);
                helper.changeCareerLevel(email,selectedCareerLevel);

                FirebaseUser user = firebaseAuth.getCurrentUser();



                databaseReference.child("Players").child(user.getUid()).setValue(playerEditedProfile, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                        if (databaseError != null) {
//                            Log.e("Tag", "Failed to write message", databaseError.toException());
                            System.out.println("Error in writing to database: " + databaseError.toException() );
                        }
                    }
                });


//                databaseReference.child("Users").child(user.getUid()).child("Gender").setValue(selectedGender);
//                databaseReference.child("Users").child(user.getUid()).child("Weight").setValue(selectedWeight);
//                databaseReference.child("Users").child(user.getUid()).child("Location").setValue(selectedLocation);
//                databaseReference.child("Users").child(user.getUid()).child("DOB").setValue(selectedDOB);
//                databaseReference.child("Users").child(user.getUid()).child("Bowling Arm").setValue(selectedBowlingArm);
//                databaseReference.child("Users").child(user.getUid()).child("Bowling Style").setValue(selectedBowlingStyle);
//                databaseReference.child("Users").child(user.getUid()).child("Career Level").setValue(selectedCareerLevel);


                Toast.makeText(ActivityProfileEdit.this, "Profile Updated" , Toast.LENGTH_SHORT).show();

                saveImage(getApplicationContext(),bitmapImage, email,"jpeg");

                Intent intent = new Intent(ActivityProfileEdit.this, ActivityMain.class);
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






    private void initializeBowlingStylesSpinner(String arm,String bowlingStyle){

        Context context=getApplicationContext();

//        String bowlingStyle = helper.getBowlingStyle(email);
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

    private void initializeCareerLevelSpinner(String careerLevel){
        Context context=getApplicationContext();
        String[] careerLevels = context.getResources().getStringArray(R.array.careerlevel_array);
        careerLevelSpinner = (Spinner)findViewById(R.id.careerlevel_spinner);
        //ArrayList<String> careerLevels = new ArrayList( Arrays.asList( R.array.careerlevel_array ) );
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spiner_item, careerLevels);





        careerLevelSpinner.setAdapter(adapter);


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

        name = (TextView) findViewById(R.id.edp_name);
        bowlingStylesSpinner = (Spinner)findViewById(R.id.bowlingstyle_spinner);
        birthDate = (EditText) findViewById(R.id.edp_dob);
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

        weightOfPerson = (EditText)findViewById(R.id.pv_weight);
    }

    private void setDateTimeField() {

//        birthDate.setOnClickListener(this);

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


//    @Override
//    public void onClick(View view) {
//        if(view == birthDate) {
//            birthDatePickerDialog.show();
//        }
//    }


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

    private int getAge(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        System.out.println("Day today ,"+today.get(Calendar.DAY_OF_YEAR)  +"DOB DAY : " +dob.get(Calendar.DAY_OF_YEAR));
        if (today.get(Calendar.DAY_OF_YEAR) < (dob.get(Calendar.DAY_OF_YEAR)-31)){
            age--;
        }

        Integer ageInt = new Integer(age);
        return  ageInt;
    }

}
