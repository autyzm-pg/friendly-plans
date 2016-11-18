/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.dto;

import android.content.ContentValues;

import com.przyjaznyplan.sqlCreate.Terminarz;

public class TerminarzDto extends BaseDto {

    public TerminarzDto(){

    }
    @Override
    public String getID() {
        return null;
    }

    @Override
    public String getCOLUMN_ID() {
        return Terminarz.ID;
    }

    //TODO
    @Override
    public String[] getColumnsToRead() {
        if(columnsToRead==null) return null;
        else return columnsToRead;
    }

    //TODO
    @Override
    public ContentValues getContentValues() {
        if(contentValues==null){
                return null;
        }else{
            return contentValues;
        }

    }

    @Override
    public void setColumnsToRead(String[] columns) {
        this.columnsToRead=columns;
    }

    @Override
    public void setContentValues(ContentValues values) {
        this.contentValues=values;
    }
}
