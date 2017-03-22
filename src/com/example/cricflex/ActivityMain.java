package com.example.cricflex;

import java.io.FileInputStream;

import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.app.Fragment;
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
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;


public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {


    Boolean inHome = true;
    Boolean inHistory = false;
    Boolean inAwards = false;
    Boolean inTraining = false;
    Boolean inFriends = false;
    Boolean inHelp = false;
    Boolean inAbout = false;

    DatabaseHelper helper = new DatabaseHelper(this);
//    TextView mainEmail;
    private TextView emailText ;
    private TextView nameText;
    Fragment fragment = null;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int abc = SaveSharedPreference.getEmail(ActivityMain.this).length();


        if(SaveSharedPreference.getEmail(ActivityMain.this).length() == 0)
        {
            Intent intent = new Intent(ActivityMain.this, ActivitySplash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
            startActivity(intent);
        }

        View inflatedView = getLayoutInflater().inflate(R.layout.nav_header_main, null);
        emailText = (TextView) inflatedView.findViewById(R.id.email);
        emailText.setText(SaveSharedPreference.getEmail(ActivityMain.this));
        nameText = (TextView) inflatedView.findViewById(R.id.name);
        nameText.setText(R.string.name);            //////////////////////////////////////////////////// from database

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if(inHome)
        {
            fragment = new FragmentHome();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            setTitle("Home");
            navigationView.setCheckedItem(R.id.nav_home);

        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }

        else if(!drawer.isDrawerOpen(GravityCompat.START) && inHome){
            moveTaskToBack(true);
        }

        else if(inHistory || inAwards || inTraining || inFriends || inFriends || inHelp || inAbout){

            fragment = new FragmentHome();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            setTitle("Home");
            navigationView.setCheckedItem(R.id.nav_home);
            inHome = true;
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

                Intent intent0 = new Intent(ActivityMain.this, ActivityWelcome.class);

                startActivity(intent0);
                return true;

            case R.id.calibrate:

                Intent intent1 = new Intent(ActivityMain.this, ActivityCalibrate.class);
                startActivity(intent1);
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

        inHome = false;


        if (id == R.id.nav_home) {
            fragment = new FragmentHome();
            inHome = true;
            inHistory = false;
            inAwards = false;
            inTraining = false;
            inFriends = false;
            inHelp = false;
            inAbout = false;

        }
        else if (id == R.id.nav_history) {
            fragment = new FragmentHistory();

            inHome = false;
            inHistory = true;
            inAwards = false;
            inTraining = false;
            inFriends = false;
            inHelp = false;
            inAbout = false;
        }

        else if (id == R.id.nav_awards) {
            Toast.makeText(this, "Update to premium for this feature.", Toast.LENGTH_SHORT).show();
            //fragment = new FragmentAwards();

            inHome = false;
            inHistory = false;
            inAwards = true;
            inTraining = false;
            inFriends = false;
            inHelp = false;
            inAbout = false;

            return true;
        }

        else if (id == R.id.nav_training) {
            Toast.makeText(this, "Update to premium for this feature.", Toast.LENGTH_SHORT).show();
            //fragment = new FragmentTraining();

            inHome = false;
            inHistory = false;
            inAwards = false;
            inTraining = true;
            inFriends = false;
            inHelp = false;
            inAbout = false;

            return true;
        }

        else if (id == R.id.nav_friends) {
            Toast.makeText(this, "Update to premium for this feature.", Toast.LENGTH_SHORT).show();
            //fragment = new FragmentFriends();

            inHome = false;
            inHistory = false;
            inAwards = false;
            inTraining = false;
            inFriends = true;
            inHelp = false;
            inAbout = false;

            return true;
        }

        else if (id == R.id.nav_help) {
            fragment = new FragmentHelp();

            inHome = false;
            inHistory = false;
            inAwards = false;
            inTraining = false;
            inFriends = false;
            inHelp = true;
            inAbout = false;

        }

        else if (id == R.id.nav_about) {
            fragment = new FragmentAbout();

            inHome = false;
            inHistory = false;
            inAwards = false;
            inTraining = false;
            inFriends = false;
            inHelp = false;
            inAbout = true;

        }


        if (fragment != null) {

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();

            setTitle(item.getTitle());
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // error in creating fragment
            Log.e("ActivityMain", "Error in creating fragment");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
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
//        mTitle = title;
        //getSupportActionBar().setTitle(title);
        getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#FFFFFF\">" + title + "</font>")));

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


