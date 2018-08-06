package ro.stefanhalus.android.blindtransport.DatabaseModel;

public class StopsModel {
    public static final String TABLE_NAME = "lines";

    public static final String COLUMN_STATION_ID = "station_id";
    public static final String COLUMN_LINE_ID = "line_id";

    private int stationId;
    private int lineId;

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
            + COLUMN_STATION_ID + " INTEGER NOT NULL, "
            + COLUMN_LINE_ID + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + COLUMN_STATION_ID + ") REFERENCES " + StationsModel.TABLE_NAME + " (" + StationsModel.COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE, " +
            "FOREIGN KEY (" + COLUMN_LINE_ID + ") REFERENCES " + LinesModel.TABLE_NAME + " (" + LinesModel.COLUMN_ID + ") ON DELETE CASCADE ON UPDATE CASCADE);";

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
