package com.example.pietro.sabato16aprile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Pietro on 16/04/16.
 */
public class PrivateZone extends Activity {


    protected void onCreate(Bundle savedInstanceState)

    {   //il super deve essere messo
        super.onCreate(savedInstanceState);
        setContentView(R.layout.privatezone);
        //apro l intent
        Intent i=getIntent();
        String username=i.getStringExtra("username");

    }


}