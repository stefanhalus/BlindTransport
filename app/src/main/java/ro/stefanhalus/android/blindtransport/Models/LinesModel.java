package ro.stefanhalus.android.blindtransport.Models;

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

    public static String POPULATE = "INSERT INTO lines (id,name,start,end) VALUES " +
            "(1,'1',1,2), " +
            "(2,'3',1,2)," +
            "(3,'4',1,2)," +
            "(4,'5',1,2)," +
            "(5,'6',1,2)," +
            "(6,'7',1,2)," +
            "(7,'8',1,2);\n" +
            "(8,'8L',1,2)," +
            "(9,'9',1,2)," +
            "(10,'18',1,2)," +
            "(11,'19',1,2)," +
            "(12,'20',1,2)," +
            "(13,'21',1,2);\n" +
            "(14,'22',1,2)," +
            "(15,'23',1,2)," +
            "(16,'23L',1,2)," +
            "(17,'24',1,2)," +
            "(18,'24B',1,2)," +
            "(19,'25',1,2)," +
            "(20,'25N',1,2)," +
            "(21,'26',1,2)," +
            "(22,'26L',1,2)," +
            "(23,'27',1,2)," +
            "(24,'28',1,2)," +
            "(25,'28B',1,2)," +
            "(26,'29',1,2)," +
            "(27,'30',1,2)," +
            "(28,'31',1,2)," +
            "(29,'32',1,2)," +
            "(30,'32B',1,2)," +
            "(31,'33',1,2)," +
            "(32,'34',1,2)," +
            "(33,'35',1,2)," +
            "(34,'36B',1,2)," +
            "(35,'36L',1,2)," +
            "(36,'37',1,22)," +
            "(37,'38',1,2)," +
            "(38,'39',1,2)," +
            "(39,'39L',1,2)," +
            "(40,'40',1,2)," +
            "(41,'40S',1,2)," +
            "(42,'41',1,2)," +
            "(43,'42',1,2)," +
            "(44,'43',1,2)," +
            "(45,'43B',1,2)," +
            "(46,'43P',1,2)," +
            "(47,'46',1,2)," +
            "(48,'46B',1,2)," +
            "(49,'47',1,2)," +
            "(50,'48',1,2)," +
            "(51,'48L',1,2)," +
            "(52,'50',1,2)," +
            "(53,'50L',1,2)," +
            "(54,'52',1,2)," +
            "(55,'87B',1,2)," +
            "(56,'100',1,2)," +
            "(57,'101',1,2)," +
            "(58,'102',1,2)," +
            "(59,'102L',1,2);";

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

    @Override
    public String toString() {
        return this.name;
    }

}
