/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplan.dao;

import android.database.sqlite.SQLiteDatabase;

import com.przyjaznyplan.dto.TerminarzDto;

public class TerminarzDao extends AbstractDao<TerminarzDto> {

    public TerminarzDao(SQLiteDatabase db){
        super(db);
    }

    @Override
    public void create(TerminarzDto object) {
        //TODO
    }

    @Override
    public TerminarzDto read(TerminarzDto object) {
        return null;
    }

    @Override
    public void update(TerminarzDto object) {
        try {
            db.update(object.getTable().getTableName(), object.getContentValues(), object.getCOLUMN_ID() + "=" + object.getID(), null);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(TerminarzDto objBaseDto) {
        try {
            db.delete(objBaseDto.getTable().getTableName(), objBaseDto.getCOLUMN_ID() + "=" + objBaseDto.getID(), null);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
