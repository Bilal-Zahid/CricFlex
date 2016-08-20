package com.example.cricflex;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentHome extends Fragment {

    public FragmentHome(){}


    private ArcProgress arcProgress;
    private Button startButton;








    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        arcProgress = (ArcProgress) rootView.findViewById(R.id.arc_progress);
        arcProgress.setProgress(50);
        startButton = (Button) rootView.findViewById(R.id.home_start);


        startButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ActivityMonitor.class);
                startActivity(intent);
            }
        });


        return rootView;
    }
}
