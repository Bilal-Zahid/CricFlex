package com.example.cricflex;




import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ViewSwitcher;

public class FragmentHistory extends Fragment {

    ViewSwitcher switcher;
    Button Daily, Monthly;

    Boolean daily_tab_selected;
    Boolean monthly_tab_selected;

    public FragmentHistory(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);



        switcher = (ViewSwitcher) rootView.findViewById(R.id.ViewSwitcher);

        Daily = (Button) rootView.findViewById(R.id.daily_button);
        Monthly = (Button) rootView.findViewById(R.id.monthly_button);

        daily_tab_selected=true;
        monthly_tab_selected=false;

        Daily.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub


                if(!daily_tab_selected)
                {
                    switcher.showNext();
                    daily_tab_selected=true;
                    monthly_tab_selected=false;
                }
            }
        });

        Monthly.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(!monthly_tab_selected)
                {
                    switcher.showPrevious();
                    daily_tab_selected=false;
                    monthly_tab_selected=true;
                }
            }
        });

        
        
        



        return rootView;
    }


}