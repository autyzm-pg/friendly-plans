/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.sqlCreate;

public final class Czynnosc extends BaseSQL{

    public static final String TABLE_NAME="CZYNNOSC";
    public static final String ID = "ID";
    public static final String TEXT = "TEKST";
    public static final String IMAGE = "OBRAZEK";
    public static final String AUDIO = "AUDIO";
    public static final String TIMER = "TIMER";
    public static final String STATUS ="STATUS";

    public static final String[] COLUMN_SLIDE_ALL_COLUMNS = {ID, TEXT, AUDIO, TIMER, IMAGE, STATUS};




    @Override
    public String createSql() {
        return createTable(TABLE_NAME)
                .addField(ID,TEXT_FIELD,PRIMARY_KEY)
                .addField(TEXT,TEXT_FIELD)
                .addField(AUDIO,TEXT_FIELD)
                .addField(TIMER,INTEGER_FIELD)
                .addField(IMAGE,TEXT_FIELD)
                .addField(STATUS,INTEGER_FIELD)
                .finishCreateSql();

    }


    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
