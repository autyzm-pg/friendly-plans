/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.factories;

import android.database.sqlite.SQLiteDatabase;

import com.przyjaznyplan.sqlCreate.AK_PL;
import com.przyjaznyplan.sqlCreate.Aktywnosc;
import com.przyjaznyplan.sqlCreate.CZ_AK;
import com.przyjaznyplan.sqlCreate.ChoosenUser;
import com.przyjaznyplan.sqlCreate.Czynnosc;
import com.przyjaznyplan.sqlCreate.Plan;
import com.przyjaznyplan.sqlCreate.Terminarz;
import com.przyjaznyplan.sqlCreate.UST_USER;
import com.przyjaznyplan.sqlCreate.UstawieniaUzytkownika;
import com.przyjaznyplan.sqlCreate.Uzytkownik;

public class FactoryDataBaseSQL {
    public FactoryDataBaseSQL(){

    }

    public void CreateDatabaseSQL(SQLiteDatabase db){

try {
    db.execSQL(new Czynnosc().createSql());
    db.execSQL(new Aktywnosc().createSql());
    db.execSQL(new CZ_AK().createSql());
    db.execSQL(new Uzytkownik().createSql());
    db.execSQL(new Plan().createSql());
    db.execSQL(new Terminarz().createSql());
    db.execSQL(new AK_PL().createSql());
    db.execSQL(new UstawieniaUzytkownika().createSql());
    db.execSQL(new UST_USER().createSql());
    db.execSQL(new ChoosenUser().createSql());
}catch (Exception e){
    System.out.println(e.getMessage());
}

    }

    public void DropDatabaseSql(SQLiteDatabase db){

        try {
            db.execSQL(new Czynnosc().dropSql());
            db.execSQL(new Aktywnosc().dropSql());
            db.execSQL(new CZ_AK().dropSql());
            db.execSQL(new Uzytkownik().dropSql());
            db.execSQL(new Plan().dropSql());
            db.execSQL(new Terminarz().dropSql());
            db.execSQL(new AK_PL().dropSql());
            db.execSQL(new UstawieniaUzytkownika().dropSql());
            db.execSQL(new UST_USER().dropSql());
            db.execSQL(new ChoosenUser().dropSql());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


}
