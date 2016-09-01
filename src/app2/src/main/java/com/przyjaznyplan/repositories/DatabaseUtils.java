/*
 *
 *  * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 *  * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *  *
 *  * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 *
 */

package com.przyjaznyplan.repositories;

import android.database.sqlite.SQLiteDatabase;

import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.factories.FactoryDataBaseSQL;
import com.przyjaznyplan.factories.InsertSqlFactory;

public class DatabaseUtils {

    private static SQLiteDatabase databaseConnection = MySQLiteHelper.getDb();
    private static FactoryDataBaseSQL factoryDataBaseSQL = new FactoryDataBaseSQL();
    private static InsertSqlFactory insertSqlFactory = new InsertSqlFactory();

    public static void rebuildDatabaseWithInitData() {
        dropDatabase();
        createDatabase();
        insertInitData();
    }

    private static void dropDatabase() {
        factoryDataBaseSQL.DropDatabaseSql(databaseConnection);
    }

    private static void createDatabase() {
        factoryDataBaseSQL.CreateDatabaseSQL(databaseConnection);
    }

    private static void insertInitData() {
        insertSqlFactory.execInsert(databaseConnection);
    }



}
