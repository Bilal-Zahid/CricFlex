package com.example.cricflex;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.TextUtils;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.mukesh.countrypicker.models.Country;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Asawal on 8/13/2016.
 */
public class ActivityProfileSetup extends FragmentActivity {

    DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
//    DatabaseHelper helper = new DatabaseHelper(this);

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
    CircleImageView profileImage;

    String backCheck = "activity setup profile 1";

    //private TextView location;

    //private String location = "not set";

    private TextView birthDate;

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

    private static final String TAG = "CameraExif";

    //Firebase Things
    private DatabaseReference rootDatabaseReference = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = rootDatabaseReference.child("Users");
    DatabaseReference userNameReference = userRef.child("name");
    DatabaseReference userValuesReference = userNameReference.child("values");


    //Firebase Storage
    StorageReference mStorage = FirebaseStorage.getInstance().getReference();

    private static final int GALLERY_INTENT = 2;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile_setup);
        findViewsById();

        bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon_large);

//        initializeCountrySpinner();
//
        mProgressDialog = new ProgressDialog(this);

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


        profileImage = (CircleImageView) findViewById(R.id.profilepicture);
        profileImage.setClickable(true);

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);


                Intent i = new Intent(
                        Intent.ACTION_PICK);

                i.setType("image/*");

                startActivityForResult(i, GALLERY_INTENT);


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
                final String email = intent.getStringExtra("email");

                List<String> prevEmails = new ArrayList<String>(SaveSharedPreference.getEmailList(ActivityProfileSetup.this));
//        prevEmails = ;

                boolean emailExistCheck = true;
                System.out.println("Previous mails: " + prevEmails);

                int j;
                for( j=0;j<prevEmails.size();j++){
                    if(prevEmails.get(j).equals(email)){
                        System.out.println("In if of Previous mails for loop: ");

                        emailExistCheck = false;
                    }
                    System.out.println("In Previous mails for loop: " + prevEmails.get(j));
                }

                if(emailExistCheck){
                    System.out.println("In Email exist check: " + email);

                    prevEmails.add(email);

                    System.out.println("bhinot: ");

                    System.out.println("Kabhi yahan bhi aao" + prevEmails);
                    SaveSharedPreference.setEmailList(ActivityProfileSetup.this,prevEmails);
                }




                String password = intent.getStringExtra("password");
//                        String security = intent.getStringExtra("security");
                String gender = selectedGender;
                String location = location_text.getText().toString();
                String DOB = selectedDOB;
                String bowlingArm = selectedBowlingArm;
                String bowlingStyle = selectedBowlingStyle;
                String careerLevel = selectedCareerLevel;
                String weightOfPlayer;



                selectedDOB = birthDate.getText().toString();

                name = nameOfPerson.getText().toString();
//                country = picker.getUserCountryInfo(ActivityProfileSetup.this);
//                selectedCountry = country.getName();

//                location = selectedCountry;
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
//                saveImage(getApplicationContext(),bitmapImage,email,"jpeg");






//                        userRef.child("name").setValue(player);
//                        userRef.child("name").child("values").setValue(player);
                //helper.addEntry(email,dbBitmapUtility.getBytes(bitmapImage));



//                helper.changePlayerInfo(player);
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






//                Intent i = new Intent(ActivityProfileSetup.this, ActivityMain.class);
//                ActivityProfileSetup.this.startActivity(i);
                userLogin();

