package com.coffee.nfc_gladys;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.StrictMode;
import android.widget.Toast;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ghost on 21/05/2016.
 */
public class AuthService extends IntentService{
    private String ip;
    private String token;
    private String email;
    private String password;
    private OkHttpClient client;
    private NfcGladysDataBase db;

    public AuthService() {
        super("AuthService");
        this.client = new OkHttpClient();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        this.db = new NfcGladysDataBase(getBaseContext());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.ip = intent.getStringExtra("ip");
        this.email = intent.getStringExtra("email");
        this.password = intent.getStringExtra("password");

        try {
            this.token = generateToken();
            if(!token.isEmpty())
                db.insertGladysInfo(ip, token);
            else
                Toast.makeText(getBaseContext(),"Error: invalid credentials or IP address, please try again", Toast.LENGTH_LONG).show();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    public String generateToken() throws IOException, JSONException {
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
