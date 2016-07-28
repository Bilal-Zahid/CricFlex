package com.example.cricflex;


import java.util.Random;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class GraphActivity extends Activity{
	//GraphView init
			static LinearLayout GraphView;
			static GraphView graphView;
			static GraphView graph;

		//	static GraphViewSeries Series;
			//graph value
			private static double moving = 0;
			private static double Xmin=0;
			private static double Xmax=10;
			private static final Random RANDOM = new Random();
			private static LineGraphSeries<DataPoint> series;
			  
		
			private final BroadcastReceiver rfduinoReceiver = new BroadcastReceiver() {
		        @Override
		        public void onReceive(Context context, Intent intent) {
		            final String action = intent.getAction();
		            
		            if (RFDService.ACTION_DATA_AVAILABLE.equals(action)) {
		                plot(intent.getByteArrayExtra(RFDService.EXTRA_DATA));
		                
		             //   connectionStatusText.setText(MacAdd);

		            }
		        }
		    };
		
		@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	       
	        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			requestWindowFeature(Window.FEATURE_NO_TITLE);//Hide title
			this.getWindow().setFlags(WindowManager.LayoutParams.
					FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//Hide Status bar
			setContentView(R.layout.activity_graph);
			//set background color
			LinearLayout background = (LinearLayout)findViewById(R.id.bg);
			//background.setBackgroundColor(Color.BLACK);
			//init();
			
			// we get graph view instance
		    graph = (GraphView) findViewById(R.id.Graph);
		    // data
		    series = new LineGraphSeries<DataPoint>();
		    graph.addSeries(series);
		     series.setColor(Color.RED);
		     series.setThickness(6);
		     series.setBackgroundColor(Color.WHITE);
		     
		     graph.getGridLabelRenderer().setHorizontalLabelsColor(Color.RED);
		     graph.getGridLabelRenderer().setVerticalLabelsColor(Color.RED);
		   //  graph.getGridLabelRenderer().sethori
		    graph.getGridLabelRenderer().setNumVerticalLabels(8);
		    graph.getGridLabelRenderer().setNumHorizontalLabels(6);

		    // customize a little bit viewport
		  
		    Viewport viewport = graph.getViewport();
		    viewport.setYAxisBoundsManual(true);
		    viewport.setScrollable(true);
		    viewport.scrollToEnd();
		    viewport.setMinY(-12);
		    viewport.setMaxY(90);
		    viewport.setMinX(0);
		    viewport.setMaxX(10);
	}
		
		 @Override
		    protected void onStart() {
		        super.onStart();
		       
		        registerReceiver(rfduinoReceiver, RFDService.getIntentFilter());
		        
		    }
		
		 
		public static void plot(byte[] data) 
		{
		
			int value = 0;
			for (int i = 0; i < data.length; i++)
			{
			   value += ((int) data[i] & 0xffL) << (8 * i);
			}
			
			//Calendar cal = Calendar.getInstance();

			
		//	a[i]=new DataPoint(cal.getTimeInMillis(),VALUE);
			

		
			
			int incoming=(int)value;
			// int incoming =(int)((data[1] << 8) + (data[0] & 0xFF));
			
			  if(272<=incoming && incoming<=527)
			 {
		    		incoming=incoming-400;
		    		
			 }
				
				graph.getViewport().setXAxisBoundsManual(true);
					
							
							
			  series.appendData(new DataPoint(moving,incoming),true,150);			//X-axis control
			  moving+=0.1;
			 
			  
											
								// set manual X bounds
					
		
		}

}