//                FirebaseAuth.getInstance().signOut();


            }
        });


    }   //end OnCreate


    private void userLogin() {

        final String email = getIntent().getStringExtra("email");;
        String password = getIntent().getStringExtra("password");





        if(TextUtils.isEmpty(email) || email.equals("")){
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return ;
        }
        if(TextUtils.isEmpty(password ) || password.equals("")){
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return ;
        }


        final ProgressDialog progressDialog = new ProgressDialog(ActivityProfileSetup.this);
        progressDialog.setMessage("Signing In");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {




                        progressDialog.dismiss();

                        if(task.isSuccessful()){
                            //logging in





//                            System.out.println("Email: " + email);
                            Toast.makeText(ActivityProfileSetup.this, "Signed In" ,
                                    Toast.LENGTH_SHORT).show();

                            SaveSharedPreference.setEmail(ActivityProfileSetup.this,getIntent().getStringExtra("email"));

                            Intent i = new Intent(ActivityProfileSetup.this, ActivityMain.class);
                            ActivityProfileSetup.this.startActivity(i);

                            finish();


                        }
                        else{
                            progressDialog.dismiss();

                            Toast.makeText(ActivityProfileSetup.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });









    }



    //Method for handling rotation
    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){


            Uri uri = data.getData();





            byte[] imageByteArray = null;
            try {

                //Rotation by orientation
                ExifInterface exif = new ExifInterface(getRealPathFromUri(ActivityProfileSetup.this,uri));

                int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int rotationInDegrees = exifToDegrees(rotation);


                System.out.println("Rotation: " + rotation + "  Rotation In degrees: " + rotationInDegrees );
                Matrix matrix = new Matrix();
                if (rotation != 0f) {matrix.preRotate(rotationInDegrees);}

                Bitmap bitmap  = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                bitmap = Bitmap.createBitmap(bitmap, 0, 0,bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 20, bos)) {
                    //image is now compressed into the output stream
                    imageByteArray = bos.toByteArray();
                } else {
                    //compress failed
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

//            int orientation  = getOrientation(imageByteArray);






//            Bitmap bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);

//            bmp = rotateImage(bmp,orientation);
//
////            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            imageByteArray = stream.toByteArray();
//            Bitmap bmp = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);



//            String path = MediaStore.Images.Media.insertImage(ActivityProfileSetup.this.getContentResolver(), bmp, "Title", null);



            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            StorageReference userPictureRef = mStorage.child("Photos").child(user.getUid());

            userPictureRef.delete();

            mProgressDialog.setMessage("Uploading ... ");
            mProgressDialog.show();
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
            userPictureRef.putBytes(imageByteArray).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(ActivityProfileSetup.this, "Upload Done...", Toast.LENGTH_SHORT).show();
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                    taskSnapshot.;


                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()

                            .setPhotoUri(downloadUrl)
                            .build();



                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("done! ", "User profile updated.");

                                        for (UserInfo profile : user.getProviderData()) {
                                            // Id of the provider (ex: google.com)
//                String providerId = profile.getProviderId();
//
//                // UID specific to the provider
//                String uid = profile.getUid();

                                            // Name, email address, and profile photo Url
//                String name = profile.getDisplayName();
//                String email = profile.getEmail();
                                            Uri photoUrl = profile.getPhotoUrl();

                                            Picasso.with(ActivityProfileSetup.this).load(photoUrl).fit().centerCrop().into(profileImage);

                                            mProgressDialog.dismiss();
                                        };
                                    }
                                }
                            });

//                    Picasso.with(ActivityProfileSetup.this).load(downloadUrl).fit().centerCrop().into(profileImage);
////;
//                    mProgressDialog.dismiss();
                }
            });
        }
    }

    //    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//
//            ImageView profileImage = (ImageView) findViewById(R.id.profilepicture);
//            bitmapImage = BitmapFactory.decodeFile(picturePath);
//            bitmapImage = Bitmap.createScaledBitmap(bitmapImage, 500, 500, true);
//
//
//            profileImage.setImageBitmap(bitmapImage);
////            ImageView imageView = (ImageView) findViewById(R.id.imgView);
////            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//
//        }
//
//    }



//    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage)
//            throws IOException {
//        int MAX_HEIGHT = 1024;
//        int MAX_WIDTH = 1024;
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
//        BitmapFactory.decodeStream(imageStream, null, options);
//        imageStream.close();
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        imageStream = context.getContentResolver().openInputStream(selectedImage);
//        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);
//
//        img = rotateImageIfRequired(img, selectedImage);
//        return img;
//    }
//
//    private static int calculateInSampleSize(BitmapFactory.Options options,
//                                             int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            // Calculate ratios of height and width to requested height and width
//            final int heightRatio = Math.round((float) height / (float) reqHeight);
//            final int widthRatio = Math.round((float) width / (float) reqWidth);
//
//            // Choose the smallest ratio as inSampleSize value, this will guarantee a final image
//            // with both dimensions larger than or equal to the requested height and width.
//            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
//
//            // This offers some additional logic in case the image has a strange
//            // aspect ratio. For example, a panorama may have a much larger
//            // width than height. In these cases the total pixels might still
//            // end up being too large to fit comfortably in memory, so we should
//            // be more aggressive with sample down the image (=larger inSampleSize).
//
//            final float totalPixels = width * height;
//
//            // Anything more than 2x the requested pixels we'll sample down further
//            final float totalReqPixelsCap = reqWidth * reqHeight * 2;
//
//            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
//                inSampleSize++;
//            }
//        }
//        return inSampleSize;
//    }


//    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {
//
//        ExifInterface ei = new ExifInterface(selectedImage.getPath());
//        //int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
//
////                ExifInterface exif = new ExifInterface(pathToImage);
////                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
////                Log.d("EXIF", "Exif: " + orientation);
////                Matrix matrix = new Matrix();
////                if (orientation == 6) {
////                    matrix.postRotate(90);
////                }
////                else if (orientation == 3) {
////                    matrix.postRotate(180);
////                }
////                else if (orientation == 8) {
////                    matrix.postRotate(270);
////                }
//        switch (orientation) {
//            case ExifInterface.ORIENTATION_ROTATE_90:
//                return rotateImage(img, 90);
//            case ExifInterface.ORIENTATION_ROTATE_180:
//                return rotateImage(img, 180);
//            case ExifInterface.ORIENTATION_ROTATE_270:
//                return rotateImage(img, 270);
//            default:
//                return img;
//        }
//    }
//    private static Bitmap rotateImage(Bitmap img, int degree) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(degree);
//        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
//        img.recycle();
//        return rotatedImg;
//    }




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




