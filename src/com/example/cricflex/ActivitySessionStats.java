package com.example.cricflex;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.cricflex.R;
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
import com.github.mikephil.charting.highlight.*;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivitySessionStats extends Activity {

    private Button endSessionButton;
    ArrayList<Integer> angleValues = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_stats);

        getActionBar().hide();

        final Intent intent = getIntent();
        final Bundle extraBundle = intent.getExtras();

        angleValues = extraBundle.getIntegerArrayList("angleValues");

//        System.out.println("Values of angles in session stats: "+ angleValues);

        endSessionButton = (Button)findViewById(R.id.end_session_button);

        endSessionButton.setOnClickListener(new handleEndSessionButton());


        LineChart lineChart = (LineChart) findViewById(R.id.chart);


        //YourData[] dataObjects = ...;


        List<Entry> entries = new ArrayList<Entry>();
//        entries.add(new Entry(4f, 0));
//        entries.add(new Entry(8f, 1));
//        entries.add(new Entry(6f, 2));
//        entries.add(new Entry(2f, 3));
//        entries.add(new Entry(18f, 4));
//        entries.add(new Entry(9f, 5));

        entries.add(new Entry(0, 7));
        entries.add(new Entry(1, 10));
        entries.add(new Entry(2, 13));
        entries.add(new Entry(3, 12));
        entries.add(new Entry(4, 9));
        entries.add(new Entry(5, 15));
        entries.add(new Entry(6, 14));
        entries.add(new Entry(7, 7));
        entries.add(new Entry(8, 10));
        entries.add(new Entry(9, 13));
        entries.add(new Entry(10, 12));
        entries.add(new Entry(11, 9));
        entries.add(new Entry(12, 15));
        entries.add(new Entry(13, 14));
        entries.add(new Entry(14, 10));
        entries.add(new Entry(15, 13));
        entries.add(new Entry(16, 12));
        entries.add(new Entry(17, 9));
        entries.add(new Entry(18, 15));
        entries.add(new Entry(19, 14));
        entries.add(new Entry(20, 7));
        entries.add(new Entry(21, 10));
        entries.add(new Entry(22, 13));
        entries.add(new Entry(23, 12));
        entries.add(new Entry(24, 9));
        entries.add(new Entry(25, 15));
        entries.add(new Entry(26, 14));
//        for (int i=0; i<angleValues.size();i++){
//            float value = (float)angleValues.get(i);
//            entries.add(new Entry(i,value));
//        }


        Collections.sort(entries, new EntryXComparator());



//        ArrayList<String> labels = new ArrayList<String>();
//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        labels.add("April");
//        labels.add("May");
//        labels.add("June");

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

    class handleEndSessionButton implements View.OnClickListener {
        public void onClick(View v) {

            Intent i = new Intent(ActivitySessionStats.this, ActivityMain.class);
            ActivitySessionStats.this.startActivity(i);


        }
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent i = new Intent(ActivitySessionStats.this, ActivityMain.class);
        ActivitySessionStats.this.startActivity(i);
    }

}
