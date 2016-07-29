package com.example.cricflex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.UUID;

import android.util.Log;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;

public class MainActivity extends Activity implements BluetoothAdapter.LeScanCallback {

//COMITTING ///
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    //check commit by bilal
    
    //changings
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;

    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;
    private File accelerationFile = new File("/sdcard/acceleration.txt");
    private long temp;
/*
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("callbackType", String.valueOf(callbackType));
            Log.i("result", result.toString());
            BluetoothDevice btDevice = result.getDevice();
            connectToDevice(btDevice);
        }
*/    //changings


    // State machine
    public static int genFlex = 0;
    final private static int STATE_BLUETOOTH_OFF = 1;
    final private static int STATE_DISCONNECTED = 2;
    final private static int STATE_CONNECTING = 3;
    final private static int STATE_CONNECTED = 4;
    public static int AjmalFlex = -1;
    public static int IshantFlex = -1;
    public static int once = 0;
    public static boolean firstnegativevalueafterpositive = false;
    private int state;
    private int ref;

    private boolean scanStarted;
    private boolean scanning;
    private boolean stopButtonPressed = false;
    private boolean connectedToBand = false;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    // private LeDeviceListAdapter mLeDeviceListAdapter;
    private RFDService rfduinoService;
    private TextView connectionStatusText;
    private TextView scanStatusText;
    private TextView deviceInfoText;
    private TextView angleText;
    private TextView degreeSign;
    private TextView legal;
    private TextView acceleration;
    //private TextView checkLabel;
    private TextView timer;
    private TextView rssiValue;
    //private Button scanButton;
    private ImageButton stopButton;
    private ImageButton startButton;
    private ImageButton graphButton;
    //private Button connectButton;
    private EditData valueEdit;
    private static final String TAG = "MyActivity";

    //final Button checkLabel = (Button) findViewById(R.id.checkLabel);
    private String in = new String();
    private String MacAdd = "yahooooooooooooooooooooooooooooo";
    //private LinearLayout dataLayout;


