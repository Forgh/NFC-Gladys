package com.coffee.nfc_gladys;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.*;

import com.coffee.nfc_gladys.PartieMetier.Ambiance;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button_createAmbiance = (Button) findViewById(R.id.buttonCreateAmbiance);
        Button button_write_tag      = (Button) findViewById(R.id.buttonWriteTag);
        Button button_read_tag       = (Button) findViewById(R.id.buttonReadTag);

        button_createAmbiance.setOnClickListener(CreateAmbiance);
        button_write_tag     .setOnClickListener(WriteTag);
        button_read_tag      .setOnClickListener(ReadTag);


        NfcGladysDataBase db = new NfcGladysDataBase(getBaseContext());
        /* Si on n'a pas d'adresse ip dans la base de données */
        if(db.getIpGladys()==null){
            //System.err.println("Je suis dans le if--------------------------->");
            new AlertDialog.Builder(this).setView(getLayoutInflater().inflate(R.layout.authentication_layout, null))
                    // Add action buttons
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            /*Ici on sauvegarde dans la base de donnée*/

                        }
                    })
                    /*.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //LoginDialogFragment.this.getDialog().cancel();
                        }
                    })*/.create().show()
            ;

            /*new AlertDialog.Builder(this).setTitle("Tag").setIcon(R.drawable.nfc).setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    System.err.println("Je neDialog");

                }
            }).create().show();*/
        }
        else{
            System.err.println("Je ne suis pas dans le if--------------------------->");

        }

    }

    public OnClickListener CreateAmbiance = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, CreateAmbiance.class);
            startActivity(intent);
        }
    };

    public OnClickListener WriteTag = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //Intent intent = new Intent(MainActivity.this, WriteTag.class);
            Intent intent = new Intent(MainActivity.this, ListAmbiance.class);
            startActivity(intent);
        }
    };

    public OnClickListener ReadTag = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, ReadTag.class);
            startActivity(intent);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settingsItem:
                new AlertDialog.Builder(this).setView(getLayoutInflater().inflate(R.layout.authentication_layout, null))
                        // Add action buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            /*Ici on sauvegarde dans la base de donnée*/

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }).create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
