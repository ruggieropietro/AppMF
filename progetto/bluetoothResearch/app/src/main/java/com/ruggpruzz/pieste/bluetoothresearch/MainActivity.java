package com.ruggpruzz.pieste.bluetoothresearch;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt =null;
    private BluetoothAdapter btAdapter;
    public  int staticrssi=0;
    public static int stateconnected=0;
    public final static String gattconnected ="connection gate";
    public final static String gattdisconnected ="disconnection gate";


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
                    String intentAction;
                    if (status != BluetoothGatt.GATT_SUCCESS) {
                        gatt.disconnect();
                        return;
                    }
                    if ((newState == BluetoothProfile.STATE_CONNECTED ))
                    {
                        mBluetoothGatt.readRemoteRssi();
                        intentAction = gattconnected;
                        broadcastUpdate(intentAction);

                    }
                    else if ((newState == BluetoothProfile.STATE_DISCONNECTED ))
                    {
                        intentAction = gattdisconnected;
                        broadcastUpdate(intentAction);
                        gatt.close();
                    }
                }

                    };
    //QUESTO BROADCAST UPDATE SERVE A FARE COMUNICARE IL GATTCALBAC (onconnectionstatechange)K E E L'ACTIVITY PER CAMBIARE LA
    //STRINGA 10,QUELLA DOVE C'è SCRITTO LO STATO DELLA CONNESSIONE
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
        TextView gattconn = (TextView) findViewById(R.id.textView10);
        Toast.makeText(MainActivity.this,"we are here2" + action, Toast.LENGTH_LONG).show();
        if (action=="connection gate") {
            gattconn.setText("Connecting");
        }
        else
        {
            gattconn.setText("disconetting");
        }

    }
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
                if ((device.hashCode() == 676210690)|| (device.hashCode() == -1001190065))
                {
                    mBluetoothGatt= device.connectGatt(context, false, mGattCallback);
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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        //qui nel onCreate la memorizzazione formale di stati precedenti per il controllo di cambio
        //degli stati
        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver1, filter1);
        IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter2);
        TextView gattconn = (TextView) findViewById(R.id.textView10);
        if (stateconnected==1) {
            gattconn.setText("Connecting");
        }
        else
        {
            gattconn.setText("disconetting");
        }
            TextView tv = (TextView) findViewById(R.id.textView);
        TextView req = (TextView) findViewById(R.id.textView2);
        //qui il controllo con textView e toast riguardo la verifica di esistenza del bluetooth interno
        if (mBluetoothAdapter == null) {
            tv.setText("Internal Bluetooth not found!");
            Toast.makeText(MainActivity.this, "Internal bluetooth not found! ", Toast.LENGTH_SHORT).show();
        } else {
            tv.setText("InternalBluetooth  found!");
            if (!mBluetoothAdapter.isEnabled()) {
                mBluetoothAdapter.enable();
                req.setText("Bluetooth abled!");
            } else {
                mBluetoothAdapter.disable();
                req.setText("Bluetooth disabled!");
            }
        }
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

        Button rssirefresh = (Button) findViewById(R.id.button3);
        rssirefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mBluetoothGatt.readRemoteRssi();
                TextView reqrssi = (TextView) findViewById(R.id.textView8);
                reqrssi.setText("RSSI"+staticrssi);


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
        btAdapter.startDiscovery();
    }
    //qui la distruzione
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
        unregisterReceiver(mReceiver);
    }
}