    private final BroadcastReceiver bluetoothStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            if (state == BluetoothAdapter.STATE_ON) {
                upgradeState(STATE_DISCONNECTED);
            } else if (state == BluetoothAdapter.STATE_OFF) {
                downgradeState(STATE_BLUETOOTH_OFF);
            }
        }
    };

    private final BroadcastReceiver scanModeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            scanning = (bluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_NONE);
            scanStarted &= scanning;

        }
    };

    private final ServiceConnection rfduinoServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {


            rfduinoService = ((RFDService.LocalBinder) service).getService();
            if (rfduinoService.initialize()) {
                if (rfduinoService.connect(bluetoothDevice.getAddress())) {
                    upgradeState(STATE_CONNECTING);


                }

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            rfduinoService = null;
            downgradeState(STATE_DISCONNECTED);
        }
    };

    private final BroadcastReceiver rfduinoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            final String action = intent.getAction();

            if (RFDService.ACTION_CONNECTED.equals(action)) {
                upgradeState(STATE_CONNECTED);
                Toast.makeText(getApplicationContext(), "Connected to CricFlex Band", Toast.LENGTH_SHORT).show();
                connectedToBand = true;
                legal.setText("Straighten the arm");
                once = 0;
            } else if (RFDService.ACTION_DISCONNECTED.equals(action)) {
                downgradeState(STATE_DISCONNECTED);
            } else if (RFDService.ACTION_DATA_AVAILABLE.equals(action)) {
                addData(intent.getByteArrayExtra(RFDService.EXTRA_DATA));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        startButton = (ImageButton) findViewById(R.id.start);
        stopButton = (ImageButton) findViewById(R.id.stop);
        graphButton = (ImageButton) findViewById(R.id.graph);

        startButton.setOnClickListener(new handleStartButton());
        stopButton.setOnClickListener(new handleStopButton());
        graphButton.setOnClickListener(new handleGraphButton());

        angleText = (TextView) findViewById(R.id.angle);
        degreeSign = (TextView) findViewById(R.id.degreesign);
        legal = (TextView) findViewById(R.id.legal);
//        acceleration = (TextView) findViewById(R.id.acceleration);
//        timer = (TextView) findViewById(R.id.timer);
//        connectionStatusText = (TextView) findViewById(R.id.test);



        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);

        }



    }


    class handleStartButton implements OnClickListener {
        public void onClick(View v) {
            stopButtonPressed = false;
            startButton.setVisibility(View.GONE);
            stopButton.setVisibility(View.VISIBLE);
            graphButton.setVisibility(View.VISIBLE);
            //angleText.setTextSize(100);
//            angleText.setTextColor(0xFF000000);
//            degreeSign.setTextColor(0xFF000000);
//            legal.setTextColor(0xFF000000);

            firstnegativevalueafterpositive = false;

            //connectionStatusText.setText("Start");    =====ASAWAL====
            scanStarted = true;

            // mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            // mLEScanner.startScan(filters, settings, mScanCallback);
            bluetoothAdapter.startLeScan(
                    new UUID[]{RFDService.UUID_SERVICE},
                    MainActivity.this);
        }
    }

    class handleStopButton implements OnClickListener {
        public void onClick(View v) {

            startButton.setVisibility(View.VISIBLE);
            stopButton.setVisibility(View.GONE);
            graphButton.setVisibility(View.GONE);

            if (genFlex > 15) {
                stopButtonPressed = true;
                angleText.setTextColor(0xFFFF0000);
                angleText.setText(Integer.toString(genFlex));
                degreeSign.setTextColor(0xFFFF0000);
                //angleText.setTextSize(200);
                legal.setText("Illegal");
                legal.setTextColor(0xFFFF0000);
            } else {
                stopButtonPressed = true;
                angleText.setTextColor(0xFF00FF00);
                angleText.setText(Integer.toString(genFlex));
                degreeSign.setTextColor(0xFF00FF00);
                //angleText.setTextSize(200);
                legal.setText("Legal");
                legal.setTextColor(0xFF00FF00);
            }


        }
    }
    class handleGraphButton implements OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, GraphActivity.class);
            startActivity(intent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();


        registerReceiver(scanModeReceiver, new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED));
        registerReceiver(bluetoothStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(rfduinoReceiver, RFDService.getIntentFilter());
        updateState(bluetoothAdapter.isEnabled() ? STATE_DISCONNECTED : STATE_BLUETOOTH_OFF);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


        bluetoothAdapter.stopLeScan(this);
        unregisterReceiver(scanModeReceiver);
        unregisterReceiver(bluetoothStateReceiver);
        unregisterReceiver(rfduinoReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    private void upgradeState(int newState) {
        if (newState > state) {
            updateState(newState);

        }
    }

    private void downgradeState(int newState) {
        if (newState < state) {
            updateState(newState);
        }
    }

    private void updateState(int newState) {
        state = newState;
        // updateUi();
    }

    void addData(byte[] data) {
    	
    if(!stopButtonPressed){
       
    		int value = 0;
    		for (int i = 0; i < data.length; i++)
    		{
    		   value += ((int) data[i] & 0xffL) << (8 * i);
    		}
    		int incoming=(int)value;
    		if (once==0 || once==1)
        	{
        		if (incoming==120)
        		{
        			legal.setText("Bend arm at 45");
        			once=1;
        		}
        		else if (incoming==119)
        		{
        			legal.setText("Monitoring...");
        			once=2;
        		}
        		
        	}
        	
    		// int incoming =(int)((data[1] << 8) + (data[0] & 0xFF));
            else if(-128<=incoming && incoming<=127)
    		 {
    			 firstnegativevalueafterpositive=true;
                 String in=new String();
                 in=String.valueOf(value);
                 angleText.setText(in);
    		 }
    		 else if(272<=incoming && incoming<=527)
    		 {
    	    		 String in=new String();
    	    		 in=String.valueOf(value-400);
    	    		 angleText.setText(in);
    	    		 if(firstnegativevalueafterpositive)
    	         	{
    	 		        if(getIntent().getStringExtra("StringName").equals("Ajmal"))
    	 		    	{;
    	 		        	AjmalFlex=(incoming-400);
    	 		        	genFlex=AjmalFlex;
    	 		        	mappend("/sdcard/Ajmal.txt");
    	 		        	
    	 		    	}
    	 		    	else if(getIntent().getStringExtra("StringName").equals("Ishant"))
    	 		    	{
    	 		    		IshantFlex=(incoming-400);
    	 		    		genFlex=IshantFlex;
    	 		    		mappend("/sdcard/Ishant.txt");
    	 		    		
    	 		    	}
    	 		        firstnegativevalueafterpositive=false;
    	 		    }
       
    }
}
    }


    /*
    void addData(byte[] data) throws IOException {


        int value = 0;
        for (int i = 0; i < data.length; i++) {
            value += ((int) data[i] & 0xffL) << (8 * i);
        }
        //int incoming=(int)value;
        //System.out.println(value);



        in = String.valueOf(value);
        acceleration.setText(in);




        if (!accelerationFile.exists()) {
            accelerationFile.createNewFile();
        }

        final Button checkLabel = (Button) findViewById(R.id.checkLabel);
        checkLabel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                in = "-"+in;

            }
        });
        writeInFile(in);



    }
    //
    */

    public void writeInFile(String in){

        long timeSystem = System.currentTimeMillis();
        long value1;
        value1 = Math.abs(timeSystem - temp);
        timer.setText(String.valueOf(value1));
        temp = timeSystem;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(accelerationFile, true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                //for (int i = 0; i < in.length(); i++) {
                fos.write(in.getBytes());
                //if (i < in.length() - 1) {

                timeSystem = System.currentTimeMillis();
                String timeOfSystem = Long.toString(timeSystem);
                fos.write(",".getBytes());
                fos.write(timeOfSystem.getBytes());
                fos.write("\n".getBytes());

                //}
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /*
        private void writeToFile(String data) {
            try {
                //Context context = new
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("/sdcard/acceleration.txt", Context.MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }
        */
    //my code
    public void onLeScan(BluetoothDevice device, final int rssi, final byte[] scanRecord) {

        bluetoothDevice = device;
    /*
        Context mContext = getBaseContext();
        BluetoothAdapter mAdapter = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE).getAdapter;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            BluetoothLeScanner mLeScanner = mAdapter.getBluetoothLeScanner();
        }
    BluetoothAdapter.getBluetoothLeScanner();
    */

        bluetoothAdapter.stopLeScan(this);


        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                connectionStatusText.setText(bluetoothDevice.getName());
                String a = String.valueOf(rssi);
                connectionStatusText.setText(a);


            }
        });


        if (!(bluetoothDevice.getName().equals("Abdullah"))) {
            bluetoothAdapter.startLeScan(new UUID[]{RFDService.UUID_SERVICE}, MainActivity.this);
        } else {
            Intent rfduinoIntent = new Intent(MainActivity.this, RFDService.class);
            bindService(rfduinoIntent, rfduinoServiceConnection, BIND_AUTO_CREATE);
        }


    }


    private void mappend(String file)
    {
    	
    	        try {
    	            BufferedReader br= new BufferedReader(new FileReader(file));
    	            String dataRow=null;
    	            String tempText="";
    	            
    	    			while ( (dataRow= br.readLine()) != null){
    	    			    if(dataRow.substring(0,4).equals("Flex")){
    	    			        tempText+="Flex:"+String.valueOf(genFlex);
    	    			        tempText+=System.getProperty("line.separator");
    	    			       }
    	    			    else
    	    			    {
    	    			    tempText+=dataRow;
    	    			    tempText+=System.getProperty("line.separator");
    	    			    }
    	    			}
    	    		
    	    		

    	            FileWriter writer;
    	    		
    	    			writer = new FileWriter(file);
    	    			writer.write(tempText);
    	    			writer.flush();
                        writer.close();
    	    		} catch (IOException e) {
    	    			// TODO Auto-generated catch block
    	    			e.printStackTrace();
    	    		}
    	
    }
}


