package com.example.cricflex;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import me.grantland.widget.AutofitTextView;

import static android.content.ContentValues.TAG;

public class FragmentHome extends Fragment {

    public FragmentHome(){}
    private ArcProgress successRate;
    private Button startButton;
    private TextView totalBalls;
    private AutofitTextView averageAngle;
    private AutofitTextView averageForce;
    private AutofitTextView averageArmTwist;
    private AutofitTextView averageActionTime;
    private String email;
    private String legalBalls,illegalBalls;


    //Firebase Work
    AllMetrics allMetricsInMonitor;

    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        DatabaseHelper helper = new DatabaseHelper(getActivity());
        email = SaveSharedPreference.getEmail(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        totalBalls = (TextView) rootView.findViewById(R.id.home_totalballs);
        averageAngle = (AutofitTextView) rootView.findViewById(R.id.home_average_angle);
        averageForce = (AutofitTextView) rootView.findViewById(R.id.home_force);
        averageActionTime = (AutofitTextView) rootView.findViewById(R.id.home_action_time);
        averageArmTwist = (AutofitTextView) rootView.findViewById(R.id.home_arm_twist);
        successRate = (ArcProgress) rootView.findViewById(R.id.home_legal_to_illegal_ratio);

        startButton = (Button) rootView.findViewById(R.id.home_start);

//        legalBalls = helper.getLegalCount(email);
//        illegalBalls = helper.getIllegalCount(email);

//        int totalBowls;
//        totalBowls = Integer.valueOf(legalBalls) + Integer.valueOf(illegalBalls);

//        totalBalls.setText(Integer.toString(totalBowls));




//        float ratio = (float)Integer.valueOf(legalBalls) / (float) totalBowls;
//        ratio *= 100;
//
//        int successRateValue = (int)ratio;



        //Firebase Work

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        ValueEventListener AllMetricsListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get AllMetrics object and use the values to update the UI
                // ...


//                System.out.println("Datasnapshot mai ara hai : ");
//                User playerProfile = new User();
//
//                playerProfile = dataSnapshot.getValue(User.class);
//
//
//                if(playerProfile==null){
//                    System.out.println("Cant fetch data");
//                    return;
//                }

                System.out.println("Datasnapshot mai ara hai : ");
                AllMetrics allMetrics = new AllMetrics();

                allMetrics = dataSnapshot.getValue(AllMetrics.class);


                if(allMetrics==null){
                    System.out.println("Cant fetch data");
                    allMetricsInMonitor = null;
                    return;
                }
                else{
                    allMetricsInMonitor = allMetrics;

                    int allBalls = allMetrics.legalBalls+allMetrics.illegalBalls;

                    totalBalls.setText(Integer.toString(allBalls));


                    float ratio = (float)allMetrics.legalBalls / (float) allBalls;
                    ratio *= 100;

                    int successRateValue = (int)ratio;
                    successRate.setProgress(successRateValue);


                    DecimalFormat df = new DecimalFormat("#.#");
                    df.setRoundingMode(RoundingMode.HALF_UP);



                    String angle = String.valueOf(df.format(calculateAverageOfIntegerMetrics(allMetrics.angleValues))) + "\u00b0";
                    averageAngle.setText(angle);
                    averageForce.setText(String.valueOf(df.format(calculateAverageOfIntegerMetrics(allMetrics.forceValues))) + " N");
                    averageArmTwist.setText(String.valueOf(df.format(calculateAverageOfIntegerMetrics(allMetrics.armTwistValues))) + "\u00b0");


                    DecimalFormat df1 = new DecimalFormat("#.##");
                    df1.setRoundingMode(RoundingMode.HALF_UP);
                    averageActionTime.setText(String.valueOf(df1.format(calculateAverageOfActionTime(allMetrics.actionTimeValues))) + " s");


                    System.out.println("Angle values from firebase: " + allMetricsInMonitor.angleValues);



                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };

        DatabaseReference allMetricsRef = databaseReference.child("AllMetrics")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        allMetricsRef.addValueEventListener(AllMetricsListener);


        startButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ActivityMonitor.class);
                startActivity(intent);
            }
        });


        return rootView;





        // For Average Angle
