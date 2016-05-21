package com.coffee.nfc_gladys;
import com.coffee.nfc_gladys.PartieMetier.Ambiance;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by s-setsuna-f on 10/05/16.
 */
public class NfcGladysDataBase extends SQLiteOpenHelper{

    //Database info
    public static final String DATABASE_NAME    = "NFCGladysDataBase";
    private static final int   DATABASE_VERSION = 1;

    //Table Ambiance
    public static final String AMBIANCE_TABLE_NAME  = "Ambiance";
    public static final String AMBIANCE_COLUMN_ID   = "id";
    public static final String AMBIANCE_COLUMN_NAME = "name";
    public static final String AMBIANCE_COLUMN_CODE = "code";

    //Table Gladys
    public static final String GLADYS_TABLE_NAME    = "Gladys";
    public static final String GLADYS_COLUMN_ID     = "id";
    public static final String GLADYS_COLUMN_IP     = "ip";
    public static final String GLADYS_COLUMN_TOKEN  = "token";

    //==================================================================
    //Constructor
    public NfcGladysDataBase(Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableAmbiance="";
        String tableGladys  ="";
        /*String tableLight   ="";
        String tableMusic   ="";
        String tableAlarm   ="";*/

        tableAmbiance+="CREATE TABLE "     +AMBIANCE_TABLE_NAME+"(" ;
        tableAmbiance+=AMBIANCE_COLUMN_ID  +" INTEGER PRIMARY KEY, ";
        tableAmbiance+=AMBIANCE_COLUMN_NAME+" TEXT, ";
        tableAmbiance+=AMBIANCE_COLUMN_CODE+" TEXT)" ;

        tableGladys+="CREATE TABLE "    +GLADYS_TABLE_NAME  +"(" ;
        tableGladys+=GLADYS_COLUMN_ID   +" INTEGER PRIMARY KEY,";
        tableGladys+=GLADYS_COLUMN_IP   +" TEXT,";
        tableGladys+=GLADYS_COLUMN_TOKEN+" TEXT)";

        db.execSQL(tableAmbiance);
        db.execSQL(tableGladys);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + AMBIANCE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + GLADYS_TABLE_NAME);
        this.onCreate(db);
    }


    //==================================================================
    //Insert Ambiance
    public void insertAmbiance(Ambiance ambiance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(AMBIANCE_COLUMN_NAME, ambiance.getName());
        contentValues.put(AMBIANCE_COLUMN_CODE, ambiance.getCode());

        db.insert(AMBIANCE_TABLE_NAME, null, contentValues);
    }

    //Insert GladysInfo
    public void insertGladysInfo(String ip, String token){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(GLADYS_COLUMN_IP, ip);
        contentValues.put(GLADYS_COLUMN_TOKEN, token);

        db.insert(GLADYS_TABLE_NAME, null, contentValues);
    }


    //==================================================================
    //Get All Ambiance
    public ArrayList<Ambiance> getAllAmbiance(){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM "+AMBIANCE_TABLE_NAME+"", null);
        // S'il n'y a aucun element dans la base de donnee on retourn null.
        if(res==null)
            return null;
        res.moveToFirst();

        ArrayList<Ambiance> ambiances = new ArrayList<>();
        while (!res.isAfterLast()) {
            Ambiance ambiance = cursorToAmbiance(res);
            ambiances.add(ambiance);
            res.moveToNext();
        }

        return ambiances;
    }

    //Get one Ambiance
    public Ambiance getDataAmbianceById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM " + AMBIANCE_TABLE_NAME + " WHERE id=" + id + "", null);
        // S'il n'y a aucun element dans la base de donnee on retourn null.
        if(res==null)
            return null;

        Ambiance ambiance = cursorToAmbiance(res);

        // fermeture du curseur
        res.close();
        return ambiance;
    }

    //Get GladysInfo
    public Cursor getDataGladysInfo(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT * FROM " + GLADYS_TABLE_NAME + "", null);
        res.moveToPosition(0);
        return res;
    }

    public String getIpGladys(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT "+GLADYS_COLUMN_IP+" FROM "+GLADYS_TABLE_NAME+"", null);
        // S'il n'y a aucun element dans la base de donnee on retourn null.

        if(res==null)
            return null;

        String val=null;
        if (res.moveToFirst()) {
            val = res.getString(res.getColumnIndex(GLADYS_COLUMN_IP));
            res.close();
        }
        return val;
    }

    public String getTokenGladys(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res=db.rawQuery("SELECT "+GLADYS_COLUMN_TOKEN+" FROM "+GLADYS_TABLE_NAME+"", null);
        // S'il n'y a aucun element dans la base de donnee on retourn null.

        if(res==null)
            return null;

        String val=null;
        if (res.moveToFirst()) {
            val = res.getString(res.getColumnIndex(GLADYS_COLUMN_TOKEN));
            res.close();
        }
        return val;
    }

    //==================================================================
    //delete Ambiance
    public Integer deleteAmbianceById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(AMBIANCE_TABLE_NAME, "id=?", new String[] {Integer.toString(id)});
    }

    //delete GladysInfo
    public Integer deleteGladysInfo(/*int id*/){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(GLADYS_TABLE_NAME, null, null);//, "id=?", new String[] {Integer.toString(id)});
    }

    private Ambiance cursorToAmbiance(Cursor cursor){
        Ambiance ambiance = new Ambiance();
        ambiance.setId(cursor.getInt(0));
        ambiance.setName(cursor.getString(1));
        ambiance.setCode(cursor.getString(2));
        return ambiance;
    }

}
