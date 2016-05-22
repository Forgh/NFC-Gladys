package com.coffee.nfc_gladys;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.coffee.nfc_gladys.PartieMetier.Light;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;


import org.w3c.dom.Text;

/**
 * Created by s-setsuna-f on 25/04/16.
 */
public class LightActivity extends AppCompatActivity {
    //EditText eText_color;
    SeekBar seekBar;
    CheckBox checkBox_auto;
    Switch mySwitch;
    Button button_back;
    Button button_next;
    String str_code;
    Light light;
    int progressBar=80;
    String str_color="FFFFFFFF";

    private View root;


    private int currentBackgroundColor = 0xffffffff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.light_layout);

        seekBar = (SeekBar) findViewById(R.id.seekBarLight);
        mySwitch = (Switch) findViewById(R.id.switch_light);
        checkBox_auto = (CheckBox) findViewById(R.id.checkBoxLightAuto);

        button_back = (Button) findViewById(R.id.buttonLightBackCreateAmbiance);
        button_next = (Button) findViewById(R.id.buttonLightNextCreateAmbiance);

        button_back.setOnClickListener(BackToCreatAmbiance);
        button_next.setOnClickListener(NextToCreatAmbiance);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressBar=progress;
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        selectColor();
    }

    public View.OnClickListener BackToCreatAmbiance = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           finish();
        }
    };

    public View.OnClickListener NextToCreatAmbiance = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*il faudra mettre des parametres*/
            light = new Light();

            light.setColor(str_color);
            str_code=light.outputToNFCTagString("setColor")+";";
            //System.out.println("la couleur est de : "+str_color+"----------------------------------------");

            light.setBrightness(progressBar);
            str_code+=light.outputToNFCTagString("setBrightness")+";";
            finish();
        }
    };

    @Override
    public void finish() {
        Intent intent = new Intent();
        intent.setType(str_code);
        setResult(RESULT_OK, intent);
        super.finish();
    }


    public void selectColor(){

        findViewById(R.id.buttonSelectColor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context context = LightActivity.this;

                ColorPickerDialogBuilder
                        .with(context)
                        .setTitle(getResources().getString(R.string.ChooseColor))
                        .initialColor(currentBackgroundColor)
                        .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                        .density(12)
                        .setOnColorSelectedListener(new OnColorSelectedListener() {
                            @Override
                            public void onColorSelected(int selectedColor) {
                                toast(getResources().getString(R.string.OnColorSelected)+": 0x" + Integer.toHexString(selectedColor));
                                str_color=""+Integer.toHexString(selectedColor).toUpperCase();
                            }
                        })
                        .setPositiveButton("ok", new ColorPickerClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                                changeBackgroundColor(selectedColor);
                                if (allColors != null) {
                                    StringBuilder sb = null;

                                    for (Integer color : allColors) {
                                        if (color == null)
                                            continue;
                                        if (sb == null)
                                            sb = new StringBuilder(getResources().getString(R.string.ColorList)+":");
                                        sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                                    }

                                    if (sb != null)
                                        Toast.makeText(getApplicationContext(), sb.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .showColorEdit(true)
                        .build()
                        .show();
            }
        });
    }
    private void changeBackgroundColor(int selectedColor) {
        currentBackgroundColor = selectedColor;
        //root.setBackgroundColor(selectedColor);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
