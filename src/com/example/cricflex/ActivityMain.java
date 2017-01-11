package com.example.cricflex;

import java.io.FileInputStream;
import java.util.ArrayList;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.util.Log;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityMain extends ActionBarActivity {


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }

    ListView mDrawerList;
    RelativeLayout mDrawerPane;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    ArrayList<NavDrawerItem> navDrawerItems = new ArrayList<NavDrawerItem>();

    private TextView usernameText  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        int abc = SaveSharedPreference.getUserName(ActivityMain.this).length();

        if(SaveSharedPreference.getUserName(ActivityMain.this).length() == 0)
        {
            Intent intent = new Intent(ActivityMain.this, ActivitySplash.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();

            startActivity(intent);
//            this.finish();
//            finish();
////            (Activity).finish();
//            ActivityMain.this.finish();
//
////            onDestroy();
//            return;

        }

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);



        setContentView(R.layout.activity_main);


        usernameText = (TextView) findViewById(R.id.userName);
        usernameText.setText(SaveSharedPreference.getUserName(ActivityMain.this));



        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.argb(50, 0, 0, 0)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#50000000")));




        // Home
        navDrawerItems.add(new NavDrawerItem("Home", R.drawable.home));
        // History
        navDrawerItems.add(new NavDrawerItem("History", R.drawable.history));
        // Awards
        navDrawerItems.add(new NavDrawerItem("Awards", R.drawable.award));
        // Training
        navDrawerItems.add(new NavDrawerItem("Training", R.drawable.coach));
        // Friends
        navDrawerItems.add(new NavDrawerItem("Friends", R.drawable.friends));
        // Help
        navDrawerItems.add(new NavDrawerItem("Help", R.drawable.help));
        // About
        navDrawerItems.add(new NavDrawerItem("About", R.drawable.about));

        //Drawer Layout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        // Populate the Navigtion Drawer with options

        mDrawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        NavDrawerListAdapter adapter = new NavDrawerListAdapter(this, navDrawerItems);
        mDrawerList.setAdapter(adapter);


        // Drawer Item click listeners
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItemFromDrawer(position);
            }
        });


        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
//                Log.d(TAG, "onDrawerClosed: " + getTitle());

                invalidateOptionsMenu();
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            // on first time display view for first nav item
            selectItemFromDrawer(0);
        }


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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {



            case R.id.tour_cricflex:

                Intent intent0 = new Intent(ActivityMain.this, TestActivityMetrics.class);

                Bundle extras = new Bundle();
                extras.putString("id", "1");
                extras.putString("city", "2");
                extras.putString("place", "3");
                extras.putString("station", "4");
                intent0.putExtras(extras);
                startActivity(intent0);
                return true;

            case R.id.calibrate:

                Intent intent1 = new Intent(ActivityMain.this, ActivityCalibrate.class);
                startActivity(intent1);
                return true;


            case R.id.settings:


                Intent intent2 = new Intent(ActivityMain.this, test_asawal.class);
                startActivity(intent2);
                return true;



            case R.id.logout:

                Intent intent3 = new Intent(ActivityMain.this, ActivityLogin.class);
                //ActivityMain.this.finish();


                SaveSharedPreference.clearUserName(ActivityMain.this);

                intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent3);
                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }



    /*
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerPane);
        //menu.findItem(R.id.profile).setVisible(!drawerOpen);
        //MenuItem username = menu.findItem(R.id.username);
        //Intent intent = getIntent();
        //String uname = intent.getStringExtra("username");
        String usernamestr = SaveSharedPreference.getUserName(ActivityMain.this);
        //setOptionTitle("username",uname);
        //username.setTitle(usernamestr);

        //Adding Picture on profile layout
        CircleImageView circleImageView = (CircleImageView) findViewById(R.id.profilepicture);
        Bitmap b = getImageBitmap(this,usernamestr,"jpeg");
        circleImageView.setImageBitmap(b);



        //System.out.println(uname+intent.getStringExtra("email"));
        //MenuItem email = menu.findItem(R.id.email);
        String emailstr = SaveSharedPreference.getEmail(ActivityMain.this);
        //email.setTitle(intent.getStringExtra("email"));
        //email.setTitle(emailstr);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void selectItemFromDrawer(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {

            case 0:
                fragment = new FragmentHome();
                break;
            case 1:
                fragment = new FragmentHistory();
                break;
            case 2:
                Toast.makeText(this, "Update to premium for this feature.", Toast.LENGTH_SHORT).show();
                //fragment = new FragmentAwards();
                break;
            case 3:

                Toast.makeText(this, "Update to premium for this feature.", Toast.LENGTH_SHORT).show();
                //fragment = new FragmentTraining();
                break;
            case 4:
                Toast.makeText(this, "Update to premium for this feature.", Toast.LENGTH_SHORT).show();
                //fragment = new FragmentFriends();
                break;
            case 5:
                fragment = new FragmentHelp();
                break;
            case 6:
                fragment = new FragmentAbout();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navDrawerItems.get(position).getTitle());
            mDrawerLayout.closeDrawer(mDrawerPane);
        } else {
            // error in creating fragment
            Log.e("ActivityMain", "Error in creating fragment");
        }
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
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void viewProfile(View v){
        Fragment fragment = null;
        fragment = new FragmentProfile();
        Bundle bundle = new Bundle();
        String username = SaveSharedPreference.getUserName(ActivityMain.this);
        bundle.putString("username", username);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        setTitle(R.string.profile);

        mDrawerList.setItemChecked(mDrawerList.getCheckedItemPosition(), false);
        mDrawerLayout.closeDrawer(mDrawerPane);



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


