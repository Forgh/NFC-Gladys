package com.coffee.nfc_gladys.PartieMetier;

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
}
