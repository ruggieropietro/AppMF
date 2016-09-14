//in questo programma si è voluto ottenere un calcolatore di distanze tra dispositivi bluetooth
//la prima fase è chiaramente la ricerca bluetooth ,è possibile ottenere la potenzqa del segnale senza creare
// gatt servetr,la seconda è il calcolo della distanza
//attraverso i vari algoritmi,siccome l'unico dato utile che possiamo ottenere è la potenza del segnale(Rssi).
//in questo codice è facile capire come android sia facilmente utilizzabile con la programmazione orientata agli eventi,dove
//l'activity si occupa di inizializzare "cicli" e stati,mentre "all'infuori" possiamo quasi definire i gestori di stati come
//callback ,questi ultimi si comportano come "contratti" che attivano processi quando si verificano gli stessi eventi
//si è inoltre cercato di limitare i toast alla sola annunciazione di un exception o per comunicazioni utili all'utente
package com.ruggpruzz.pieste.bluetoothresearch;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothAdapter btAdapter;
    //qui il mac del stick and find utilizzato
    public String mac = "E1:D4:23:D7:61:7D";
    public boolean research = false;
    private String rssiString="";
    //Coefficenti del calcolo approssimato polinomiale
    private double c3= -0.00000030899;
    private double c2= -0.000069142;
    private double c1= -0.00522257;
    private double c0= -0.12794642;
    private float mediumValue=0;
    private int count=0;
    private int capo=0;
    //Coefficenti del calcolo approssimato lineare LAM
    private int a=-70;
    private double k=0.066;
    private double stima =0;
    private double stimaLam=0;
    //il primo gestore di eventi deve riguardare la scanerizzazione,ovvero quando il dispositivo cerca  altri dispositivi in zona questi prendono il nome di associati
    //ci sono state  problematiche per via delle compatibilità API per  BLE ma si è potuto procedere facendo finta di niente,
    //l'app resta comunque limitata a un range di API
    private final BluetoothAdapter.LeScanCallback mScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                //quando viene trovato un associato
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    //per poter porre cambiamenti durante un callback si deve mettere  un runner
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("", "MAC: " + device.getAddress());
                            Log.i("", "RSSI: " + rssi);
                            //filtriamo  il mac interessa
                            boolean uguale =mac.equals(device.getAddress());
                            if(uguale)
                            {
                                capo++;
                                if (capo==2)
                                {
                                    rssiString=rssiString+"\n";
                                    capo=0;
                                }
                                rssiString = rssiString + rssi;
                                mediumValue=mediumValue+rssi;
                                count++;
                                Log.d("MEDIO",""+ mediumValue);
                                Log.d("UGUALE","entrati nell'if");
                                //qui la chiamata a metodo,per processi di sviluppo.
                                AddItem(device.getAddress(), rssiString);
                            }
                            new CountDownTimer(12000, 1000) {
                                public void onTick(long millisUntilFinished) {
                                }
                                public void onFinish() {
                                    //in ogni caso di analize "resetta" gli stati del bluetooth adapter,quello utile per lo scan
                                    mBluetoothAdapter.cancelDiscovery();
                                    mBluetoothAdapter.disable();
                                }
                            }.start();
                            Log.d("control", "Countdown Partito");
                        }
                    });
                }
            };
    //l'oncreate,questa per noi si occupa di avviare altri stati e processi di eventi.
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//abbiamo pensato fosse utile ,visti i problemi di compatibilità ,efettuare da subito un controllo API
        if ((Build.VERSION.SDK_INT < 21) && (Build.VERSION.SDK_INT > 17)) {
            setContentView(R.layout.activity_main);
            //qui viene creato l'adapter,che sarebbe il nostro dispositivo,
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            //tutti i dispositivi dovrebbero avere il bluetooth,ma nel caso partciolare in cui sia difettoso ,staccato ecc...
            //ne siamo subito a conoscenza
            if (mBluetoothAdapter == null) {
                Toast.makeText(MainActivity.this, "Internal bluetooth not found! ", Toast.LENGTH_SHORT).show();
            }
            TextView hashtext = (TextView) findViewById(R.id.textView11);
            hashtext.setText("" + mac);
        }
        //come detto prima siamo subito a conoscenza anche per le problematiche di compatibilità,pultroppo possibili nel nostro caso specifico
        else {
            Toast.makeText(MainActivity.this, "API correctly are :(17<API<21)", Toast.LENGTH_LONG).show();
        }
    }
    //dentro l'on resume abbiamo bisogno di spegnere ilbluetooth,questo perchè un'attivazione esterna del bkluetooth puo' compromettere la comprensione
    //del app all'utente,ovvero,ogni qualvolta che il la ricerca trova il dispositivo preferito un edittext ci dice collegato e ci formula l'rssi
    //ma qui dietro le quinte vediamo che non è completamente vero,perchè il nostro bluetooth puo' essere acceso senza essere ne in ricerca ne connesso,
    //è possibile pero' ovviare il problema limitando l'accensione del bluetooth solo per ricerca e conessione,interrompendo qualsiasi caso esterno.
    //Facendo cosi abbiamo piu' coerenza con le regole di risparmio energetico BLE
    protected void onResume() {
        super.onResume();
        if (!research) {
            mBluetoothAdapter.disable();
        }
    }
    //qui la distruzione che si deve occupare di ripulire le risorse usate
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothAdapter.disable();
    }
    //metodo chiamato quando si schiaccia il bottone,fa quindi partire la scanerizzazione dei dispositivi associati
    public void OtherMac(View v) {
        rssiString="";
        if (!btAdapter.isEnabled()) {
            //se il bluetooth è spento chiama l'activity di sistema
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
        } else
            load();
    }
    //onactivity result serve a gestire l'activity di sistema,utile ad accendere il bluetooth quando si vuole usare il programma
    //è override appunto perchè è un implementazione necessario,siccome potremmo avere il bluetooth spento
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        load();
    }
    //a questo punto il load fa partire la scanerizzazione ,da notare che per rimanere coerenti con le regole di risparmio
    //di energia dovute alla tecnologia BLE dopo 12 secondi la scanerizzazione viene terminata
    private void load() {
        research = true;
        TextView reqrssi = (TextView) findViewById(R.id.textView9);
        reqrssi.setText("Scanning ");
        //qui il problema di compatibiltà
        btAdapter.startLeScan(mScanCallback);
        //questo countdown è il cervello del calcolo,si occupa di comunicare all'utente che sta scanerizzando,o calcolando,
        //essendo avviato subito dopo startlescan ci si ritrova ad avere l'aggionameto degli rssi ottenuti attraverso scancallback,
        //Cosi che alla fine del countdown vengano aggiornati i text informativi e venga stimata la distanza attraverso i 2 algoritmi
        new CountDownTimer(12000, 1000) {
            int punti = 0;
            TextView reqrssi = (TextView) findViewById(R.id.textView9);
            //scelta di una piccola animazione per fare notare la continuità del processo
            public void onTick(long millisUntilFinished) {
                if (punti == 0) {
                    reqrssi.setText("Calculating ");
                    punti += 1;
                } else if (punti == 1) {
                    reqrssi.setText("Calculating. ");
                    punti += 1;
                } else if (punti == 2) {
                    reqrssi.setText("Calculating.. ");
                    punti += 1;
                } else if (punti == 3) {
                    reqrssi.setText("Calculating...");
                    punti = 0;
                }
            }
            public void onFinish() {
                reqrssi.setText("Click for Scan");
                research = false;
                btAdapter.stopLeScan(mScanCallback);
                mediumValue=mediumValue/count;
                TextView tvCount = (TextView) findViewById(R.id.tvcount);
                tvCount.setText("" + count);
                TextView tvMedium = (TextView) findViewById(R.id.tvmedium);
                tvMedium.setText("" + mediumValue);
                double terza = Math.pow(mediumValue,3.0);
                double seconda = Math.pow(mediumValue,2.0);
                Log.d("STIMA",""+c3);
                Log.d("STIMA",""+c2);
                Log.d("STIMA",""+c1);
                Log.d("STIMA",""+c0);
                Log.d("POTENZA",""+terza);
                Log.d("POTENZA",""+seconda);
                stima=(c3*terza+c2*seconda+c1*mediumValue+c0)*1000;
                String stimaString = String.format("%.02f", stima);
                TextView tvStima = (TextView) findViewById(R.id.tvEstimate);
                tvStima.setText("" + stimaString +"m");
                double logStima= -k*mediumValue+(-k*a);
                Log.d("LAM","k"+k);
                Log.d("LAM","a"+a);
                Log.d("LAM",""+logStima);
                stimaLam=Math.pow(10.0,logStima);
                Log.d("LAM","stimaLAM"+stimaLam);
                //String lamString = String.format("%.02f", stimaLam);
                TextView tvstima2 =(TextView) findViewById(R.id.tvEstimate2);
                tvstima2.setText(""+stimaLam+"m");
                mediumValue=0;
                count=0;
                //a fine ricerca  si disabilità il bluetooth interno,questo perchè
                //si vuole rispettare le regole di risparmio energetico BLE e quindi spegnere il bluetooth
                    mBluetoothAdapter.disable();
                    //ulteriormente se non ervamo in cerca di nuovi codici hash ma stavamo cercando di conetterci al dispositivo preferito
                    // e se quest'ultimo non è stato trovato lo dobbiamo annunciare
                        Toast.makeText(MainActivity.this, "Estimation Completed!", Toast.LENGTH_LONG).show();
            }
        }.start();
    }
//metodo utile in fase di sviluppo,per raccogliere una lista rssi direttamente dall'aplicazione
    void AddItem(String name, String rssi) {
        //Se si vogliono vedere i valori RSSI sui quali stiamo effettuando la media rimuovere le slash da questo.
        //TextView reqrssi = (TextView) findViewById(R.id.textView8);
        //reqrssi.setText("" + rssi);
    }
}
//considerazioni finali:
//per ottenre il risultato si è dovuto comunque passare per la questione della compatibilità,come già spiegato il BLE è una tecnologia HW
//inserita dall'API 18 in poi,ma le librerie otimizzate per la sua gestione arrivano solo con lAPI 21,questo porta dei problemi ,
// avevamo un dispositivo con API 19,quindi è la tecnica migliore usare il bluetooth,o sarebbe stato meglio valutare altre tecnolgie come wi-fi o gps?