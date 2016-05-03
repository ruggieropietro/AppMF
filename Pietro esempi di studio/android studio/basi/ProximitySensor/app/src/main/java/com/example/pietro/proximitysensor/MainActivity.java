//in questo progetto si cercava di usare il sensore diprossimità
//per simulare una macchina di theremin,problema primario è che non tutti i
//dispositivi hanno un sensore di prossimità,secondario è che possono avere due sensori di
//prossimità,ovvero che danno in uscita un valore analogico da 0 a 5 mentre gli altri sono
// digitali  ,da 0 a 1.
//Si pensava di lavorare quindi con un sensore analogico ,dando un valore al suono secondo
//distanza,per poi ricavare la traiettoria del "mouse" estraendo x y per usarlo come
//ulteriore attibuto da inserire nel suono.
package com.example.pietro.proximitysensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener{
//le variabili sono create fuori dal tutto
    private SensorManager sensorManager;
    TextView tVProximity;


    @Override
    //nell'on create si controlla che ci sia il sensore
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tVProximity = (TextView)findViewById(R.id.tVProximity);
        //create instance of sensor manager and get system service to interact with Sensor
        sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //controllando che esista il sensore di prossimita nel caso non ci fosse
        //viene inviato un toast,il toast puo avere solo durata lubga o corta
        Sensor proximitySensor= sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null){
            Toast.makeText(MainActivity.this,"No Proximity Sensor Found! ",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(MainActivity.this,"Proximity Sensor Found! ",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {

        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //qui viene ricavato il valore del sensore di prossimita'
        if(event.sensor.getType()==Sensor.TYPE_PROXIMITY){

                tVProximity.setText(String.valueOf(event.values[0]));
        }}
}