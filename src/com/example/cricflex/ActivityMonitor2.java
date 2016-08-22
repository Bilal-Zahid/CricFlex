package com.example.cricflex;



import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;


public class ActivityMonitor2 extends Activity implements BluetoothAdapter.LeScanCallback {


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
    private boolean connectedToBand = false;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    // private LeDeviceListAdapter mLeDeviceListAdapter;
    private RFDService rfduinoService;
    private TextView connectionStatusText;
    private TextView scanStatusText;
    private TextView deviceInfoText;

    //private Button connectButton;
    private EditData valueEdit;
    private static final String TAG = "MyActivity";

    //final Button checkLabel = (Button)  findViewById(R.id.checkLabel);
    private String in = new String();
    private String MacAdd = "yahooooooooooooooooooooooooooooo";
    //private LinearLayout dataLayout;

    boolean startButtonPressed = false;


    

    private TextView angleText;
    private TextView monitorStatusText;
    private Chronometer monitorTimer;
    private ImageButton monitorTimerButton;
    private TextView monitorLegalBalls;
    private TextView monitorIllegalBalls;
    private Button monitorFinishButton;
    private Button monitorStartButton;

    private boolean timerOn = true;
    private long timeWhenStopped = 0;

    //private TextView checkLabel;
    private TextView timer;
    private TextView rssiValue;



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
                //Toast.makeText(getApplicationContext(), "Connected to CricFlex Band", Toast.LENGTH_SHORT).show();
                connectedToBand = true;
                monitorStatusText.setText("Straighten the arm");
                once = 0;
            } else if (RFDService.ACTION_DISCONNECTED.equals(action)) {
                downgradeState(STATE_DISCONNECTED);
            } else if (RFDService.ACTION_DATA_AVAILABLE.equals(action)) {
                addData(intent.getByteArrayExtra(RFDService.EXTRA_DATA));
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_monitor);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
        firstnegativevalueafterpositive = false;

        //connectionStatusText.setText("Start");    =====ASAWAL====
        scanStarted = true;

        // mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
        // mLEScanner.startScan(filters, settings, mScanCallback);
        bluetoothAdapter.startLeScan(
                new UUID[]{RFDService.UUID_SERVICE},
                ActivityMonitor2.this);
        timerOn=true;

        initializelayoutitems();
    }


    private void initializelayoutitems() {

        angleText = (TextView)findViewById(R.id.monitor_angle_text);
        monitorStatusText = (TextView)findViewById(R.id.monitor_status_text);
        monitorTimer = (Chronometer)findViewById(R.id.monitor_timer);
        monitorTimerButton = (ImageButton)findViewById(R.id.monitor_timer_button);
        monitorLegalBalls = (TextView)findViewById(R.id.monitor_legal_balls);
        monitorIllegalBalls = (TextView)findViewById(R.id.monitor_illegal_balls);
        monitorFinishButton = (Button)findViewById(R.id.monitor_finish_button);

        monitorStartButton = (Button)findViewById(R.id.monitor_start_button);


        //monitorTimer.setFormat("H:MM:SS");
        monitorTimerButton.setOnClickListener(new handlemonitorTimerButton());
        monitorFinishButton.setOnClickListener(new handlemonitorFinishButton());

        monitorStartButton.setOnClickListener(new handlemonitorStartButton());
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

                monitorTimer.setBase(SystemClock.elapsedRealtime() + timeWhenStopped);
                startChronometer(null);
                //
                monitorTimerButton.setImageResource(R.drawable.pause_circle_large);
                timerOn=true;
            }




        }
    }

    class handlemonitorFinishButton implements OnClickListener {
        public void onClick(View v) {

            //genFlex=15;

            if (genFlex > 15) {
                angleText.setTextColor(0xFFFF0000);
                angleText.setText(Integer.toString(genFlex)+"\u00b0");
                monitorStatusText.setText(R.string.illegal_monitor_status);
                monitorStatusText.setTextColor(0xFFFF0000);
            } else {
                angleText.setTextColor(0xFF00FF00);
                angleText.setText(Integer.toString(genFlex)+"\u00b0");
                monitorStatusText.setText(R.string.legal_monitor_status);
                monitorStatusText.setTextColor(0xFF00FF00);
            }

        }
    }

    class handlemonitorStartButton implements OnClickListener {
        public void onClick(View v) {

            startButtonPressed = true;

        }
    }


    @Override
    public void onStart() {
        super.onStart();

/////...................................................................................................///////////////////
        this.registerReceiver(scanModeReceiver, new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED));
        this.registerReceiver(bluetoothStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        this.registerReceiver(rfduinoReceiver, RFDService.getIntentFilter());
        updateState(bluetoothAdapter.isEnabled() ? STATE_DISCONNECTED : STATE_BLUETOOTH_OFF);
        /////////////////////////////////////////....................................................////////////////////////


    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        /////////////////////////////////////////....................................................////////////////////////
        bluetoothAdapter.stopLeScan(this);
        this.unregisterReceiver(scanModeReceiver);
        this.unregisterReceiver(bluetoothStateReceiver);
        this.unregisterReceiver(rfduinoReceiver);

        /////////////////////////////////////////....................................................////////////////////////
    }

    @Override
    public void onStop() {
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

        if(timerOn ){
            angleText.setTextColor(0xFFFFFFFF);
//
            monitorStatusText.setTextColor(0xFFFFFFFF);
            monitorStatusText.setText("Monitoring");

            //monitorStatusText.setTextColor(0xFFFF0000);
            int value = 0;
            for (int i = 0; i < data.length; i++)
            {
                value += ((int) data[i] & 0xffL) << (8 * i);
            }
            int incoming=(int)value;


            //Toast.makeText(getApplicationContext(), "check toast", Toast.LENGTH_SHORT).show();
            if (once==0 || once==1)
            {
                Toast.makeText(getApplicationContext(), "check toast  3", Toast.LENGTH_SHORT).show();
                if (incoming==120)
                {
                    Toast.makeText(getApplicationContext(), "check toast  4", Toast.LENGTH_SHORT).show();
                    monitorStatusText.setText("Bend arm at 45");
                    once=1;
                }
                else if (incoming==119)
                {
                    monitorStatusText.setText("Monitoring...");
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
                    startButtonPressed = false;
                    genFlex=(incoming-400);
                    //angleText.setTextColor();

                    if (genFlex > 15) {
                        angleText.setTextColor(0xFFFF0000);
                        angleText.setText(Integer.toString(genFlex)+"\u00b0");
                        monitorStatusText.setText(R.string.illegal_monitor_status);
                        monitorStatusText.setTextColor(0xFFFF0000);
                    } else {
                        angleText.setTextColor(0xFF00FF00);
                        angleText.setText(Integer.toString(genFlex)+"\u00b0");
                        monitorStatusText.setText(R.string.legal_monitor_status);
                        monitorStatusText.setTextColor(0xFF00FF00);
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

        final Button checkLabel = (Button) rootView.findViewById(R.id.checkLabel);
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

        /////////////////////////////////////////....................................................////////////////////////
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //connectionStatusText.setText(bluetoothDevice.getName());
                //String a = String.valueOf(rssi);
                //connectionStatusText.setText(a);

            }
        });
        /////////////////////////////////////////....................................................////////////////////////

        if (!(bluetoothDevice.getName().equals("Abdullah"))) {
            bluetoothAdapter.startLeScan(new UUID[]{RFDService.UUID_SERVICE}, ActivityMonitor2.this);
        } else {


            /////////////////////////////////////////....................................................////////////////////////
            Intent rfduinoIntent = new Intent(this, RFDService.class);
            this.bindService(rfduinoIntent, rfduinoServiceConnection, Context.BIND_AUTO_CREATE);

            /////////////////////////////////////////....................................................////////////////////////
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

    public void startChronometer(View view) {
        ((Chronometer) findViewById(R.id.monitor_timer)).start();
    }

    public void stopChronometer(View view) {
        ((Chronometer) findViewById(R.id.monitor_timer)).stop();
    }


}






