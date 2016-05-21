package com.coffee.nfc_gladys.PartieMetier;

/**
 * Created by s-setsuna-f on 13/05/16.
 */
import java.io.Serializable;

public class ModuleSerializable implements Serializable {
    private static final long serialVersionUID = -5435670920302756945L;

    private String code = "";

    public ModuleSerializable(String code) {
        this.setCode(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}