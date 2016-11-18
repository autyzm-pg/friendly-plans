/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplan.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.przyjaznyplan.DbHelper.PKGen;
import com.przyjaznyplan.dto.ActivityDto;
import com.przyjaznyplan.dto.SlideDto;
import com.przyjaznyplan.models.Slide;
import com.przyjaznyplan.sqlCreate.CZ_AK;
import com.przyjaznyplan.sqlCreate.Czynnosc;

import java.util.ArrayList;
import java.util.List;

public class SlideDao extends AbstractDao<SlideDto> {

    public SlideDao(SQLiteDatabase db){
        super(db);
    }

    @Override
    public void create(SlideDto object) {
        try {
            //insert do CZYNNOSC
            String id = PKGen.GenPK();

            if(object.getContentValues().get(Czynnosc.ID)==null){
                object.getContentValues().put(Czynnosc.ID,id);
                object.getSlide().setId(id);
            }
            long val=db.insert(object.getTable().getTableName(), null, object.getContentValues());

            String id2 = PKGen.GenPK();
            ContentValues values = new ContentValues();
            values.put(CZ_AK.ID_AK,object.getSlide().getIdActivity());
            values.put(CZ_AK.ID_CZ,object.getSlide().getId());
            values.put(CZ_AK.POZ,object.getSlide().getPosition());

            values.put(CZ_AK.ID,id2);
            //INSERT DO CZ_AK
            long id_cz_ak=db.insert(CZ_AK.TABLE_NAME, null, values);
            SlideDto s= this.read(object);


        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Override
    public SlideDto read(SlideDto object) {

        Cursor cursor = null;
        try {

            cursor = db.query(object.getTable().getTableName(), object.getColumnsToRead(), object.getSelectionString(), object.getSelectionArgs(), object.groupBy(), object.having(),object.orderBy());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        cursor.moveToFirst();
        if(cursor.isAfterLast()){
            object.setSlide(null);
            return object;
        }
        Slide slide = object.getSlide();
        if(slide==null) slide = new Slide();

        slide.setId(cursor.getString(cursor.getColumnIndex(Czynnosc.ID)));
        slide.setAudioPath(cursor.getString(cursor.getColumnIndex(Czynnosc.AUDIO)));
        slide.setImagePath(cursor.getString(cursor.getColumnIndex(Czynnosc.IMAGE)));
        slide.setText(cursor.getString(cursor.getColumnIndex(Czynnosc.TEXT)));
        slide.setTime(cursor.getInt(cursor.getColumnIndex(Czynnosc.TIMER)));
        slide.setStatus(cursor.getInt(cursor.getColumnIndex(Czynnosc.STATUS)));


        return object;

    }
    @Override
    public void delete(SlideDto objBaseDto) {
        try {
            db.delete(objBaseDto.getTable().getTableName(), objBaseDto.getCOLUMN_ID() + "='" + objBaseDto.getID()+"'", null);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteSlideAndCzAk(String id_cz_ak, String cz_id){
        Cursor cursor = null;
        long id;
        cursor = db.query(CZ_AK.TABLE_NAME,
                CZ_AK.ALL_COLUMNS, CZ_AK.ID_AK + " = '" + id_cz_ak + "' AND '" + CZ_AK.ID_CZ + "' = '" + cz_id+"'", null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            id = cursor.getInt(cursor.getColumnIndex(CZ_AK.ID));
            cursor.moveToNext();
            db.delete(CZ_AK.TABLE_NAME, CZ_AK.ID + "='" + id+"'", null);
        }
        cursor.close();
        db.delete(Czynnosc.TABLE_NAME, Czynnosc.ID + "='" + cz_id+"'", null);
    }

    @Override
    public void update(SlideDto object) {
        Cursor cursor = null;
        ContentValues values = null;
        String id;
        try {
            ContentValues c2 = object.getContentValues();
            int v=db.update(Czynnosc.TABLE_NAME, c2 , Czynnosc.ID+ "='" + object.getID()+"'", null);

            cursor = db.query(CZ_AK.TABLE_NAME,
                    CZ_AK.ALL_COLUMNS, CZ_AK.ID_AK + " = '" + object.getSlide().getIdActivity() + "' AND " + CZ_AK.ID_CZ + " = '" + object.getSlide().getId()+"'", null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                values = new ContentValues();
                values.put(CZ_AK.POZ, object.getSlide().getPosition());
                id = cursor.getString(cursor.getColumnIndex(CZ_AK.ID));
                db.update(CZ_AK.TABLE_NAME, values, CZ_AK.ID + "='" + id + "'", null);
                cursor.moveToNext();
            }
            cursor.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public List<Slide> getAllActivitySlides(String activityId){
        List<Slide> slides = new ArrayList<Slide>();
        Cursor cursor = null;
        try {
            cursor = db.query(CZ_AK.TABLE_NAME,
                    CZ_AK.ALL_COLUMNS, CZ_AK.ID_AK + " = '" + activityId+"'", null, null, null, CZ_AK.POZ + " ASC");
        } catch (Exception e) {
            Log.i("SQLEX", e.getMessage());
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            SlideDto slideDto = new SlideDto();
            Slide slide = new Slide();
            slide.setIdActivity(activityId);
            slide.setId(cursor.getString(cursor.getColumnIndex(CZ_AK.ID_CZ)));
            slide.setPosition(cursor.getInt(cursor.getColumnIndex(CZ_AK.POZ)));
            slideDto.setSlide(slide);
            slideDto = read(slideDto);
            if(slideDto.getSlide()!=null)
                slides.add(slideDto.getSlide());
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return slides;
    }


}
