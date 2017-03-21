package com.example.cricflex;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityCalibrate extends Activity {


    private Button nextButton1;
    private Button nextButton2;
    private Button nextButton3;
    private Button doneButton4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        getActionBar().hide();

        setContentView(R.layout.activity_calibrate1);

        PlayGifView pGif = (PlayGifView) findViewById(R.id.arm_straight_gifView);
        pGif.setImageResource(R.drawable.arm_straight);




        nextButton1 = (Button) findViewById(R.id.cali1_next);
        nextButton1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                setContentView(R.layout.activity_calibrate2);
                initializeSecondLayout();
            }

        });


    }

    private void initializeSecondLayout(){

        PlayGifView pGif = (PlayGifView) findViewById(R.id.arm_bend_gifView);
        pGif.setImageResource(R.drawable.arm_bend_gif);

        nextButton2 = (Button) findViewById(R.id.cali2_next);
        nextButton2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                setContentView(R.layout.activity_calibrate3);
                initializeThirdLayout();
            }

        });

    }
    private void initializeThirdLayout(){

        PlayGifView pGif = (PlayGifView) findViewById(R.id.arm_twistLeft_gifView);
        pGif.setImageResource(R.drawable.arm_bend_gif);

        nextButton3 = (Button) findViewById(R.id.cali3_next);
        nextButton3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                setContentView(R.layout.activity_calibrate4);
                initializeFourthLayout();
            }

        });

    }

    private void initializeFourthLayout(){

        PlayGifView pGif = (PlayGifView) findViewById(R.id.arm_twistRight_gifView);
        pGif.setImageResource(R.drawable.arm_bend_gif);

        doneButton4 = (Button) findViewById(R.id.cali4_done);
        doneButton4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent i = new Intent(ActivityCalibrate.this, ActivityMain.class);
                ActivityCalibrate.this.startActivity(i);
            }

        });

    }

}
