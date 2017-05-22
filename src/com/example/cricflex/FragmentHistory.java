package com.example.cricflex;


import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.percent.PercentRelativeLayout;

import static android.content.ContentValues.TAG;


public class FragmentHistory extends Fragment {


    View rootView;

    DatabaseHelper helper;
    String email;

    // date selector items
    private TextView currentMonth;
    private ImageButton prevMonth;
    private ImageButton prevYear;
    private ImageButton nextMonth;
    private ImageButton nextYear;
    private Calendar _calendar;
    private int month, year;
    private static final String dateTemplate = "MMMM yyyy";

    // tabs
    private Button angleTab;
    private Button forceTab;
    private Button timeTab;
    private Button twistTab;
    String checkForTab = "angle";

    // swipes
    PercentRelativeLayout swipeLayout;

    //chart
    LineChart lineChart;
    LineDataSet dataset;
    ChartCustomMarkerView markerView;
    LimitLine limitLine;
    YAxis yAxisLeft;
    YAxis yAxisRight;
    XAxis xAxis;
    ArrayList<Entry> entriesAngle = new ArrayList<>();
    ArrayList<Entry> entriesForce = new ArrayList<>();
    ArrayList<Entry> entriesArmTwist = new ArrayList<>();
    ArrayList<Entry> entriesActionTime = new ArrayList<>();

    //footer stats
    TextView maximumValue;
    TextView minimumValue;
    TextView averageValue;

    int angleMax, angleMin, angleAvg;
    int forceMax, forceMin, forceAvg;
    float timeMax, timeMin, timeAvg;
    int twistMax, twistMin, twistAvg;
    int angleDataSize, forceDataSize, timeDataSize, twistDataSize;


