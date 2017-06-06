package com.example.cricflex;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Fragment;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;


public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    String inFragment = "Home";
    Boolean changeFragment = false;

//    DatabaseHelper helper = new DatabaseHelper(this);
//    TextView mainEmail;
    private TextView emailTextView ;
    private TextView nameTextView;
    Fragment fragment = null;
    NavigationView navigationView;

    DrawerLayout navDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // full screen view
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // setting the layout
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);







        int abc = SaveSharedPreference.getEmail(ActivityMain.this).length();

        System.out.println("email list in main activity: " + SaveSharedPreference.getEmailList(ActivityMain.this));

        String emailInSharedPreferance = SaveSharedPreference.getEmail(ActivityMain.this);

        if(emailInSharedPreferance.length() == 0)
        {
            Intent intent = new Intent(ActivityMain.this, ActivitySplash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }



        List<String> prevEmails = new ArrayList<String>(SaveSharedPreference.getEmailList(ActivityMain.this));
//        prevEmails = ;

        boolean emailExistCheck = true;
        System.out.println("Previous mails: " + prevEmails);

        int j;
        for( j=0;j<prevEmails.size();j++){
            if(prevEmails.get(j).equals(emailInSharedPreferance)){
                System.out.println("In if of Previous mails for loop: ");

                emailExistCheck = false;
            }
            System.out.println("In Previous mails for loop: " + prevEmails.get(j));
        }

        if(emailExistCheck){
            System.out.println("In Email exist check: " + emailInSharedPreferance);

            prevEmails.add(emailInSharedPreferance);

            System.out.println("bhinot: ");

            System.out.println("Kabhi yahan bhi aao" + prevEmails);
            SaveSharedPreference.setEmailList(ActivityMain.this,prevEmails);
        }

        navDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, navDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);

                if (fragment != null && changeFragment) {

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frame_container, fragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    setTitle(inFragment);

                }
                else {

                    // error in creating fragment
                    Log.e("ActivityMain", "Fragment not Created");

                }

            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
            }
        };
        navDrawer.addDrawerListener(toggle);
        toggle.syncState();



        navigationView = (NavigationView) findViewById(R.id.nav_view);

        View nav_header =  navigationView.getHeaderView(0);
        nameTextView = (TextView)nav_header.findViewById(R.id.name);
//        nameTextView.setText(helper.getName(SaveSharedPreference.getEmail(ActivityMain.this)));
        emailTextView = (TextView)nav_header.findViewById(R.id.email);
        emailTextView.setText(SaveSharedPreference.getEmail(ActivityMain.this));


        boolean imageCheck = false;

        ImageView profilePicture = (ImageView) nav_header.findViewById(R.id.profilepicture);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        for (UserInfo profile : user.getProviderData()) {

            Uri photoUrl = profile.getPhotoUrl();
            if(photoUrl!= null){
                imageCheck = true;
            }
            Picasso.with(ActivityMain.this).load(photoUrl).fit().centerCrop().into(profilePicture);
        }
        if (!imageCheck){
            profilePicture.setImageDrawable(getResources().getDrawable(R.drawable.profile_icon));
        }

        ValueEventListener userListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                System.out.println("Datasnapshot mai ara hai : ");
                User playerProfile = new User();

                playerProfile = dataSnapshot.getValue(User.class);
                View nav_header =  navigationView.getHeaderView(0);
                TextView name = (TextView)nav_header.findViewById(R.id.name);
                name.setText(playerProfile.nameOfPerson);
                TextView email = (TextView)nav_header.findViewById(R.id.email);
                email.setText(SaveSharedPreference.getEmail(ActivityMain.this));






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






        nav_header.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                System.out.println("In Click Listener!!!");
                Intent i = new Intent(ActivityMain.this, ActivityProfile.class);
                startActivity(i);
