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
import java.util.ArrayList;
import java.util.Calendar;
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


    ArrayList<Integer> angleValues = new ArrayList<Integer>();

    public FragmentHistory(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);


//        mCalendar = Calendar.getInstance();
//
//        DatePicker datePicker = (DatePicker) rootView.findViewById(R.id.date_picker);
//
//        datePicker.init(mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH),null);
//
//
//        //datePicker.getChildAt(0).setVisibility(GONE);
//        //datePicker.getChildAt(0).setVisibility(INVISIBLE);
//        datePicker.getChildAt(0).setEnabled(false);
//        //        selecttab();




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



//        DatePicker dp_mes = (DatePicker) rootView.findViewById(R.id.date_picker);
//
//        int year    = dp_mes.getYear();
//        int month   = dp_mes.getMonth();
//        int day     = dp_mes.getDayOfMonth();
//
//        dp_mes.init(year, month, day, new DatePicker.OnDateChangedListener() {
//            @Override
//            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                int month_i = monthOfYear + 1;
//                Log.e("selected month:", Integer.toString(month_i));
//                //Add whatever you need to handle Date changes
//            }
//        });
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
//            if (daySpinnerId != 0)
//            {
//                View daySpinner = dp_mes.findViewById(daySpinnerId);
//                if (daySpinner != null)
//                {
//                    daySpinner.setVisibility(View.GONE);
//                }
//            }
//
//            int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
//            if (monthSpinnerId != 0)
//            {
//                View monthSpinner = dp_mes.findViewById(monthSpinnerId);
//                if (monthSpinner != null)
//                {
//                    monthSpinner.setVisibility(View.VISIBLE);
//                }
//            }
//
//            int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");
//            if (yearSpinnerId != 0)
//            {
//                View yearSpinner = dp_mes.findViewById(yearSpinnerId);
//                if (yearSpinner != null)
//                {
//                    yearSpinner.setVisibility(View.VISIBLE);
//                }
//            }
//        } else { //Older SDK versions
//            Field f[] = dp_mes.getClass().getDeclaredFields();
//            for (Field field : f)
//            {
//                if(field.getName().equals("mDayPicker") || field.getName().equals("mDaySpinner"))
//                {
//                    field.setAccessible(true);
//                    Object dayPicker = null;
//                    try {
//                        dayPicker = field.get(dp_mes);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    ((View) dayPicker).setVisibility(View.GONE);
//                }
//
//                if(field.getName().equals("mMonthPicker") || field.getName().equals("mMonthSpinner"))
//                {
//                    field.setAccessible(true);
//                    Object monthPicker = null;
//                    try {
//                        monthPicker = field.get(dp_mes);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    ((View) monthPicker).setVisibility(View.VISIBLE);
//                }
//
//                if(field.getName().equals("mYearPicker") || field.getName().equals("mYearSpinner"))
//                {
//                    field.setAccessible(true);
//                    Object yearPicker = null;
//                    try {
//                        yearPicker = field.get(dp_mes);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
//                    ((View) yearPicker).setVisibility(View.VISIBLE);
//                }
//            }
//        }




        LineChart lineChart = (LineChart) rootView.findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<Entry>();









        /////////////////////////////////////////////////////////////
        //Code for getting angle values
        DatabaseHelper helper = new DatabaseHelper(getActivity());
        String username = SaveSharedPreference.getUserName(getActivity());
        String angleValues = helper.getAngleValues(username);


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


            //System.out.println("Angle Values in Home Screen 2: : " + list);
            //int[] longestStreak = new int[list.size()];

//            int longestStreakCount = 0;
//            int noOfStreaks = 0;
//            int maxStreak = 0;
//
//            for (int i = 0; i < list.size(); i++) {
//                if (doubleAngleValues[i] <= 15.0) {
//                    longestStreakCount++;
//                }
//                if (doubleAngleValues[i] > 15.0 || i == list.size()-1) {
//                    if (maxStreak < longestStreakCount) {
//                        maxStreak = longestStreakCount;
//                    }
//                    //longestStreak[noOfStreaks++] = longestStreakCount;
//                    longestStreakCount = 0;
//                }
//            }



        }



        System.out.println("List of angle values in fragment history : "+ list);





        for (int i=0;i<list.size();i++){

            entries.add(new Entry(i,Integer.valueOf(list.get(i))));

        }





//        ////////////////////////////////////////////////////////////////////////////////
//        entries.add(new Entry(0, 7));
//        entries.add(new Entry(1, 10));
//        entries.add(new Entry(2, 13));
//        entries.add(new Entry(3, 12));
//        entries.add(new Entry(4, 9));
//        entries.add(new Entry(5, 15));
//        entries.add(new Entry(6, 14));
//        entries.add(new Entry(7, 7));
//        entries.add(new Entry(8, 10));
//        entries.add(new Entry(9, 13));
//        entries.add(new Entry(10, 12));
//        entries.add(new Entry(11, 9));
//        entries.add(new Entry(12, 15));
//        entries.add(new Entry(13, 14));
//        entries.add(new Entry(14, 10));
//        entries.add(new Entry(15, 13));
//        entries.add(new Entry(16, 12));
//        entries.add(new Entry(17, 9));
//        entries.add(new Entry(18, 15));
//        entries.add(new Entry(19, 14));
//        entries.add(new Entry(20, 7));
//        entries.add(new Entry(21, 10));
//        entries.add(new Entry(22, 13));
//        entries.add(new Entry(23, 12));
//        entries.add(new Entry(24, 9));
//        entries.add(new Entry(25, 15));
//        entries.add(new Entry(26, 14));


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




        mProgress = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        mProgressStatus = 50;
        mProgress.setProgress(mProgressStatus);
//        // Start lengthy operation in a background thread
//        new Thread(new Runnable() {
//            public void run() {
//                while (mProgressStatus < 100) {
//                    mProgressStatus = 50;
//
//                    // Update the progress bar
//                    mHandler.post(new Runnable() {
//                        public void run() {
//                            mProgress.setProgress(mProgressStatus);
//                        }
//                    });
//                }
//            }
//        }).start();







        return rootView;
    }

    private void setGridCellAdapterToDate(int month, int year) {

        _calendar.set(year, month, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate,
                _calendar.getTime()));


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