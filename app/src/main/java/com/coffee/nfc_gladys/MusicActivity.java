package com.coffee.nfc_gladys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.coffee.nfc_gladys.PartieMetier.Light;
import com.coffee.nfc_gladys.PartieMetier.Music;

/**
 * Created by s-setsuna-f on 25/04/16.
 */
public class MusicActivity extends Activity {

    Spinner spinner;
    Button button_back;
    Button button_next;
    Music music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_layout);
        spinner = (Spinner) findViewById(R.id.spinnerMusic);

        setupButton();
    }

    private void setupButton(){
        button_back = (Button) findViewById(R.id.buttonMusicBackCreateAmbiance);
        button_next = (Button) findViewById(R.id.buttonMusicNextCreateAmbiance);

        button_back.setOnClickListener(BackToCreatAmbiance);
        button_next.setOnClickListener(NextToCreatAmbiance);
    }

    public View.OnClickListener BackToCreatAmbiance = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MusicActivity.this, CreateAmbiance.class);
            startActivity(intent);
        }
    };

    public View.OnClickListener NextToCreatAmbiance = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            /*il faudra mettre des parametres*/
            music = new Music();
            finish();
        }
    };

    @Override
    public void finish() {
        Intent intent = new Intent();
        //intent.setType(music.generateCode());
        setResult(RESULT_OK, intent);
        super.finish();

    }
}
