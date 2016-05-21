package com.coffee.nfc_gladys;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
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
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.coffee.nfc_gladys.PartieMetier.Ambiance;
import com.coffee.nfc_gladys.PartieMetier.ModuleSerializable;

public class ListAmbiance extends Activity {
    boolean mWriteMode = false;
    private NfcAdapter mNfcAdapter;
    private PendingIntent mNfcPendingIntent;
    private String msg;

    private List<String> data;
    SingleListAdapter adapter;
    ListView lvView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_ambiance);
        data = new ArrayList<String>();
        fillData();
        adapter = new SingleListAdapter(this, data);
        lvView = (ListView) findViewById(R.id.list);
        lvView.setAdapter(adapter);

        Button b = (Button)findViewById(R.id.buttonWriteAmbianceSelected);
        b.setOnClickListener(write);
    }

    public View.OnClickListener write = new View.OnClickListener() {
        View lv;
        @Override
        public void onClick(View v) {
            final SparseBooleanArray checkedItems = lvView.getCheckedItemPositions();

            if (checkedItems == null) {
                Toast.makeText(ListAmbiance.this, "No selection info available", Toast.LENGTH_LONG).show();
                //return;
            }
            //boolean isFirstSelected = true;
            final int checkedItemsCount = checkedItems.size();
            for (int i = 0; i < checkedItemsCount; ++i) {
                final int position = checkedItems.keyAt(i);
                final boolean isChecked = checkedItems.valueAt(i);
                if (isChecked) {
                    msg = data.get(position);
                    //setupButtonWriteOnTag();
                    mNfcAdapter = NfcAdapter.getDefaultAdapter(ListAmbiance.this);
                    mNfcPendingIntent = PendingIntent.getActivity(ListAmbiance.this, 0, new Intent(ListAmbiance.this, ListAmbiance.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

                    enableTagWriteMode();

                    new AlertDialog.Builder(ListAmbiance.this).setTitle("Tag").setIcon(R.drawable.nfc).setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            disableTagWriteMode();
                        }
                    }).create().show();
                }
            }

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    void fillData() {
        NfcGladysDataBase db = new NfcGladysDataBase(getBaseContext());
        ArrayList<Ambiance> dataAmbiance = db.getAllAmbiance();
        if(dataAmbiance!=null)
            for(Ambiance a : dataAmbiance)
                if(a.getCode()!=null)
                    data.add(a.getCode());
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
                intent = new Intent(ListAmbiance.this, MainActivity.class);
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