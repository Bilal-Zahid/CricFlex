package com.example.cricflex;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class ActivityGraph4 extends Activity {
    private RelativeLayout instantGraphLayout;
    private LineChart mChart;

    private static int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph4);

        instantGraphLayout = (RelativeLayout) findViewById(R.id.instantGraphLayout);

        mChart = new LineChart(this);

        // Adding graph to layout
//        instantGraphLayout.addView(mChart, new AbsListView.LayoutParams (AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));?
//        instantGraphLayout.addView(mChart);

//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//
//        instantGraphLayout.addView(mChart,params);?

        instantGraphLayout.addView(mChart, new AbsListView.LayoutParams
                (AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));
       // instantGraphLayout.addView(mChart, new AbsListView.LayoutParams        (AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT));?
//        mChart.setDescription("Instant Ball Graph");
//        mChart.setNoDataTextDescription("No data at the moment");
//        mChart.setNoDataTextColor(Color.BLACK);
//        mChart.setNoDataText("no data bro");

        mChart.setDescription("Instant Ball Graph");
        mChart.setNoDataText("No Chart Data"); // this is the top line
        mChart.setNoDataTextDescription("..."); // this is one line below the no-data-text
        mChart.setNoDataTextColor(Color.BLACK);
        mChart.invalidate();

        //enable value highlighting
        mChart.setHighlightPerTapEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(true);

        mChart.setPinchZoom(true);

        mChart.setBackgroundColor(Color.LTGRAY);

        //DAta Time

        LineData data = new LineData();
        data.setValueTextColor(Color.BLACK);

        mChart.setData(data);

        //

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);


//        XAxis x1 = mChart.getXAxis();
//        x1.setTextColor(Color.BLACK);
//        x1.setDrawGridLines(true);
//        x1.setAvoidFirstLastClipping(true);
        //x1.setEnabled(true);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.BLACK);
        y1.setAxisMaxValue(60f);
        y1.setDrawGridLines(true);
//        y1.setAxisMaximumValue(100f);
        y1.setAxisMinValue(0f);

        y1.setEnabled(true);


//        YAxis y12 = mChart.getAxisRight();
        //y12.setEnabled(true);
    }

    private void addEntry(){
        LineData data = mChart.getData();

        if(data != null){
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null){
                set = createSet();
                data.addDataSet(set);
            }

            // add a new value
            data.addEntry(
                    new Entry(set.getEntryCount(),(float) (Math.random() * 45)), 0);

            //notify chart data has changed
            mChart.notifyDataSetChanged();

            // limit no of visible entries
            mChart.setVisibleXRange(6,6);

            //scroll to the last entry
            System.out.println("DataSet Count: " + data.getDataSetCount());
            mChart.moveViewToX(++counter);


        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        // Real Time Data Addition

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0 ; i>-1; i++){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addEntry();
                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //method to create set
    private LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null, "SPL Db");
        //set.setDrawCubic(true);

        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(Color.rgb(244,117,177));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);


        return set;
    }
}
