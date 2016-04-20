/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.dto;

import android.content.ContentValues;

import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.sqlCreate.Aktywnosc;

public class ActivityDto extends BaseDto {

    private Activity activity;

    public boolean createMode = false;

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public ActivityDto(){
        super(new Aktywnosc());
    }

    @Override
    public String getID() {
        return ""+activity.getId();
    }

    @Override
    public String getCOLUMN_ID() {
        return Aktywnosc.ID;
    }

    @Override
    public String[] getColumnsToRead() {
        if(columnsToRead==null) return Aktywnosc.COLUMN_CHILD_ACTIVITY_ALL_COLUMNS;
        else return columnsToRead;
    }

    /**
     * Domyslniee contentValues odwzorowuje wszystkie pola na bazie utworzonego modelu
     * @return
     */
    @Override
    public ContentValues getContentValues() {
        if(contentValues==null){

            contentValues=new ContentValues();
            if(this.createMode == false)
                contentValues.put(Aktywnosc.ID,this.getID());
            contentValues.put(Aktywnosc.COLUMN_AUDIO,activity.getAudioPath());
            contentValues.put(Aktywnosc.COLUMN_TITLE,activity.getTitle());
            contentValues.put(Aktywnosc.COLUMN_TIME,activity.getTime());
            contentValues.put(Aktywnosc.COLUMN_ACTIVITY_LAST_SLIDE_NUMBER,activity.getLastSlideNumber());
            contentValues.put(Aktywnosc.COLUMN_ICON,activity.getIconPath());
            contentValues.put(Aktywnosc.COLUMN_STATUS,activity.getStatus());
            contentValues.put(Aktywnosc.COLUMN_TYPE_FLAG,activity.getTypeFlag());
            contentValues.put(Aktywnosc.COLUMN_ACTIVITY_NUMBER,activity.getNumber());
            contentValues.put(Aktywnosc.COLUMN_DATE,activity.getDate());

            return contentValues;
        }
        return contentValues;
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
