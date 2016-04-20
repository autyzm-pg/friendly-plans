/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;


import com.przyjaznyplan.dao.ActivityDao;
import com.przyjaznyplan.dao.SlideDao;
import com.przyjaznyplan.dto.ActivityDto;
import com.przyjaznyplan.dto.SlideDto;
import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.Slide;
import com.przyjaznyplan.sqlCreate.Aktywnosc;
import com.przyjaznyplan.sqlCreate.CZ_AK;
import com.przyjaznyplan.sqlCreate.Czynnosc;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;


public final class CsvConverter {

    private ActivityDao activityDao ;
    private SlideDao slideDao;
    private CSVReader reader;
    ArrayList<String[]> lines;
    public final static String JPG_EXT=".jpg";
    public final static String JPEG_EXT=".jpeg";
    public final static String PNG_EXT=".png";
    public final static String MP3_EXT=".mp3";
    public final static String TXT_EXT=".txt";
    public final static char SEPARATOR = ',';
    private SQLiteDatabase database;
    private String rootFolder;

    public CsvConverter(SQLiteDatabase db){
        this.database=db;
        activityDao=new ActivityDao(db);
        slideDao=new SlideDao(db);
    }

    public void open(String path) throws FileNotFoundException {

        path = Environment.getExternalStorageDirectory().toString() + "/" + path;
        if(! new File(path).exists()){
            Log.i("NO FILE",path+" DOES NOT EXIST");
            throw new FileNotFoundException(" DOES NOT EXIST");
        }

        lines = new ArrayList<String[]>();
        try {
            reader = new CSVReader(new FileReader(path),SEPARATOR);
        } catch (FileNotFoundException e) {
            Log.e("CSV Reader EXCEPTION", e.getMessage());
        }
        String[] nextLine;

        try {
            while ((nextLine = reader.readNext()) != null) {

                lines.add(nextLine);


            }
        }catch(Exception e)
        {
            Log.e("CSV Reader EXCEPTION", e.getMessage());
        }

        rootFolder=new File(path).getParent();

    }

    public void CreateSlides(){

        ContentValues sqlValues = new ContentValues();
        sqlValues.put(Aktywnosc.COLUMN_TITLE,new File(this.rootFolder).getName());
        sqlValues.put(Aktywnosc.COLUMN_STATUS,0);
        sqlValues.put(Aktywnosc.COLUMN_TYPE_FLAG,""+ Activity.TypeFlag.ACTIVITY);

        Activity activity = new Activity();

        ActivityDto adto  = new ActivityDto();

        adto.setContentValues(sqlValues);

        adto.setActivity(activity);

        activityDao.create(adto);


        int i=0;
        for(String[] values : lines){
            CreateSlide(values, activity.getId(),++i);
        }
    }

    private void CreateSlide(String[] values, String idActivity,int pos) {

        ContentValues sqlValues = new ContentValues();

        sqlValues.put(Czynnosc.STATUS,0);

        for(String value : values){

           ifImageGetIt(value,sqlValues);
           ifAudioGetIt(value,sqlValues);
           ifTextGetIt(value,sqlValues);
        }

        SlideDto slideDto = new SlideDto();

        Slide slide = new Slide();
        slide.setPosition(pos);
        slide.setIdActivity(idActivity);
        slideDto.setContentValues(sqlValues);
        slideDto.setSlide(slide);


        slideDao.create(slideDto);


        if(slide.getId().equals("")){
            Log.e("SQL INSERT", "INSERT WASN EXECUTED");
        }

    }
    private void ifImageGetIt(String value, ContentValues sqlValues){
        if(value!=null && (value.endsWith(JPG_EXT) || value.endsWith(JPEG_EXT) || value.endsWith(PNG_EXT)))
        {

            Collection files = FileUtils.listFiles(new File(rootFolder), new NameFileFilter(value), TrueFileFilter.INSTANCE);
            Iterator<File> iterator = files.iterator();
            if(iterator.hasNext()){
                File f = iterator.next();
                sqlValues.put(Czynnosc.IMAGE,f.getAbsolutePath());
            }

        }
    }
    private void ifAudioGetIt(String value, ContentValues sqlValues){
        if(value!=null && value.endsWith(MP3_EXT))
        {
            Collection files = FileUtils.listFiles(new File(rootFolder), new NameFileFilter(value), TrueFileFilter.INSTANCE);
            Iterator<File> iterator = files.iterator();
            if(iterator.hasNext()){
                File f = iterator.next();
                sqlValues.put(Czynnosc.AUDIO,f.getAbsolutePath());
            }


        }
    }
    private void ifTextGetIt(String value, ContentValues sqlValues){
        if(value!=null && value.endsWith(TXT_EXT))
        {
            Collection files = FileUtils.listFiles(new File(rootFolder), new NameFileFilter(value), TrueFileFilter.INSTANCE);
            Iterator<File> iterator = files.iterator();
            if(iterator.hasNext()){
                File f = iterator.next();
                List<String> linesOfText=null;
                try {
                    linesOfText = FileUtils.readLines(f);
                } catch (IOException e) {
                    Log.e("File IO",e.getMessage());
                }
                StringBuilder builder = new StringBuilder();

                if(linesOfText!=null) {
                    for (String line : linesOfText) {
                        builder.append(line);
                    }
                }

                sqlValues.put(Czynnosc.TEXT, builder.toString());
            }


        }
        else if(value != null && value.length()>0)
        {
            sqlValues.put(Czynnosc.TEXT,value);
        }
    }
}
