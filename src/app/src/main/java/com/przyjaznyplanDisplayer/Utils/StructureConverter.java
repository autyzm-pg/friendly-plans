/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.Utils;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;


public abstract class StructureConverter {

    public final static  String SD_CARD_ROOT =  Environment.getExternalStorageDirectory().toString();
    protected  String rootFolder;
    protected  String rootFolderName;
    protected  boolean folderExist;

    protected ArrayList<File> files;

    public void open(String root){
        files = new ArrayList<File>();
        rootFolder = SD_CARD_ROOT+"/"+root;

        File directory = new File(rootFolder);
        folderExist = directory.exists();
        if(!folderExist) return;
        rootFolderName = directory.getName();
        File[] fList = directory.listFiles();

        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                open(file.getAbsolutePath());
            }
        }
    }
}