//                finish();
            }
        });



        navigationView.setNavigationItemSelectedListener(this);


        fragment = new FragmentHome();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
        setTitle("Home");
        navigationView.setCheckedItem(R.id.nav_home);
        inFragment = "Home";

    }


    @Override
    public void onBackPressed() {

        if (navDrawer.isDrawerOpen(GravityCompat.START)) {                         //close drawer if opened
            navDrawer.closeDrawer(GravityCompat.START);
        }

        else if(!navDrawer.isDrawerOpen(GravityCompat.START) && inFragment.equals("Home")){        // if drawer is closed and user is in home fragment
            moveTaskToBack(true);
        }

        else if(!inFragment.equals("Home") && !inFragment.equals("")){     // if user in some other fragment than home

            fragment = new FragmentHome();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            setTitle("Home");
            navigationView.setCheckedItem(R.id.nav_home);
            inFragment = "Home";
        }

        else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        // Handle action buttons
        switch(item.getItemId()) {



            case R.id.tour_cricflex:

                Intent intent0 = new Intent(ActivityMain.this, ActivityTour.class);

                startActivity(intent0);
                return true;

            case R.id.calibrate:

//                Intent intent1 = new Intent(ActivityMain.this, ActivityCalibrate.class);
//                startActivity(intent1);
                return true;



            case R.id.logout:

                Intent intent3 = new Intent(ActivityMain.this, ActivityLogin.class);
                //ActivityMain.this.finish();


                SaveSharedPreference.clearEmail(ActivityMain.this);

                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                FirebaseAuth.getInstance().signOut();

                startActivity(intent3);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.nav_home:{

                if(!inFragment.equals("Home")){
                    inFragment = "Home";
                    fragment = new FragmentHome();
                    changeFragment = true;
                }
                else
                    changeFragment = false;

                break;
            }
            case R.id.nav_history:{

                if(!inFragment.equals("History")){
                    inFragment = "History";
                    fragment = new FragmentHistory();
                    changeFragment = true;
                }
                else
                    changeFragment = false;

                break;
            }
            case R.id.nav_awards:{
                Toast.makeText(this, "Update to premium for this feature.", Toast.LENGTH_SHORT).show();
                //fragment = new FragmentAwards();

                break;
            }
            case R.id.nav_training:{
                Toast.makeText(this, "Update to premium for this feature.", Toast.LENGTH_SHORT).show();
                //fragment = new FragmentTraining();


                break;
            }
            case R.id.nav_friends:{
                Toast.makeText(this, "Update to premium for this feature.", Toast.LENGTH_SHORT).show();
                //fragment = new FragmentFriends();


                break;
            }
            case R.id.nav_help:{
                if(!inFragment.equals("Help")){
                    inFragment = "Help";
                    fragment = new FragmentHelp();
                    changeFragment = true;
                }
                else
                    changeFragment = false;

                break;
            }
            case R.id.nav_about:{
                if(!inFragment.equals("About")){
                    inFragment = "About";
                    fragment = new FragmentAbout();
                    changeFragment = true;
                }
                else
                    changeFragment = false;

                break;
            }
            default:

                break;
        }


        navDrawer.closeDrawer(GravityCompat.START);
        return true;
    }


//
//    /*
//     * Called when invalidateOptionsMenu() is triggered
//     */
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        // if nav drawer is opened, hide the action items
//        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerPane);
//        //menu.findItem(R.id.profile).setVisible(!drawerOpen);
//        //MenuItem username = menu.findItem(R.id.username);
//        //Intent intent = getIntent();
//        //String uname = intent.getStringExtra("username");
//        String usernamestr = SaveSharedPreference.getEmail(ActivityMain.this);
//        //setOptionTitle("username",uname);
//        //username.setTitle(usernamestr);
//
//        //Adding Picture on profile layout
//        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.profilepicture);
//        Bitmap b = getImageBitmap(this,usernamestr,"jpeg");
//        circleImageView.setImageBitmap(b);
//
//
//
//        //System.out.println(uname+intent.getStringExtra("email"));
//        //MenuItem email = menu.findItem(R.id.email);
//        String emailstr = SaveSharedPreference.getEmail(ActivityMain.this);
//        //email.setTitle(intent.getStringExtra("email"));
//        //email.setTitle(emailstr);
//        return super.onPrepareOptionsMenu(menu);
//    }


    @Override
    public boolean onNavigateUp()
    {
        getFragmentManager().popBackStack();
        return true;
    }


    @Override
    public void setTitle(CharSequence title) {

        getSupportActionBar().setTitle(title);
//      getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + title + "</font>")));

    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
//        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
//        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void viewProfile(View v){
        Intent intent = new Intent(ActivityMain.this, ActivityProfile.class);
        startActivity(intent);

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





}


