package com.example.cricflex;




import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.GONE;

public class FragmentHistory extends Fragment {

//    ViewSwitcher switcher;
//    Button Daily, Monthly;
//
//    Boolean daily_tab_selected;
//    Boolean monthly_tab_selected;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    private TextView currentMonth;
    private Button prevMonth;
    private Button prevYear;
    private Button nextMonth;
    private Button nextYear;

    private Calendar _calendar;
    private int month, year;
    //private final DateFormat dateFormatter = new DateFormat();
    private static final String dateTemplate = "MMMM yyyy";

    private String dateForDatabase = "";

//    DatabaseHelper helper;

    DatabaseHelper helper;
    View rootView;


    ArrayList<Integer> angleValues = new ArrayList<Integer>();

    public FragmentHistory(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_history, container, false);


        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH);
        year = _calendar.get(Calendar.YEAR);





        prevYear = (Button) rootView.findViewById(R.id.prevYear);
        prevYear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                year--;
                setGridCellAdapterToDate(month, year);
            }
        });

        prevMonth = (Button) rootView.findViewById(R.id.prevMonth);
        prevMonth.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
//                Log.e(TAG, "prevMonth pressed");
                if (month <= 1) {
                    month = 12;
                    year--;
                } else {
                    month--;
                }
                setGridCellAdapterToDate(month, year);
            }
        });

        currentMonth = (TextView) rootView.findViewById(R.id.currentMonth);
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));

        nextMonth = (Button) rootView.findViewById(R.id.nextMonth);
        nextMonth.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

//                Log.e(TAG, "nextMonth pressed");
                if (month > 11) {
                    month = 1;
                    year++;
                } else {
                    month++;
                }
                setGridCellAdapterToDate(month, year);
            }
        });

        nextYear = (Button) rootView.findViewById(R.id.nextYear);
        nextYear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                year++;
                setGridCellAdapterToDate(month, year);
            }
        });


        setGridCellAdapterToDate(month, year);



        //testing date

//testing date format
        Date curDate = new Date();
//            SimpleDateFormat format = new SimpleDateFormat();
        SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
        String DateToStr = format.format(curDate);
//        if()

        System.out.println("Date in Fragment history: "+ currentMonth.getText().toString());

        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressStatus = 50;
        mProgress.setProgress(mProgressStatus);

        makeGraph();

        return rootView;
    }


    private void makeGraph(){

        LineChart lineChart = (LineChart) rootView.findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<Entry>();

        /////////////////////////////////////////////////////////////
        //Code for getting angle values
        helper = new DatabaseHelper(getActivity());
        String username = SaveSharedPreference.getUserName(getActivity());
//        String angleValues = helper.getAngleValues(username);
        //Code for getting angle values
//        helper = new DatabaseHelper(getActivity());
//        String  = SaveSharedPreference.getUserName(getActivity());
        String angleValuesWithDate = helper.getAngleValuesWithDate(username,currentMonth.getText().toString());


        //making arraylist after getting response from database
        ArrayList<String> list1 = new ArrayList<String>();
        if(!angleValuesWithDate.equals("not found")) {
            // Getting Arraylist back
            JSONObject json1 = null;
            try {
                json1 = new JSONObject(angleValuesWithDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray = json1.optJSONArray("angleArray");
            ;
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        list1.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        System.out.println("List of angle values with date in fragment history: " + list1);

        for (int i=0;i<list1.size();i++){
            entries.add(new Entry(i,Integer.valueOf(list1.get(i))));
        }

        LineDataSet dataset = new LineDataSet(entries, "Labels");


        dataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataset.setCubicIntensity(0.001f);
        dataset.setAxisDependency(YAxis.AxisDependency.LEFT);

        dataset.setColor(Color.parseColor("#00aff0"));
        dataset.setLineWidth(2);

        dataset.setDrawFilled(false);
        dataset.setFillAlpha(80);
        dataset.setFillColor(Color.WHITE);

        dataset.setValueTextColor(Color.WHITE);
        dataset.setDrawValues(false);
        dataset.setCircleColor(Color.WHITE);
        dataset.setCircleRadius(4);
        dataset.setCircleHoleRadius(2);

        dataset.setHighlightEnabled(true); // allow highlighting for DataSet

        // set this to false to disable the drawing of highlight indicator (lines)
        //dataset.setDrawHighlightIndicators(true);
        dataset.setHighLightColor(Color.RED); // color for highlight indicator



        LineData data = new LineData(dataset);

        //dataset.setColors(ColorTemplate.COLORFUL_COLORS); //


//        LimitLine l;
//        l.enableDashedLine(30,5,2);


        lineChart.setData(data);

        lineChart.setDescription("");
        lineChart.setNoDataText("No Chart Data"); // this is the top line
        lineChart.setNoDataTextDescription("..."); // this is one line below the no-data-text
        lineChart.setNoDataTextColor(Color.BLACK);
        lineChart.invalidate();

        //enable value highlighting
        //lineChart.setHighlightPerTapEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.setPinchZoom(true);

        lineChart.setBackground(null);
        lineChart.setBackgroundColor(Color.TRANSPARENT);
        lineChart.setBorderColor(Color.TRANSPARENT);
        lineChart.setDrawBorders(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setGridBackgroundColor(Color.TRANSPARENT);


        float upperLimit=15f;
        LimitLine ll = new LimitLine(upperLimit, "");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(3f);
        ll.enableDashedLine(10f,10f,0f);


//        ll.setTextColor(Color.WHITE);
//        ll.setTextSize(12f);

        YAxis y2 = lineChart.getAxisRight();
        y2.setDrawGridLines(false);
        y2.setDrawAxisLine(false);
        y2.setDrawLabels(false);
        y2.setDrawLimitLinesBehindData(false);

        YAxis y1 = lineChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setTextSize(15f);
        //y1.setAxisMaxValue(30);
        y1.setAxisMinValue(0);
        y1.setEnabled(true);

        //y1.setDrawGridLines(false);
        y1.setDrawAxisLine(true);
        //y1.setDrawLabels(false);

        y1.setDrawLimitLinesBehindData(true);
        //y1.setDrawTopYLabelEntry(false);
        y1.setDrawZeroLine(false);
        y1.addLimitLine(ll);

        XAxis x1 = lineChart.getXAxis();
        x1.setDrawGridLines(false);
        x1.setDrawAxisLine(false);
        x1.setDrawLabels(false);
        x1.setDrawLimitLinesBehindData(false);


        lineChart.animateY(2000);

    }


    private void setGridCellAdapterToDate(int month, int year) {

        _calendar.set(year, month, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));
        makeGraph();


    }
//    private void selecttab(){
//
//        if(daily_tab_selected) {
//
//            Daily.setPressed(true);
//            Monthly.setPressed(false);
//        }
//        else if(monthly_tab_selected) {
//
//            Daily.setPressed(false);
//            Monthly.setPressed(true);
//        }
//    }


}