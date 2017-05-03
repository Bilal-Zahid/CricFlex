package com.example.cricflex;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;

/**
 * Created by abcd on 4/17/2017.
 */

public class ChartCustomMarkerView extends MarkerView {

//    private TextView yAxisValue;
    TextView angleValue;
    TextView forceValue;
    TextView timeValue;
    TextView twistValue;
    ArrayList<Entry> angleValues, forceValues, timeValues, twistValues;

    public ChartCustomMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        // this markerview only displays a textview

        angleValue = (TextView) findViewById(R.id.value_angle);
        forceValue = (TextView) findViewById(R.id.value_force);
        timeValue = (TextView) findViewById(R.id.value_time);
        twistValue = (TextView) findViewById(R.id.value_twist);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        angleValue.setText(String.valueOf((int)angleValues.get(((int) e.getX())-1).getY()) + "\u00b0");
        forceValue.setText(String.valueOf((int)forceValues.get(((int) e.getX())-1).getY()) + "N");
        timeValue.setText(String.valueOf(timeValues.get(((int) e.getX())-1).getY()) + "s");
        twistValue.setText(String.valueOf((int)twistValues.get(((int) e.getX())-1).getY()) + "\u00b0");
    }


    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        return -(getWidth() / 2);
    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        return -getHeight();
    }

    public void setMetricData(ArrayList<Entry> angle, ArrayList<Entry> force, ArrayList<Entry> time, ArrayList<Entry> twist){

        angleValues = angle;
        forceValues = force;
        timeValues = time;
        twistValues = twist;
    }
}
