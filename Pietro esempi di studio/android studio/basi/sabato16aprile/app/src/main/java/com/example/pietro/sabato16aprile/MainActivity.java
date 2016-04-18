package com.example.pietro.sabato16aprile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.security.PrivateKey;

public class MainActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //unisco il suo xml
        setContentView(R.layout.activity_main);
        //associo la variabile button al r id del bottone nel main
        Button btnHome=(Button)findViewById(R.id.button);
        //assegno un listener alla variabile button
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0){
                //inizializzo intent
                Intent i=new Intent(MainActivity.this,PrivateZone.class);
                //da dove a dove
            i.putExtra("username",10);
                //llo avvio
            startActivity(i);
        }
            //siccome deve rimaere attivo il button ha questo strano formalismio
            //di mettere la parentesi tonda in questo modo
            });

        }
    }


