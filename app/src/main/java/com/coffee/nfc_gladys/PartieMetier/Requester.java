package com.coffee.nfc_gladys.PartieMetier;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by ghost_000 on 02/05/2016.
 */
public class Requester {
    private String ip;
    private String token;
    private HashMap<String,Module> modules;
    private String action;
    private OkHttpClient client;


    public Requester(String ip, String tok){
        this.ip = ip;
        this.token = tok;
        this.client = new OkHttpClient();
        this.modules = new HashMap<>();
        this.initModules();

    }


    private void initModules(){
        Music m = new Music();
        Light l = new Light();
        Alarm a = new Alarm();
        this.modules.put(m.getId(), m);
        this.modules.put(l.getId(),l);
        this.modules.put(a.getId(),a);

    }


    public String send(String command) throws IOException {
        String[] frags;
        frags = command.split("\\.");
        System.out.println(frags[0]);
        System.out.println("http://"+this.ip+":1337"+this.modules.get(frags[0]).generateUrlFragment(frags[1])+"token="+this.token);
        Request request = new Request.Builder()
                .url("http://"+this.ip+":1337"+this.modules.get(frags[0]).generateUrlFragment(frags[1])+"token="+this.token)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