//        String angleValues = helper.getAngleValues(email);
//        ArrayList<String> list = new ArrayList<String>();
//        if(!angleValues.equals("")) {
//            // Getting Arraylist back
//            JSONObject json1 = null;
//            try {
//                json1 = new JSONObject(angleValues);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            JSONArray jsonArray = json1.optJSONArray("angleArray");
//            if (jsonArray != null) {
//                int len = jsonArray.length();
//                for (int i = 0; i < len; i++) {
//                    try {
//                        list.add(jsonArray.get(i).toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            Double[] doubleAngleValues = new Double[list.size()];
//            Double sum = 0.0;
//            for (int i = 0; i < list.size(); i++) {
//                doubleAngleValues[i] = Double.valueOf(list.get(i));
//                sum += doubleAngleValues[i];
//            }
//            Double averageAngleOfPlayer;
//            if(list.size()!=0)
//                averageAngleOfPlayer = sum / (double) list.size();
//            else
//                averageAngleOfPlayer = 0.0;
//            DecimalFormat df = new DecimalFormat("#.##");
//            df.setRoundingMode(RoundingMode.HALF_UP);
//
//
//            averageAngle.setText(String.valueOf(df.format(averageAngleOfPlayer)) + "\u00b0");
//            successRate.setProgress(successRateValue);
//        }
//        else {
//            averageAngle.setText("0" + "\u00b0");
//            successRate.setProgress(0);
//        }
//
//
//
//
//
//
//
//
//
//        //For Average Force
//        String forceValues = helper.getForceValues(email);
//        System.out.println("Force Values in Fragment Home: "  + forceValues);
//        ArrayList<String> list1 = new ArrayList<String>();
//        if(!forceValues.equals("")) {
//            // Getting Arraylist1 back
//            JSONObject json1 = null;
//            try {
//                json1 = new JSONObject(forceValues);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            JSONArray jsonArray = json1.optJSONArray("forceArray");
//            if (jsonArray != null) {
//                int len = jsonArray.length();
//                for (int i = 0; i < len; i++) {
//                    try {
//                        list1.add(jsonArray.get(i).toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            Double[] doubleForceValues = new Double[list1.size()];
//            Double sum = 0.0;
//            for (int i = 0; i < list1.size(); i++) {
//                doubleForceValues[i] = Double.valueOf(list1.get(i));
//                sum += doubleForceValues[i];
//            }
//            Double averageForceOfPlayer;
//            if(list1.size()!=0)
//                averageForceOfPlayer = sum / (double) list1.size();
//            else
//                averageForceOfPlayer = 0.0;
//            DecimalFormat df = new DecimalFormat("#.##");
//            df.setRoundingMode(RoundingMode.HALF_UP);
//
//
//            averageForce.setText(String.valueOf(df.format(averageForceOfPlayer)) + " N");
//        }
//        else {
//            averageForce.setText("0" + " N");
//        }
//
//
//
//
//        // for Arm Twist
//        String armTwistValues = helper.getArmTwistValues(email);
//        calculateAverageValuesOfParameters(averageArmTwist,armTwistValues,"armTwistArray","\u00b0");
//
//
//        // for Average Action Time
//
//        String actionTimeValues = helper.getActionTimeValues(email);
//        calculateAverageValuesOfParameters(averageActionTime,actionTimeValues,"actionTimeArray"," s");


    }

    private double calculateAverageOfIntegerMetrics(ArrayList <Integer> metricValues) {
        Integer sum = 0;
        if(!metricValues.isEmpty()) {
            for (Integer mark : metricValues) {
                sum += mark;
            }
            return sum.doubleValue() / metricValues.size();
        }
        return sum;
    }

    private double calculateAverageOfActionTime(ArrayList <Float> metricValues) {
        Float sum = new Float(0);
        if(!metricValues.isEmpty()) {
            for (Float mark : metricValues) {
                sum += mark;
            }
            return sum.doubleValue() / metricValues.size();
        }
        return sum;
    }

//    public void calculateAverageValuesOfParameters(TextView metricToChange, String values, String jsonArrayName , String sign){
//
//
//        ArrayList<String> list1 = new ArrayList<String>();
//
//        if(!values.equals("")) {
//            // Getting Arraylist1 back
//            JSONObject json1 = null;
//            try {
//                json1 = new JSONObject(values);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            JSONArray jsonArray = json1.optJSONArray(jsonArrayName);
//            if (jsonArray != null) {
//                int len = jsonArray.length();
//                for (int i = 0; i < len; i++) {
//                    try {
//                        list1.add(jsonArray.get(i).toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            Double[] doubleForceValues = new Double[list1.size()];
//            Double sum = 0.0;
//            for (int i = 0; i < list1.size(); i++) {
//                doubleForceValues[i] = Double.valueOf(list1.get(i));
//                sum += doubleForceValues[i];
//            }
//            Double averageForceOfPlayer;
//            if(list1.size()!=0)
//                averageForceOfPlayer = sum / (double) list1.size();
//            else
//                averageForceOfPlayer = 0.0;
//            DecimalFormat df = new DecimalFormat("#.##");
//            df.setRoundingMode(RoundingMode.HALF_UP);
//
//            metricToChange.setText(String.valueOf(df.format(averageForceOfPlayer)) + sign);
//
//        }
//        else {
//            metricToChange.setText("0" + sign);
//        }
//
//
//    }


}
