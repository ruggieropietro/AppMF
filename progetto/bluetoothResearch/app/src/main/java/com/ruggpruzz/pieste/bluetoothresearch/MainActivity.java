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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity
{
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt = null;
    private BluetoothAdapter btAdapter;
    public int staticrssi = 0;
    public int hash = 1001190065;
    public boolean research = false;
    public boolean connect = false;
    public boolean analize = false;
    //il primo gestore di eventi deve riguardare la scanerizzazione,ovvero quando il dispositivo cerca  altri dispositivi in zona questi prendono il nome di associati
    //in questo broadcast receiver vogliamo sostituire il onlescancallback,siccome ci sono problemi di compatibilità,questo perchè alcune librerie sono deprecate da dopo il 20
    //ma la tecnologia BLE era già arrivata al  API 18,avendo quindi un dispositivo API 19 si devono aooviare alcuni problemi.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            //quando viene trovato un associato
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                //per gestire l'analisi degli associati, quando li si trova ,gli si deve assegnare un oggetto,per poterlo analizzare,android studio fornisce
                //l'ogetto bluetooth devide
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //analize è una variabile booleana,l scanerizazione viene chiamata da 2 tasti,il primo ,quando analize è vero,serve solo a vedere e annunciare
                //i dispositivi bluetooth associati su toast,il motivo è conoscere i codici hash dei dispositivi,come si vedrà nell'activity sarà possibile
                //utilizzare la preference del codice hash preferito
                if (analize)
                {
                    Toast.makeText(MainActivity.this, device.getName() + " " + device.hashCode(), Toast.LENGTH_LONG).show();
                }
                //nell'altro caso ,il dispositivo non deve annunciare con i toast del trovamento di altri dispositivi,ma deve solo conettersi al dispositivo
                //con il codice hash preferito,facendo cosi sarà possibile utilizzare l'app universalmente e con il dispositivo preferito
                //già inserito a ogni avvio
                if ((Math.abs(device.hashCode()) == hash))
                {
                    if (!analize)
                    {
                        //qui si connette se trova il dispositivo con il codice hash
                        mBluetoothGatt = device.connectGatt(context, false, mGattCallback);
                        connect = true;
                        TextView req = (TextView) findViewById(R.id.textView2);
                        req.setText("Calculated");
                    }
                    //la tecnologia BLE è otimizzata al risparmio energetico,una delle sue caratteristiche è quindi la disconessione automatica
                    //qunando non c'è un flusso di dati ,in input o output,da piu' di 12 secondi,siccome noi non possiamo abattere questa regol HW
                    //siamo costretti a chiudere tutto dopo questi 12 secondi
                    new CountDownTimer(12000, 1000)
                    {
                        public void onTick(long millisUntilFinished)
                        {
                        }
                        public void onFinish()
                        {
                            //in ogni caso di analize "resetta" gli stati del bluetooth adapter,quello utile per lo scan
                            mBluetoothAdapter.cancelDiscovery();
                            mBluetoothAdapter.disable();
                            //invece nel caso in cui ci si era connessi llora disconetti tutto e chiudi il gatt
                            if (!analize)
                            {
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
    //questo è un gestore di eventi piu' ufficioso del broadcast receiver usato per lo scan ,infatti useremo il gattcallback come "contratto"
    //ogni volta che avremo bisogno di conettersi a un GATT
    //per completezza ,l'organizzazione GATT è dovuta al BLE,ovvero possiamo evitare di parlare di server ,socket ecc... siccome il BLE
    //prevede una forma diversa,in sintesi è lui che gestisce chi fa da host e chi fa da client,siccome i ruoli si scambiano,percio'
    //i dispositivi prendono tutti il nome di GATT server
    private final BluetoothGattCallback mGattCallback =
            new BluetoothGattCallback()
            {
                //questo è quello che succede ogni volta che viene letto l'rssi(il dato che ci interessa)
                //notare la forma un po' spinta,siccome nell'activity si deve usare una booleana per fare partire la lettura dell'rssi,che viene appunto gestita come
                //evento secondo a quello che viene scritto qua dentro
                @Override
                public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                    staticrssi = rssi;
                }
                //l'on connetionstate change è quello che succede quando l'evento"stato della conessione" cambia,
                //vista il controllo logico del programma sulla questione si è evitata l'implementazione  qui,semplificandone il contenuto.
                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    super.onConnectionStateChange(gatt, status, newState);
                }
            };
    //l'oncreate,questa per noi si occupa di avviare altri stati e processi di eventi.
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//abbiamo pensato fosse utile ,visti i problemi di compatibilità descritti in precedenza,efettuare da subito un controllo API
        if ((Build.VERSION.SDK_INT < 21) && (Build.VERSION.SDK_INT > 17)) {
            //questo è la preferenza del codice hash preferito,serve a poterlo salvare ed averlo a ogni avvio
            SharedPreferences settings = getSharedPreferences("hash", hash);
            hash = settings.getInt("hash", 1001190065);
            //il countdown serve a refreshare l'rssi,ovvero siccome la conessione resta per circa 12 secondi ,noi una volta conessi avremo un refresh dell'rssi
            //ogni secondo finche la conessione resta attiva
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
            //qui viene creato l'adapter,che sarebbe il nostro dispositivo,l'intent per
            //il broadcast receiver
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            btAdapter = BluetoothAdapter.getDefaultAdapter();
            IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, filter2);
            //tutti i dispositivi dovrebbero avere il bluetooth,ma nel caso partciolare in cui sia difettoso ,staccato ecc...
            //ne siamo subito a conoscenza
            if (mBluetoothAdapter == null)
            {
                Toast.makeText(MainActivity.this, "Internal bluetooth not found! ", Toast.LENGTH_SHORT).show();
            }
            TextView hashtext = (TextView) findViewById(R.id.textView11);
            hashtext.setText("" + hash);
        }
        //come detto prima siamo subito a conoscenza anche per le problematiche di compatibilità,pultroppo possibili nel nostro caso specifico
        else
        {
            Toast.makeText(MainActivity.this, "API correctly are :(17<API<21)", Toast.LENGTH_LONG).show();
        }
    }
    //dentro l'on resume abbiamo bisogno di spegnere ilbluetooth,questo perchè un'attivazione esterna del bkluetooth puo' compromettere la comprensione
    //del app all'utente,ovvero,ogni qualvolta che il la ricerca trova il dispositivo preferito un edittext ci dice collegato e ci formula l'rssi
    //ma qui dietro le quinte vediamo che non è completamente vero,perchè il nostro bluetooth puo' essere acceso senza essere ne in ricerca ne connesso,
    //è possibile pero' ovviare il problema limitando l'accensione del bluetooth solo per ricerca e conessione,interrompendo qualsiasi caso esterno.
    //Facendo cosi abbiamo piu' coerenza con le regole di risparmio energetico BLE
    protected void onResume()
    {
        super.onResume();
        if (!research) {
            mBluetoothAdapter.disable();
        }
    }
    //qui la distruzione che si deve occupare di ripulire le risorse usate
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        mBluetoothAdapter.disable();
        mBluetoothGatt.disconnect();
        mBluetoothGatt.close();
    }
    //questo è il metodo che viene chiaamato a ciclo continuo da dentro l'activity,serve  rifreshare l'rssi
    //se siamo connessi al GATT
    protected void refreshrssi()
    {
        try
        {
            mBluetoothGatt.readRemoteRssi();
            TextView reqrssi = (TextView) findViewById(R.id.textView8);
            reqrssi.setText("RSSI " + staticrssi);
        }
        catch (NullPointerException e)
        {
            //  non fare niente
        }
    }
    //scan è utilizzato dal tasto utile a rilevare e annunciare i dispositivi associati,la differenza è ottenuta con la variabile booleana analize
    public void otherhash(View v)
    {
        analize = true;
        if (!btAdapter.isEnabled())
        {
            //se il bluetooth è spento chiama l'activity di sistema
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
        }
        else
            load();
    }
    //qui invece analize è false,il tasto che chiama questo metodo servirà a connettersi al dispositivo preferito
    public void scan(View v)
    {
        analize = false;
        if (!btAdapter.isEnabled())
        {
            //se il bluetooth è spento chiama l'activity di sistema
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 1);
        }
        else
            load();
    }
    //onactivity result serve a gestire l'activity di sistema,utile ad accendere il bluetooth quando si vuole usare il programma
    //è override appunto perchè è un implementazione necessario,siccome potremmo avere il bluetooth spento
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        load();
    }
    //a questo punto il load fa partire la scanerizzazione ,da notare che per rimanere coerenti con le regole di risparmio
    //di energia dovute alla tecnologia BLE dopo 12 secondi la scanerizzazione viene terminata
    private void load()
    {
        research = true;
        TextView reqrssi = (TextView) findViewById(R.id.textView9);
        reqrssi.setText("Scanning ");
        btAdapter.startDiscovery();
        new CountDownTimer(12000, 1000)
        {
            int punti = 0;
            TextView reqrssi = (TextView) findViewById(R.id.textView9);
            //scelta di una piccola animazione per fare notare la continuità del processo
            public void onTick(long millisUntilFinished)
            {
                if (punti == 0)
                {
                    reqrssi.setText("Scanning ");
                    punti += 1;
                }
                else if (punti == 1)
                {
                    reqrssi.setText("Scanning. ");
                    punti += 1;
                }
                else if (punti == 2)
                {
                    reqrssi.setText("Scanning.. ");
                    punti += 1;
                }
                else if (punti == 3)
                {
                    reqrssi.setText("Scanning... ");
                    punti = 0;
                }
            }
            public void onFinish()
            {
                reqrssi.setText("Not scanning ");
                research = false;
                //a fine ricerca se non si è connesso si si disabilità il bluetooth interno,questo perchè
                //si vuole rispettare le regole di risparmio energetico BLE e quindi spegnere il bluetooth
                //nel caso fosse conness se ne occuperebbe il codice dopo i 12 secondi di conessione visti in precedenza
                if (!connect)
                {
                    mBluetoothAdapter.disable();
                    //ulteriormente se non ervamo in cerca di nuovi codici hash ma stavamo cercando di conetterci al dispositivo preferito
                    // e se quest'ultimo non è stato trovato lo dobbiamo annunciare
                    if (!analize)
                    {
                        Toast.makeText(MainActivity.this, "Not found " + hash, Toast.LENGTH_LONG).show();
                    }
                }
            }
        }.start();
    }
    //l'ultimo metodo chiamato da un bottone è quello utile a salvare la preference hash
    public void savehash(View v)
    {
        try
        {
            TextView hashsave = (EditText) findViewById(R.id.editText);
            hash = Integer.parseInt(hashsave.getText().toString());
            SharedPreferences settings = getSharedPreferences("hash", hash);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("hash", hash);
            editor.commit();
            TextView req = (TextView) findViewById(R.id.textView11);
            req.setText("" + hash);
            mBluetoothAdapter.disable();
        }
        catch (NumberFormatException e)
        {
            Toast.makeText(MainActivity.this, "invalid insert", Toast.LENGTH_LONG).show();
        }
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