/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplan.sqlCreate;

public final class AK_PL extends BaseSQL {

    public static final String TABLE_NAME="AK_PL";
    public static final String ID = "ID";
    public static final String ID_PL = "ID_PL";
    public static final String ID_AK = "ID_AK";
    public static final String POZ_AK = "POZ_AK";
    public static final String TYP_AK = "TYP_AK";
    public static final String STATUS_AK = "STATUS_AK";


    public AK_PL(){
        super();
    }

    public static final String[] ALL_COLUMNS = {ID,ID_PL,ID_AK,POZ_AK,TYP_AK, STATUS_AK};

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String createSql() {

        return createTable(TABLE_NAME)
                .addField(ID, TEXT_FIELD, PRIMARY_KEY)
                .addField(ID_PL, TEXT_FIELD, REFERENCES(Plan.TABLE_NAME, Plan.ID))
                .addField(ID_AK, TEXT_FIELD, REFERENCES(Aktywnosc.TABLE_NAME, Aktywnosc.ID))
                .addField(POZ_AK, INTEGER_FIELD, NOT_NULL)
                .addField(TYP_AK, TEXT_FIELD)
                .addField(STATUS_AK, TEXT_FIELD)
                .finishCreateSql();
    }



}
