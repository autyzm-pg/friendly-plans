package database;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Schema;


public class DatabaseCreator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1086, "database");
        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }
}


