package com.example.cricflex;

/**
 * Created by bilal on 8/21/2016.
 */

import java.io.IOException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import android.os.Handler;
import android.os.SystemClock;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActivityMonitor extends Activity implements BluetoothAdapter.LeScanCallback {

    //COMITTING ///
    private static Boolean exit = false;



    //check commit by bilal zahid 1234

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
    private int test1234;


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
//    public static boolean firstnegativevalueafterpositive = false;
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
    //private TextView angleText;
    //private TextView degreeSign;
    private TextView monitorLegalText;
    //private TextView ;
    private TextView acceleration;
    //private TextView checkLabel;
    private TextView timer;
    private TextView rssiValue;
    //private Button scanButton;
    private Button stopButton;
    private Button startButton;
    //private ImageButton graphButton;
    //private Button connectButton;
    private EditData valueEdit;
    private static final String TAG = "MyActivity";

    //final Button checkLabel = (Button) findViewById(R.id.checkLabel);
    private String in = new String();
    private String MacAdd = "yahooooooooooooooooooooooooooooo";
    //private LinearLayout dataLayout;


    //other variables for new layout






    private int counterLegal=0,counterIllegal=0;

    private TextView angleText;
    //private TextView monitorStatusText;
    private Chronometer monitorTimer;
    private ImageButton monitorTimerButton;
    private TextView monitorLegalBalls;
    private TextView monitorIllegalBalls;
    private Button monitorFinishButton;
    private Button monitorStartButton;
    private ImageButton instantGraphButton;


    private boolean timerOn = false;
    private long timeWhenStopped = 0;

    boolean startButtonPressed = false;
    boolean monitoringTextChange = true;
    boolean timerWithStart = true;
    boolean boolForTimerLogic = true;


//For Stats and home page
    DatabaseHelper helper = new DatabaseHelper(this);
    Player p = new Player();
    String username;
    ArrayList<Integer> angleValues = new ArrayList<Integer>();

    String legalBalls;
    String illegalBalls;
    String totalBalls;
    String averageAngle;
    String longestStreak;
    String lastBallAngle;

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
                monitorLegalText.setText("Straighten the arm");
                once = 0;
            } else if (RFDService.ACTION_DISCONNECTED.equals(action)) {
                downgradeState(STATE_DISCONNECTED);
            } else if (RFDService.ACTION_DATA_AVAILABLE.equals(action)) {
                addData(intent.getByteArrayExtra(RFDService.EXTRA_DATA));
            }
        }
    };


    private void initializelayoutitems() {

        angleText = (TextView)findViewById(R.id.monitor_angle_text);
        //monitorStatusText = (TextView)findViewById(R.id.monitor_status_text);
        monitorTimer = (Chronometer)findViewById(R.id.monitor_timer);
        monitorTimerButton = (ImageButton)findViewById(R.id.monitor_timer_button);
        monitorLegalBalls = (TextView)findViewById(R.id.monitor_legal_balls);
        monitorIllegalBalls = (TextView)findViewById(R.id.monitor_illegal_balls);
        monitorFinishButton = (Button)findViewById(R.id.monitor_finish_button);

        //monitorStartButton = (Button)findViewById(R.id.monitor_start_button);


        //monitorTimer.setFormat("H:MM:SS");
        monitorTimerButton.setOnClickListener(new handlemonitorTimerButton());
        //monitorFinishButton.setOnClickListener(new handlemonitorFinishButton());

        //monitorStartButton.setOnClickListener(new handlemonitorStartButton());
    }



    class handlemonitorFinishButton implements OnClickListener {
        public void onClick(View v) {

            //genFlex=15;

//            if (genFlex > 15) {
//                angleText.setTextColor(0xFFFF0000);
//                angleText.setText(Integer.toString(genFlex)+"\u00b0");
//                monitorStatusText.setText(R.string.illegal_monitor_status);
//                monitorStatusText.setTextColor(0xFFFF0000);
//            } else {
//                angleText.setTextColor(0xFF00FF00);
//                angleText.setText(Integer.toString(genFlex)+"\u00b0");
//                monitorStatusText.setText(R.string.legal_monitor_status);
//                monitorStatusText.setTextColor(0xFF00FF00);
//            }


            p.setUsername(username);


            p.setIllegalBowls(String.valueOf(counterIllegal));
            p.setLegalBowls(String.valueOf(counterLegal));


            System.out.println("Legal Bowls: "+ p.getLegalBowls()+"\nIllegal Bowls:" + p.getIllegalBowls());
            System.out.println("Angle Values: \n"+ angleValues);

//            Gson gson = new Gson();

//            converting array into JSON
            JSONObject json = new JSONObject();
            try {
                json.put("angleArray", new JSONArray(angleValues));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String convertedArrayListToString = json.toString();

            System.out.println("Arraylist : " + convertedArrayListToString);


            helper.changeAngleValues(username ,convertedArrayListToString);

            //ArrayList items ;




            // Getting Arraylist back
//            JSONObject json1 = null;
//            try {
//                json1 = new JSONObject(convertedArrayListToString);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            ArrayList<String> list = new ArrayList<String>();
//            JSONArray jsonArray = (JSONArray)json1.optJSONArray("angleArray");;
//            if (jsonArray != null) {
//                int len = jsonArray.length();
//                for (int i=0;i<len;i++){
//                    try {
//                        list.add(jsonArray.get(i).toString());
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            System.out.println("Re-Converting to arraylist : " + list);


            helper.insertPlayerStats(p);
            helper.changeStatLegalIllegal(p.getUsername(),p.getLegalBowls(),p.getIllegalBowls());

//            Disabling bluetooth connection
            if(mBluetoothAdapter==null){

            }
            else if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }

            Intent i = new Intent(ActivityMonitor.this, ActivitySessionStats.class);

            Bundle extraBundle = new Bundle();

            extraBundle.putIntegerArrayList("angleValues", angleValues);

            i.putExtras(extraBundle);
            //i.putExtra("angleValues",angleValues);
            ActivityMonitor.this.startActivity(i);
            finish();

        }
    }

    class handlemonitorTimerButton implements OnClickListener {
        public void onClick(View v) {



            if(timerOn){
                timeWhenStopped = monitorTimer.getBase() - SystemClock.elapsedRealtime();
                stopChronometer(null);
                monitorTimerButton.setImageResource(R.drawable.start_circle_large);
                timerOn=false;
            }

            else if(!timerOn){

                boolForTimerLogic = false;
                monitorTimer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                startChronometer(null);
                //
                monitorTimerButton.setImageResource(R.drawable.pause_circle_large);
                timerOn=true;
                timerWithStart = false;
            }
        }
    }


    class handlemonitorStartButton implements OnClickListener {
        public void onClick(View v) {
            if(timerWithStart){

                if(boolForTimerLogic) {
                    monitorTimer.setBase(SystemClock.elapsedRealtime());
                    boolForTimerLogic = false;
                }

                startChronometer(null);
                //

                monitorTimerButton.setImageResource(R.drawable.pause_circle_large);
                timerOn=true;
                timerWithStart = false;
            }
            startButtonPressed = true;
//            stopButtonPressed = false;
            monitorStartButton.setVisibility(View.INVISIBLE);
            monitorFinishButton.setVisibility(View.VISIBLE);
            //graphButton.setVisibility(View.VISIBLE);

            //angleText.setTextSize(100);
//            angleText.setTextColor(0xFF000000);
//            degreeSign.setTextColor(0xFF000000);
//            monitorLegalText.setTextColor(0xFF000000);

//            firstnegativevalueafterpositive = false;

            //connectionStatusText.setText("Start");    =====ASAWAL====
            scanStarted = true;

            // mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            // mLEScanner.startScan(filters, settings, mScanCallback);
            bluetoothAdapter.startLeScan(
                    new UUID[]{RFDService.UUID_SERVICE},
                    ActivityMonitor.this);
        }
    }

    class handleStopButton implements OnClickListener {
        public void onClick(View v) {

            //startButton.setVisibility(View.VISIBLE);
            //stopButton.setVisibility(View.GONE);
            //graphButton.setVisibility(View.GONE);

//            if (genFlex > 15) {
//                stopButtonPressed = true;
//                angleText.setTextColor(0xFFFF0000);
//                angleText.setText(Integer.toString(genFlex)+"\u00b0");
//                //degreeSign.setTextColor(0xFFFF0000);
//                //angleText.setTextSize(200);
//                monitorLegalText.setText("Illegal");
//                monitorLegalText.setTextColor(0xFFFF0000);
//            } else {
//                stopButtonPressed = true;
//                angleText.setTextColor(0xFF00FF00);
//                angleText.setText(Integer.toString(genFlex)+"\u00b0");
//                //degreeSign.setTextColor(0xFF00FF00);
//                //angleText.setTextSize(200);
//                monitorLegalText.setText("Legal");
//                monitorLegalText.setTextColor(0xFF00FF00);
//            }

//            Intent i = new Intent(ActivityMonitor.this, ActivityMain.class);
//            ActivityMonitor.this.startActivity(i);

        }
    }
    class handleGraphButton implements OnClickListener {
        public void onClick(View v) {
            Intent intent = new Intent(ActivityMonitor.this, ActivityGraph5.class);
            startActivity(intent);
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);
        getActionBar().hide();
        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        monitorStartButton = (Button) findViewById(R.id.monitor_start_button);
        monitorFinishButton = (Button) findViewById(R.id.monitor_finish_button);

        instantGraphButton = (ImageButton) findViewById(R.id.monitor_graph_button) ;
        //graphButton = (ImageButton) d(R.id.graph);


        instantGraphButton.setOnClickListener(new handleGraphButton());
        monitorStartButton.setOnClickListener(new handlemonitorStartButton());
        monitorFinishButton.setOnClickListener(new handlemonitorFinishButton());
        //graphButton.setOnClickListener(new handleGraphButton());

        angleText = (TextView) findViewById(R.id.monitor_angle_text);
        //degreeSign = (TextView) findViewById(R.id.degreesign);
        monitorLegalText = (TextView) findViewById(R.id.monitor_status_text);

        //timerOn=true;

        monitorTimerButton = (ImageButton)findViewById(R.id.monitor_timer_button);

        monitorTimerButton.setOnClickListener(new handlemonitorTimerButton());


        monitorTimer = (Chronometer)findViewById(R.id.monitor_timer);

        monitorLegalBalls = (TextView)findViewById(R.id.monitor_legal_balls);
        monitorIllegalBalls = (TextView)findViewById(R.id.monitor_illegal_balls);
//        acceleration = (TextView) findViewById(R.id.acceleration);
//        timer = (TextView) findViewById(R.id.timer);
//        connectionStatusText = (TextView) findViewById(R.id.test);

        username = SaveSharedPreference.getUserName(ActivityMonitor.this);
        System.out.println("Username: "+ username);


        //initializelayoutitems();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);

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

        if(startButtonPressed && timerOn){


            angleText.setTextColor(0xFFFFFFFF);
            angleText.setText(Integer.toString(genFlex)+"\u00b0");

            if(monitoringTextChange)
                monitorLegalText.setText("Monitoring");

            monitorLegalText.setTextColor(0xFFFFFFFF);

            int value = 0;
            for (int i = 0; i < data.length; i++)
            {
                value += ((int) data[i] & 0xffL) << (8 * i);
            }


            System.out.print("Receiving: "+ value);
//            int[] values = new int[10];
//
//            for (int i=0 ; i<1 ; i++){
//                for (int j=0 ; j<2 ; j++){
//                    values[i] += ((int) data[j + (i*2)] & 0xffL) << (8 * j );
//                }
//            }

//            System.out.println("Value: "+ value);
            int incoming=(int)value;
            if (once==0 || once==1)
            {
                if (incoming==120)
                {
                    monitorLegalText.setText("Bend arm at 45");
                    once=1;
                }
                else if (incoming==119)
                {
                    monitorLegalText.setText("Monitoring...");
                    once=2;
                }

            }
            else if( incoming == 118)
            {
//                Toast.makeText(ActivityMonitor.this, "Bowl" , Toast.LENGTH_SHORT).show();
                monitorLegalText.setText("Bowl");
                monitoringTextChange = false;

//                System.out.println("118 received and this is test print");
                rfduinoService.send("a".getBytes());

            }

            // int incoming =(int)((data[1] << 8) + (data[0] & 0xFF));
            else if(-128<=incoming && incoming<=127)
            {
//                firstnegativevalueafterpositive=true;
                String in=new String();
                in=String.valueOf(value);
                angleText.setText(in+"\u00b0");
            }
            else if(272<=incoming && incoming<=527)
            {
                String in=new String();
                in=String.valueOf(value-400);
                angleText.setText(in+"\u00b0");
                rfduinoService.send("a".getBytes());
//                if(firstnegativevalueafterpositive)
//                {



                    startButtonPressed = false;
                    monitorStartButton.setVisibility(View.VISIBLE);
                    genFlex = incoming-400;

                    angleValues.add(genFlex);

                    if (genFlex > 15) {
                        //stopButtonPressed = true;

                        monitorIllegalBalls.setText(String.valueOf(++counterIllegal));
                        angleText.setTextColor(0xFFFF0000);
                        angleText.setText(Integer.toString(genFlex)+"\u00b0");
                        monitorLegalText.setText("Illegal");
                        monitorLegalText.setTextColor(0xFFFF0000);
                    } else {
                        monitorLegalBalls.setText(String.valueOf(++counterLegal));
                        //stopButtonPressed = true;
                        angleText.setTextColor(0xFF00FF00);
                        angleText.setText(Integer.toString(genFlex)+"\u00b0");
                        //degreeSign.setTextColor(0xFF00FF00);
                        //angleText.setTextSize(200);
                        monitorLegalText.setText("Legal");
                        monitorLegalText.setTextColor(0xFF00FF00);
                    }


//                    if(getIntent().getStringExtra("StringName").equals("Ajmal"))
//                    {;
//                        AjmalFlex=(incoming-400);
//                        genFlex=AjmalFlex;
//                        mappend("/sdcard/Ajmal.txt");
//
//                    }
//                    else if(getIntent().getStringExtra("StringName").equals("Ishant"))
//                    {
//                        IshantFlex=(incoming-400);
//                        genFlex=IshantFlex;
//                        mappend("/sdcard/Ishant.txt");
//
//                    }
//                    firstnegativevalueafterpositive=false;
//                }

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


        ActivityMonitor.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //connectionStatusText.setText(bluetoothDevice.getName());
                String a = String.valueOf(rssi);
                //connectionStatusText.setText(a);


            }
        });


        if (!(bluetoothDevice.getName().equals("Abdullah"))) {
            bluetoothAdapter.startLeScan(new UUID[]{RFDService.UUID_SERVICE}, ActivityMonitor.this);
        } else {
            Intent rfduinoIntent = new Intent(ActivityMonitor.this, RFDService.class);
            bindService(rfduinoIntent, rfduinoServiceConnection, BIND_AUTO_CREATE);
        }


    }
//    @Override
//    public void onBackPressed() {
//        if (exit) {
//            this.finishAffinity(); // finish activity
//        } else {
//            Toast.makeText(this, "Press Back again to Exit.",
//                    Toast.LENGTH_SHORT).show();
//            exit = true;
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    exit = false;
//                }
//            }, 3 * 1000);
//
//        }
//
//    }

    @Override
    public void onBackPressed() {



        if(mBluetoothAdapter==null){

        }
        else if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
        Intent i = new Intent(ActivityMonitor.this, ActivityMain.class);

        ActivityMonitor.this.startActivity(i);
        finish();

//        close();

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

    public void startChronometer(View view) {
        ((Chronometer) findViewById(R.id.monitor_timer)).start();
    }

    public void stopChronometer(View view) {
        ((Chronometer) findViewById(R.id.monitor_timer)).stop();
    }
}