    //Firebase History Maintenance
    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    public FragmentHistory(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //inflating layout
        rootView = inflater.inflate(R.layout.fragment_history, container, false);

        //Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        ValueEventListener metricsWithDatesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Getting all metrics object
                // ...


                System.out.println("Datasnapshot mai ara hai : ");
                MetricsWithDates metricsOfDate = new MetricsWithDates();

                metricsOfDate = dataSnapshot.getValue(MetricsWithDates.class);


                if(metricsOfDate==null){
                    System.out.println("Cant fetch data");

                    return;
                }
                else{
//                    System.out.println("Angle values from firebase: " + allMetricsInMonitor.angleValues);
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };




        //getting email from shared preference
        email = SaveSharedPreference.getEmail(getActivity());

        //database
        helper = new DatabaseHelper(getActivity());



        //initializing date selector items
        prevYear = (ImageButton) rootView.findViewById(R.id.prevYear);
        prevMonth = (ImageButton) rootView.findViewById(R.id.prevMonth);
        currentMonth = (TextView) rootView.findViewById(R.id.currentMonth);
        nextMonth = (ImageButton) rootView.findViewById(R.id.nextMonth);
        nextYear = (ImageButton) rootView.findViewById(R.id.nextYear);

        //initializing tabs
        angleTab = (Button) rootView.findViewById(R.id.history_angle_tab);
        forceTab = (Button) rootView.findViewById(R.id.history_force_tab);
        timeTab = (Button) rootView.findViewById(R.id.history_time_tab);
        twistTab = (Button) rootView.findViewById(R.id.history_twist_tab);

        //initializing screen swipes for changing the tabs
        swipeLayout = (PercentRelativeLayout) rootView.findViewById(R.id.fragment_history_layout);

        //Initializing graph
        lineChart = (LineChart) rootView.findViewById(R.id.chart);


        //initializing stats on footer
        maximumValue = (TextView)rootView.findViewById(R.id.history_stat_maximum);
        minimumValue = (TextView)rootView.findViewById(R.id.history_stat_minimum);
        averageValue = (TextView)rootView.findViewById(R.id.history_stat_average);




        //date selector items listeners

        prevYear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                year--;
                updateGraphToDate(month, year);
            }
        });

        prevMonth.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                if (month <= 1) {
                    month = 12;
                    year--;
                } else {
                    month--;
                }
                updateGraphToDate(month, year);
            }
        });

        nextMonth.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {


                if (month > 11) {
                    month = 1;
                    year++;
                } else {
                    month++;
                }
                updateGraphToDate(month, year);
            }
        });

        nextYear.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                year++;
                updateGraphToDate(month, year);
            }
        });




        // tabs' listener

        angleTab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(!checkForTab.equals("angle"))
                    showAngleData();

                return true;
            }
        });

        forceTab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(!checkForTab.equals("force"))
                    showForceData();

                return true;
            }
        });

        timeTab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(!checkForTab.equals("time"))
                    showActionTimeData();

                return true;
            }
        });

        twistTab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(!checkForTab.equals("twist"))
                    showTwistData();

                return true;
            }
        });


        // swipes listener for tabs
        swipeLayout.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {

            public void onSwipeRight() {
                //Toast.makeText(getActivity(), "right", Toast.LENGTH_SHORT).show();

                switch(checkForTab){
                    case "twist":{
                        showActionTimeData();
                        break;
                    }
                    case "time":{
                        showForceData();
                        break;
                    }
                    case "force":{
                        showAngleData();
                        break;
                    }
                    case "angle":{
                        showTwistData();
                        break;
                    }
                }

            }
            public void onSwipeLeft() {
                //Toast.makeText(getActivity(), "left", Toast.LENGTH_SHORT).show();

                switch(checkForTab){
                    case "angle":{
                        showForceData();
                        break;
                    }
                    case "force":{
                        showActionTimeData();
                        break;
                    }
                    case "time":{
                        showTwistData();
                        break;
                    }
                    case "twist":{
                        showAngleData();
                        break;
                    }
                }

            }

        });



        // updating the graph on start
        // gets the local month and year
        _calendar = Calendar.getInstance(Locale.getDefault());
        month = _calendar.get(Calendar.MONTH);
        year = _calendar.get(Calendar.YEAR);

        // update date selector to local date
        currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));

        // updates the graph to local month year
        // sets to angle graph on first start
        updateGraphToDate(month, year);


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        selectTab(checkForTab);

    }


    // updates graph
    public void updateGraph(ArrayList<Entry> entries, final String metricType, int count){

        // sets the new data
        dataset = new LineDataSet(entries, metricType);
        LineData data = new LineData(dataset);
        lineChart.clear();
        lineChart.setData(data);

        //data settings
        dataset.setMode(LineDataSet.Mode.LINEAR);
        dataset.setAxisDependency(YAxis.AxisDependency.LEFT);
        dataset.setColor(Color.parseColor("#00cc99"));
        dataset.setLineWidth(2);
        dataset.setDrawFilled(true);
        dataset.setFillAlpha(50);
        dataset.setFillColor(Color.parseColor("#8000cc99"));
        dataset.setValueTextColor(Color.WHITE);
        dataset.setDrawValues(false);
        dataset.setCircleColor(Color.parseColor("#00cc99"));
        dataset.setCircleRadius(4);
        dataset.setCircleHoleRadius(0);
        dataset.setHighlightEnabled(true); // allow highlighting for DataSet
        dataset.setHighLightColor(Color.TRANSPARENT); // color for highlight indicator

        // line graph setting
        lineChart.setDescription("");
        lineChart.setNoDataText("No Chart Data"); // this is the top line
        lineChart.setNoDataTextDescription("..."); // this is one line below the no-data-text
        lineChart.setNoDataTextColor(Color.BLACK);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.setPinchZoom(false);
        lineChart.setDoubleTapToZoomEnabled(true);
        lineChart.setBackground(null);
        lineChart.setBackgroundColor(Color.TRANSPARENT);
        lineChart.setBorderColor(Color.TRANSPARENT);
        lineChart.setDrawBorders(false);
        lineChart.setDrawGridBackground(false);
        lineChart.setGridBackgroundColor(Color.TRANSPARENT);
        lineChart.getLegend().setEnabled(false);
        lineChart.setVisibleXRangeMaximum(24);
        lineChart.setVisibleXRangeMinimum(12);

        // shows popup on data point when clicked
        markerView = new ChartCustomMarkerView(getActivity(), R.layout.chart_custom_marker_view);
        markerView.setMetricData(entriesAngle, entriesForce, entriesActionTime, entriesArmTwist);
        lineChart.setMarkerView(markerView);

        // limit line settings
        float upperLimit=15f;
        limitLine = new LimitLine(upperLimit, "ICC 15\u00b0 limit");
        limitLine.setTextSize(10f);
        limitLine.setTextColor(Color.WHITE);
        limitLine.setLineColor(Color.RED);
        limitLine.setLineWidth(1.5f);
        limitLine.enableDashedLine(10f,10f,0f);

        // y axis right settings
        yAxisRight = lineChart.getAxisRight();
        yAxisRight.setDrawGridLines(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawLimitLinesBehindData(false);

        // y axis left settings
        yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setTextColor(Color.WHITE);
        yAxisLeft.setTextSize(15f);
        yAxisLeft.setAxisMinValue(0);
        yAxisLeft.setDrawAxisLine(true);
        yAxisLeft.setDrawLimitLinesBehindData(false);
        yAxisLeft.setDrawGridLines(false);
        yAxisLeft.setDrawZeroLine(false);
        yAxisLeft.setDrawLabels(true);

        // x axis settings
        xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(10f);
        xAxis.setAxisMinValue(1);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(true);
        xAxis.setDrawLimitLinesBehindData(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setAxisMinValue(1);
        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f);               // minimum interval on the x-axis
        xAxis.setLabelCount(count);             // no. of balls in the respective month


        xAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int axisValue = (int)value;
                if (axisValue % 6 == 0)
                    return Integer.toString(axisValue/6);
                return "";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        // remove all limit line before plotting the new graph
        yAxisLeft.removeAllLimitLines();

        // adds a 15 degree limit line for angle tab
        if(metricType.equals("angle")) {

            System.out.println("adding limit line at 15");
            yAxisLeft.setDrawLimitLinesBehindData(true);
            yAxisLeft.addLimitLine(limitLine);

        }

        // sets the yAxis value format according to selected tab
        yAxisLeft.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                DecimalFormat mFormat;
                mFormat = new DecimalFormat("###,###,##0.00");


                switch(metricType) {
                    case "angle": {
                        return Integer.toString((int) value) + "\u00b0";
                    }
                    case "force": {
                        return Integer.toString((int) value) + " N";
                    }
                    case "time": {
                        return mFormat.format(value) + " s";
//                        return Float.toString(value) + " s";
                    }
                    case "twist": {
                        return Integer.toString((int) value) + "\u00b0";
                    }
                    default:
                        return Float.toString(value);
                }

            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        // notify chart that data is changed
        lineChart.notifyDataSetChanged();

        // update and animate graph
        lineChart.animateY(1000, Easing.EasingOption.EaseOutBack);
    }

    // shows angle data of the selected month
    public void showAngleData(){

        selectTab("angle");

        updateGraph(entriesAngle, checkForTab, angleDataSize);

        maximumValue.setText(String.valueOf(angleMax) + "\u00b0");
        minimumValue.setText(String.valueOf(angleMin) + "\u00b0");
        averageValue.setText(String.valueOf(angleAvg) + "\u00b0");


    }

    // shows force data of the selected month
    public void showForceData(){

        selectTab("force");

        updateGraph(entriesForce, checkForTab, forceDataSize);

        maximumValue.setText(String.valueOf(forceMax) + "N");
        minimumValue.setText(String.valueOf(forceMin) + "N");
        averageValue.setText(String.valueOf(forceAvg) + "N");

    }

    // shows actionTime data of the selected month
    public void showActionTimeData(){

        selectTab("time");

        updateGraph(entriesActionTime, checkForTab, timeDataSize);

        maximumValue.setText(String.valueOf(timeMax) + "s");
        minimumValue.setText(String.valueOf(timeMin) + "s");
        averageValue.setText(String.valueOf(timeAvg) + "s");

    }

    // shows twist data of the selected month
    public void showTwistData(){

        selectTab("twist");

        updateGraph(entriesArmTwist, checkForTab, twistDataSize);

        maximumValue.setText(String.valueOf(twistMax) + "\u00b0");
        minimumValue.setText(String.valueOf(twistMin) + "\u00b0");
        averageValue.setText(String.valueOf(twistAvg) + "\u00b0");

    }

    // updates graph when date changed
    private void updateGraphToDate(int month, int year) {

        // sets the date selector to selected date

        entriesAngle.clear();
        entriesActionTime.clear();
        entriesArmTwist.clear();
        entriesForce.clear();
        _calendar.set(year, month, _calendar.get(Calendar.DAY_OF_MONTH));
        currentMonth.setText(DateFormat.format(dateTemplate, _calendar.getTime()));



        ValueEventListener metricsWithDatesListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Getting all metrics object
                // ...


                System.out.println("Datasnapshot mai ara hai : ");
                MetricsWithDates metricsOfDate = new MetricsWithDates();

                metricsOfDate = dataSnapshot.getValue(MetricsWithDates.class);


                if(metricsOfDate==null){
                    System.out.println("Cant fetch data");
//                    metricOfDateInMonitor = null;
                    return;
                }
                else{
//                    metricOfDateInMonitor = metricsOfDate;



                    //Setting angle values with date
                    System.out.println("Angle Values: " + metricsOfDate.angleValues);

                    angleDataSize = metricsOfDate.angleValues.size();

                    angleMax = metricsOfDate.angleValues.get(0);
                    angleMin = metricsOfDate.angleValues.get(0);

                    for (int i = 0; i < angleDataSize; i++) {

//                        value = Integer.valueOf(list1.get(i));
                        entriesAngle.add(new Entry(i + 1, metricsOfDate.angleValues.get(i)));

                        angleAvg += metricsOfDate.angleValues.get(i);
                        if (metricsOfDate.angleValues.get(i) > angleMax)
                            angleMax = metricsOfDate.angleValues.get(i);
                        if (metricsOfDate.angleValues.get(i) < angleMin)
                            angleMin = metricsOfDate.angleValues.get(i);
                    }

                    angleAvg = angleAvg / angleDataSize;




                    //Setting Force Values from firebase

                    forceDataSize = metricsOfDate.forceValues.size();

                    forceMax = metricsOfDate.forceValues.get(0);
                    forceMin = metricsOfDate.forceValues.get(0);

                    for (int i = 0; i < forceDataSize; i++) {

//                        value = Integer.valueOf(list2.get(i));
                        entriesForce.add(new Entry(i + 1,  metricsOfDate.forceValues.get(i)));

                        forceAvg += metricsOfDate.forceValues.get(i);
                        if (metricsOfDate.forceValues.get(i) > forceMax)
                            forceMax = metricsOfDate.forceValues.get(i);
                        if (metricsOfDate.forceValues.get(i) < forceMin)
                            forceMin = metricsOfDate.forceValues.get(i);
                    }

                    forceAvg = forceAvg / forceDataSize;




                    //Action Time Data

                    timeDataSize = metricsOfDate.actionTimeValues.size();

                    timeMax = metricsOfDate.actionTimeValues.get(0);
                    timeMin = metricsOfDate.actionTimeValues.get(0);

                    for (int i = 0; i < timeDataSize; i++) {
//                        valueFloat = Float.valueOf(list3.get(i));
                        entriesActionTime.add(new Entry(i + 1, metricsOfDate.actionTimeValues.get(i)));

                        timeAvg += metricsOfDate.actionTimeValues.get(i);


                        if (metricsOfDate.actionTimeValues.get(i) > timeMax)
                            timeMax = metricsOfDate.actionTimeValues.get(i);
                        if (metricsOfDate.actionTimeValues.get(i) < timeMin)
                            timeMin = metricsOfDate.actionTimeValues.get(i);
                    }

                    timeAvg = timeAvg / timeDataSize;


                    //Twist Data

                    twistDataSize = metricsOfDate.armTwistValues.size();

                    twistMax = metricsOfDate.armTwistValues.get(0);
                    twistMin = metricsOfDate.armTwistValues.get(0);

                    for (int i = 0; i < twistDataSize; i++) {

//                        value = Integer.valueOf(list4.get(i));
                        entriesArmTwist.add(new Entry(i + 1, metricsOfDate.armTwistValues.get(i)));

                        timeAvg += metricsOfDate.armTwistValues.get(i);

                        if (metricsOfDate.armTwistValues.get(i) > twistMax)
                            twistMax = metricsOfDate.armTwistValues.get(i);
                        if (metricsOfDate.armTwistValues.get(i) < twistMin)
                            twistMin = metricsOfDate.armTwistValues.get(i);
                    }

                    timeAvg = timeAvg / twistDataSize;
//                    System.out.println("Angle values from firebase: " + allMetricsInMonitor.angleValues);

                    // show data according to selected tab
                    switch (checkForTab){

                        case "angle":{
                            showAngleData();
                            break;
                        }
                        case "force":{
                            showForceData();
                            break;
                        }
                        case "time":{
                            showActionTimeData();
                            break;
                        }
                        case "twist":{
                            showTwistData();
                            break;
                        }

                        default:
                            break;
                    }
                }





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };



        DatabaseReference MetricsOfDateRef = databaseReference.child("MetricsWithDates")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(currentMonth.getText().toString());

        MetricsOfDateRef.addValueEventListener(metricsWithDatesListener);



        // get data from database of respective month
//        getAngleDataFromDatabase();
//        getForceDataFromDatabase();
//        getTimeDataFromDatabase();
//        getTwistDataFromDatabase();



    }

    // selects the tab
    public void selectTab(String tab){
        switch (tab){
            case "angle":{
                checkForTab = "angle";
                angleTab.setSelected(true);
                angleTab.setPressed(true);
                forceTab.setSelected(false);
                forceTab.setPressed(false);
                timeTab.setSelected(false);
                timeTab.setPressed(false);
                twistTab.setSelected(false);
                twistTab.setPressed(false);
                break;
            }
            case "force":{
                checkForTab = "force";
                angleTab.setSelected(false);
                angleTab.setPressed(false);
                forceTab.setSelected(true);
                forceTab.setPressed(true);
                timeTab.setSelected(false);
                timeTab.setPressed(false);
                twistTab.setSelected(false);
                twistTab.setPressed(false);
                break;
            }
            case "time":{
                checkForTab = "time";
                angleTab.setSelected(false);
                angleTab.setPressed(false);
                forceTab.setSelected(false);
                forceTab.setPressed(false);
                timeTab.setSelected(true);
                timeTab.setPressed(true);
                twistTab.setSelected(false);
                twistTab.setPressed(false);
                break;
            }
            case "twist":{
                checkForTab = "twist";
                angleTab.setSelected(false);
                angleTab.setPressed(false);
                forceTab.setSelected(false);
                forceTab.setPressed(false);
                timeTab.setSelected(false);
                timeTab.setPressed(false);
                twistTab.setSelected(true);
                twistTab.setPressed(true);
                break;
            }

        }
    }


    public void getAngleDataFromDatabase() {

        entriesAngle.clear();
        int value;

        String angleValuesWithDate = helper.getAngleValuesWithDate(email, currentMonth.getText().toString());
        //making arraylist after getting response from database
        ArrayList<String> list1 = new ArrayList<>();

        if (!angleValuesWithDate.equals("not found")) {
            // Getting Arraylist back
            JSONObject json1 = null;
            try {
                json1 = new JSONObject(angleValuesWithDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray = json1.optJSONArray("angleArray");
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

        if (list1.size() != 0) {

            angleDataSize = list1.size();

            angleMax = Integer.valueOf(list1.get(0));
            angleMin = Integer.valueOf(list1.get(0));

            for (int i = 0; i < list1.size(); i++) {

                value = Integer.valueOf(list1.get(i));
                entriesAngle.add(new Entry(i + 1, value));

                angleAvg += value;
                if (value > angleMax)
                    angleMax = value;
                if (value < angleMin)
                    angleMin = value;
            }

            angleAvg = angleAvg / list1.size();

        }

    }

    public void getForceDataFromDatabase() {

        entriesForce.clear();
        int value;

        String forceWithDate = helper.getForceValuesWithDate(email, currentMonth.getText().toString());

        //making arraylist after getting response from database
        ArrayList<String> list2 = new ArrayList<>();
        if (!forceWithDate.equals("not found")) {
            // Getting Arraylist back
            JSONObject json1 = null;
            try {
                json1 = new JSONObject(forceWithDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray = json1.optJSONArray("forceArray");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        list2.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        if (list2.size() != 0) {

            forceDataSize = list2.size();

            forceMax = Integer.valueOf(list2.get(0));
            forceMin = Integer.valueOf(list2.get(0));

            for (int i = 0; i < list2.size(); i++) {

                value = Integer.valueOf(list2.get(i));
                entriesForce.add(new Entry(i + 1, value));

                forceAvg += value;
                if (value > forceMax)
                    forceMax = value;
                if (value < forceMin)
                    forceMin = value;
            }

            forceAvg = forceAvg / list2.size();

        }


    }

    public void getTimeDataFromDatabase() {

        entriesActionTime.clear();
        float valueFloat;

        String actionTimeWithDate = helper.getActionTimeValuesWithDate(email, currentMonth.getText().toString());

        //making arraylist after getting response from database
        ArrayList<String> list3 = new ArrayList<>();
        if (!actionTimeWithDate.equals("not found")) {
            // Getting Arraylist back
            JSONObject json1 = null;
            try {
                json1 = new JSONObject(actionTimeWithDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray = json1.optJSONArray("actionTimeArray");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        list3.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (list3.size() != 0) {

            timeDataSize = list3.size();

            timeMax = Float.valueOf(list3.get(0));
            timeMin = Float.valueOf(list3.get(0));

            for (int i = 0; i < list3.size(); i++) {
                valueFloat = Float.valueOf(list3.get(i));
                entriesActionTime.add(new Entry(i + 1, valueFloat));

                timeAvg += valueFloat;

                if (valueFloat > timeMax)
                    timeMax = valueFloat;
                if (valueFloat < timeMin)
                    timeMin = valueFloat;
            }

            timeAvg = timeAvg / list3.size();

        }


    }

    public void getTwistDataFromDatabase() {

        entriesArmTwist.clear();

        int value;

        String armTwistWithDate = helper.getArmTwistValuesWithDate(email,currentMonth.getText().toString());

        //making arraylist after getting response from database
        ArrayList<String> list4 = new ArrayList<>();

        if(!armTwistWithDate.equals("not found")) {
            // Getting Arraylist back
            JSONObject json1 = null;
            try {
                json1 = new JSONObject(armTwistWithDate);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JSONArray jsonArray = json1.optJSONArray("armTwistArray");
            if (jsonArray != null) {
                int len = jsonArray.length();
                for (int i = 0; i < len; i++) {
                    try {
                        list4.add(jsonArray.get(i).toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if(list4.size()!=0) {

            twistDataSize = list4.size();

            twistMax = Integer.valueOf(list4.get(0));
            twistMin = Integer.valueOf(list4.get(0));

            for (int i = 0; i < list4.size(); i++) {

                value = Integer.valueOf(list4.get(i));
                entriesArmTwist.add(new Entry(i + 1, value));

                timeAvg += value;

                if (value > twistMax)
                    twistMax = value;
                if (value < twistMin)
                    twistMin = value;
            }

            timeAvg = timeAvg / list4.size();
        }



    }

}