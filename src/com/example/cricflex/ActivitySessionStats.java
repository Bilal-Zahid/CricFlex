package com.example.cricflex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ActivitySessionStats extends AppCompatActivity {


    private Button endSessionButton;


    ArrayList<Integer> angleValues = new ArrayList<Integer>();
    ArrayList<Integer> forceValues = new ArrayList<Integer>();
    ArrayList<Integer> armTwistValues = new ArrayList<Integer>();
    ArrayList<Float> actionTimeValues = new ArrayList<Float>();

    TextView averageAngle;
    TextView averageArmTwist;
    TextView averageActionTime;
    TextView averageForce;


    LinearLayout angleGraph ;     ///////////////////////////////////
    LinearLayout forceGraph;
    LinearLayout armTwistGraph;
    LinearLayout actionTimeGraph;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_stats);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().hide();

        final Intent intent = getIntent();
        final Bundle extraBundle = intent.getExtras();

        angleValues = extraBundle.getIntegerArrayList("angleValues");
        forceValues = extraBundle.getIntegerArrayList("forceValues");
        armTwistValues = extraBundle.getIntegerArrayList("armTwistValues");
        actionTimeValues = (ArrayList<Float>) getIntent().getSerializableExtra("actionTimeValues");

        endSessionButton = (Button)findViewById(R.id.session_finish_button);

        averageAngle = (TextView) findViewById(R.id.session_angle);
        averageForce = (TextView) findViewById(R.id.session_force);
        averageActionTime = (TextView) findViewById(R.id.session_time);
        averageArmTwist = (TextView) findViewById(R.id.session_twist);


        angleGraph = (LinearLayout) findViewById(R.id.tile_angle); //////////////////////////////////
        forceGraph = (LinearLayout) findViewById(R.id.tile_force);
        actionTimeGraph = (LinearLayout) findViewById(R.id.tile_time);
        armTwistGraph = (LinearLayout) findViewById(R.id.tile_twist);

        angleGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivitySessionStats.this, ActivitySessionGraph.class);
                Bundle extraBundle = new Bundle();
                extraBundle.putIntegerArrayList("values", angleValues);
                i.putExtra("parameter", "Arm Angle");
                i.putExtras(extraBundle);
                ActivitySessionStats.this.startActivity(i);
            }
        });

        forceGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivitySessionStats.this, ActivitySessionGraph.class);
                Bundle extraBundle = new Bundle();
                extraBundle.putIntegerArrayList("values", forceValues);
                i.putExtra("parameter", "Force");
                i.putExtras(extraBundle);
                ActivitySessionStats.this.startActivity(i);
            }
        });

        armTwistGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivitySessionStats.this, ActivitySessionGraph.class);
                Bundle extraBundle = new Bundle();
                extraBundle.putIntegerArrayList("values", armTwistValues);
                i.putExtra("parameter", "Arm Twist");
                i.putExtras(extraBundle);
                ActivitySessionStats.this.startActivity(i);
            }
        });

        actionTimeGraph.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ActivitySessionStats.this, ActivitySessionGraphActionTime.class);
                i.putExtra("actionTimeValues", actionTimeValues);
                i.putExtra("parameter", "Action Time");
                i.putExtras(extraBundle);
                ActivitySessionStats.this.startActivity(i);
            }
        });



        DecimalFormat dfForAngleAndArmTwist = new DecimalFormat("#.#");
        dfForAngleAndArmTwist.setRoundingMode(RoundingMode.HALF_UP);

        DecimalFormat dfForForce = new DecimalFormat("#");
        dfForForce.setRoundingMode(RoundingMode.HALF_UP);

        DecimalFormat dfForActionTime = new DecimalFormat("#.###");
        dfForActionTime.setRoundingMode(RoundingMode.HALF_UP);



        averageAngle.setText(String.valueOf(dfForAngleAndArmTwist.format(calculateAverageForIntegerArray(angleValues))) + "\u00b0");
        averageForce.setText(String.valueOf(dfForForce.format(calculateAverageForIntegerArray(forceValues))) + " N");
        averageActionTime.setText(String.valueOf(dfForActionTime.format(calculateAverage(actionTimeValues))) + " s");
        averageArmTwist.setText(String.valueOf(dfForAngleAndArmTwist.format(calculateAverageForIntegerArray(armTwistValues))) + "\u00b0");


        endSessionButton.setOnClickListener(new handleEndSessionButton());

    }



    public float calculateAverageForIntegerArray (ArrayList<Integer> values){



        if(values.size()==0){
            return 0;
        }
        float average =0;

        for(int i=0;i<values.size();i++){
            average += values.get(i);
        }

        average = average/values.size();



        return average;
    }

    public float calculateAverage (ArrayList<Float> values){

        if(values.size()==0){
            return 0;
        }
        float average =0;

        for(int i=0;i<values.size();i++){
            average += values.get(i);
        }

        average = average/values.size();



        return average;
    }

    class handleEndSessionButton implements View.OnClickListener {
        public void onClick(View v) {

            Intent i = new Intent(ActivitySessionStats.this, ActivityMain.class);
            ActivitySessionStats.this.startActivity(i);


        }
    }
}
