package com.example.cricflex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
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
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by Asawal on 8/13/2016.      Chah gayay ho aap
 */
public class ActivitySetupProfile extends Activity implements View.OnClickListener {

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

    Context ctx;

    Bitmap bitmapImage;

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


//    private ImageView profileImage;




    private static final int SELECT_PICTURE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().hide();

        setContentView(R.layout.activity_profile_setup1);
        findViewsById();

        bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.profile_icon_large);

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
                //Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
//                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                takePicture.putExtra("android.intent.extras.CAMERA_FACING", 1);
//                startActivityForResult(takePicture, RESULT_LOAD_IMG);

                //Image imageFromGallery = new;
//                Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);


//                Intent pickIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                //pickIntent.setType("image/*");
//                //pickIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//                String pickTitle = "Select or take a new Picture"; // Or get from strings.xml
//                Intent chooserIntent = Intent.createChooser(pickIntent, pickTitle);
//                chooserIntent.putExtra
//                        (
//                                Intent.EXTRA_INITIAL_INTENTS,
//                                new Intent[] { takePhotoIntent }
//                        );
//
////                startActivityForResult(chooserIntent, SELECT_PICTURE);
//                startActivityForResult(pickIntent, SELECT_PICTURE);


                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, 1);


            }
        });




        //Bitmap bitmap = ((BitmapDrawable)profileImage.getDrawable()).getBitmap();

        //profileImage.setImageBitmap(bitmap);


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
                        String name = intent.getStringExtra("name");
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
                        String weightOfPlayer = weight;



                        System.out.println("name : " + name);
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

                        player.setName(name);
                        player.setUsername(username);
                        player.setEmail(email);
                        player.setPassword(password);
                        player.setSecurity(security);
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


                        saveImage(getApplicationContext(),bitmapImage,username,"jpeg");






                        //helper.addEntry(username,dbBitmapUtility.getBytes(bitmapImage));



                        helper.insertPlayerStats(player);
                        helper.insertPlayer(player);

                        helper.insertPlayerAngleValues(player.getUsername(),"");
                        helper.insertPlayerActionTimeValues(player.getUsername(),"");
                        helper.insertPlayerForceValues(player.getUsername(),"");
                        helper.insertPlayerArmTwistValues(player.getUsername(),"");


                        helper.insertPlayerAngleValuesWithDate(player.getUsername(),"","");
                        helper.insertPlayerActionTimeValuesWithDate(player.getUsername(),"","");
                        helper.insertPlayerForceValuesWithDate(player.getUsername(),"","");
                        helper.insertPlayerArmTwistValuesWithDate(player.getUsername(),"","");


                        Toast toast = Toast.makeText(ActivitySetupProfile.this, "Registered Account!" , Toast.LENGTH_SHORT);

                        toast.show();
                        Intent i = new Intent(ActivitySetupProfile.this, ActivityLogin.class);
                        ActivitySetupProfile.this.startActivity(i);


                    }
                });



            }
        });



    }

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
//            ImageView imageView = (ImageView) findViewById(R.id.imgView);
//            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }




//        new code start
//        try {
//
//            switch (requestCode) {
//                case 0:
//                    if (resultCode == RESULT_OK) {
//                        Uri selectedImage = data.getData();
//
//                        bitmapImage = handleSamplingAndRotationBitmap(this.getApplicationContext(), selectedImage);
//                        ImageView profileImage = (ImageView) findViewById(R.id.profilepicture);
//                        profileImage.setImageBitmap(bitmapImage);
////                    imageview.setImageURI(selectedImage);
//                    }
//
//                    break;
//                case 1:
//                    if (resultCode == RESULT_OK) {
//                        Uri selectedImage = data.getData();
//                        bitmapImage = handleSamplingAndRotationBitmap(this.getApplicationContext(), selectedImage);
//                        ImageView profileImage = (ImageView) findViewById(R.id.profilepicture);
//                        profileImage.setImageBitmap(bitmapImage);
////                    imageview.setImageURI(selectedImage);
//                    }
//                    break;
//            }
//        }
//        catch (Exception e){
//
//        }

        // comment start
//        System.out.println("Before try....");
//
////        try {
//            // When an Image is picked
//
//            System.out.println("Before try if");
//            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
//                    && data !=null && data.getData()!=null) {
//                // Get the Image from data
//
//                System.out.println("After try if");
//
//
//
//                Uri selectedImage = data.getData();
//                System.out.print("uri: " + selectedImage.toString());
//                //File f = new File(selectedImage.getPath());
//
//                //selectedImage = Uri.fromFile(f);
//                //ContextWrapper context = new ContextWrapper();
//                //Bitmap b;
//                try {
//                    bitmapImage = handleSamplingAndRotationBitmap( this.getApplicationContext(), selectedImage);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
////                bitmapImage = b;
//                ImageView profileImage = (ImageView) findViewById(R.id.profilepicture);
//                profileImage.setImageBitmap(bitmapImage);
//
//                //saveImage(this.getApplicationContext(),bitmapImage,);
//
//
//            } else {
//                Toast.makeText(this, "You haven't picked Image",
//                        Toast.LENGTH_LONG).show();
//            }

        //cooment end


//                String[] filePathColumn = { MediaStore.Images.Media.DATA };
//
//                // Get the cursor
//                Cursor cursor = getContentResolver().query(selectedImage,
//                        filePathColumn, null, null, null);
////                // Move to first row
//                cursor.moveToFirst();
//
//                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                imgDecodableString = cursor.getString(columnIndex);
////                cursor.close();
////
////                // Set the Image in ImageView after decoding the String
//                bitmapImage =  BitmapFactory.decodeFile(imgDecodableString);
////
//
//
//
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 8;

//                ByteArrayInputStream inputStreamImage = new ByteArrayInputStream(DbBitmapUtility.getBytes(bitmapImage));
//                Bitmap compressedBitmap = BitmapFactory.decodeStream(inputStreamImage, null, options);

                //String pathToImage = selectedImage.getPath();

                //compressedBitmap = rotateImageIfRequired(compressedBitmap,selectedImage);






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
//                compressedBitmap = Bitmap.createBitmap(compressedBitmap, 0, 0, compressedBitmap.getWidth(), compressedBitmap.getHeight(), matrix, true); // rotating bitmap


//                ExifInterface ei = new ExifInterface(selectedImage.getPath());
//                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//                switch (orientation) {
//                    case ExifInterface.ORIENTATION_ROTATE_90:
//                        compressedBitmap = rotateImage(compressedBitmap, 90);
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_180:
//                        compressedBitmap = rotateImage(compressedBitmap, 180);
//                        break;
//                    case ExifInterface.ORIENTATION_ROTATE_270:
//                        compressedBitmap = rotateImage(compressedBitmap, 270);
//                        break;
//                    default:
//
//                }
                //compressedBitmap = rotateImage(compressedBitmap, 90);



//                bitmapImage  = compressedBitmap;


//        } catch (Exception e) {
//            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT)
//                    .show();
//        }

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



}
