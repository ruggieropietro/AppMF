//in questo programma si è voluto ottenere un calcolatore di distanze tra dispositivi bluetooth
//la prima fase è chiaramente la ricerca bluetooth per la conseguente conessione,la seconda è il calcolo della distanza
//attraverso i vari algoritmi,siccome l'unico dato utile che possiamo ottenere è la potenza del segnale(Rssi)
//Si è cercato di rimanere fedeli alla programmazione orientata agli eventi,generalmente l'activity farà avviare dei processi,
//se durante questi processi si verificheranno ,appunto,gli eventi utili,il programma farà azioni specifiche
//in questo codice è facile capire come android sia facilmente utilizzabile con la programmazione orientata agli eventi,dove
//l'activity si occupa di inizializzare "cicli" e stati,mentre "all'infuori" possiamo quasi definire i gestori di stati come
//callback ,questi ultimi si comportano come "contratti" che attivano processi quando si verificano gli stessi eventi
//verificatosi durante i "cicli" iniziati dall'activity.
//si è inoltre cercato di limitare i toast alla sola annunciazione di un exception,escluso un particolare caso che si vedrà
//piu' avanti
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

import org.w3c.dom.Text;

public class MainActivity extends Activity {
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothAdapter btAdapter;
    public String mac = "E1:D4:23:D7:61:7D";

    public boolean research = false;
    public boolean connect = false;
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
    private boolean debug=false;
    //il primo gestore di eventi deve riguardare la scanerizzazione,ovvero quando il dispositivo cerca  altri dispositivi in zona questi prendono il nome di associati
    //in questo broadcast receiver vogliamo sostituire il onlescancallback,siccome ci sono problemi di compatibilità,questo perchè alcune librerie sono deprecate da dopo il 20
    //ma la tecnologia BLE era già arrivata al  API 18,avendo quindi un dispositivo API 19 si devono aooviare alcuni problemi.
    private final BluetoothAdapter.LeScanCallback mScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    //quando viene trovato un associato



                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("", "MAC: " + device.getAddress());
                            Log.i("", "RSSI: " + rssi);
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
                                AddItem(device.getAddress(), rssiString);
                            }


                            //analize è una variabile booleana,l scanerizazione viene chiamata da 2 tasti,il primo ,quando analize è vero,serve solo a vedere e annunciare
                            //i dispositivi bluetooth associati su toast,il motivo è conoscere i codici hash dei dispositivi,come si vedrà nell'activity sarà possibile
                            //utilizzare la preference del codice hash preferito

                            AddItem(device.getAddress(), rssiString);

                            //nell'altro caso ,il dispositivo non deve annunciare con i toast del trovamento di altri dispositivi,ma deve solo conettersi al dispositivo
                            //con il codice hash preferito,facendo cosi sarà possibile utilizzare l'app universalmente e con il dispositivo preferito
                            //già inserito a ogni avvio

                            //la tecnologia BLE è otimizzata al risparmio energetico,una delle sue caratteristiche è quindi la disconessione automatica
                            //qunando non c'è un flusso di dati ,in input o output,da piu' di 12 secondi,siccome noi non possiamo abattere questa regol HW
                            //siamo costretti a chiudere tutto dopo questi 12 secondi
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
//abbiamo pensato fosse utile ,visti i problemi di compatibilità descritti in precedenza,efettuare da subito un controllo API
        if ((Build.VERSION.SDK_INT < 21) && (Build.VERSION.SDK_INT > 17)) {

            setContentView(R.layout.activity_main);
            //qui viene creato l'adapter,che sarebbe il nostro dispositivo,l'intent per
            //il broadcast receiver
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

    //scan è utilizzato dal tasto utile a rilevare e annunciare i dispositivi associati,la differenza è ottenuta con la variabile booleana analize
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
        btAdapter.startLeScan(mScanCallback);
        new CountDownTimer(12000, 1000) {
            int punti = 0;
            TextView reqrssi = (TextView) findViewById(R.id.textView9);

            //scelta di una piccola animazione per fare notare la continuità del processo
            public void onTick(long millisUntilFinished) {
                if (punti == 0) {
                    reqrssi.setText("Scanning ");
                    punti += 1;
                } else if (punti == 1) {
                    reqrssi.setText("Scanning. ");
                    punti += 1;
                } else if (punti == 2) {
                    reqrssi.setText("Scanning.. ");
                    punti += 1;
                } else if (punti == 3) {
                    reqrssi.setText("Calculating");
                    punti = 0;
                }
            }

            public void onFinish() {
                reqrssi.setText("Click to Scan");
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

                //a fine ricerca se non si è connesso si si disabilità il bluetooth interno,questo perchè
                //si vuole rispettare le regole di risparmio energetico BLE e quindi spegnere il bluetooth
                //nel caso fosse conness se ne occuperebbe il codice dopo i 12 secondi di conessione visti in precedenza
                if (!connect) {
                    mBluetoothAdapter.disable();
                    //ulteriormente se non ervamo in cerca di nuovi codici hash ma stavamo cercando di conetterci al dispositivo preferito
                    // e se quest'ultimo non è stato trovato lo dobbiamo annunciare
                        Toast.makeText(MainActivity.this, "Estimation Completed!", Toast.LENGTH_LONG).show();
                }
            }
        }.start();
    }


    void AddItem(String name, String rssi) {
        //Se si vogliono vedere i valori RSSI sui quali stiamo effettuando la media rimuovere le slash da questo.
        //TextView reqrssi = (TextView) findViewById(R.id.textView8);
        //reqrssi.setText("" + rssi);
    }
}
//considerazioni finali:
//per ottenre il risultato si è dovuto comunque passare per la questione della compatibilità,come già spiegato il BLE è una tecnologia HW
//inserita dall'API 18 in poi,ma le librerie otimizzate per la sua gestione arrivano solo con lAPI 21,questo porta dei problemi seri,specialmente nel nostro caso,
//perchè avevamo un dispositivo con API 19,quindi è la tecnica migliore usare il bluetooth,o sarebbe stato meglio valutare altre tecnolgie come wi-fi o gps?
//un'altro problema decisionale è dovuto appunto alla coerenza con la filosofia BLE ,dove si cerca di limitare i consumi chiudendo "cicli" inutiliozzati,come conessione
//ricerca e intercambiando le identità client e server creando appunto il gatt server,la questione sta appunto nel rischire di perdere questo vantaggio:
//come già spiegato se il BLE non vede streaming in input e output per piu' di 12 secondi si spegne automaticamente,facendoci perdere la ricezione RSSI,android studio
//non gestisce molto bene questa situazione,percio' si è dovuto reare un timer che sconnette anche il nostro dispositivo,cancella la lista e spegne il
//bluetooth,a questo punto una nuova ricerca per l'rssi ci costa in accensione bluetooth,scanerizzaione e conessione,
//l'alternativa era usare il EXTRA_RSSI,un dato extra che ci viene fornito solo al momento della trovata associazione ,questo avrebbe voluto dire
//che non ci sarebbe piu' stato bisogno di conettersi al bluetooth preferito,ma facendo cosi avremmo ottenuto solo 1 RSSI,numero insufficente agli algoritmi utilizzzati.