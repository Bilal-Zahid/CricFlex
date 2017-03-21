package com.example.cricflex;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;


public class test_asawal extends Activity {


    RadioButton rdbLeft, rdbRight;
    RadioGroup rgBowlingArm;
    private Spinner bowlingStylesSpinner;
    private Spinner careerLevelSpinner;


    String selectedBowlingArm = "left";
    String selectedBowlingStyle = "fast";
    String selectedCareerLevel = "international";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_asawal);
        setTitle("Profile");

        rgBowlingArm = (RadioGroup) findViewById(R.id.rgBowlingArm);
        rdbLeft = (RadioButton) findViewById(R.id.rdbLeft);
        rdbRight = (RadioButton) findViewById(R.id.rdbRight);

        initializeBowlingStylesSpinner(selectedBowlingArm);
        rgBowlingArm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rdbLeft:
                        // do operations specific to this selection
                        selectedBowlingArm = "left";
                        initializeBowlingStylesSpinner(selectedBowlingArm);
                        break;

                    case R.id.rdbRight:
                        // do operations specific to this selection
                        selectedBowlingArm = "right";
                        initializeBowlingStylesSpinner(selectedBowlingArm);
                        break;
                }
            }
        });
        initializeCareerLevelSpinner();



    }

    private void initializeBowlingStylesSpinner(String arm){

        Context context=getApplicationContext();
        bowlingStylesSpinner = (Spinner)findViewById(R.id.bowlingstyle_spinner);
        if(arm.equals("left"))
        {
            String[] bowlingStylesArray = context.getResources().getStringArray(R.array.bowlingstyle_left_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spiner_item, bowlingStylesArray);
            bowlingStylesSpinner.setAdapter(adapter);
        }
        else if(arm.equals("right"))
        {
            String[] bowlingStylesArray = context.getResources().getStringArray(R.array.bowlingstyle_right_array);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spiner_item, bowlingStylesArray);
            bowlingStylesSpinner.setAdapter(adapter);
        }

    }

    private void initializeCareerLevelSpinner(){
        Context context=getApplicationContext();
        String[] careerLevels = context.getResources().getStringArray(R.array.careerlevel_array);
        careerLevelSpinner = (Spinner)findViewById(R.id.careerlevel_spinner);
        //ArrayList<String> careerLevels = new ArrayList( Arrays.asList( R.array.careerlevel_array ) );
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.spiner_item, careerLevels);
        careerLevelSpinner.setAdapter(adapter);
    }
}
