/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.Utils;

import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;


//TODO poprawic malo pilne
public class ImageStructureConverter extends StructureConverter {

    public final static String JPG_EXT=".jpg";
    public final static String JPEG_EXT=".jpeg";
    public final static String PNG_EXT=".png";



    @Override
    public void open(String root) {
        super.open(root);
        for(File file : files){
            String path = file.getAbsolutePath();
            if(!path.endsWith(JPG_EXT) && !path.endsWith(JPEG_EXT) && !path.endsWith(PNG_EXT))
            {
                files.remove(file);
            }
        }
        SortFilesByNames();
    }

    private void SortFilesByNames(){
        Collections.sort(files,new Comparator<File>() {
            @Override
            public int compare(File file, File file2) {
                return file.getName().compareTo(file2.getName());
            }
        });
    }

    //TODO poprawic
    public void ConvertAndLoad(SQLiteDatabase database){
    /*
        if(!folderExist) return;
        //ACTIVITY INSERT
        ContentValues values = new ContentValues();
        values.put("title",this.rootFolderName);
        values.put("status",0);
        values.put("type_flag",""+MySQLiteHelper.TypeFlag.ACTIVITY);
        long idActivity  = database.insert(MySQLiteHelper.TABLE_CHILD_ACTIVITES,null, values);


        for(File f:files){
            //SLIDES INSERT
            values = new ContentValues();
            values.put(MySQLiteHelper.COLUMN_SLIDE_STATUS,0);
            values.put(MySQLiteHelper.COLUMN_SLIDE_IMAGE_PATH,f.getAbsolutePath());
            values.put(MySQLiteHelper.COLUMN_SLIDE_REF_TO_ACTIVITY,idActivity);
            try {
                long result = database.insert(MySQLiteHelper.TABLE_SLIDES, null, values);
                result = result;
            }catch(Exception e)
            {
                Log.e("SQL EX", e.getMessage());
            }

        }*/
    }

}
