package ro.stefanhalus.android.blindtransport.DatabaseModel;

public class StationsModel {
    public static final String TABLE_NAME = "stations";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";

    private int id;
    private String name;

    // Create table SQL query
    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME + " TEXT)";

    public StationsModel() {
    }

    StationsModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

}
