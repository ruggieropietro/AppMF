//si è voluta creare un utiliti che dice,sensore per sensore,in una lista e con dei
// toast,se il dispositivo ne è
//o non è integrato,di tale sensore,alcuni type sensor sono deprecati,vedesi android studio manual for developers
//siccome per funzionarer l'activity chiede e attiva ogni sensore,il consumo di energia è elevato
//per cui un onStop o un onPause provoca l'immediata chiusura dell'activity
package com.allms.pietro.allhw;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.allms.pietro.allhw.R;

public class MainActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //create instance of sensor manager and get system service to interact with Sensor
        sensorManager= (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        //controllando che esista il sensore di prossimita nel caso non ci fosse
        //viene inviato un toast,il toast puo avere solo durata lubga o corta
        Sensor proximitySensor= sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null){
            Toast.makeText(MainActivity.this, "No proximity sensor found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView2);
            tv.setText("No proximity sensor found! ");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Proximity sensor found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView2);
            tv.setText("Proximity sensor found!");
        }
        Sensor acceSensor= sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (acceSensor == null){
            Toast.makeText(MainActivity.this, "No accelerometer sensor found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView);
            tv.setText("No accelerometer sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Accelerometer Sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView);
            tv.setText("Accelerometer sensor found!");
        }
        Sensor ambTempSensor= sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (ambTempSensor == null){
            Toast.makeText(MainActivity.this, "No ambient tenperature sensor found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView3);
            tv.setText("No ambient temperature sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Ambient temperature sensor found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView3);
            tv.setText("Ambient temperature sensor found!");
        }
        Sensor rotVecSens= sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        if (rotVecSens == null){
            Toast.makeText(MainActivity.this, "No rotation vector sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView4);
            tv.setText("No rotation vector sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Rotation vector  sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView4);
            tv.setText("Rotation vector sensor found!");
        }
        Sensor geoRot= sensorManager.getDefaultSensor(Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR);
        if (geoRot == null){
            Toast.makeText(MainActivity.this, "No geomagnetic rotation vector sensor found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView5);
            tv.setText("No geomagnetic rotation vector sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Geomagnetic rotation vector sensor found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView5);
            tv.setText("Geomagnetic rotation vector sensor found!");
        }
        Sensor grav= sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if (grav == null){
            Toast.makeText(MainActivity.this, "No gravity sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView6);
            tv.setText("No gravity sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Gravity  sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView6);
            tv.setText("Gravity sensor found!");
        }
        Sensor gyr= sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyr == null){
            Toast.makeText(MainActivity.this, "No gyroscope sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView7);
            tv.setText("No gyroscope sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Gyroscope  sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView7);
            tv.setText("Gyroscope sensor found!");
        }
        Sensor gyrUnc= sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE_UNCALIBRATED);
        if (gyrUnc == null){
            Toast.makeText(MainActivity.this, "No gyroscope uncalibrated sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView8);
            tv.setText("No gyroscope uncalibrated sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Gyroscope  uncalibrated sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView8);
            tv.setText("Gyroscope uncalibrated sensor found!");
        }
        Sensor heartRate= sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        if (gyrUnc == null){
            Toast.makeText(MainActivity.this, "No heart rate sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView9);
            tv.setText("No heart rate sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Heart rate sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView9);
            tv.setText("Heart rate sensor found!");
        }
        Sensor light= sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (light == null){
            Toast.makeText(MainActivity.this, "No light sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView10);
            tv.setText("No light sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Light sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView10);
            tv.setText("Light sensor found!");
        }
        Sensor linAcc= sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if (light == null){
            Toast.makeText(MainActivity.this, "No linear acceleration sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView11);
            tv.setText("No linear acceleration sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Linear acceleration sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView11);
            tv.setText("linear acceleration sensor found!");
        }
        Sensor magFil= sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magFil == null){
            Toast.makeText(MainActivity.this, "No magnetic field sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView12);
            tv.setText("No magnetic field sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Magnetic field sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView12);
            tv.setText("magnetic field sensor found!");
        }
        Sensor magFilUnc= sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
        if (magFilUnc == null){
            Toast.makeText(MainActivity.this, "No magnetic field uncalibrated sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView13);
            tv.setText("No magnetic field uncalibrated sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Magnetic field uncalibrated sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView13);
            tv.setText("magnetic field uncalibrated sensor found!");
        }
        Sensor press= sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if (press == null){
            Toast.makeText(MainActivity.this, "No press sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView14);
            tv.setText("No press  sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Press  sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView14);
            tv.setText("Press   sensor found!");
        }
        Sensor relUm= sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        if (relUm == null){
            Toast.makeText(MainActivity.this, "No relative umidity sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView15);
            tv.setText("No relative umidity  sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Relative umidity  sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView15);
            tv.setText("Relative umidity   sensor found!");
        }
        Sensor rotVec= sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (rotVec == null){
            Toast.makeText(MainActivity.this, "No rotation vector sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView16);
            tv.setText("No rotation vector  sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Rotation vector  sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView16);
            tv.setText("Rotation vector   sensor found!");
        }
        Sensor sigMot= sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        if (sigMot == null){
            Toast.makeText(MainActivity.this, "No significant motion sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView17);
            tv.setText("No significant motion  sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Significant motion  sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView17);
            tv.setText("Significant motion    sensor found!");
        }
        Sensor steCou= sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (steCou == null){
            Toast.makeText(MainActivity.this, "No step counter sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView18);
            tv.setText("No step counter  sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Step counter  sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView18);
            tv.setText("Step counter    sensor found!");
        }
        Sensor stedec= sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (steCou == null){
            Toast.makeText(MainActivity.this, "No step detector sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView19);
            tv.setText("No step detector  sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Step detector  sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView19);
            tv.setText("Step detector    sensor found!");
        }
        Sensor temp= sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (temp == null){
            Toast.makeText(MainActivity.this, "No ambient temperature sensor Found! ", Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView20);
            tv.setText("No ambient temperature sensor found!");
        }
        else
        {
            Toast.makeText(MainActivity.this,"Ambient temperature  sensor Found! ",Toast.LENGTH_LONG).show();
            TextView tv = (TextView)findViewById(R.id.textView20);
            tv.setText("Ambient temperature    sensor found!");
        }
    }
    @Override
    protected void onPause(){
        finish();
        super.onPause();
    }
    @Override
    protected void onStop(){
        finish();
        super.onStop();
    }
}
