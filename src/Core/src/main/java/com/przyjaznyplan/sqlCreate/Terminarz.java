/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.sqlCreate;

public final class Terminarz extends BaseSQL{

    public static final String TABLE_NAME="TERMINARZ";
    public static final String ID="ID";
    public static final String ID_PL="ID_PL";
    public static final String ID_USR="ID_USR";
    public static final String DATE="DATA";

    @Override
    public String createSql() {
        return createTable(TABLE_NAME)
                .addField(ID,TEXT_FIELD,PRIMARY_KEY)
                .addField(ID_PL,TEXT_FIELD,REFERENCES(Plan.TABLE_NAME,Plan.ID))
                .addField(ID_USR, TEXT_FIELD, REFERENCES(Uzytkownik.TABLE_NAME, Uzytkownik.ID))
                .addField(DATE, DATE_FIELD, NOT_NULL).finishCreateSql();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
