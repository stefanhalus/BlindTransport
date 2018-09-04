package ro.stefanhalus.android.blindtransport.DatabaseModel;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ro.stefanhalus.android.blindtransport.Models.LinesModel;
import ro.stefanhalus.android.blindtransport.Models.StationsModel;
import ro.stefanhalus.android.blindtransport.Models.StopsModel;

public class DBHelper extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 4;

    // Database Name
    private static final String DATABASE_NAME = "blind_transport.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(StationsModel.CREATE_TABLE);
        db.execSQL(LinesModel.CREATE_TABLE);
        db.execSQL(StopsModel.CREATE_TABLE);
    }

    private void populate() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransactionNonExclusive();
        try {
            db.rawQuery(LinesModel.POPULATE, null);
            db.rawQuery(StationsModel.POPULATE, null);
            db.rawQuery(StopsModel.POPULATE, null);
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StationsModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LinesModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StopsModel.TABLE_NAME);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StationsModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LinesModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + StopsModel.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public long insertStation(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StationsModel.COLUMN_NAME, name);
        long id = db.insert(StationsModel.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public long insertStop(StopsModel stop) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(StopsModel.COLUMN_STATION_ID, stop.getStationId());
        values.put(StopsModel.COLUMN_LINE_ID, stop.getLineId());
        long id = db.insert(StationsModel.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public long insertLine(LinesModel line) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LinesModel.COLUMN_NAME, line.getName());
        values.put(LinesModel.COLUMN_START, line.getStart());
        values.put(LinesModel.COLUMN_END, line.getEnd());
        long id = db.insert(StationsModel.TABLE_NAME, null, values);
        db.close();
        return id;
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

    public int getStationIdFromStationName(String stationId) {
        int station = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(StationsModel.TABLE_NAME,
                new String[]{StationsModel.COLUMN_ID},
                StationsModel.COLUMN_NAME + "=?",
                new String[]{stationId},
                null,
                null,
                null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            station = cursor.getInt(0);
        }
        return station;
    }

    public ArrayList<LinesModel> getStopsByStationId(int stationId) {
        ArrayList<LinesModel> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT `stops`.`line_id`, `lines`.`id` AS `id`, `lines`.`name` AS `name`, `lines`.`start` AS `start`, `lines`.`end` AS `end` FROM `stops` " +
                "INNER JOIN `lines` ON `stops`.`line_id` = `lines`.`id` " +
                "WHERE `stops`.`station_id` = ?" +
                "ORDER BY `id` ASC; ";
        @SuppressLint("Recycle") Cursor res = db.rawQuery(sql, new String[]{ String.valueOf(stationId) });
        res.moveToFirst();
        while (!res.isAfterLast()) {
            LinesModel line = new LinesModel();
            line.setId(Integer.parseInt(res.getString(res.getColumnIndex("id"))));
            line.setName(res.getString(res.getColumnIndex("name")));
            line.setStart(Integer.parseInt(res.getString(res.getColumnIndex("start"))));
            line.setEnd(Integer.parseInt(res.getString(res.getColumnIndex("end"))));
            array_list.add(line);
            res.moveToNext();
        }
        return array_list;
    }

    public ArrayList<StationsModel> getAllStationsAsArrays() {
        ArrayList<StationsModel> dataArrays = new ArrayList<>();
        Cursor cursor;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.query(
                    StationsModel.TABLE_NAME,
                    new String[]{ StationsModel.COLUMN_ID, StationsModel.COLUMN_NAME },
                    " (SELECT COUNT(`station_id`) AS `rows` FROM `stops` WHERE `stops`.`station_id` = `stations`.`id`) > 0",
                    null,
                    null,
                    null,
                    StationsModel.COLUMN_NAME
            );
cursor.moveToFirst();
if (!cursor.isAfterLast()) {
                do {
                    dataArrays.add(new StationsModel(cursor.getInt(0), cursor.getString(1)));
                }
                while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataArrays;
    }

    public ArrayList<StationsModel> getAllStationsAsArrays(String search) {
        ArrayList<StationsModel> dataArrays = new ArrayList<>();
        Cursor cursor;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            cursor = db.query(
                    StationsModel.TABLE_NAME,
                    new String[]{StationsModel.COLUMN_ID, StationsModel.COLUMN_NAME},
                    StationsModel.COLUMN_NAME + " LIKE ? ",
                    new String[]{"%" + search + "%"},
                    null,
                    null,
                    StationsModel.COLUMN_NAME
            );
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                do {
                    dataArrays.add(new StationsModel(cursor.getInt(0), cursor.getString(1)));
                }
                while (cursor.moveToNext());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataArrays;
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
        String[] selectionArgs = {stationName};
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
