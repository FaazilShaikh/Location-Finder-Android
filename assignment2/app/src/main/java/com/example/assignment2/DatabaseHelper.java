package com.example.assignment2;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final String TABLE_NAME = "location";

    private static final String ADDRESS = "address";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";


    public DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    //create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ADDRESS +" TEXT," +
                LATITUDE + " TEXT," +
                LONGITUDE + " TEXT)";

        db.execSQL(createTable);
    }
    //upgrade table
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


     // Adds data to the database

    public boolean addData(String address, String latitude,String longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ADDRESS, address);
        contentValues.put(LATITUDE, latitude);
        contentValues.put(LONGITUDE, longitude);


        Log.d(TAG, "addData: Adding " + address + " to " + TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //it will return -1 if failed
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    // Returns all of the data from database

    public ArrayList<Locations> getDataArr(){
        ArrayList<Locations> locations = new ArrayList<Locations>();

        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;

        Log.d("query",query);
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                locations.add(new Locations(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3)));
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return locations;
    }

    //updates locations
    public void update(int id, String address, String latitude, String longitude){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ADDRESS, address);
        contentValues.put(LATITUDE, latitude);
        contentValues.put(LONGITUDE, longitude);

         db.update(TABLE_NAME, contentValues,"ID =" + id, null);

    }

    //deletes any location where the id is equal to the id specified
    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "ID =" + id, null);

    }


}

