//    private void initializeCountrySpinner(){

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


//    }

    private void findViewsById() {

        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rdbMale = (RadioButton) findViewById(R.id.rdbMale);
        rdbFemale = (RadioButton) findViewById(R.id.rdbMale);





        birthDate = (TextView) findViewById(R.id.stp_dob);
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

//    public Bitmap getImageBitmap(Context context,String name,String extension){
//        name=name+"."+extension;
//        try{
//            FileInputStream fis = context.openFileInput(name);
//            Bitmap b = BitmapFactory.decodeStream(fis);
//            fis.close();
//            return b;
//        }
//        catch(Exception e){
//        }
//        return null;
//    }
//
//
//
//    private void takePictureFromGallery()
//    {
//        startActivityForResult(
//                Intent.createChooser(
//                        new Intent(Intent.ACTION_GET_CONTENT)
//                                .setType("image/*"), "Choose an image"),
//                1);
//    }

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



    // Returns the degrees in clockwise. Values are 0, 90, 180, or 270.
//    public static int getOrientation(byte[] jpeg) {
//        if (jpeg == null) {
//            return 0;
//        }
//
//        int offset = 0;
//        int length = 0;
//
//        // ISO/IEC 10918-1:1993(E)
//        while (offset + 3 < jpeg.length && (jpeg[offset++] & 0xFF) == 0xFF) {
//            int marker = jpeg[offset] & 0xFF;
//
//            // Check if the marker is a padding.
//            if (marker == 0xFF) {
//                continue;
//            }
//            offset++;
//
//            // Check if the marker is SOI or TEM.
//            if (marker == 0xD8 || marker == 0x01) {
//                continue;
//            }
//            // Check if the marker is EOI or SOS.
//            if (marker == 0xD9 || marker == 0xDA) {
//                break;
//            }
//
//            // Get the length and check if it is reasonable.
//            length = pack(jpeg, offset, 2, false);
//            if (length < 2 || offset + length > jpeg.length) {
//                Log.e(TAG, "Invalid length");
//                return 0;
//            }
//
//            // Break if the marker is EXIF in APP1.
//            if (marker == 0xE1 && length >= 8 &&
//                    pack(jpeg, offset + 2, 4, false) == 0x45786966 &&
//                    pack(jpeg, offset + 6, 2, false) == 0) {
//                offset += 8;
//                length -= 8;
//                break;
//            }
//
//            // Skip other markers.
//            offset += length;
//            length = 0;
//        }
//
//        // JEITA CP-3451 Exif Version 2.2
//        if (length > 8) {
//            // Identify the byte order.
//            int tag = pack(jpeg, offset, 4, false);
//            if (tag != 0x49492A00 && tag != 0x4D4D002A) {
//                Log.e(TAG, "Invalid byte order");
//                return 0;
//            }
//            boolean littleEndian = (tag == 0x49492A00);
//
//            // Get the offset and check if it is reasonable.
//            int count = pack(jpeg, offset + 4, 4, littleEndian) + 2;
//            if (count < 10 || count > length) {
//                Log.e(TAG, "Invalid offset");
//                return 0;
//            }
//            offset += count;
//            length -= count;
//
//            // Get the count and go through all the elements.
//            count = pack(jpeg, offset - 2, 2, littleEndian);
//            while (count-- > 0 && length >= 12) {
//                // Get the tag and check if it is orientation.
//                tag = pack(jpeg, offset, 2, littleEndian);
//                if (tag == 0x0112) {
//                    // We do not really care about type and count, do we?
//                    int orientation = pack(jpeg, offset + 8, 2, littleEndian);
//                    switch (orientation) {
//                        case 1:
//                            return 0;
//                        case 3:
//                            return 180;
//                        case 6:
//                            return 90;
//                        case 8:
//                            return 270;
//                    }
//                    Log.i(TAG, "Unsupported orientation");
//                    return 0;
//                }
//                offset += 12;
//                length -= 12;
//            }
//        }
//
//        Log.i(TAG, "Orientation not found");
//        return 0;
//    }
//
//    private static int pack(byte[] bytes, int offset, int length,
//                            boolean littleEndian) {
//        int step = 1;
//        if (littleEndian) {
//            offset += length - 1;
//            step = -1;
//        }
//
//        int value = 0;
//        while (length-- > 0) {
//            value = (value << 8) | (bytes[offset] & 0xFF);
//            offset += step;
//        }
//        return value;
//    }

//    public Bitmap rotateImage(int angle, Bitmap bitmapSrc) {
//        Matrix matrix = new Matrix();
//        matrix.postRotate(angle);
//        return Bitmap.createBitmap(bitmapSrc, 0, 0,
//                bitmapSrc.getWidth(), bitmapSrc.getHeight(), matrix, true);
//    }
}
