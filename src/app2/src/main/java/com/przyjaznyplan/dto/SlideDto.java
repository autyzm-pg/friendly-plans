/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplan.dto;

import android.content.ContentValues;

import com.przyjaznyplan.sqlCreate.Czynnosc;
import com.przyjaznyplan.models.Slide;

public class SlideDto extends  BaseDto {

    public Slide getSlide() {
        return slide;
    }

    public void setSlide(Slide slide) {
        this.slide = slide;
    }

    private Slide slide;

    public boolean createMode = false;

    public SlideDto(){
        super(new Czynnosc());
    }

    @Override
    public String getID() {
        return slide.getId();
    }

    @Override
    public String getCOLUMN_ID() {
        return Czynnosc.ID;
    }

    @Override
    public String[] getColumnsToRead() {
        if(columnsToRead==null){
            return Czynnosc.COLUMN_SLIDE_ALL_COLUMNS;
        }else{
            return columnsToRead;
        }
    }

    /**
     * Domysle contentValues zwraca odwzorowanie z modelu
     * @return
     */
    @Override
    public ContentValues getContentValues() {
        if(contentValues==null){
            contentValues = new ContentValues();
            if(createMode==false){
                contentValues.put(Czynnosc.ID,getID());
            }
            contentValues.put(Czynnosc.TEXT,slide.getText());
            contentValues.put(Czynnosc.AUDIO,slide.getAudioPath());
            contentValues.put(Czynnosc.IMAGE,slide.getImagePath());
            contentValues.put(Czynnosc.STATUS,slide.getStatus());
            contentValues.put(Czynnosc.TIMER,slide.getTime());
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
