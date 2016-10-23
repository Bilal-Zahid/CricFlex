package com.example.cricflex;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

public class ActivityGraph5 extends Activity {
    private RelativeLayout instantGraphLayout;
    private LineChart mChart;


    private final BroadcastReceiver rfduinoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (RFDService.ACTION_DATA_AVAILABLE.equals(action)) {
                addEntry(intent.getByteArrayExtra(RFDService.EXTRA_DATA));

                //   connectionStatusText.setText(MacAdd);

            }
        }
    };



    private static int counter = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph5);
        getActionBar().hide();
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

        mChart.setDescription("");
        mChart.setNoDataText("No Chart Data"); // this is the top line
        mChart.setNoDataTextDescription("..."); // this is one line below the no-data-text
        mChart.setNoDataTextColor(Color.BLACK);
        mChart.invalidate();

        //enable value highlighting
        mChart.setHighlightPerTapEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        mChart.setPinchZoom(true);

        mChart.getAxisRight().setDrawLabels(false);
        mChart.setBackground(null);
        mChart.setBackgroundColor(Color.TRANSPARENT);
        mChart.setBorderColor(Color.TRANSPARENT);
        mChart.setDrawBorders(false);
        mChart.setDrawGridBackground(false);
        mChart.setGridBackgroundColor(Color.TRANSPARENT);
        //mChart.setAutoScaleMinMaxEnabled(true);
        //DAta Time

        LineData data = new LineData();
        //data.setValueTextColor(Color.WHITE);

        mChart.setData(data);

        mChart.getLegend().setEnabled(false);

        float upperLimit=15f;
        LimitLine ll = new LimitLine(upperLimit, "");
        ll.setLineColor(Color.RED);
        ll.setLineWidth(3f);
        ll.enableDashedLine(10f,10f,0f);

        YAxis y2 = mChart.getAxisRight();
        y2.setDrawGridLines(false);
        y2.setDrawAxisLine(false);
        y2.setDrawLabels(false);
        y2.setDrawLimitLinesBehindData(false);

        YAxis y1 = mChart.getAxisLeft();
        y1.setTextColor(Color.WHITE);
        y1.setTextSize(15f);
        y1.setAxisMaxValue(30f);
        y1.setAxisMinValue(0f);
        y1.setEnabled(true);

        //y1.setDrawGridLines(false);
        y1.setDrawAxisLine(false);
        //y1.setDrawLabels(false);
        y1.setDrawLimitLinesBehindData(false);
        //y1.setDrawTopYLabelEntry(false);
        y1.setDrawZeroLine(true);
        y1.addLimitLine(ll);


        XAxis x1 = mChart.getXAxis();
        x1.setDrawGridLines(false);
        x1.setDrawAxisLine(false);
        x1.setDrawLabels(false);
        x1.setDrawLimitLinesBehindData(false);











    }


    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(rfduinoReceiver, RFDService.getIntentFilter());

    }

    @Override
    protected void onStop()
    {
        unregisterReceiver(rfduinoReceiver);
        super.onStop();
    }

    private void addEntry(byte[] dataInBytes){

        int value = 0;
        for (int i = 0; i < dataInBytes.length; i++)
        {
            value += ((int) dataInBytes[i] & 0xffL) << (8 * i);
        }


        int incoming=value;
        // int incoming =(int)((data[1] << 8) + (data[0] & 0xFF));

        if(272<=incoming && incoming<=527)
        {
            incoming=incoming-400;

        }


        LineData data = mChart.getData();

        if(data != null){
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null){
                set = createSet();
                data.addDataSet(set);
            }

            // add a new value
//            data.addEntry(new Entry(set.getEntryCount(),(float) (Math.random() * 45)), 0);

            data.addEntry(new Entry(set.getEntryCount(),(float) incoming), 0);

            TextView layoutAngleText = (TextView) findViewById(R.id.graph_angle_text);

            if(incoming>15)
            {layoutAngleText.setTextColor(Color.RED);}
            else
            {layoutAngleText.setTextColor(Color.WHITE);}

            layoutAngleText.setText(""+incoming+"\u00b0");
            //notify chart data has changed
            mChart.notifyDataSetChanged();

            // limit no of visible entries

            mChart.setVisibleXRange(6,10);

            //scroll to the last entry
//            System.out.println("DataSet Count: " + data.getDataSetCount());
            mChart.moveViewToX(++counter);


        }
    }

//    @Override
//    protected void onResume(){
//        super.onResume();
//
//        // Real Time Data Addition
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i=0 ; i>-1; i++){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            byte[] test = "1".getBytes();
//                            addEntry(test);
//                        }
//                    });
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }).start();
//    }

    //method to create set
    private LineDataSet createSet(){
        LineDataSet set = new LineDataSet(null, "");
        //set.setDrawCubic(true);

        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.WHITE);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(5f);
        set.setFillAlpha(65);
        set.setFillColor(Color.WHITE);
        //set.setHighLightColor(Color.rgb(244,117,177));
        set.setValueTextColor(Color.WHITE);
       // set.setValueTextSize(10f);
        set.setDrawHighlightIndicators(false);
        set.setDrawValues(false);
        set.setCircleRadius(1f);
        set.setCircleHoleRadius(1f);
        set.setHighLightColor(Color.WHITE);



        return set;
    }
}
