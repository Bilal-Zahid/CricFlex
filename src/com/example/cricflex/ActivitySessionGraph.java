package com.example.cricflex;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivitySessionGraph extends AppCompatActivity {


    TextView parameterNameTextView;

    private Button endSessionButton;

    ArrayList<Integer> values = new ArrayList<Integer>();
    String parameterName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_graph);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActionBar().hide();

        endSessionButton = (Button)findViewById(R.id.session_finish_button);


        parameterNameTextView = (TextView) findViewById(R.id.parameter_name);
        final Intent intent = getIntent();
        final Bundle extraBundle = intent.getExtras();

        values = extraBundle.getIntegerArrayList("values");
        parameterName = intent.getStringExtra("parameter");
        parameterNameTextView.setText(parameterName);


        LineChart lineChartValues = (LineChart) findViewById(R.id.parameter_graph);
        drawLineChart(lineChartValues,values,parameterName);

        System.out.println(parameterName + " " + values);


        endSessionButton.setOnClickListener(new handleEndSessionButton());

    }

    public void drawLineChart(LineChart lineChart, ArrayList<Integer> values , String description){

        List<Entry> entries = new ArrayList<Entry>();

        for (int i=0; i<values.size();i++){
            float value = (float)values.get(i);
            entries.add(new Entry(i,value));
        }


        Collections.sort(entries, new EntryXComparator());
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


        lineChart.setData(data);

        lineChart.setDescription(description);
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

            Intent i = new Intent(ActivitySessionGraph.this, ActivityMain.class);
            ActivitySessionGraph.this.startActivity(i);


        }
    }
}
