package com.ruggpruzz.pieste.bluetoothresearch;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends Activity {

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt =null;
    private BluetoothAdapter btAdapter;
    public  int staticrssi=0;
    public    int hash =  1001190065;
public boolean research =false;
public boolean connect = false;
    public boolean analize = false;
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
                if (analize) {
                    Toast.makeText(MainActivity.this, device.getName() + " " + device.hashCode(), Toast.LENGTH_LONG).show();
                }
                    if ((Math.abs(device.hashCode()) == hash)) {
                        if (!analize) {
                            mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
                            connect = true;
                            TextView req = (TextView) findViewById(R.id.textView2);
                            req.setText("Calculated");
                        }
                        new CountDownTimer(12000, 1000) {

                            public void onTick(long millisUntilFinished) {


                            }

                            public void onFinish() {
                                mBluetoothAdapter.cancelDiscovery();
                                mBluetoothAdapter.disable();
                                if (!analize) {
                                    mBluetoothGatt.disconnect();
                                    mBluetoothGatt.close();
                                    connect = false;
                                    TextView reqrssi = (TextView) findViewById(R.id.textView8);
                                    reqrssi.setText("RSSI ");
                                    TextView req = (TextView) findViewById(R.id.textView2);
                                    req.setText("Not connecting");
                                }
                            }
                        }.start();
                    }

            }
            }

    };



    protected void refreshrssi() {


            try {
                mBluetoothGatt.readRemoteRssi();
                TextView reqrssi = (TextView) findViewById(R.id.textView8);
                reqrssi.setText("RSSI " + staticrssi);
            } catch (NullPointerException e) {
             //   Toast.makeText(MainActivity.this, "GATT d'ont connected", Toast.LENGTH_LONG).show();
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
                    if (connect) {
                        refreshrssi();
                    }
                    start();
                }
            }.start();

            setContentView(R.layout.activity_main);
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            //qui nel onCreate la memorizzazione formale di stati precedenti per il controllo di cambio
            //degli stati
            IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter2);
            TextView req = (TextView) findViewById(R.id.textView2);
            //qui il controllo con textView e toast riguardo la verifica di esistenza del bluetooth interno
            if (mBluetoothAdapter == null) {
                Toast.makeText(MainActivity.this, "Internal bluetooth not found! ", Toast.LENGTH_SHORT).show();
            }
            TextView hashtext = (TextView) findViewById(R.id.textView11);
            hashtext.setText("" + hash);
            //qui l'ascolto al tasto per abilitare o disabilitare il bluetooth dall'interno
            //dell'aplicazione


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



        }
        else
        {

            Toast.makeText(MainActivity.this, "API correctly are :(17<API<21)", Toast.LENGTH_LONG).show();

        }
    }

    protected void onResume() {
        super.onResume();

        if (!research)
        {
            mBluetoothAdapter.disable();
        }
    }
        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            load();
        }

    public void scan(View v) {
        analize=false;
        if (!btAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
        } else
            load();
    }
    public void otherhash(View v) {
        analize=true;
        if (!btAdapter.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
        } else
            load();
    }
    private void load() {
        research=true;
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
                research=false;
                if (!connect)
                        {
                            mBluetoothAdapter.disable();
                            if (!analize) {
                                Toast.makeText(MainActivity.this, "Not found " + hash, Toast.LENGTH_LONG).show();
                            }
                        }
            }
        }.start();


    }

    //qui la distruzione
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mBluetoothAdapter.disable();
        mBluetoothGatt.disconnect();
        mBluetoothGatt.close();
    }
}







