package com.example.cricflex;




import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.ViewSwitcher;

public class FragmentHistory extends Fragment {

    ViewSwitcher switcher;
    Button Daily, Monthly;

    Boolean daily_tab_selected = true;
    Boolean monthly_tab_selected = false;

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


        Daily.setPressed(true);         // Daily tab selected initially
        daily_tab_selected=true;


        monthly_tab_selected=false;


        Daily.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(!daily_tab_selected)
                {
                    daily_tab_selected=true;
                    monthly_tab_selected=false;
                    switcher.showNext();
                }
//                switcher.showNext();
                Monthly.setPressed(false);
                Daily.setPressed(true);
                return true;
            }
        });

        Monthly.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(!monthly_tab_selected)
                {
                    switcher.showPrevious();
                    daily_tab_selected=false;
                    monthly_tab_selected=true;
                }
                Monthly.setPressed(true);
                Daily.setPressed(false);
                return true;
            }
        });
        return rootView;
    }



}