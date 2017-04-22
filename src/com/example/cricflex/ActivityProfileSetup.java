package com.example.cricflex;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
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


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Asawal on 8/13/2016.
 */
public class ActivityProfileSetup extends FragmentActivity {

    DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
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
    String weight = "80";

    TextView location_text;
    CountryPicker picker;
    Country country;
    Drawable drawable;
    ImageView country_flag;
    int scale_height, scale_width;

    Context ctx;

    Bitmap bitmapImage;
    de.hdodenhof.circleimageview.CircleImageView profileImage;

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

    final Player player = new Player();

    private EditText nameOfPerson;
    EditText weightOfPerson;

    User playerProfile = new User();


//    private ImageView profileImage;


    //Firebase Things
    private DatabaseReference rootDatabaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = rootDatabaseReference.child("Users");
    DatabaseReference userNameReference = userRef.child("name");
    DatabaseReference userValuesReference = userNameReference.child("values");


    private static final int SELECT_PICTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile_setup);
        findViewsById();

        bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon_large);

        initializeCountrySpinner();

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
//        setDateTimeField();
        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTimeField();
                birthDatePickerDialog.show();
            }
        });



        location_text = (TextView) findViewById(R.id.location);
        country_flag = (ImageView) findViewById(R.id.country_flag);



        picker = CountryPicker.newInstance("Select Country");
        country = picker.getUserCountryInfo(this);


        weightOfPerson = (EditText) findViewById(R.id.pv_weight);
        nameOfPerson = (EditText) findViewById(R.id.stp_name);


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


        profileImage = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profilepicture);
        profileImage.setClickable(true);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);


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

        initializeBowlingStylesSpinner("Left");
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


        final Button setupProfileDoneButton = (Button) findViewById(R.id.stp_done);
        setupProfileDoneButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){

                int ijp = 0;
                Log.d("MyApp","I am here " + ijp++);
                selectedBowlingStyle = bowlingStylesSpinner.getSelectedItem().toString();
                selectedCareerLevel = careerLevelSpinner.getSelectedItem().toString();
                //DataBase Work to do!!!

                selectedDOB = birthDate.getText().toString();
                Intent intent = getIntent();
                String name;
//                String name = intent.getStringExtra("name");
//                        String username = intent.getStringExtra("email");
                String email = intent.getStringExtra("email");
                String password = intent.getStringExtra("password");
//                        String security = intent.getStringExtra("security");
                String gender = selectedGender;
                String location = selectedCountry;
                String DOB = selectedDOB;
                String bowlingArm = selectedBowlingArm;
                String bowlingStyle = selectedBowlingStyle;
                String careerLevel = selectedCareerLevel;
                String weightOfPlayer;



                selectedDOB = birthDate.getText().toString();

                name = nameOfPerson.getText().toString();
//                country = picker.getUserCountryInfo(ActivityProfileSetup.this);
//                selectedCountry = country.getName();

                location = selectedCountry;
                System.out.println("Country Name: " + selectedCountry);
                //String selectedGender = gender;


                Log.d("MyApp","I am here" + ijp++);
                if(selectedDOB.equals("")){
                    final Toast toast = Toast.makeText(ActivityProfileSetup.this, "Date Of Birth Not Selected" , Toast.LENGTH_SHORT);
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

                if(weightOfPerson.getText().toString().equals("")){
                    Toast.makeText(ActivityProfileSetup.this, "Please Enter Weight." , Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    weightOfPlayer = weightOfPerson.getText().toString();;
                }

                if(name.equals("")){
                    Toast.makeText(ActivityProfileSetup.this, "Please Enter Name." , Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d("MyApp","I am here" + ijp++);

                String str[] = DOB.split("-");
                int day = Integer.parseInt(str[0]);
                int month = Integer.parseInt(str[1]);
                int year = Integer.parseInt(str[2]);


                if(getAge(year,month,day)<10){
                    Toast.makeText(ActivityProfileSetup.this, "Player's age should be atleast 10" , Toast.LENGTH_SHORT).show();
                    return;
                }
//                        String weightOfPlayer = weightOfPerson.getText().toString();
//                        weightOfPlayer= weightOfPerson.getText().toString();



                Log.d("MyApp","I am here" + ijp++);









                player.setName(name);
//                        player.setUsername(username);
                player.setEmail(email);
                player.setPassword(password);
//                        player.setSecurity(security);
                player.setGender(gender);
                player.setLocation(location);
                player.setDOB(DOB);
                player.setBowlingArm(bowlingArm);
                player.setBowlingStyle(bowlingStyle);
                player.setCareerLevel(careerLevel);
                player.setWeight(weightOfPlayer);


                player.setLegalBowls("0");
                player.setIllegalBowls("0");
                player.setAverageAngle("0");
                player.setLongestStreak("0");
                player.setLastBowlAngle("0");
                Log.d("MyApp","I am here" + ijp++);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user==null){
                    System.out.println("User not registered!");
                    return;
                }
                else{
                    System.out.println("User Id: " + user.getUid());
                }
                Log.d("MyApp","I am here" + ijp++);
//                this.bowlingArm = bowlingArm;
//                this.bowlingStyle = bowlingStyle;
//                this.careerLevel = careerLevel;
//                this.DOB = DOB;
//                this.emailId = emailId;
//                this.gender = gender;
//                this.location = location;
//                this.nameOfPerson = nameOfPerson;
//                this.weight = weight;

                User playerProfile = new User(bowlingArm,bowlingStyle,careerLevel,DOB,email,gender,location,name,weightOfPlayer,user.getUid());
//                playerProfile.setNameOfPerson(name);
//                playerProfile.setCareerLevel(careerLevel);
//                playerProfile.setBowlingArm(bowlingArm);
//                playerProfile.setBowlingStyle(bowlingStyle);
//                playerProfile.setDOB(DOB);
//                playerProfile.setEmailId(email);
//                playerProfile.setGender(gender);
//                playerProfile.setLocation(location);
//                playerProfile.setWeight(weightOfPlayer);
//                playerProfile.setUserId(user.getUid());
//
//                playerProfile.setNameOfPerson(name);
                Log.d("MyApp","I am here" + ijp++);
//                System.out.println("L hogya");
                if(playerProfile == null){
                    System.out.println("L hogya");
                }


//                System.out.println("name : " + playerProfile.getNameOfPerson() );
//                System.out.println("email : " + playerProfile.getEmailId());
//                System.out.println("gender : "+ playerProfile.getGender());
//                System.out.println("location : "+ playerProfile.getLocation());
//                System.out.println("DOB : "+ playerProfile.getDOB());
//                System.out.println("bowlingArm : "+ playerProfile.getBowlingArm());
//                System.out.println("bowlingStyle : "+ playerProfile.getBowlingStyle());
//                System.out.println("careerLevel : "+ playerProfile.getCareerLevel());
//                System.out.println("weight : "+ playerProfile.getWeight());
//                System.out.println("UUID : " + playerProfile.getUserId()  );


//                System.out.println("had hogae ha " + user.getUid());
//                System.out.println("User id Bhinot: " + user.getUid());
                Log.d("MyApp","I am here" + ijp++);



                //
//                rootDatabaseReference.child("Players").child(user.getEmail()).child("Name").setValue(player.getName());
//                rootDatabaseReference.child("Players").child(user.getEmail()).child("Gender").setValue(player.getGender());
//                rootDatabaseReference.child("Players").child(user.getEmail()).child("Email").setValue(player.getEmail());
//                rootDatabaseReference.child("Players").child(user.getEmail()).child("Weight").setValue(player.getWeight());
//                rootDatabaseReference.child("Players").child(user.getEmail()).child("Location").setValue(player.getLocation());
//                rootDatabaseReference.child("Players").child(user.getEmail()).child("DOB").setValue(player.getDOB());
//                rootDatabaseReference.child("Players").child(user.getEmail()).child("Bowling Arm").setValue(player.getBowlingArm());
//                rootDatabaseReference.child("Players").child(user.getEmail()).child("Bowling Style").setValue(player.getBowlingStyle());
//                rootDatabaseReference.child("Players").child(user.getEmail()).child("Career Level").setValue(player.getCareerLevel());



//                rootDatabaseReference.child("Players").child(user.getUid()).push().setValue(playerProfile);
//                Log.d("MyApp","I am here" + ijp++);

                rootDatabaseReference.child("Players").child(user.getUid()).setValue(playerProfile, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                        if (databaseError != null) {
//                            Log.e("Tag", "Failed to write message", databaseError.toException());
                            System.out.println("Error in writing to database: " + databaseError.toException() );
                        }
                    }
                });


                Log.d("MyApp","I am here" + ijp++);
                saveImage(getApplicationContext(),bitmapImage,email,"jpeg");






//                        userRef.child("name").setValue(player);
//                        userRef.child("name").child("values").setValue(player);
                //helper.addEntry(email,dbBitmapUtility.getBytes(bitmapImage));



                helper.changePlayerInfo(player);
//                        helper.insertPlayer(player);

//                        helper.insertPlayerAngleValues(player.getEmail(),"");
//                        helper.insertPlayerActionTimeValues(player.getEmail(),"");
//                        helper.insertPlayerForceValues(player.getEmail(),"");
//                        helper.insertPlayerArmTwistValues(player.getEmail(),"");
//
//
//                        helper.insertPlayerAngleValuesWithDate(player.getEmail(),"","");
//                        helper.insertPlayerActionTimeValuesWithDate(player.getEmail(),"","");
//                        helper.insertPlayerForceValuesWithDate(player.getEmail(),"","");
//                        helper.insertPlayerArmTwistValuesWithDate(player.getEmail(),"","");


                Toast toast = Toast.makeText(ActivityProfileSetup.this, "Registered Account!" , Toast.LENGTH_SHORT);

                toast.show();
                Intent i = new Intent(ActivityProfileSetup.this, ActivityLogin.class);
                ActivityProfileSetup.this.startActivity(i);

                FirebaseAuth.getInstance().signOut();


            }
        });


    }   //end OnCreate

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
            bitmapImage = Bitmap.createScaledBitmap(bitmapImage, 500, 500, true);


            profileImage.setImageBitmap(bitmapImage);
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }



    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
            throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
            // with both dimensions larger than or equal to the requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }


    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        //int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

