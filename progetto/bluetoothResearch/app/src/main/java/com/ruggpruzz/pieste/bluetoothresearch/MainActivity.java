package com.ruggpruzz.pieste.bluetoothresearch;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt =null;
    private BluetoothAdapter btAdapter;
    public  int staticrssi=0;
    public    int hash =  1001190065;
    public CheckBox refreshcheck;
    static boolean refreshbool = true;
 static String statechange ="Disconneting";
    // Various callback methods defined by the BLE API.
    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                    staticrssi = rssi;
                }
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    super.onConnectionStateChange(gatt, status, newState);
                    statechange =gatt.EXTRA_STATE;

                    }


                    };


//QUESTO DOVREBBE DIVENTARE IL onlescanCALLBACK
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public  void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Add the name and address to an array adapter to show in a ListView
                Toast.makeText(MainActivity.this, device.getName() + " " + device.hashCode(), Toast.LENGTH_LONG).show();
                if ((Math.abs(device.hashCode())== hash))
                {
                    mBluetoothGatt= device.connectGatt(context,false, mGattCallback);

                }
            }
        }
    };


    //qui viene creato il broadcast receiver e inpostato per ascoltare il bluetooth e i suoi stati generalizzati
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            TextView req = (TextView) findViewById(R.id.textView2);
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                //qui uno switch con le varie reazioni che si vuole dare a seconda di quello che sente il broadcast receiver
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        req.setText("Bluetooth disabled!");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        req.setText("Bluetooth disabled!");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        req.setText("Bluetooth abled!");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        req.setText("Bluetooth abled!");
                        break;
                }
            }
        }
    };

    protected void refreshrssi() {

        if (refreshbool) {
            try {
                mBluetoothGatt.readRemoteRssi();
                TextView reqrssi = (TextView) findViewById(R.id.textView8);
                reqrssi.setText("RSSI " + staticrssi);
            } catch (NullPointerException e) {
             //   Toast.makeText(MainActivity.this, "GATT d'ont connected", Toast.LENGTH_LONG).show();
            }


        }
    }

    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((Build.VERSION.SDK_INT < 21) && (Build.VERSION.SDK_INT >17))

        {
            SharedPreferences settings = getSharedPreferences("hash", hash);


            hash = settings.getInt("hash", 1001190065);

            new CountDownTimer(1000, 1000) {

                public void onTick(long millisUntilFinished) {

                }

                public void onFinish() {
                    refreshrssi();
                    start();
                }
            }.start();

            setContentView(R.layout.activity_main);
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            //qui nel onCreate la memorizzazione formale di stati precedenti per il controllo di cambio
            //degli stati
            IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, filter1);
            IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter2);
            TextView tv = (TextView) findViewById(R.id.textView);
            TextView req = (TextView) findViewById(R.id.textView2);
            //qui il controllo con textView e toast riguardo la verifica di esistenza del bluetooth interno
            if (mBluetoothAdapter == null) {
                tv.setText("HD not found");
                Toast.makeText(MainActivity.this, "Internal bluetooth not found! ", Toast.LENGTH_SHORT).show();
            } else {
                tv.setText("HD found");
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                    req.setText("Bluetooth abled!");
                } else {
                    mBluetoothAdapter.disable();
                    req.setText("Bluetooth disabled!");
                }
            }
            TextView hashtext = (TextView) findViewById(R.id.textView11);
            hashtext.setText("" + hash);
            //qui l'ascolto al tasto per abilitare o disabilitare il bluetooth dall'interno
            //dell'aplicazione
            Button btnHome = (Button) findViewById(R.id.button);
            btnHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    TextView req = (TextView) findViewById(R.id.textView2);
                    if (!mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.enable();
                        req.setText("Bluetooth abled!");

                    } else {
                        mBluetoothAdapter.disable();
                        req.setText("Bluetooth disabled!");
                    }
                }
            });
            Button savehash = (Button) findViewById(R.id.button4);
            savehash.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        TextView hashsave = (EditText) findViewById(R.id.editText);
                        hash = Integer.parseInt(hashsave.getText().toString());
                        SharedPreferences settings = getSharedPreferences("hash", hash);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt("hash", hash);

                        editor.commit();
                        TextView req = (TextView) findViewById(R.id.textView11);
                        req.setText("" + hash);
                        mBluetoothAdapter.disable();
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "invalid insert", Toast.LENGTH_LONG).show();
                    }

                }

            });
            refreshcheck = (CheckBox) findViewById(R.id.checkBox);
            refreshcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            if (isChecked)

                                                            {
                                                                refreshbool = true;
                                                            } else {
                                                                refreshbool = false;
                                                            }

                                                        }
                                                    }
            );
            Button rssirefresh = (Button) findViewById(R.id.button3);
            rssirefresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    try {
                        mBluetoothGatt.readRemoteRssi();
                        TextView reqrssi = (TextView) findViewById(R.id.textView8);
                        reqrssi.setText("RSSI " + staticrssi);

                    } catch (NullPointerException e) {
                     //   Toast.makeText(MainActivity.this, "GATT d'ont connected", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
        else
        {
            int buildi =Build.VERSION.SDK_INT;
            Toast.makeText(MainActivity.this," "+buildi, Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this, "API correctly are :(17<API<21)", Toast.LENGTH_LONG).show();

        }
    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            load();
        }

    public void scan(View v) {
        if (!btAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
        } else
            load();
    }

    private void load() {
        TextView reqrssi = (TextView) findViewById(R.id.textView9);
        reqrssi.setText("Scanning ");

        btAdapter.startDiscovery();
        new CountDownTimer(12000, 1000) {
            int punti = 0;
            TextView reqrssi = (TextView) findViewById(R.id.textView9);
            public void onTick(long millisUntilFinished) {
                if (punti ==0) {
                    reqrssi.setText("Scanning ");
                     punti+=1;
                }
                else if (punti ==1) {
                    reqrssi.setText("Scanning. ");
                    punti += 1;
                }
                else if (punti ==2) {
                    reqrssi.setText("Scanning.. ");
                    punti += 1;
                }
                else if (punti ==3) {
                    reqrssi.setText("Scanning... ");
                    punti = 0;
                }
            }

            public void onFinish() {

                reqrssi.setText("Not scanning ");
            }
        }.start();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView req = (TextView) findViewById(R.id.textView10);

                   req.setText(""+statechange);

            }
        });
    }

    //qui la distruzione
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mReceiver);
        mBluetoothAdapter.disable();
    }
}







