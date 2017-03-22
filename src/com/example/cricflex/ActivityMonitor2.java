package com.example.cricflex;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.grantland.widget.AutofitTextView;

public class ActivityMonitor2 extends Activity implements BluetoothAdapter.LeScanCallback {

    private static Boolean exit = false;

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
    private TextView monitorLegalText;
    private static final String TAG = "MyActivity";

    private String in = new String();
    private String MacAdd = "yahooooooooooooooooooooooooooooo";

    boolean isBound = false;

    //other variables for new layout

    private int counterLegal=0,counterIllegal=0;

    private TextView monitorAngleValue;
    private TextView monitorLegalBalls;
    private TextView monitorIllegalBalls;
    private AutofitTextView monitorForce;
    private AutofitTextView monitorActionTime;
    private AutofitTextView monitorArmTwist;

    private Button monitorFinishButton;

    boolean monitoringTextChange = true;



    // check for metrics
    int metric_check=0;

    final ProgressDialog mProgressDialog = null ;

    //For Metrics Activity

    String armAngle_value;
    String actionTime_value;
    String armTwist_value;
    String force_value;




    //For Stats and home page
    DatabaseHelper helper = new DatabaseHelper(this);
    Player playerStats = new Player();
    String email;
    ArrayList<Integer> angleValues = new ArrayList<Integer>();
    ArrayList<Integer> forceValues = new ArrayList<Integer>();
    ArrayList<Integer> armTwistValues = new ArrayList<Integer>();
    ArrayList<Float> actionTimeValues = new ArrayList<Float>();


//    for history maintanance

    private DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;


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
//            unbindService(rfduinoServiceConnection);
            rfduinoService.send("r".getBytes());
            if (isBound)
                getApplicationContext().unbindService(rfduinoServiceConnection);
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

