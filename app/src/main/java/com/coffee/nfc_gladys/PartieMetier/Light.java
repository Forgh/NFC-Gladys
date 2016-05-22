package com.coffee.nfc_gladys.PartieMetier;

import com.coffee.nfc_gladys.R;












import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.coffee.nfc_gladys.PartieMetier.Alarm;
import com.coffee.nfc_gladys.PartieMetier.Ambiance;
import com.coffee.nfc_gladys.PartieMetier.Light;
import com.coffee.nfc_gladys.PartieMetier.ModuleSerializable;

import com.coffee.nfc_gladys.PartieMetier.Music;

/**
 * Created by ghost_000 on 25/04/2016.
 */
public class Light extends Module {
    private String color;
    private int brightness;
    public Light() {
        super("003", "light");
        this.actionsList.put("switchOn", "001");
        this.actionsList.put("switchOff","002");
        this.actionsList.put("toggle","003");
        this.actionsList.put("setColor","004:");
        this.actionsList.put("setBrightness","005:");
    }

    public void on(){};
    public void off(){};
    public void toggle(){};
    public void setColor(String hex){
        this.color = hex;
        this.actionsList.put("setColor","004:"+color);
    };
    public void setBrightness(int brightness){
        this.brightness = brightness;
        this.actionsList.put("setBrightness","005:"+brightness);
    }

    public String parseActionFragment(String fragment){

        String ret = "";
        if(fragment.contains(":")){
            String[] split;
            split = fragment.split(":");
            switch (split[0]){
                case "004":
                    this.setColor(split[1]);
                    ret = split[0];
                    break;
                case "005":
                    this.setBrightness(Integer.parseInt(split[1]));
                    ret = split[0];
                    break;
                default: ret = split[0]; break;
            }
        }else ret = fragment;
        System.out.println(ret);
        return ret;
    }

    public String generateUrlFragment(String idAction){
        String frag = "/hue/";
        String act = this.getActionFromId(parseActionFragment(idAction));
        System.out.println(act);
        switch (act) {
            case "setColor":
                frag += act + "?color=" + this.color + "&";
                break;
            case "setBrightness":
                frag += act + "?bri=" + this.brightness + "&";
                break;
            default:
                frag += act + "?";
                break;
        }
        return frag;
    }

    public String getNameActionByCode(String code, Context context){
        String []split=code.split(":");
        String ret = "";
        switch (split[0]){
            case "004":
                ret = context.getResources().getString(R.string.LightColor)+"=#<font color=#"+split[1]+">"+split[1]+"</font>";
                break;
            case "005":
                ret =  context.getResources().getString(R.string.LightBrightness)+"="+split[1];;
                break;
            default:
                ret = code; break;
        }
        return ret;
    }
}
