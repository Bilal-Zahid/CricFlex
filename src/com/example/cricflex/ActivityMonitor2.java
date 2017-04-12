package com.example.cricflex;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.DateFormat;
import java.util.Date;


@TargetApi(21)
public class ActivityMonitor2 extends Activity {

    private static Boolean exit = false;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    LocationManager locationManager;
    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private static final long SCAN_PERIOD = 10000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;
    private Handler mHandler;
    private BluetoothDevice bluetoothDevice;
    private RFDService rfduinoService;

    // State machine
    public static int genFlex = 0;
    final private static int STATE_BLUETOOTH_OFF = 1;
    final private static int STATE_DISCONNECTED = 2;
    final private static int STATE_CONNECTING = 3;
    final private static int STATE_CONNECTED = 4;
    public static int once = 0;
    private int state;
    private boolean connectedToBand = false;
    private boolean calibrationDone = false;
    private boolean monitoring = false;



    private static final String TAG = "MyActivity";

    boolean isBound = false;

    //other variables for new layout

    private int counterLegal=0,counterIllegal=0;


    ProgressBar batteryProgressBar;
    TextView batteryProgressText;
    ImageView ble_indicator;
    private TextView monitorStatusText;
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

            System.out.println("inside BroadcastReceiver bluetoothStateReceiver");

            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);

            if (state == BluetoothAdapter.STATE_ON) {
                upgradeState(STATE_DISCONNECTED);

            }
            else if (state == BluetoothAdapter.STATE_OFF) {
                downgradeState(STATE_BLUETOOTH_OFF);
                bluetoothDisabled();
            }
        }
    };

    private final BroadcastReceiver BleConnectionReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("inside BroadcastReceiver BleConnectionReceiver");

            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);


            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                //Device is now connected
            }

            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                //Device has disconnected
                bandDisconnected();
            }

//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                //Device found
//            }
//
//            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                //Done searching
//            }
//            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
//                //Device is about to disconnect
//            }

        }
    };
    private final BroadcastReceiver scanModeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("inside BroadcastReceiver scanModeReceiver");

        }
    };

    private final ServiceConnection bleServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {


            System.out.println("inside ServiceConnection bleServiceConnection");

            rfduinoService = ((RFDService.LocalBinder) service).getService();
            if (rfduinoService.initialize()) {
                if (rfduinoService.connect(bluetoothDevice.getAddress())) {
                    upgradeState(STATE_CONNECTING);

                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {


            System.out.println("inside onServiceDisconnected");
            if (isBound)
                getApplicationContext().unbindService(bleServiceConnection);

            if(rfduinoService != null){
                rfduinoService.disconnect();
                rfduinoService.close();
                rfduinoService.stopSelf();
                rfduinoService = null;
            }

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
                ble_indicator.setImageResource(R.drawable.bluetooth_connected_icon);
                connectedToBand = true;
                scanLeDevice(false);
                monitorStatusText.setText(R.string.straighten_arm);        ////////////////////
                once = 0;
            }
            else if (RFDService.ACTION_DISCONNECTED.equals(action)) {
                ble_indicator.setImageResource(R.drawable.bluetooth_disconnected_icon);
                Toast.makeText(getApplicationContext(), "Disconnected from CricFlex Band", Toast.LENGTH_SHORT).show();
                monitorStatusText.setText(R.string.monitor_connecting);
                connectedToBand = false;
                downgradeState(STATE_DISCONNECTED);
            }
            else if (RFDService.ACTION_DATA_AVAILABLE.equals(action)) {

//                intent.getDataString();
//                convert(intent.getByteArrayExtra(RFDService.EXTRA_DATA));
//                dataRecieved(intent.getByteArrayExtra(RFDService.EXTRA_DATA));
//                addData(intent.getByteArrayExtra(RFDService.EXTRA_DATA));
                viewData(intent.getByteArrayExtra(RFDService.EXTRA_DATA));
            }
        }
    };

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


        batteryProgressBar = (ProgressBar)findViewById(R.id.battery_progress);
        batteryProgressText = (TextView)findViewById(R.id.battery_text);
        ble_indicator = (ImageView)findViewById(R.id.monitor_ble_icon);
        ble_indicator.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String abc = "123";
//                rfduinoService.send(abc);
                rfduinoService.send(abc.getBytes());
            }
        });

        monitorFinishButton = (Button) findViewById(R.id.monitor_finish_button);
        monitorFinishButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                endSession();
            }
        });

        monitorAngleValue = (TextView) findViewById(R.id.monitor_angle_text);
        monitorStatusText = (TextView) findViewById(R.id.monitor_status_text);
        monitorActionTime = (AutofitTextView) findViewById(R.id.monitor_action_time);
        monitorArmTwist = (AutofitTextView) findViewById(R.id.monitor_arm_twist);
        monitorForce = (AutofitTextView) findViewById(R.id.monitor_force);

        monitorLegalBalls = (TextView)findViewById(R.id.monitor_legal_balls);
        monitorIllegalBalls = (TextView)findViewById(R.id.monitor_illegal_balls);

        email = SaveSharedPreference.getEmail(ActivityMonitor2.this);
        System.out.println("Username: "+ email);

