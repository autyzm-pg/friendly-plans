/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.sqlCreate;

public final class Plan extends BaseSQL{

    public static final String TABLE_NAME="PLAN";
    public static final String ID = "ID";
    public static final String NAZWA = "NAZWA";

    public static final String[] PLAN_ALL_COLUMNS = {ID, NAZWA};

    public String getTableName(){
        return TABLE_NAME;
    }

    @Override
    public String createSql() {

                return createTable(TABLE_NAME)
                        .addField(ID, TEXT_FIELD, PRIMARY_KEY)
                        .addField(NAZWA, TEXT_FIELD, NOT_NULL)
                        .finishCreateSql();
    }
}
