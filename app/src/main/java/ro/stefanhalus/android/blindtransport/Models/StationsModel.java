package ro.stefanhalus.android.blindtransport.Models;

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

    public static String POPULATE = "INSERT INTO stations (id,name) VALUES " +
            "(1,'Agronomia'), " +
            "(2,'Andrei Muresanu'), " +
            "(3,'Anton Pann'), " +
            "(4,'Barbu Patriciu'), " +
            "(5,'Becas Est'), " +
            "(6,'Becas Vest'), " +
            "(7,'Bistritei'), " +
            "(8,'Borsec Nord'), " +
            "(9,'Borsec Sud'), " +
            "(10,'Calea Manastur'), " +
            "(11,'Centrul Medical'), " +
            "(12,'Closca'), " +
            "(13,'Compexit'), " +
            "(14,'Compl. Tempus Nord'), " +
            "(15,'Compl. Tempus Sud'), " +
            "(16,'Dacia Service'), " +
            "(17,'Dacia Service Sos'), " +
            "(18,'Disp. Clabucet'), " +
            "(19,'Disp. Unirii'), " +
            "(20,'Disp. Zorilor'), " +
            "(21,'Drapelului Est'), " +
            "(22,'Drapelului Vest'), " +
            "(23,'Drum Faget'), " +
            "(24,'Fabrica de Bere'), " +
            "(25,'Fagului'), " +
            "(26,'Garbau'), " +
            "(27,'Ghe. Dima'), " +
            "(28,'Gheorghe Doja'), " +
            "(29,'Gradini Manastur'), " +
            "(30,'Herculane'), " +
            "(31,'I.P. Voitesti Nord'), " +
            "(32,'I.P. Voitesti Sud'), " +
            "(33,'Ion Mester '), " +
            "(34,'Izlazului'), " +
            "(35,'Memorandumului Nord'), " +
            "(36,'Memorandumului Sud'), " +
            "(37,'Minerva'), " +
            "(38,'N. Titulescu'), " +
            "(39,'Observatorului Nord'), " +
            "(40,'Observatorului Sud'), " +
            "(41,'Opera'), " +
            "(42,'Opera'), " +
            "(43,'P-ta 1 Mai'), " +
            "(44,'P-ta 1 Mai Sos'), " +
            "(45,'P-ta Avram Iancu'), " +
            "(46,'P-ta Cipariu Est'), " +
            "(47,'P-ta Cipariu Nord'), " +
            "(48,'P-ta Cipariu Sud'), " +
            "(49,'P-ta Garii Sos'), " +
            "(50,'P-ta Garii Sud'), " +
            "(51,'P-ta I Agarbiceanu Est'), " +
            "(52,'P-ta I Agarbiceanu Vest'), " +
            "(53,'P-ta M. Viteazul Vest'), " +
            "(54,'Paris'), " +
            "(55,'Peana'), " +
            "(56,'Ploiesti'), " +
            "(57,'PMV 2 Sos'), " +
            "(58,'Pod Traian'), " +
            "(59,'Primaverii'), " +
            "(60,'Regionala CFR'), " +
            "(61,'Septimiu Albini Nord'), " +
            "(62,'Septimiu Albini Sud'), " +
            "(63,'Silviu Dragomir'), " +
            "(64,'Snagov Nord'), " +
            "(65,'Snagov Sud'), " +
            "(66,'Sora'), " +
            "(67,'Spitalul de Copii'), " +
            "(68,'Spitalul Recuperare Nord'), " +
            "(69,'Spitalul Recuperare Sud'), " +
            "(70,'Teatru'), " +
            "(71,'Traian'), " +
            "(72,'Trifoiului Nord'), " +
            "(73,'Trifoiului Sud'), " +
            "(74,'Victoria'), " +
            "(75,'Vitacom'), " +
            "(76,'Vitacom Nord'), " +
            "(77,'Zorilor');";

    public StationsModel() {
        super();
    }

    public StationsModel(int id, String name) {
        super();
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

    @Override
    public String toString() {
        return this.name;
    }

}
