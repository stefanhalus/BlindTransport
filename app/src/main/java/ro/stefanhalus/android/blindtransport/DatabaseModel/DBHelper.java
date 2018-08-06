package ro.stefanhalus.android.blindtransport.DatabaseModel;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "blind_transport";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // create stations table
        db.execSQL(StationsModel.CREATE_TABLE);
        db.execSQL(LinesModel.CREATE_TABLE);
        db.execSQL(StopsModel.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + StationsModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LinesModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StopsModel.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public void onDowngrade(){

    }

    public long insertStation(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StationsModel.COLUMN_NAME, name);
        long id = db.insert(StationsModel.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public void insertStationsAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.close();
    }

    public ArrayList<String> getAllStations() {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res = db.rawQuery("SELECT * FROM " + StationsModel.TABLE_NAME + ";", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<String> getAllStationsSearch(String search) {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        @SuppressLint("Recycle") Cursor res = db.rawQuery("SELECT * FROM " + StationsModel.TABLE_NAME + " WHERE name LIKE '%" + search + "%'", null);
        res.moveToFirst();
        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex("name")));
            res.moveToNext();
        }
        return array_list;
    }

    public int getStationsCount() {
        String countQuery = "SELECT  * FROM " + StationsModel.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateStation(StationsModel station) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StationsModel.COLUMN_NAME, station.getName());

        // updating row
        return db.update(StationsModel.TABLE_NAME, values, StationsModel.COLUMN_ID + " = ?",
                new String[]{String.valueOf(station.getId())});
    }

    public void deleteStation(String stationName) {
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = StationsModel.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = { stationName };
        int deletedRows = db.delete(StationsModel.TABLE_NAME, selection, selectionArgs);
        db.close();
    }





/// DEPRECATED

    public StationsModel getStation(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(StationsModel.TABLE_NAME,
                new String[]{StationsModel.COLUMN_ID, StationsModel.COLUMN_NAME},
                StationsModel.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        StationsModel station = new StationsModel(
                cursor.getInt(cursor.getColumnIndex(StationsModel.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(StationsModel.COLUMN_NAME)));

        // close the db connection
        cursor.close();

        return station;
    }

}
