package com.lrg.triangleresolutor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.Math;

public class MainActivity extends AppCompatActivity {
    boolean haveA = false;
    boolean haveB = false;
    boolean haveC = false;
    boolean havea = false;
    boolean haveb = false;
    boolean havec = false;
    static int maxInput=0;
    double A =0;
    double B=0;
    double C =0;
    double a = 0;
    double b =0;
    double c =0;

    public void onEntry(View view) {
        TextView textA = (TextView) findViewById(R.id.textA);
        textA.setText("cateto A = " );
        TextView textB = (TextView) findViewById(R.id.textB);
        textB.setText("cateto B = " );
        TextView textC = (TextView) findViewById(R.id.textC);
        textC.setText("cateto C = " );
        TextView texta = (TextView) findViewById(R.id.texta);
        texta.setText("angle a = " );
        TextView textb = (TextView) findViewById(R.id.textb);
        textb.setText("angle b = " );
        TextView textc = (TextView) findViewById(R.id.textc);
        textc.setText("angle c = " );
        if (maxInput!=3)
        {
            Toast.makeText(MainActivity.this,"don't have 3 input", Toast.LENGTH_LONG).show();
        }
        else if ((havea==true)&&(haveb==true)&&(havec==true))
        {
            Toast.makeText(MainActivity.this,"3 angle input is impossible", Toast.LENGTH_LONG).show();
        }
        else {
            EditText editA = (EditText) findViewById(R.id.editTextA);
            EditText editB = (EditText) findViewById(R.id.editTextB);
            EditText editC = (EditText) findViewById(R.id.editTextC);
            EditText edita = (EditText) findViewById(R.id.editTexta);
            EditText editb = (EditText) findViewById(R.id.editTextb);
            EditText editc = (EditText) findViewById(R.id.editTextc);

            if (haveA==false) {
                editA.setEnabled(false);
                editA.setText("");

            }
            else
            {
               editA.setEnabled(true);
                editA.setText("0");
            }
            if (haveB==false) {
                editB.setEnabled(false);
                editB.setText("");
            }
            else
            {
                editB.setEnabled(true);
                editB.setText("0");
            }
            if (haveC==false) {
                editC.setEnabled(false);
                editC.setText("");
            }
            else
            {
                editC.setEnabled(true);
                editC.setText("0");
            }
            if (havea==false) {
                edita.setEnabled(false);
                edita.setText("");
            }
            else
            {
                edita.setEnabled(true);
                edita.setText("0");
            }
            if (haveb==false) {
                editb.setEnabled(false);
                editb.setText("");
            }
            else
            {
                editb.setEnabled(true);
                editb.setText("0");
            }
            if (havec==false) {
                editc.setEnabled(false);
                editc.setText("");
            }
            else
            {
                editc.setEnabled(true);
                editc.setText("0");
            }

        }
    }
    public void onCalculate(View view) {
        if ((haveA) && (haveB) && (haveC)) {
                EditText editA = (EditText) findViewById(R.id.editTextA);
                EditText editB = (EditText) findViewById(R.id.editTextB);
                EditText editC = (EditText) findViewById(R.id.editTextC);
                A = Double.parseDouble(editA.getText().toString());
                B = Double.parseDouble(editB.getText().toString());
                C = Double.parseDouble(editC.getText().toString());
                if ((A <= 0) || (B <= 0) || (C <= 0)) {
                    Toast.makeText(MainActivity.this, "error,input <=0", Toast.LENGTH_LONG).show();
                } else {

                    a = Math.floor((((Math.acos(((B * B) + (C * C) - (A * A)) / (2 * B * C))) * 180) / (Math.PI)) * (Math.pow(10, 5))) / (Math.pow(10, 5));
                    b = Math.floor((((Math.acos(((A * A) + (C * C) - (B * B)) / (2 * A * C))) * 180) / (Math.PI)) * (Math.pow(10, 5))) / (Math.pow(10, 5));
                    c = Math.floor((((Math.acos(((A * A) + (B * B) - (C * C)) / (2 * A * B))) * 180) / (Math.PI)) * (Math.pow(10, 5))) / (Math.pow(10, 5));
                    TextView textA = (TextView) findViewById(R.id.textA);
                    textA.setText("cateto A = " + A);
                    TextView textB = (TextView) findViewById(R.id.textB);
                    textB.setText("cateto B = " + B);
                    TextView textC = (TextView) findViewById(R.id.textC);
                    textC.setText("cateto C = " + C);
                    TextView texta = (TextView) findViewById(R.id.texta);
                    texta.setText("angle a = " + a);
                    TextView textb = (TextView) findViewById(R.id.textb);
                    textb.setText("angle b = " + b);
                    TextView textc = (TextView) findViewById(R.id.textc);
                    textc.setText("angle c = " + c);
                }
        }
    }
                public void onCheckboxClicked(View view) {
                    // Is the view now checked?
                    boolean checked = ((CheckBox) view).isChecked();

                    // Check which checkbox was clicked
                    switch (view.getId()) {
                        case R.id.radioButtonA:
                            if (checked) {
                                maxInput += 1;
                                haveA = true;
                            } else {
                                maxInput -= 1;
                                haveA = false;
                            }
                            break;
                        case R.id.radioButtonB:
                            if (checked) {
                    maxInput += 1;
                                haveB = true;
                } else {
                    maxInput -= 1;
                                haveB = false;
                }
                break;
            case R.id.radioButtonC:
                if (checked) {
                    maxInput += 1;
                    haveC = true;
                } else {
                    maxInput -= 1;
                    haveC = false;
                }
                break;
            case R.id.radioButtona:
                if (checked) {
                    maxInput += 1;
                    havea = true;
                } else {
                    maxInput -= 1;
                    havea = false;
                }
                break;
            case R.id.radioButtonb:
                if (checked) {
                    maxInput += 1;
                    haveb = true;
                } else {
                    maxInput -= 1;
                    haveb = false;
                }
                break;
            case R.id.radioButtonc:
                if (checked) {
                    maxInput += 1;
                    havec = true;
                } else {
                    maxInput -= 1;
                    havec = false;
                }
                break;
        }
                }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editA = (EditText) findViewById(R.id.editTextA);
        EditText editB = (EditText) findViewById(R.id.editTextB);
        EditText editC = (EditText) findViewById(R.id.editTextC);
        EditText edita = (EditText) findViewById(R.id.editTexta);
        EditText editb = (EditText) findViewById(R.id.editTextb);
        EditText editc = (EditText) findViewById(R.id.editTextc);
        editA.setEnabled(false);
        editB.setEnabled(false);
        editC.setEnabled(false);
        edita.setEnabled(false);
        editb.setEnabled(false);
        editc.setEnabled(false);
    }


}
