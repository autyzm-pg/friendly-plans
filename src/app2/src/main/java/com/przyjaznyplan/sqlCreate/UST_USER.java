/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.sqlCreate;

public class UST_USER extends BaseSQL{

    public static final String TABLE_NAME = "UST_USER";
    public static final String ID = "ID";
    public static final String ID_USER = "ID_USER";
    public static final String ID_USER_PREFERENCES = "ID_USTAWIENIA";


    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String createSql() {
        return createTable(TABLE_NAME)
                .addField(ID_USER,TEXT_FIELD,REFERENCES(Uzytkownik.TABLE_NAME,Uzytkownik.ID))
                .addField(ID_USER_PREFERENCES,TEXT_FIELD,REFERENCES(UstawieniaUzytkownika.TABLE_NAME,UstawieniaUzytkownika.ID))
                .finishCreateSql();
    }
}
