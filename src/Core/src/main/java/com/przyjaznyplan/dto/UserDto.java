/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.dto;

import android.content.ContentValues;

import com.przyjaznyplan.models.User;
import com.przyjaznyplan.models.UserPreferences;
import com.przyjaznyplan.sqlCreate.Uzytkownik;

public class UserDto extends BaseDto {

    private User user;
    private UserPreferences preferences;

    public UserDto(){super(new Uzytkownik());}

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getID() {
        return user.getId();
    }

    @Override
    public String getCOLUMN_ID() {
        return Uzytkownik.ID;
    }

    public void setColumnsToRead(String[] columnsToRead) {
        this.columnsToRead = columnsToRead;
    }

    public void setContentValues(ContentValues contentValues) {
        this.contentValues = contentValues;
    }

    @Override
    public String[] getColumnsToRead() {
        if(columnsToRead==null){
            return Uzytkownik.ALL_COLUMNS;
        }
        return columnsToRead;
    }

    /**
     * Domysle zwraca contentValues calego modelu
     * @return
     */
    public ContentValues getContentValues(){
        if(contentValues==null) {
            contentValues = new ContentValues();
            contentValues.put(Uzytkownik.ID, user.getId());
            contentValues.put(Uzytkownik.NAME, user.getName());
            contentValues.put(Uzytkownik.SURNAME, user.getSurname());
        }
        return contentValues;
    }




}
