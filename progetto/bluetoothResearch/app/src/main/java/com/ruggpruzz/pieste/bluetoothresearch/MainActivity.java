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

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
//qui viene creato il broadcast receiver e inpostato per ascoltare il bluetooth e i suoi stati generalizzati
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            TextView req = (TextView) findViewById(R.id.textView2);
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                //qui uno switch con le varie reazioni che si vuole dare a seconda di quello che sente il broadcast receiver
                switch(state) {
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

        //qui nel onCreate la memorizzazione formale di stati precedenti per il controllo di cambio
        //degli stati
        IntentFilter filter1 = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver1, filter1);
        setContentView(R.layout.activity_main);
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        TextView tv = (TextView) findViewById(R.id.textView);
        TextView req = (TextView) findViewById(R.id.textView2);
        //qui il controllo con textView e toast riguardo la verifica di esistenza del bluetooth interno
        if (mBluetoothAdapter == null) {
            tv.setText("Bluetooth not found!");
            Toast.makeText(MainActivity.this, "Internal bluetooth not found! ", Toast.LENGTH_LONG).show();
        } else {
            tv.setText("Bluetooth  found!");
            Toast.makeText(MainActivity.this, "Internal bluetooth found! ", Toast.LENGTH_LONG).show();
            if (!mBluetoothAdapter.isEnabled()) {
                req.setText("Bluetooth disabled");
                mBluetoothAdapter.enable();
                req.setText("Bluetooth abled");
            } else {
                req.setText("Bluetooth abled");
            }
        }
        //qui l'ascolto al tasto per abilitare o disabilitare il bluetooth dall'interno
        //dell'aplicazione
        Button btnHome=(Button)findViewById(R.id.button);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                TextView req = (TextView) findViewById(R.id.textView2);
                if (!mBluetoothAdapter.isEnabled()) {
                    mBluetoothAdapter.enable();
                    req.setText("Bluetooth abled");
                } else {
                    mBluetoothAdapter.disable();
                    req.setText("Bluetooth disabled");
                }
            }
        });
        }
    //qui la distruzione
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver1);
    }
    }




