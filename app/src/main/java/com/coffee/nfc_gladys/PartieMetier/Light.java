package com.coffee.nfc_gladys.PartieMetier;

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
            case "setColorLight":
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

    public String getNameActionByCode(String code){
        String []split=code.split(":");
        String ret = "";
        switch (split[0]){
            case "004":
                ret = "Color=#<font color=#"+split[1]+">"+split[1]+"</font>";
                break;
            case "005":
                ret = "Brightness="+split[1];;
                break;
            default:
                ret = code; break;
        }
        return ret;
    }
}
