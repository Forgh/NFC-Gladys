package com.coffee.nfc_gladys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.*;
import android.widget.EditText;



public class MainActivity extends AppCompatActivity {
    private boolean invalidCredentials;
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


        final NfcGladysDataBase db = new NfcGladysDataBase(getBaseContext());
        invalidCredentials = false;
        /* Si on n'a pas d'adresse ip dans la base de données */
        //db.deleteGladysInfo();
        if(db.getIpGladys()==null){

            final View loginView = getLayoutInflater().inflate(R.layout.authentication_layout, null);

            new AlertDialog.Builder(this).setView(loginView)
                    // Add action buttons
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            /*Ici on génère le token et on l'enregistre via un service */
                            String ip=((EditText)loginView.findViewById(R.id.eTextIp)).getText().toString();
                            String email=((EditText)loginView.findViewById(R.id.eTextEmail)).getText().toString();
                            String password=((EditText)loginView.findViewById(R.id.eTextPassword)).getText().toString();

                            Intent i = new Intent(MainActivity.this, AuthService.class);

                            i.putExtra("ip", ip);
                            i.putExtra("email",email);
                            i.putExtra("password",password);

                            startService(i);

                        }

                    }).create().show();


        }
        else{
            System.err.println("IDs have been retrieved");

        }

    }

    public OnClickListener CreateAmbiance = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, CreateAmbianceActivity.class);
            startActivity(intent);
        }
    };

    public OnClickListener WriteTag = new OnClickListener() {
        @Override
        public void onClick(View v) {
            //Intent intent = new Intent(MainActivity.this, WriteTagActivity.class);
            Intent intent = new Intent(MainActivity.this, ListAmbianceActivity.class);
            startActivity(intent);
        }
    };

    public OnClickListener ReadTag = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, ReadTagActivity.class);
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
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            /*Ici on sauvegarde dans la base de donnée*/

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        }).create().show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
