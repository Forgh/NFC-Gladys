package com.coffee.nfc_gladys.PartieMetier;

import android.content.Context;

import com.coffee.nfc_gladys.R;

/**
 * Created by ghost_000 on 25/04/2016.
 */
public class Alarm extends Module{

    public Alarm() {
        super("002", "Alarm");
        this.actionsList.put("stopAlarm","001");
    }


    public void stop(){};

    public String generateUrlFragment(String idAction){
        return "/script/run?name=alarm.js&";
    }

    public String getNameActionByCode(String code, Context context){
        String []split=code.split(":");
        String ret = "";
        switch (split[0]){
            case "001":
                ret = context.getResources().getString(R.string.StopAlarm);
                break;
            default:
                ret = code; break;
        }
        return ret;
    }
}
