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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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



//        entries.add(new Entry(0, 4f));
//        entries.add(new Entry(1, 8f));
//        entries.add(new Entry(2, 6f));
//        entries.add(new Entry(3, 2f));
//        entries.add(new Entry(4, 18f));
//        entries.add(new Entry(5, 9f));

        for (int i=0; i<angleValues.size();i++){
            float value = (float)angleValues.get(i);
            entries.add(new Entry(i,value));
        }


        Collections.sort(entries, new EntryXComparator());



//        ArrayList<String> labels = new ArrayList<String>();
//        labels.add("January");
//        labels.add("February");
//        labels.add("March");
//        labels.add("April");
//        labels.add("May");
//        labels.add("June");

        LineDataSet dataset = new LineDataSet(entries, "Labels");


        //LineDataSet set = new LineDataSet(entries, "LineDataSet");

        LineData data = new LineData(dataset);

        dataset.setColors(ColorTemplate.COLORFUL_COLORS); //
        //dataset.setDrawCubic(true);
        dataset.setDrawFilled(true);

        lineChart.setData(data);
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
