package database;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;


public class DatabaseCreator {
    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1086, "database");
        createChild(schema);

        new DaoGenerator().generateAll(schema, "./app/src/main/java");
    }

    private static Entity createChild(Schema schema) {
        Entity child = schema.addEntity("Child");
        child.addIdProperty();
        child.addStringProperty("name").notNull();
        child.addStringProperty("surname").notNull();
        child.addStringProperty("font_size");
        child.addStringProperty("picture_size");
        child.addStringProperty("display_mode");

        return child;
    }
   //TODO: create other entities and relations
}


