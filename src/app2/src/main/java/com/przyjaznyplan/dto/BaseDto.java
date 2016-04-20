/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.dto;

import android.content.ContentValues;

import com.przyjaznyplan.sqlCreate.BaseSQL;

public abstract class BaseDto {


    protected BaseSQL table;
    protected String[] columnsToRead;
    protected ContentValues contentValues;
    public BaseDto(){}

    public BaseDto(BaseSQL baseSQL){

        table=baseSQL;
    }

    public abstract String getID();
    public abstract  String getCOLUMN_ID();


    public BaseSQL getTable() {
        return table;
    }

    public void setTable(BaseSQL table) {
        this.table = table;
    }

    public abstract String[] getColumnsToRead();
    public abstract ContentValues getContentValues();
    public abstract void setColumnsToRead(String[] columns);
    public abstract void setContentValues(ContentValues values);

    public  String getSelectionString(){
        return getCOLUMN_ID()+"='"+getID()+"'";
    };

    public  String[] getSelectionArgs(){
        return null;
    }

    public  String groupBy(){
        return null;
    }

    public String having(){
        return null;
    }

    public String orderBy(){
        return null;
    }
}
