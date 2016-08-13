package com.example.cricflex;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentHome extends Fragment {

    public FragmentHome(){}


    private ArcProgress arcProgress;








    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        arcProgress = (ArcProgress) rootView.findViewById(R.id.arc_progress);
        arcProgress.setProgress(45);

        return rootView;
    }
}
