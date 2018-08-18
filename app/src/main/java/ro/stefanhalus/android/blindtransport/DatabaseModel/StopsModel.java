package ro.stefanhalus.android.blindtransport.DatabaseModel;

public class StopsModel {
    public static final String TABLE_NAME = "stops";

    public static final String COLUMN_STATION_ID = "station_id";
    public static final String COLUMN_LINE_ID = "line_id";

    private int stationId;
    private int lineId;

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_STATION_ID + " INTEGER NOT NULL, "
            + COLUMN_LINE_ID + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_STATION_ID + ") REFERENCES " + StationsModel.TABLE_NAME + " (" + StationsModel.COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, " +
            "FOREIGN KEY (" + COLUMN_LINE_ID + ") REFERENCES " + LinesModel.TABLE_NAME + " (" + LinesModel.COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";

    public static String POPULATE = "INSERT INTO stops\n" +
            "(station_id, line_id)\n" +
            "VALUES(1, 1), "+
            "VALUES(2, 2), "+
            "VALUES(3, 3), "+
            "VALUES(4, 4), "+
            "VALUES(5, 5), "+
            "VALUES(5, 6), "+
            "VALUES(5, 7), "+
            "VALUES(4, 8), "+
            "VALUES(3, 9), "+
            "VALUES(3, 10), "+
            "VALUES(3, 11), "+
            "VALUES(3, 12), "+
            "VALUES(7, 13), "+
            "VALUES(6, 14), "+
            "VALUES(5, 15), "+
            "VALUES(6, 19), "+
            "VALUES(7, 16), "+
            "VALUES(6, 17), "+
            "VALUES(7, 18), "+
            "VALUES(7, 20), "+
            "VALUES(8, 21), "+
            "VALUES(9, 22), "+
            "VALUES(9, 23), "+
            "VALUES(8, 24), "+
            "VALUES(9, 25), "+
            "VALUES(2, 26), "+
            "VALUES(3, 27);";

    public StopsModel() {
    }

    public StopsModel(int stationId, int lineId) {
        this.stationId = stationId;
        this.lineId = lineId;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

}
