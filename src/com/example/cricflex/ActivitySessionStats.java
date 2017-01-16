package com.example.cricflex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivitySessionStats extends AppCompatActivity {


    private Button endSessionButton;


    ArrayList<Integer> angleValues = new ArrayList<Integer>();
    ArrayList<Integer> forceValues = new ArrayList<Integer>();
    ArrayList<Integer> armTwistValues = new ArrayList<Integer>();
    ArrayList<Float> actionTimeValues = new ArrayList<Float>();

    TextView averageAngle;
    TextView averageArmTwist;
    TextView avarageActionTime;
    TextView averageForce;


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

        endSessionButton = (Button)findViewById(R.id.end_session_button);

        endSessionButton.setOnClickListener(new handleEndSessionButton());

    }

    class handleEndSessionButton implements View.OnClickListener {
        public void onClick(View v) {

            Intent i = new Intent(ActivitySessionStats.this, ActivityMain.class);
            ActivitySessionStats.this.startActivity(i);


        }
    }
}
