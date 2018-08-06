package ro.stefanhalus.android.blindtransport.DatabaseModel;

import android.widget.SimpleCursorTreeAdapter;

public class LinesModel {
    public static final String TABLE_NAME = "lines";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_START = "start";
    public static final String COLUMN_END = "end";

    private int id;
    private String name;
    private int start;
    private int end;

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL, "
            + COLUMN_NAME + " TEXT NOT NULL, "
            + COLUMN_START + " INTEGER, "
            + COLUMN_END + " INTEGER, " +
            "FOREIGN KEY (" + COLUMN_START + ") REFERENCES " + StationsModel.TABLE_NAME + " (" + StationsModel.COLUMN_ID + ") ON DELETE RESTRICT ON UPDATE CASCADE, " +
            "FOREIGN KEY (" + COLUMN_END + ") REFERENCES " + StationsModel.TABLE_NAME + " (" + StationsModel.COLUMN_ID + ") ON DELETE RESTRICT ON UPDATE CASCADE);";

    public LinesModel() {

    }

    public LinesModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public LinesModel(int id, String name, int start, int end) {
        this.id = id;
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