        monitorAngleValue = (TextView)findViewById(R.id.monitor_angle_text);
        monitorLegalBalls = (TextView)findViewById(R.id.monitor_legal_balls);
        monitorIllegalBalls = (TextView)findViewById(R.id.monitor_illegal_balls);
        monitorFinishButton = (Button)findViewById(R.id.monitor_finish_button);
    }



    class handlemonitorFinishButton implements OnClickListener {
        public void onClick(View v) {



            playerStats.setEmail(email);
            playerStats.setIllegalBowls(String.valueOf(counterIllegal));
            playerStats.setLegalBowls(String.valueOf(counterLegal));

            System.out.println("Legal Bowls: "+ playerStats.getLegalBowls()+"\nIllegal Bowls:" + playerStats.getIllegalBowls());
            System.out.println("Angle Values: \n"+ angleValues);


            //testing


//            angleValues.add(1);
//            angleValues.add(2);
//            angleValues.add(3);
//////
//            armTwistValues.add(4);
//            armTwistValues.add(5);
//            armTwistValues.add(6);
//////
//////
//            actionTimeValues.add((float)7);
//            actionTimeValues.add((float)8);
////            actionTimeValues.add((float)1);
//            actionTimeValues.add((float)9);
////////
//            forceValues.add(10);
//            forceValues.add(11);
//            forceValues.add(12);

//            angleValues.add(15);armTwistValues.add(4);
//            angleValues.add(16);
//            angleValues.add(18);
//            angleValues.add(12);


//            angleValues.add(15);



            //Angle Values for database

//            converting array into JSON

            JSONObject json = new JSONObject();
            try {
                json.put("angleArray", new JSONArray(angleValues));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String convertedArrayListOfAnglesToString = json.toString();

            JSONObject json1 = new JSONObject();
            try {
                json1.put("armTwistArray", new JSONArray(armTwistValues));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String convertedArrayListOfArmTwistToString = json1.toString();

            JSONObject json2 = new JSONObject();
            try {
                json2.put("forceArray", new JSONArray(forceValues));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String convertedArrayListOfForceToString = json2.toString();

            JSONObject json3 = new JSONObject();
            try {
                json3.put("actionTimeArray", new JSONArray(actionTimeValues));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String convertedArrayListOfActionTimeToString = json3.toString();

//            System.out.println("Arraylist : " + convertedArrayListOfAnglesToString);



            //checking date time
            Date curDate = new Date();
//            SimpleDateFormat format = new SimpleDateFormat();
            SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy");
            String DateToStr = format.format(curDate);

            System.out.println("date to store: " + DateToStr);




//            try {
//                Date strToDate = format.parse(DateToStr);
//                System.out.println("Reconverted String to Date: "+strToDate);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }



            //testing


            helper.changeAngleValues(email,convertedArrayListOfAnglesToString);
            helper.changeArmTwistValues(email,convertedArrayListOfArmTwistToString);
            helper.changeActionTimeValues(email,convertedArrayListOfActionTimeToString);
            helper.changeForceValues(email,convertedArrayListOfForceToString);







            helper.changeAngleValuesWithDate(email,convertedArrayListOfAnglesToString,DateToStr);
            helper.changeArmTwistValuesWithDate(email,convertedArrayListOfArmTwistToString,DateToStr);
            helper.changeActionTimeValuesWithDate(email,convertedArrayListOfActionTimeToString,DateToStr);
            helper.changeForceValuesWithDate(email,convertedArrayListOfForceToString,DateToStr);




            String angleValuesFromDatabase = helper.getAngleValues(email);
            ArrayList<String> ArrayListOfAngles = new ArrayList<String>();
            if(!angleValuesFromDatabase.equals("")) {
//        getting previous array list from string
                JSONObject jsonAngleValues = null;
                try {
                    jsonAngleValues = new JSONObject(angleValuesFromDatabase);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = jsonAngleValues.optJSONArray("angleArray");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        try {
                            ArrayListOfAngles.add(jsonArray.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


            String forceValuesFromDatabase = helper.getForceValues(email);
            ArrayList<String> ArrayListOfForces = new ArrayList<String>();
            if(!forceValuesFromDatabase.equals("")) {
//        getting previous array list from string
                JSONObject jsonForceValues = null;
                try {
                    jsonForceValues = new JSONObject(forceValuesFromDatabase);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = jsonForceValues.optJSONArray("forceArray");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        try {
                            ArrayListOfForces.add(jsonArray.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            String actionTimeValuesFromDatabase = helper.getActionTimeValues(email);
            ArrayList<String> ArrayListOfActionTime = new ArrayList<String>();
            if(!actionTimeValuesFromDatabase.equals("")) {
//        getting previous array list from string
                JSONObject jsonActionTimeValues = null;
                try {
                    jsonActionTimeValues = new JSONObject(actionTimeValuesFromDatabase);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = jsonActionTimeValues.optJSONArray("actionTimeArray");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        try {
                            ArrayListOfActionTime.add(jsonArray.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            String armTwistFromDatabase = helper.getArmTwistValues(email);
            ArrayList<String> ArrayListOfArmTwist = new ArrayList<String>();
            if(!armTwistFromDatabase.equals("")) {
//        getting previous array list from string
                JSONObject jsonArmTwistValues = null;
                try {
                    jsonArmTwistValues = new JSONObject(armTwistFromDatabase);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = jsonArmTwistValues.optJSONArray("armTwistArray");
                if (jsonArray != null) {
                    int len = jsonArray.length();
                    for (int i = 0; i < len; i++) {
                        try {
                            ArrayListOfArmTwist.add(jsonArray.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }




            FirebaseUser user = firebaseAuth.getCurrentUser();
            databaseReference.child("Metrics").child(user.getUid()).child("Email").setValue(email);
            databaseReference.child("Metrics").child(user.getUid()).child("Angle Values").setValue(ArrayListOfAngles);
            databaseReference.child("Metrics").child(user.getUid()).child("Force Values").setValue(ArrayListOfForces);
            databaseReference.child("Metrics").child(user.getUid()).child("Arm Twist Values").setValue(ArrayListOfArmTwist);
            databaseReference.child("Metrics").child(user.getUid()).child("Action Time Values").setValue(ArrayListOfActionTime);





            helper.insertPlayerStats(playerStats);
            helper.changeStatLegalIllegal(playerStats.getEmail(), playerStats.getLegalBowls(), playerStats.getIllegalBowls());


//            Disabling bluetooth connection
            if(mBluetoothAdapter==null){

            }
            else if (mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.disable();
            }

            Intent i = new Intent(ActivityMonitor2.this, ActivitySessionStats.class);

            Bundle extraBundle = new Bundle();
            extraBundle.putIntegerArrayList("angleValues", angleValues);
            extraBundle.putIntegerArrayList("armTwistValues", armTwistValues);
            extraBundle.putIntegerArrayList("forceValues", forceValues);
            i.putExtra("actionTimeValues",actionTimeValues);
            i.putExtras(extraBundle);

//            rfduinoService.send("r".getBytes());


            if (isBound)
                getApplicationContext().unbindService(rfduinoServiceConnection);
            rfduinoService = null;
            downgradeState(STATE_DISCONNECTED);
            ActivityMonitor2.this.startActivity(i);
            finish();


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor2);
        View decorView = getWindow().getDecorView();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        monitorFinishButton = (Button) findViewById(R.id.monitor_finish_button);
        monitorFinishButton.setOnClickListener(new handlemonitorFinishButton());

        monitorAngleValue = (TextView) findViewById(R.id.monitor_angle_text);

        monitorLegalText = (TextView) findViewById(R.id.monitor_status_text);



        monitorActionTime = (AutofitTextView) findViewById(R.id.monitor_action_time);
        monitorArmTwist = (AutofitTextView) findViewById(R.id.monitor_arm_twist);
        monitorForce = (AutofitTextView) findViewById(R.id.monitor_force);

        monitorLegalBalls = (TextView)findViewById(R.id.monitor_legal_balls);
        monitorIllegalBalls = (TextView)findViewById(R.id.monitor_illegal_balls);

        email = SaveSharedPreference.getEmail(ActivityMonitor2.this);
        System.out.println("Username: "+ email);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);

            scanStarted = true;

            // mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
            // mLEScanner.startScan(filters, settings, mScanCallback);


//            bluetoothAdapter.startLeScan(
//                    new UUID[]{RFDService.UUID_SERVICE},
//                    ActivityMonitor2.this);
        }





        scanStarted = true;

        // mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
        // mLEScanner.startScan(filters, settings, mScanCallback);
        bluetoothAdapter.startLeScan(
                new UUID[]{RFDService.UUID_SERVICE},
                ActivityMonitor2.this);



    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
        else {
            Intent i = new Intent(ActivityMonitor2.this, ActivityMain.class);
            ActivityMonitor2.this.startActivity(i);
            finish();
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


//        BluetoothAdapter.getDefaultAdapter().closeProfileProxy(BluetoothProfile.A2DP,sBluetoothA2dp);
//        rfduinoService.send("r".getBytes());

//        unbindService(rfduinoService);
//        if (isBound)
//            getApplicationContext().unbindService(rfduinoServiceConnection);
        bluetoothAdapter.stopLeScan(this);
        unregisterReceiver(scanModeReceiver);
        unregisterReceiver(bluetoothStateReceiver);
        unregisterReceiver(rfduinoReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        unbindService(rfduinoServiceConnection);

//        if (isBound)
//            getApplicationContext().unbindService(rfduinoServiceConnection);
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

        monitorAngleValue.setTextColor(0xFFFFFFFF);
//        monitorAngleValue.setText(Integer.toString(genFlex) + "\u00b0");

        if (monitoringTextChange)
            monitorLegalText.setText("Monitoring");

        monitorLegalText.setTextColor(0xFFFFFFFF);


//            String s1 = new String(data);
//            String[] result = s1.split(",");
//
//
//
//
//            armAngle_value=result[0];
//            armSpeed_value=result[1];
//            armTwist_value=result[2];
//            force_value=result[3];
//            acttionTime_value=result[4];
//
//            if(result[0].equals("-"))
//            {
//
//            }


        int value = 0;
        for (int i = 0; i < data.length; i++) {
            value += ((int) data[i] & 0xffL) << (8 * i);
        }


        System.out.print("Receiving: " + value);
//            int[] values = new int[10];
//
//            for (int i=0 ; i<1 ; i++){
//                for (int j=0 ; j<2 ; j++){
//                    values[i] += ((int) data[j + (i*2)] & 0xffL) << (8 * j );
//                }
//            }

//            System.out.println("Value: "+ value);
        int incoming = (int) value;


        if (metric_check == 0) {


            if (once == 0 || once == 1) {
                if (incoming == 120) {
                    monitorLegalText.setText("Bend arm at 45");
                    once = 1;
                } else if (incoming == 119) {
                    monitorLegalText.setText("Monitoring...");
                    once = 2;
                }

            } else if (incoming == 118) {
//                Toast.makeText(ActivityMonitor2.this, "Bowl" , Toast.LENGTH_SHORT).show();
                monitorLegalText.setText("Bowl");
                monitoringTextChange = false;

//                System.out.println("118 received and this is test print");
                rfduinoService.send("a".getBytes());

            }

            // int incoming =(int)((data[1] << 8) + (data[0] & 0xFF));
            else if (-128 <= incoming && incoming <= 127) {     //normal values
//                firstnegativevalueafterpositive=true;
                String in = new String();
                in = String.valueOf(value);
//                monitorAngleValue.setText(in + "\u00b0");
            }

            else if (272 <= incoming && incoming <= 527) {                //ball release check
                String in = new String();
                in = String.valueOf(value - 400);
//                monitorAngleValue.setText(in + "\u00b0");
//                    rfduinoService.send("a".getBytes());

                genFlex = incoming - 400;

                metric_check=1;
                armAngle_value=Integer.toString(genFlex);
                Log.e(TAG, "armAngle_value value= "+armAngle_value);
                Log.e(TAG, "metric check value= "+ Integer.toString(metric_check));






            }

        }

        else if(metric_check==1)
        {
            force_value=Integer.toString(incoming*4);           // arm mass = 4kg
            Log.e(TAG, "force_value value= "+force_value);
            metric_check=2;
        }
//            else if(metric_check==2)
//            {
//
//                float temp1 = incoming;
//
//                armSpeed_value=Float.toString(temp1*3.6f);      // 1 m/s = 3.6 kph
//                //armSpeed_value=Integer.toString(incoming*4);
//                Log.e(TAG, "armSpeed_value value= "+armSpeed_value);
//                metric_check=3;
//            }
        else if(metric_check==2)
        {
            float temp=incoming;
            actionTime_value=Float.toString(temp/1000f);  //millisecond to second


            //actionTime_value=Integer.toString(incoming/1000);
            Log.e(TAG, "actionTime_value value= "+actionTime_value);
            metric_check=3;
        }
        else if(metric_check==3)
        {
            armTwist_value=Integer.toString(incoming);
            metric_check=0;


            Log.e(TAG, "armtwist value= "+armAngle_value);


            final ProgressDialog dialog = new MyCustomProgressDialog(ActivityMonitor2.this);

            dialog.setCancelable(false);
            dialog.show();



            Runnable progressRunnable = new Runnable() {

                @Override
                public void run() {
                    dialog.cancel();
                }
            };

            Handler pdCanceller = new Handler();
            pdCanceller.postDelayed(progressRunnable, 1024);

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {


                if (genFlex > 15) {
                    //stopButtonPressed = true;

                    monitorIllegalBalls.setText(String.valueOf(++counterIllegal));
                    monitorAngleValue.setTextColor(0xFFFF0000);
//                    monitorAngleValue.setText(Integer.toString(genFlex) + "\u00b0");
                    monitorLegalText.setText("Illegal");
                    monitorLegalText.setTextColor(0xFFFF0000);
                } else {
                    monitorLegalBalls.setText(String.valueOf(++counterLegal));
                    //stopButtonPressed = true;
                    monitorAngleValue.setTextColor(0xFF00FF00);
//                    monitorAngleValue.setText(Integer.toString(genFlex) + "\u00b0");
                    //degreeSign.setTextColor(0xFF00FF00);
                    //monitorAngleValue.setTextSize(200);
                    monitorLegalText.setText("Legal");
                    monitorLegalText.setTextColor(0xFF00FF00);
                }




                angleValues.add(genFlex);
                forceValues.add(Integer.parseInt(force_value));
                armTwistValues.add(Integer.parseInt(armTwist_value));
                actionTimeValues.add(Float.valueOf(actionTime_value));



//                showAlertDialog(this, "Feedback recieved", "Thank you for your FeedBack :):):)", true);
                monitorAngleValue.setText(Integer.toString(genFlex) + "\u00b0");
                monitorArmTwist.setText(armTwist_value);
                monitorForce.setText(force_value);
                monitorActionTime.setText(actionTime_value);
                }
            });

        }

    }

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

        ActivityMonitor2.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //connectionStatusText.setText(bluetoothDevice.getName());
                String a = String.valueOf(rssi);
                //connectionStatusText.setText(a);
            }
        });


        //####################################################################################################################################################################################################

        if (!(bluetoothDevice.getName().equals("Abdullah"))) {
            bluetoothAdapter.startLeScan(new UUID[]{RFDService.UUID_SERVICE}, ActivityMonitor2.this);
        } else {
            Intent rfduinoIntent = new Intent(ActivityMonitor2.this, RFDService.class);
//            bindService(rfduinoIntent, rfduinoServiceConnection, BIND_AUTO_CREATE);
            isBound = getApplicationContext().bindService(rfduinoIntent, rfduinoServiceConnection, BIND_AUTO_CREATE);
        }

        //####################################################################################################################################################################################################

    }


    @Override
    public void onBackPressed() {

//        rfduinoService.send("r".getBytes());

//        rfduinoService.send("r".getBytes());
        if (isBound)
            getApplicationContext().unbindService(rfduinoServiceConnection);
        rfduinoService = null;
        downgradeState(STATE_DISCONNECTED);



        rfduinoService = null;
        downgradeState(STATE_DISCONNECTED);
        if(mBluetoothAdapter==null){

        }
        else if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
        }
        Intent i = new Intent(ActivityMonitor2.this, ActivityMain.class);

        ActivityMonitor2.this.startActivity(i);
        finish();

    }
}
