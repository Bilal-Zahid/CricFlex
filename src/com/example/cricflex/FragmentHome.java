package com.example.cricflex;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class FragmentHome extends Fragment {

    public FragmentHome(){}
    private ArcProgress successRate;
    private Button startButton;
    private TextView totalBalls;
    private TextView averageAngle;
    private TextView lastBowlAngle;
    private TextView longestStreak;
    // For Displaying Player Stats

    private String username;
    private String legalBalls,illegalBalls;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        username = SaveSharedPreference.getUserName(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        totalBalls = (TextView) rootView.findViewById(R.id.home_totalballs);
        averageAngle = (TextView) rootView.findViewById(R.id.home_average_angle);
        lastBowlAngle = (TextView) rootView.findViewById(R.id.home_last_angle);
        longestStreak = (TextView) rootView.findViewById(R.id.home_longest_streak);
        successRate = (ArcProgress) rootView.findViewById(R.id.home_legal_to_illegal_ratio);

        //arcProgress.setProgress(50);
        startButton = (Button) rootView.findViewById(R.id.home_start);

        legalBalls = helper.getLegalCount(username);
        illegalBalls = helper.getIllegalCount(username);

        int totalBowls;
        totalBowls = Integer.valueOf(legalBalls) + Integer.valueOf(illegalBalls);

        totalBalls.setText(Integer.toString(totalBowls));

        successRate.setProgress(totalBowls);

        float ratio = (float)Integer.valueOf(legalBalls) / (float) totalBowls;
        ratio *= 100;

        int successRateValue = (int)ratio;


        String angleValues = helper.getAngleValues(username);

        System.out.println("Angle Values in Home Screen: "+ angleValues);

        ArrayList<String> list = new ArrayList<String>();

        if(!angleValues.equals("")) {
            // Getting Arraylist back
            JSONObject json1 = null;
            try {
                json1 = new JSONObject(angleValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray = json1.optJSONArray("angleArray");
            ;
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        list.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            Double[] doubleAngleValues = new Double[list.size()];

            Double sum = 0.0;
            for (int i = 0; i < list.size(); i++) {
                doubleAngleValues[i] = Double.valueOf(list.get(i));
                sum += doubleAngleValues[i];
            }

            Double averageAngleOfPlayer;
            if(list.size()!=0)
                averageAngleOfPlayer = sum / (double) list.size();
            else
                averageAngleOfPlayer = 0.0;

            DecimalFormat df = new DecimalFormat("#.##");
            df.setRoundingMode(RoundingMode.HALF_UP);


            //System.out.println("Angle Values in Home Screen 2: : " + list);
            //int[] longestStreak = new int[list.size()];

            int longestStreakCount = 0;
            int noOfStreaks = 0;
            int maxStreak = 0;

            for (int i = 0; i < list.size(); i++) {
                if (doubleAngleValues[i] <= 15.0) {
                    longestStreakCount++;
                }
                if (doubleAngleValues[i] > 15.0 || i == list.size()-1) {
                    if (maxStreak < longestStreakCount) {
                        maxStreak = longestStreakCount;
                    }
                    //longestStreak[noOfStreaks++] = longestStreakCount;
                    longestStreakCount = 0;
                }
            }


            longestStreak.setText(String.valueOf(maxStreak));

            averageAngle.setText(String.valueOf(df.format(averageAngleOfPlayer)) + "\u00b0");

            if(doubleAngleValues.length!=0)
                lastBowlAngle.setText(String.valueOf(doubleAngleValues[doubleAngleValues.length - 1].intValue()) + "\u00b0");
            else
                lastBowlAngle.setText("0" + "\u00b0");

            successRate.setProgress(successRateValue);
        }
        else {
            longestStreak.setText("0");
            averageAngle.setText("0" + "\u00b0");
            lastBowlAngle.setText("0" + "\u00b0");
            successRate.setProgress(0);
        }
        startButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ActivityMonitor.class);
                startActivity(intent);
            }
        });


        return rootView;
    }
}
