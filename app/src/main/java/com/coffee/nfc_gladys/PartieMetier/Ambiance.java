package com.coffee.nfc_gladys.PartieMetier;

/**
 * Created by s-setsuna-f on 11/05/16.
 */

public class Ambiance{
    public Ambiance(String name, String code){this.name=name; this.code=code;}
    public Ambiance(){this.name=null; this.code=null;}
    private int id;
    private String name;
    private String code;

    public String getName(){return name;}
    public String getCode(){return code;}
    public void setId(int id){this.id=id;}
    public void setName(String name){this.name=name;}
    public void setCode(String code){this.code=code;}
}