//                ExifInterface exif = new ExifInterface(pathToImage);
//                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//                Log.d("EXIF", "Exif: " + orientation);
//                Matrix matrix = new Matrix();
//                if (orientation == 6) {
//                    matrix.postRotate(90);
//                }
//                else if (orientation == 3) {
//                    matrix.postRotate(180);
//                }
//                else if (orientation == 8) {
//                    matrix.postRotate(270);
//                }
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }
    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }




    private void initializeBowlingStylesSpinner(String arm){

        Context context=getApplicationContext();
        bowlingStylesSpinner = (Spinner)findViewById(R.id.bowlingstyle_spinner);

        if(arm.equals("Left"))
        {
            String[] bowlingStylesArray = context.getResources().getStringArray(R.array.bowlingstyle_left_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spiner_item, bowlingStylesArray);
            bowlingStylesSpinner.setAdapter(adapter);
        }
        else if(arm.equals("Right"))
        {
            String[] bowlingStylesArray = context.getResources().getStringArray(R.array.bowlingstyle_right_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spiner_item, bowlingStylesArray);
            bowlingStylesSpinner.setAdapter(adapter);
        }



//
//        String[] bowlingStylesArray = context.getResources().getStringArray(R.array.bowlingstyle_array);
//        bowlingStylesSpinner = (Spinner)findViewById(R.id.bowlingstyle_spinner);
//        //ArrayList<String> bowlingStyles = new ArrayList( Arrays.asList( R.array.bowlingstyle_array ) );
//        //System.out.println(R.array.bowlingstyle_array);
//
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spiner_item, bowlingStylesArray);
//        bowlingStylesSpinner.setAdapter(adapter);
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

