package com.piropa_field_test.data;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


import com.piropa_field_test.DataSave;



public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "PiropaDataBase";
 
    // Contacts table name
    private static final String TABLE_PIROPA = "PiropaTable";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_STRENGTH= "strength";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_CID = "cid";
    private static final String KEY_LAC = "lac";
  
    
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PIROPA_TABLE = "CREATE TABLE " 
        								+ TABLE_PIROPA + "("
        								+ KEY_ID + " INTEGER PRIMARY KEY," 
        								+ KEY_STRENGTH + " INTEGER,"
        								+ KEY_LATITUDE + " TEXT," 
        								+ KEY_LONGITUDE + " TEXT,"
        								+ KEY_CID + " INTEGER,"
        								+ KEY_LAC + " INTEGER"
        								+")";
        db.execSQL(CREATE_PIROPA_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PIROPA);
 
        // Create tables again
        onCreate(db);
    }
    
    public void addTestPunkt(DataSave datasave) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_STRENGTH, datasave.getStrength());
        values.put(KEY_LATITUDE, datasave.getLatitude());
        values.put(KEY_LONGITUDE, datasave.getLongitude());
        values.put(KEY_CID, datasave.getCid());
        values.put(KEY_LAC, datasave.getLac());
        
        // Inserting Row
        db.insert(TABLE_PIROPA, null, values);
        db.close(); // Closing database connection
    }
    
    
    public DataSave getTestPunkt(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
     
        Cursor cursor = db.query(TABLE_PIROPA, new String[] { KEY_ID,
        		KEY_STRENGTH, KEY_LATITUDE, KEY_LONGITUDE, KEY_CID, KEY_LAC}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
     
        DataSave datasave = new DataSave(Integer.parseInt(cursor.getString(0)),
                cursor.getInt(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getInt(5));
        // return database
        return datasave;
    }
    
    
    public int updateTestPunkt(DataSave data) {
        SQLiteDatabase db = this.getWritableDatabase();
     
        ContentValues values = new ContentValues();
        values.put(KEY_STRENGTH, data.getStrength());
        values.put(KEY_LATITUDE, data.getLatitude());
        values.put(KEY_LONGITUDE, data.getLongitude());
        values.put(KEY_CID, data.getCid());
        values.put(KEY_LAC, data.getLac());
     
        // updating row
        return db.update(TABLE_PIROPA, values, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
    }
    
    
    public void deleteTestPunkt(DataSave data) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PIROPA, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
        db.close();
    }
    
    
    public int getTestPunktCount() {
        String countQuery = "SELECT  * FROM " + TABLE_PIROPA;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
    
    
    public List<DataSave> getAllContacts() {
        List<DataSave> contactList = new ArrayList<DataSave>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PIROPA;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataSave contact = new DataSave();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setStrength(cursor.getInt(1));
                contact.setLatitude(cursor.getString(2));
                contact.setLongitude(cursor.getString(3));
                contact.setCid(cursor.getInt(4));
                contact.setLac(cursor.getInt(5));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
     
        // return contact list
        return contactList;
    }
  
    public List<DataSave> getSameCell(int cid, int lac) {
        List<DataSave> contactList = new ArrayList<DataSave>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PIROPA;
     
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
     
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	if (cursor.getInt(4)==cid && cursor.getInt(5)==lac)
            	{
                DataSave contact = new DataSave();
                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setStrength(cursor.getInt(1));
                contact.setLatitude(cursor.getString(2));
                contact.setLongitude(cursor.getString(3));
                contact.setCid(cursor.getInt(4));
                contact.setLac(cursor.getInt(5));
                // Adding contact to list
                contactList.add(contact);
            	}
            } while (cursor.moveToNext());
        }
     
        // return contact list
        return contactList;
    }
    
    
}
