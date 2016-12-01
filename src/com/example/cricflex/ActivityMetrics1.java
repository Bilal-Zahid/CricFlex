package com.example.cricflex;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by abcd on 10/31/2016.
 */

public class ActivityMetrics1 extends Activity {

    TextView armAngle;
//    TextView armSpeed;
    TextView actionTime;
    TextView armTwist;
    TextView force;
//    TextView runUpDist;
//    TextView runUpSpeed;
//    TextView runUpTime;
    
//    String armAngle_value;
//    String armSpeed_value;
//    String acttionTime_value;
//    String armTwist_value;
//    String force_value;
//    String runUpDist_value;
//    String runUpSpeed_value;
//    String runUpTime_value;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.activity_metrics1);

        armAngle = (TextView) findViewById(R.id.arm_angle);
//        armSpeed = (TextView) findViewById(R.id.arm_speed);
        actionTime = (TextView) findViewById(R.id.action_time);
        armTwist = (TextView) findViewById(R.id.arm_twist);
        force = (TextView) findViewById(R.id.force);
//        runUpDist = (TextView) findViewById(R.id.runup_distance);
//        runUpSpeed = (TextView) findViewById(R.id.runup_speed);
//        runUpTime = (TextView) findViewById(R.id.runup_time);




        Bundle extras = getIntent().getExtras();
        String a = extras.getString("armAngle");
//        String b = extras.getString("armSpeed");
        String c = extras.getString("actionTime");
        String d = extras.getString("armTwist");
        String e = extras.getString("force");



        armAngle.setText(a+" \u00b0");
//        armSpeed.setText(b+" kph");
        actionTime.setText(c+" sec");
        armTwist.setText(d+" \u00b0");
        force.setText(e+" N");

    }

//    public void setValues(      String armAngle_value,
//                                String armSpeed_value,
//                                String actionTime_value,
//                                String armTwist_value,
//                                String force_value,
//                                String runUpDist_value,
//                                String runUpSpeed_value,
//                                String runUpTime_value) {
//
//
//        armAngle.setText(armAngle_value+" \u00b0");
//        armSpeed.setText(armSpeed_value+" kph");
//        actionTime.setText(actionTime_value+" sec");
//        armTwist.setText(armTwist_value+" \u00b0");
//        force.setText(force_value+" N");
//        runUpDist.setText(runUpDist_value+" meter");
//        runUpSpeed.setText(runUpSpeed_value+" kph");
//        runUpTime.setText(runUpTime_value+" sec");
//
//
//
//    }
}
