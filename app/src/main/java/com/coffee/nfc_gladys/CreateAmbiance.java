package com.coffee.nfc_gladys;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.coffee.nfc_gladys.PartieMetier.Ambiance;
import com.coffee.nfc_gladys.PartieMetier.ModuleSerializable;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;


import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;

/**
 * Created by s-setsuna-f on 12/04/16.
 */
public class CreateAmbiance extends AppCompatActivity {
    boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private String msg;
    private ItemCreateAmbianceAdapter adapter;
    private ArrayList<ModuleSerializable> modules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creer_ambiance);

        modules = new ArrayList<>();

        setupListViewAdapter();

        setupButtonAddMoreModule();

        setupButtonQuit();

        setupButtonSaveAndQuit();

        setupButtonSaveAndWriteOnTag();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_module, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addModuleItem:
                displayListModuleDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void removeAtomPayOnClickHandler(View v) {
        ModuleSerializable itemToRemove = (ModuleSerializable)v.getTag();
        adapter.remove(itemToRemove);
    }

    private void setupListViewAdapter() {
        adapter = new ItemCreateAmbianceAdapter(CreateAmbiance.this, R.layout.item_create_anbiance_listview, new ArrayList<ModuleSerializable>());
        ListView atomPaysListView = (ListView)findViewById(R.id.lViewAmbiance);
        atomPaysListView.setAdapter(adapter);
    }

    private void setupButtonAddMoreModule() {
        findViewById(R.id.buttonAddModule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayListModuleDialog();
            }
        });
    }

    private void setupButtonSaveAndQuit() {
        findViewById(R.id.buttonSaveAndQuit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveInDataBase();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupButtonQuit() {
        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }



    private void displayListModuleDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select module");

        ListView modeList = new ListView(this);
        String[] stringArray = new String[] { "Light", "Music", "Alarm" };
        ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArray);
        modeList.setAdapter(modeAdapter);

        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Intent intent;

                switch(position) {
                    case 0:
                        intent = new Intent(getBaseContext(), LightActivity.class);
                        startActivityForResult(intent, 10);
                        break;
                    case 1:
                        intent = new Intent(getBaseContext(), MusicActivity.class);
                        startActivityForResult(intent, 10);
                        break;
                    case 2:
                        intent = new Intent(getBaseContext(), AlarmActivity.class);
                        startActivityForResult(intent, 10);
                        break;
                    default:
                        break;
                }
            }
        });

        builder.setView(modeList);
        final Dialog dialog = builder.create();

        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String codeModule="";
        if(resultCode == RESULT_OK) {
            if (data != null && data.getType() != null) {
                codeModule = data.getType().toString();
                ModuleSerializable ms = new ModuleSerializable(codeModule);
                modules.add(ms);
                adapter.insert(ms, 0);
            } else
                System.err.println("Cannot save this module");
        }
    }

    private void setupButtonSaveAndWriteOnTag() {
        findViewById(R.id.buttonSaveAndWrite).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                msg = saveInDataBase();

                mNfcAdapter = NfcAdapter.getDefaultAdapter(CreateAmbiance.this);
                mNfcPendingIntent = PendingIntent.getActivity(CreateAmbiance.this, 0, new Intent(CreateAmbiance.this, CreateAmbiance.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

                enableTagWriteMode();

                new AlertDialog.Builder(CreateAmbiance.this).setTitle("Tag").setIcon(R.drawable.nfc).setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        disableTagWriteMode();
                    }
                }).create().show();

            }
        });
    }

    private String saveInDataBase(){
        NfcGladysDataBase db = new NfcGladysDataBase(CreateAmbiance.this);
        String name=((EditText)findViewById(R.id.eTextNomAmbiance)).toString();
        String code="";
        for(ModuleSerializable s : modules){
            code+=s.getCode().toString();
        }
        Ambiance ambiance = new Ambiance(name, code);
        db.insertAmbiance(ambiance);
        return code;
    }


    private void enableTagWriteMode() {
        mWriteMode = true;
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter[] mWriteTagFilters = new IntentFilter[] { tagDetected };
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mWriteTagFilters, null);
    }

    private void disableTagWriteMode() {
        mWriteMode = false;
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // Tag writing mode
        if (mWriteMode && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            NdefRecord record = createTextRecord(msg, Locale.FRANCE, true);

            NdefMessage message = new NdefMessage(new NdefRecord[] { record });
            if (writeTag(message, detectedTag)) {
                intent = new Intent(CreateAmbiance.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(this, "Success: Wrote placeid to nfc tag", Toast.LENGTH_LONG).show();
            }
        }
    }

    public NdefRecord createTextRecord(String payload, Locale locale, boolean encodeInUtf8) {
        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = encodeInUtf8 ? Charset.forName("UTF-8") : Charset.forName("UTF-16");
        byte[] textBytes = payload.getBytes(utfEncoding);
        int utfBit = encodeInUtf8 ? 0 : (1 << 7);
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte) status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
        return record;
    }

    public boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(getApplicationContext(), "Error: tag not writable", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(getApplicationContext(), "Error: tag too small", Toast.LENGTH_SHORT).show();
                    return false;
                }
                ndef.writeNdefMessage(message);
                //System.out.println("PAR LE WRITE");
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        //System.out.println("PAR LE FORMAT");
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}