/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplan.sqlCreate;

public final class Aktywnosc extends BaseSQL{

    public static final String TABLE_NAME="AKTYWNOSC";
    public static final String ID = "ID";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_ICON = "iconPath";
    public static final String COLUMN_AUDIO = "audioPath";
    public static final String COLUMN_TYPE_FLAG = "type_flag";
    public static final String COLUMN_DATE = "date";//timestamp
    public static final String COLUMN_ACTIVITY_NUMBER = "number";
    public static final String COLUMN_ACTIVITY_LAST_SLIDE_NUMBER = "last_slide_number";

    public static final String[] COLUMN_CHILD_ACTIVITY_ALL_COLUMNS = {ID, COLUMN_TITLE,
            COLUMN_STATUS, COLUMN_TIME, COLUMN_ICON, COLUMN_AUDIO, COLUMN_TYPE_FLAG, COLUMN_DATE, COLUMN_ACTIVITY_NUMBER, COLUMN_ACTIVITY_LAST_SLIDE_NUMBER};



    @Override
    public String createSql() {
        return createTable(TABLE_NAME)
                .addField(ID,TEXT_FIELD, PRIMARY_KEY)
                .addField(COLUMN_TITLE, TEXT_FIELD)
                .addField(COLUMN_STATUS,TEXT_FIELD)
                .addField(COLUMN_TIME,INTEGER_FIELD)
                .addField(COLUMN_ICON,TEXT_FIELD)
                .addField(COLUMN_DATE,TEXT_FIELD)
                .addField(COLUMN_ACTIVITY_NUMBER,INTEGER_FIELD)
                .addField(COLUMN_ACTIVITY_LAST_SLIDE_NUMBER,INTEGER_FIELD)
                .addField(COLUMN_AUDIO,TEXT_FIELD)
                .addField(COLUMN_TYPE_FLAG,TEXT_FIELD)
                .finishCreateSql();
    }


    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
