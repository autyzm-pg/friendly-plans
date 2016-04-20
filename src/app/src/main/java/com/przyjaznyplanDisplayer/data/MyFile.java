/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.data;

import java.io.File;

public class MyFile extends File {

    private String dbname;

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public MyFile(String path, String DBNAME){
        super(path);
        dbname=new StringBuilder().append(path).append("/").append(DBNAME).toString();

    }



    public MyFile mkdirs2() {
        super.mkdirs();
        return this;
    }



}
