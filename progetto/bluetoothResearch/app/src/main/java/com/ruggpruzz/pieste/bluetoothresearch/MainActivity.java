//in questo progetto si è voluto iniziare a progettare il bluetooth,
//parte 1
//due tasti e due stringhe,il primo tasto serve a disattivare e disattivare il bluetooth interno,una stringa ne
//rileva lo stato,siccome è comunque possibile attivare o disattivare il bluetooth dall'esterno si è
//usato un broadcast receiver per controllare i cambiamenti cdel bluetooth.
//riguardo al broadcast receiver,ovviamente si deve richiamare tuti gli inport dovuti,poi richiamarlo con
//un onReceiver dentro il main,ma fuori dagli altri stati,per poter esssere sempre in controllo,
//comuqnue il broadcast receiver deve essere inizilizzato anche nel onCreate,questo perchè  cosi gli si dice
//da subito cosa deve ascoltare per tutto il tempo,quindi ci sono due modi per farlo,cioè attivare un qualcosa
//nel manifest oppure nel in create,in questo caso si è scelto di creare questo qualcosa nel on create,il qualcosa
//è una specie di "buffer" che memorizza lo stato precedente per poter vedere poi cosi se è cambiato,
// si è scelto comunque di usare nel destroy la sua distruzione per questioni di consumi.
//inizialmente ,nell'onCreate,c'è anche un controllo che supervisiona la presenza effettiva di un bluetooth interno,solitamente
//anche per standard ogni dispositivo ha il bluetooth,ma siccome potrebbero esserci casi che non conosciamo si è scelto
//comunque di metterlo,ovviamente il risultato è scritto nella textview citata all'inizio e non ancora descritta e
//da un toast.
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
    private BluetoothAdapter btAdapter;
    private static final int BLUETOOTH_ON = 1000;
    private BluetoothGatt mBluetoothGatt =null;
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public int mConnectionState = STATE_DISCONNECTED;



    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }
    // Various callback methods defined by the BLE API.
    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        Toast.makeText(MainActivity.this,rssi, Toast.LENGTH_LONG).show();
                    }
                }
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                                    int newState) {
                    String intentAction;
                    if (newState == BluetoothProfile.STATE_CONNECTED) {
                        intentAction = ACTION_GATT_CONNECTED;
                        mConnectionState = STATE_CONNECTED;

                        broadcastUpdate(intentAction);
                        Toast.makeText(MainActivity.this, "Connected to GATT server." , Toast.LENGTH_LONG).show();


                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        intentAction = ACTION_GATT_DISCONNECTED;
                        mConnectionState = STATE_DISCONNECTED;
                        Toast.makeText(MainActivity.this,"Disconnected from GATT server.", Toast.LENGTH_LONG).show();
                        broadcastUpdate(intentAction);
                    }
                }

                @Override
                // New services discovered
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                    } else {
                        Toast.makeText(MainActivity.this,"onServicesDiscovered received: " + status, Toast.LENGTH_LONG).show();
                    }
                }

            };

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
                    Toast.makeText(MainActivity.this, "dispositivo associato", Toast.LENGTH_LONG).show();
                    mBluetoothGatt= device.connectGatt(context, false, mGattCallback);
                   mBluetoothGatt.readRemoteRssi();
                     int  rssi = intent.getShortExtra(device.EXTRA_RSSI,  Short.MAX_VALUE);
                    TextView Rssitext = (TextView) findViewById(R.id.textView8);
                    Rssitext.setText("RSSI"+ rssi);
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
    @Override
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


        TextView tv = (TextView) findViewById(R.id.textView);
        TextView req = (TextView) findViewById(R.id.textView2);
        //qui il controllo con textView e toast riguardo la verifica di esistenza del bluetooth interno
        if (mBluetoothAdapter == null) {
            tv.setText("Internal Bluetooth not found!");
            Toast.makeText(MainActivity.this, "Internal bluetooth not found! ", Toast.LENGTH_LONG).show();
        } else {
            tv.setText("InternalBluetooth  found!");
            Toast.makeText(MainActivity.this, "Internal bluetooth found! ", Toast.LENGTH_LONG).show();
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUETOOTH_ON && resultCode == RESULT_OK) {
            load();
        }
    }

    public void scan(View v) {
        if (!btAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, BLUETOOTH_ON);
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




