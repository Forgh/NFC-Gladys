package com.coffee.nfc_gladys.PartieMetier;

import android.content.Context;

import com.coffee.nfc_gladys.R;

/**
 * Created by ghost_000 on 25/04/2016.
 */
public class Music extends Module{

    public Music() {
        super("001", "Music");
        this.actionsList.put("play","001");
        this.actionsList.put("pause","002");
        this.actionsList.put("stop", "003");
    }

    public void play(){};

    public void pause(){};

    public void stop(){};

    public String getNameActionByCode(String code, Context context){
        String []split=code.split(":");
        String ret = "";
        switch (split[0]){
            case "001":
                ret = context.getResources().getString(R.string.MusicPlay);
                break;
            case "002":
                ret = context.getResources().getString(R.string.MusicPause);;
                break;
            case "003":
                ret = context.getResources().getString(R.string.MusicStop);
                break;
            default:
                ret = code; break;
        }
        return ret;
    }
}
