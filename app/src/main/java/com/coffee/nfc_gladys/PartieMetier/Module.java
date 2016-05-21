package com.coffee.nfc_gladys.PartieMetier;

import android.annotation.TargetApi;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by ghost_000 on 25/04/2016.
 */
public abstract class Module {
    protected String id;
    protected String name;
    protected HashMap<String, String> actionsList;

    public Module(String id, String n){
        this.id = id;
        this.name = n;
        this.actionsList = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public String getIdForAction(String a){
        return actionsList.get(a);
    }


    public String outputToNFCTagString(String actionName){
        return this.id+"."+this.actionsList.get(actionName);
    }

    public String getActionFromId(String id){
        for (Map.Entry<String, String> entry : this.actionsList.entrySet()) {
            //some modules have actions that are longer than 3.
            if(entry.getValue().substring(0,3).equals(id))
            //if(entry.getValue().equals(id))
                return entry.getKey();
        }
        return null;
    }

    public String parseActionFragment(String fragment){
        return fragment;
    }

    public String generateUrlFragment(String idAction){
        return "/"+this.name.toLowerCase()+"/"+this.parseActionFragment(idAction)+"?";
    }

    /*METTRE UNE METHODE  POUR RETOURNER LE CODE du module */
    /*METHODE A CREER*/
    //Déjà fait ? C'est l'attribut id de cette classe.
    public String generateCode(){
        String string="CodeMODULE_X";
        return string;
    }

}
