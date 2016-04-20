/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplan.dto;

import android.content.ContentValues;

import com.przyjaznyplan.models.Plan;

public class PlanDto extends BaseDto {

    public PlanDto(){
        super(new com.przyjaznyplan.sqlCreate.Plan());
    }

    private Plan plan;

    public boolean createMode = false;

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    @Override
    public String getID() {
        return this.plan.getId()+"";
    }

    @Override
    public String getCOLUMN_ID() {
        return com.przyjaznyplan.sqlCreate.Plan.ID;
    }

    //TODO
    @Override
    public String[] getColumnsToRead() {
        if(columnsToRead==null){
            return com.przyjaznyplan.sqlCreate.Plan.PLAN_ALL_COLUMNS;
        }
        return columnsToRead;
    }

    //TODO
    @Override
    public ContentValues getContentValues() {
        if(contentValues==null){
            contentValues=new ContentValues();
            if(this.createMode == false)
                contentValues.put(com.przyjaznyplan.sqlCreate.Plan.ID,this.plan.getId());
            contentValues.put(com.przyjaznyplan.sqlCreate.Plan.NAZWA,this.plan.getTitle());
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
