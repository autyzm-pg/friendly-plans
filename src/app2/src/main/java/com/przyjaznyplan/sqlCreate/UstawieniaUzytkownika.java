/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.sqlCreate;

public class UstawieniaUzytkownika extends BaseSQL{

    public static final String TABLE_NAME = "USTAWIENIA_UZYTKOWNIKA";
    public static final String ID = "ID";
    public static final String TYP_WIDOK_AKTYWNOSCI = "A_VIEW_TYPE";
    public static final String TYP_WIDOK_CZYNNOSCI = "C_VIEW_TYPE";
    public static final String TYP_WIDOK_PLAN = "P_VIEW_TYPE";
    public static final String TIMER_SOUND_PATH = "TIMER_SOUND_PATH";

    public static final String[] ALL_COLUMNS={ID,TYP_WIDOK_AKTYWNOSCI,TYP_WIDOK_CZYNNOSCI,TYP_WIDOK_PLAN, TIMER_SOUND_PATH};

    @Override
    public String createSql() {
        return createTable(TABLE_NAME)
                .addField(ID,TEXT_FIELD,PRIMARY_KEY)
                .addField(TYP_WIDOK_AKTYWNOSCI,TEXT_FIELD)
                .addField(TYP_WIDOK_CZYNNOSCI, TEXT_FIELD)
                .addField(TYP_WIDOK_PLAN, TEXT_FIELD)
                .addField(TIMER_SOUND_PATH,TEXT_FIELD)
                .finishCreateSql();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

}