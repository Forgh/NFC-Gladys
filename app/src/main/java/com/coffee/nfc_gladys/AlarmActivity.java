package com.coffee.nfc_gladys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.coffee.nfc_gladys.PartieMetier.Alarm;
import com.coffee.nfc_gladys.PartieMetier.Music;

/**
 * Created by s-setsuna-f on 25/04/16.
 */
public class AlarmActivity extends AppCompatActivity {
    Button button_back;
    Button button_next;
    Alarm alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_layout);

        setupButton();
    }

    private void setupButton(){
        button_back = (Button) findViewById(R.id.buttonAlarmBackCreateAmbiance);
        button_next = (Button) findViewById(R.id.buttonAlarmNextCreateAmbiance);

        button_back.setOnClickListener(BackToCreatAmbiance);
        button_next.setOnClickListener(NextToCreatAmbiance);
    }

    public View.OnClickListener BackToCreatAmbiance = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AlarmActivity.this, CreateAmbiance.class);
            startActivity(intent);
        }
    };

    public View.OnClickListener NextToCreatAmbiance = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*il faudra mettre des parametres*/
            alarm = new Alarm();
            finish();
        }
    };

    @Override
    public void finish() {
        Intent intent = new Intent();
        //intent.setType(alarm.generateCode());
        setResult(RESULT_OK, intent);
        super.finish();

    }
}
