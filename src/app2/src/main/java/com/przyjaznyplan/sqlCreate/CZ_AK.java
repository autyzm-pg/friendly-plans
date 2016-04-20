/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.sqlCreate;

public final class CZ_AK extends BaseSQL{

    public static final String TABLE_NAME="CZ_AK";
    public static final String ID = "ID";
    public static final String ID_CZ = "ID_CZ";
    public static final String ID_AK = "ID_AK";
    public static final String POZ = "POZ";

    public static final String[] ALL_COLUMNS = {ID,ID_CZ,ID_AK,POZ};

    @Override
    public String createSql() {
        return createTable(TABLE_NAME)
                .addField(ID,TEXT_FIELD,PRIMARY_KEY)
                .addField(ID_CZ,TEXT_FIELD,REFERENCES(Czynnosc.TABLE_NAME,Czynnosc.ID))
                .addField(ID_AK,TEXT_FIELD,REFERENCES(Aktywnosc.TABLE_NAME,Aktywnosc.ID))
                .addField(POZ,INTEGER_FIELD)
                .finishCreateSql();
    }


    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
