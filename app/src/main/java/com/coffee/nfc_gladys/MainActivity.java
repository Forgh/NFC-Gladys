package com.coffee.nfc_gladys;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.View.*;
import android.widget.EditText;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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
            //System.err.println("Je suis dans le if--------------------------->");
            final View loginView = getLayoutInflater().inflate(R.layout.authentication_layout, null);

            new AlertDialog.Builder(this).setView(loginView)
                    // Add action buttons
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        /*Ici on sauvegarde dans la base de donnée*/
                            String ip=((EditText)loginView.findViewById(R.id.eTextIp)).getText().toString();
                            String email=((EditText)loginView.findViewById(R.id.eTextEmail)).getText().toString();
                            String password=((EditText)loginView.findViewById(R.id.eTextPassword)).getText().toString();

                            String token = "";
                            try {
                                token = generateToken(ip,email,password);
                            } catch (IOException | JSONException e) {
                                invalidCredentials = true;

                                e.printStackTrace();
                            }

                            if(!token.isEmpty())
                                db.insertGladysInfo(ip, token);
                            else{
                                Toast.makeText(getBaseContext(),"Error: invalid credentials or IP address, please try again", Toast.LENGTH_LONG).show();
                            }


                        }

                    }).create().show();


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

        public String generateToken(String ip, String email, String password) throws IOException, JSONException {
            int SDK_INT=android.os.Build.VERSION.SDK_INT;
            String ret = "";

            if(SDK_INT>8){
                StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
                StrictMode.setThreadPolicy(policy);


                CookieJar cookieJar=
                new PersistentCookieJar(new SetCookieCache(),new SharedPrefsCookiePersistor(getBaseContext()));

                OkHttpClient client=new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .build();
                Request request=new Request.Builder()
                .url("http://"+ip+":1337/session/create?email="+email+"&password="+password)
                .build();

                    Response response=client.newCall(request).execute();

                    Request request2=new Request.Builder()
                    .url("http://"+ip+":1337/token/create?name=nfc_gladys_android")
                    .build();
                    Response response2=client.newCall(request2).execute();

                    String jsonData=response2.body().string();
                    JSONObject Jobject=new JSONObject(jsonData);
                    ret = Jobject.getString("value");
            }
            return ret;
        }
}