//        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported", Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        locationManager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


        monitorStatusText.setText(R.string.monitor_connecting);
        monitorStatusText.setTextColor(0xFFFFFFFF);


    }  /// END onCreate


    @Override
    protected void onResume() {
        super.onResume();

        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            displayLocationSettingsRequest(getApplicationContext());    // ask user to turn on location
        }

        else
            {

            if (Build.VERSION.SDK_INT >= 21)

                {
                    mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                    settings = new ScanSettings.Builder()
                            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)    ///This mode uses the highest power, when compared to other modes. Also detects the BLE devices fastest, hence should be used when the app is in foreground
                            .build();
                    filters = new ArrayList<ScanFilter>();
            }
            System.out.println("scanLeDevice(true) called in onResume() ");
            scanLeDevice(true);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            System.out.println("scanLeDevice(false) called in onPause() ");
            scanLeDevice(false);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

//        System.out.println("scanLeDevice(false) called in onDestroy() ");
//        scanLeDevice(false);

        connectedToBand=false;

        if(rfduinoService!=null){
            rfduinoService.disconnect();
            rfduinoService.close();
            rfduinoService.stopSelf();
            rfduinoService=null;
        }


        unregisterReceiver(scanModeReceiver);
        unregisterReceiver(bluetoothStateReceiver);
        unregisterReceiver(rfduinoReceiver);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scanLeDevice(final boolean enable) {
        System.out.println("inside scanLeDevice()");
        if (enable) {

            System.out.println("SCANNING TRUE");

            if (Build.VERSION.SDK_INT < 21) {

                if(mBluetoothAdapter != null && mBluetoothAdapter.isEnabled())
                    mBluetoothAdapter.startLeScan(new UUID[]{RFDService.UUID_SERVICE}, mLeScanCallback);

            } else {

                if(mLEScanner != null)
                    mLEScanner.startScan(mScanCallback);

            }
        }
        else {
            System.out.println("SCANNING FALSE");

            if (Build.VERSION.SDK_INT < 21) {

                if(mBluetoothAdapter != null && mBluetoothAdapter.isEnabled())
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }

            else {

                if(mLEScanner != null)
                    mLEScanner.stopScan(mScanCallback);

            }
        }
    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("callbackType", String.valueOf(callbackType));
            Log.i("result", result.toString());
            System.out.println("ScanCallback mScanCallback device name:" + result.getDevice().getName());
            BluetoothDevice device = result.getDevice();
            if (device.getName() != null ) {

                if (device.getName().equals("Abdullah")) {

                    bluetoothDevice = device;
                    Intent rfduinoIntent = new Intent(ActivityMonitor2.this, RFDService.class);
                    isBound = getApplicationContext().bindService(rfduinoIntent, bleServiceConnection, BIND_AUTO_CREATE);
                    System.out.println("scanLeDevice(false) called in ScanCallback mScanCallback ");
                    scanLeDevice(false);

                }
                else {
                    System.out.println("scanLeDevice(true) called in ScanCallback mScanCallback ");
                    scanLeDevice(true);
                }
            }
            else{
                System.out.println("scanned device does not have name ");
            }

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {


                    mBluetoothAdapter.stopLeScan(this);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("onLeScan", device.toString());

                            if(device.getName() != null) {

                                if (device.getName().equals("Abdullah")) {


                                    bluetoothDevice = device;
                                    Intent rfduinoIntent = new Intent(ActivityMonitor2.this, RFDService.class);
                                    isBound = getApplicationContext().bindService(rfduinoIntent, bleServiceConnection, BIND_AUTO_CREATE);
                                    scanLeDevice(false);
                                } else {
                                    System.out.println("scanLeDevice(true) called in BluetoothAdapter.LeScanCallback mLeScanCallback ");
                                    scanLeDevice(true);
                                }
                            }
                            else{
                                System.out.println("scanned device does not have name ");
                            }

                        }
                    });
                }
            };
    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(scanModeReceiver, new IntentFilter(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED));
        registerReceiver(bluetoothStateReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
        registerReceiver(BleConnectionReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
        registerReceiver(BleConnectionReceiver, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        registerReceiver(rfduinoReceiver, RFDService.getIntentFilter());
        updateState(mBluetoothAdapter.isEnabled() ? STATE_DISCONNECTED : STATE_BLUETOOTH_OFF);

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

    @Override
    public void onBackPressed() {


        connectedToBand=false;

        if(rfduinoService!=null)
            rfduinoService.close();
        Intent i = new Intent(ActivityMonitor2.this, ActivityMain.class);

        ActivityMonitor2.this.startActivity(i);
        finish();

    }

    void bandDisconnected(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Session Ended !");
        alertDialog.setMessage("This session has ended due to disconnection from the Band. Please make sure that the band is turned on and in range.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        endSession();
                        dialog.dismiss();
                    }
                }); // OK button listener end

        alertDialog.show();
    }
    void bluetoothDisabled(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Session Ended !");
        alertDialog.setMessage("This session has ended because your bluetooth is turned off. Please make sure that your bluetooth is turned on and start a new session.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        endSession();
                        dialog.dismiss();
                    }
                    }); // OK button listener end

        alertDialog.show();
    }

    void endSession(){

        playerStats.setEmail(email);
        playerStats.setIllegalBowls(String.valueOf(counterIllegal));
        playerStats.setLegalBowls(String.valueOf(counterLegal));

        System.out.println("Legal Bowls: "+ playerStats.getLegalBowls()+"\nIllegal Bowls:" + playerStats.getIllegalBowls());
        System.out.println("Angle Values: \n"+ angleValues);


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

        Intent i = new Intent(ActivityMonitor2.this, ActivitySessionStats.class);

        Bundle extraBundle = new Bundle();
        extraBundle.putIntegerArrayList("angleValues", angleValues);
        extraBundle.putIntegerArrayList("armTwistValues", armTwistValues);
        extraBundle.putIntegerArrayList("forceValues", forceValues);
        i.putExtra("actionTimeValues",actionTimeValues);
        i.putExtras(extraBundle);

        connectedToBand=false;

        if (isBound)
            getApplicationContext().unbindService(bleServiceConnection);

        if(rfduinoService != null){
            rfduinoService.disconnect();
            rfduinoService.close();
            rfduinoService.stopSelf();
            rfduinoService = null;
        }
        downgradeState(STATE_DISCONNECTED);


        ActivityMonitor2.this.startActivity(i);
        finish();



    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(ActivityMonitor2.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    void addData(byte[] data) {

        int value = 0;
        for (int i = 0; i < data.length; i++) {
            value += ((int) data[i] & 0xffL) << (8 * i);
        }


//        monitorAngleValue.setText(String.valueOf(value));
        System.out.print("Receiving: " + value);

        int incoming = (int) value;


        if (metric_check == 0) {


            if (once == 0 || once == 1) {
                if (incoming == 120) {
                    monitorStatusText.setText("Bend arm at 45");
                    once = 1;
                } else if (incoming == 119) {
                    monitorStatusText.setText("Monitoring...");
                    once = 2;
                }

            } else if (incoming == 118) {
//                Toast.makeText(ActivityMonitor2.this, "Bowl" , Toast.LENGTH_SHORT).show();
                monitorStatusText.setText("Bowl");
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


            final ProgressDialog dialog = new MyCustomProgressDialog(ActivityMonitor2.this, R.style.AnimDialogTheme);

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
                    monitorIllegalBalls.setText(String.valueOf(++counterIllegal));
                    monitorAngleValue.setTextColor(0xFFFF0000);
                    monitorStatusText.setText("Illegal");
                    monitorStatusText.setTextColor(0xFFFF0000);
                } else {
                    monitorLegalBalls.setText(String.valueOf(++counterLegal));
                    monitorAngleValue.setTextColor(0xFF00FF00);
                    monitorStatusText.setText("Legal");
                    monitorStatusText.setTextColor(0xFF00FF00);
                }

                angleValues.add(genFlex);
                forceValues.add(Integer.parseInt(force_value));
                armTwistValues.add(Integer.parseInt(armTwist_value));
                actionTimeValues.add(Float.valueOf(actionTime_value));

                monitorAngleValue.setText(Integer.toString(genFlex) + "\u00b0");
                monitorArmTwist.setText(armTwist_value);
                monitorForce.setText(force_value);
                monitorActionTime.setText(actionTime_value);
                }
            });

        }

    }

    void viewData(byte[] data){

        String temp = bytesToHex(data);
        System.out.println("bytes recieving: " + temp);
        String msg;
        //            final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for(byte byteChar : data)
                stringBuilder.append(String.format("%02X ", byteChar));

            msg = new String(data) + "\n" + stringBuilder.toString();
            monitorAngleValue.setText(msg);
            System.out.println("string receiving: " + msg);
        }
    }

    public static String bytesToHex(byte[] data) {
        return bytesToHex(data, 0, data.length);
    }

    public static String bytesToHex(byte[] data, int offset, int length) {
        if (length <= 0) {
            return "";
        }

        StringBuilder hex = new StringBuilder();
        for (int i = offset; i < offset + length; i++) {
            hex.append(String.format(" %02X", data[i] % 0xFF));
        }
        hex.deleteCharAt(0);
        return hex.toString();
    }


    void dataRecieved(byte[] temp){




        try {

        String data = new String(temp,"UTF-8");
        System.out.println("data recieved: " + data);

        String[] parts = data.split("-");    // split string into 2 parts separated by -


        String part1 = parts[0];    // first part
        System.out.println("part1: " + part1);

        String part2 = parts[1];    // second part
        System.out.println("part2" + part2);


//        System.out.println("data recieved: " + data);
//        System.out.println("part1: " + part1);
//        System.out.println("part2" + part2);

        if(!monitoring) {

            if (part1.equals("bat")) {
                System.out.println("setting battery progress: " + part2);
                batteryProgressBar.setProgress(Integer.parseInt(part2));
                batteryProgressText.setText(part2);
            }
            else if (part1.equals("cal")) {

                System.out.println("calibrating");

                if (part2.equals("1")) {

                    System.out.println("straight arm");
                    monitorStatusText.setText(R.string.straighten_arm);

                }
                else if (part2.equals("2")) {

                    System.out.println("bend arm");
                    monitorStatusText.setText(R.string.bend_arm_45);

                }
                else if (part2.equals("done")) {

                    System.out.println("calibration done");
                    calibrationDone = true;
                    monitoring=true;
                    Toast.makeText(getApplicationContext(), "Calibration Done", Toast.LENGTH_SHORT).show();

                }

            }
        }
        else if(monitoring){


            monitorStatusText.setText("Monitoring...");
            monitorStatusText.setTextColor(0xFFFFFFFF);

            if(part1.equals("ang")){

                System.out.println("armAngle_value set: " + part2);
                armAngle_value=part2;
            }
            else if(part1.equals("frc")){

                System.out.println("force_value set: " + part2);
                force_value=part2;
            }
            else if(part1.equals("tim")){

                System.out.println("actionTime_value set: " + part2);
                actionTime_value=part2;
            }
            else if(part1.equals("tws")){

                System.out.println("armTwist_value set: " + part2);
                armTwist_value=part2;
                updateMetrics();
            }


//            if(metric_check==1){
//                armAngle_value=data;
//                metric_check=2;
//            }
//            else if(metric_check==2){
//                force_value=data;
//                metric_check=3;
//            }
//            else if(metric_check==3){
//                actionTime_value=data;
//                metric_check=4;
//            }
//            else if(metric_check==4){
//                armTwist_value=data;
//                metric_check=1;
//                updateMetrics();
//            }


        }

        }catch(IOException ex) {
            //Do something witht the exception
            System.out.println("NOT RIGHT ENCODING!!!");
        }

    }

    void updateMetrics(){

        System.out.println("updating metrics");

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


                if (Integer.parseInt(armAngle_value) > 15) {
                    //stopButtonPressed = true;

                    monitorIllegalBalls.setText(String.valueOf(++counterIllegal));
                    monitorAngleValue.setTextColor(0xFFFF0000);
//                    monitorAngleValue.setText(Integer.toString(genFlex) + "\u00b0");
                    monitorStatusText.setText("Illegal");
                    monitorStatusText.setTextColor(0xFFFF0000);
                } else {
                    monitorLegalBalls.setText(String.valueOf(++counterLegal));
                    //stopButtonPressed = true;
                    monitorAngleValue.setTextColor(0xFF00FF00);
//                    monitorAngleValue.setText(Integer.toString(genFlex) + "\u00b0");
                    //degreeSign.setTextColor(0xFF00FF00);
                    //monitorAngleValue.setTextSize(200);
                    monitorStatusText.setText("Legal");
                    monitorStatusText.setTextColor(0xFF00FF00);
                }

                angleValues.add(Integer.parseInt(armAngle_value));
                forceValues.add(Integer.parseInt(force_value));
                armTwistValues.add(Integer.parseInt(armTwist_value));
                actionTimeValues.add(Float.valueOf(actionTime_value));



//                showAlertDialog(this, "Feedback recieved", "Thank you for your FeedBack :):):)", true);
                monitorAngleValue.setText(armAngle_value + "\u00b0");
                monitorArmTwist.setText(armTwist_value + "\u00b0");
                monitorForce.setText(force_value + "N");
                monitorActionTime.setText(actionTime_value + "s");
            }
        });

    }



    void convert(byte[] data) {

        System.out.println(data);

        int value = 0;
        for (int i = 0; i < data.length; i++) {
            value += ((int) data[i] & 0xffL) << (8 * i);
        }
        System.out.print("int Receiving: " + String.valueOf(value));


        String msg;
        //            final byte[] data = characteristic.getValue();
        if (data != null && data.length > 0) {
            final StringBuilder stringBuilder = new StringBuilder(data.length);
            for(byte byteChar : data)
                stringBuilder.append(String.format("%02X ", byteChar));

            msg = new String(data) + "\n" + stringBuilder.toString();
            monitorAngleValue.setText(msg);
            System.out.println("string receiving: " + msg);
        }

//        try {
//
//
//            String data1 = new String(data, "US-ASCII");
//            String data2 = new String(data, "ISO-8859-1");
//            String data3 = new String(data, "UTF-8");
//            String data4 = new String(data, "UTF-16BE");
//            String data5 = new String(data, "UTF-16LE");
//            String data6 = new String(data, "UTF-16");
//            String data7 = new String(data, "Windows-1252");
//
//
//
//            System.out.println("data recieved: " + data1);
//            System.out.println("data recieved: " + data2);
//            System.out.println("data recieved: " + data3);
//            System.out.println("data recieved: " + data4);
//            System.out.println("data recieved: " + data5);
//            System.out.println("data recieved: " + data6);
//            System.out.println("data recieved: " + data7);
////            monitorStatusText.setText(data1);
//        }
//        catch (IOException ex){}
//
////        CharBuffer cBuffer = ByteBuffer.wrap(abc).asCharBuffer();
////        System.out.println("data recieved: " + cBuffer.toString());
////        monitorStatusText.setText(cBuffer.toString());

    }


}