//        Locale[] locale = Locale.getAvailableLocales();
//        ArrayList<String> countries = new ArrayList<String>();
//        String country;
//        for( Locale loc : locale ){
//            country = loc.getDisplayCountry();
//            if( country.length() > 0 && !countries.contains(country) ){
//                countries.add( country );
//            }
//        }
//        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

//        CountrySpinner = (Spinner)findViewById(R.id.country_spinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.spiner_item, countries);
//        CountrySpinner.setAdapter(adapter);

        // = CountrySpinner.getSelectedItem().toString();





//        drawable = getResources().getDrawable(country.getFlag());
//
//        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()),
//                (int)(drawable.getIntrinsicHeight()));
//        ScaleDrawable sd = new ScaleDrawable(drawable, 0, scaleWidth, scaleHeight);


        //location_text.setCompoundDrawables(sd.getDrawable(), null, null, null);
        //location_text.setCompoundDrawablePadding(40);
        //location_text.setCompoundDrawablesWithIntrinsicBounds(country.getFlag(), 0, 0, 0);


    }

    private void findViewsById() {

        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rdbMale = (RadioButton) findViewById(R.id.rdbMale);
        rdbFemale = (RadioButton) findViewById(R.id.rdbMale);





        birthDate = (EditText) findViewById(R.id.stp_dob);
        birthDate.setInputType(InputType.TYPE_NULL);
//        birthDate.requestFocus();

        rgBowlingArm = (RadioGroup) findViewById(R.id.rgBowlingArm);
        rdbLeft = (RadioButton) findViewById(R.id.rdbLeft);
        rdbRight = (RadioButton) findViewById(R.id.rdbRight);

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






    @Override
    public void onBackPressed() {
        Intent i = new Intent(ActivityProfileSetup.this, ActivityRegister.class);
        ActivityProfileSetup.this.startActivity(i);
        return;

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

    public Bitmap getImageBitmap(Context context,String name,String extension){
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



    private void takePictureFromGallery()
    {
        startActivityForResult(
                Intent.createChooser(
                        new Intent(Intent.ACTION_GET_CONTENT)
                                .setType("image/*"), "Choose an image"),
                1);
    }

//
//    private void handleGalleryResult(Intent data)
//    {
//        Uri selectedImage = data.getData();
//        mTmpGalleryPicturePath = getPath(selectedImage);
//        if(mTmpGalleryPicturePath!=null)
//            ImageUtils.setPictureOnScreen(mTmpGalleryPicturePath, mImageView);
//        else
//        {
//            try {
//                InputStream is = getContentResolver().openInputStream(selectedImage);
//                mImageView.setImageBitmap(BitmapFactory.decodeStream(is));
//                mTmpGalleryPicturePath = selectedImage.getPath();
//            } catch (FileNotFoundException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @SuppressLint("NewApi")
//    private String getPath(Uri uri) {
//        if( uri == null ) {
//            return null;
//        }
//
//        String[] projection = { MediaStore.Images.Media.DATA };
//
//        Cursor cursor;
//        if(Build.VERSION.SDK_INT >19)
//        {
//            // Will return "image:x*"
//            String wholeID = DocumentsContract.getDocumentId(uri);
//            // Split at colon, use second item in the array
//            String id = wholeID.split(":")[1];
//            // where id is equal to
//            String sel = MediaStore.Images.Media._ID + "=?";
//
//            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    projection, sel, new String[]{ id }, null);
//        }
//        else
//        {
//            cursor = getContentResolver().query(uri, projection, null, null, null);
//        }
//        String path = null;
//        try
//        {
//            int column_index = cursor
//                    .getColumnIndex(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            path = cursor.getString(column_index).toString();
//            cursor.close();
//        }
//        catch(NullPointerException e) {
//
//        }
//        return path;
//    }
public void show(){

    picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
    picker.setListener(new CountryPickerListener() {
        @Override
        public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
            // Implement your code here

            location_text.setText(name);
            selectedCountry = name;
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

    @Override
    protected void onStart() {
        super.onStart();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//               Player p = dataSnapshot.getClass();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
