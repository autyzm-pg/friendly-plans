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

import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.DbHelper.PKGen;
import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.Plan;
import com.przyjaznyplan.dto.PlanDto;
import com.przyjaznyplan.sqlCreate.AK_PL;
import com.przyjaznyplan.utils.BusinessLogic;

import java.util.ArrayList;
import java.util.List;

public class PlanDao extends AbstractDao<PlanDto> {


    public PlanDao(SQLiteDatabase db){
        super(db);
    }

    private ContentValues createAKPLObject(String id_ak, String id_pl, String flag, int pos, String status){
        ContentValues values = new ContentValues();
        String id = PKGen.GenPK();
        values.put(AK_PL.ID,id);
        values.put(AK_PL.ID_AK,id_ak);
        values.put(AK_PL.ID_PL,id_pl);
        values.put(AK_PL.TYP_AK,flag);
        values.put(AK_PL.POZ_AK,pos);
        if(status == null){
            status = Activity.ActivityStatus.NEW.toString();
        }
        values.put(AK_PL.STATUS_AK, status);
        return values;
    }

    private void addOrUpdateActivitiesFromPlan(String planId, List<Activity> aktywnosci, List<Activity> aktywnosciGA , List<Activity> aktywnosciPrzerwy){
        String flag = null;
        for(int i=0; i<aktywnosci.size(); i++){
            if(aktywnosci.get(i).getTypeFlag()!= null && aktywnosci.get(i).getTypeFlag().equals(Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString())){
                flag = Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString();
            } else {
                if(aktywnosci.get(i).getTypeFlag()!= null && aktywnosci.get(i).getTypeFlag().equals(Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString())){
                    flag = Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString();
                } else {
                    flag = Activity.TypeFlag.ACTIVITY.toString();
                }
            }
            db.insert(AK_PL.TABLE_NAME, null, createAKPLObject(aktywnosci.get(i).getId(),planId,flag,i,aktywnosci.get(i).getStatus()));
        }
        for(int i=0; i<aktywnosciGA.size(); i++){
            db.insert(AK_PL.TABLE_NAME, null, createAKPLObject(aktywnosciGA.get(i).getId(),planId,Activity.TypeFlag.ACTIVITY_GALLERY.toString(),i,aktywnosciGA.get(i).getStatus()));
        }
        for(int i=0; i<aktywnosciPrzerwy.size(); i++){
            db.insert(AK_PL.TABLE_NAME, null, createAKPLObject(aktywnosciPrzerwy.get(i).getId(),planId,Activity.TypeFlag.BREAK.toString(),i,aktywnosciPrzerwy.get(i).getStatus()));
        }
    }

    public List<Plan> getPlanByTitle(String title){
        List<Plan> plany = new ArrayList<Plan>();

        Cursor cursor = null;
        try {
            cursor=db.query(com.przyjaznyplan.sqlCreate.Plan.TABLE_NAME,
              com.przyjaznyplan.sqlCreate.Plan.PLAN_ALL_COLUMNS, "(" + com.przyjaznyplan.sqlCreate.Plan.NAZWA+ " LIKE ?)and(" + com.przyjaznyplan.sqlCreate.Plan.ID +"!='"+ BusinessLogic.SYSTEM_CURRENT_PLAN_ID +"')", new String[] {"%"+ title+ "%" } ,null, null, com.przyjaznyplan.sqlCreate.Plan.NAZWA + " ASC", null);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Plan plan = cursorToPlan(cursor);
            plany.add(plan);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return plany;
    }

    @Override
    public void create(PlanDto object) {
        try {
            object.createMode = true;
            String id = PKGen.GenPK();
            if(object.getContentValues().get(com.przyjaznyplan.sqlCreate.Plan.ID)==null){
                object.getContentValues().put(com.przyjaznyplan.sqlCreate.Plan.ID,id);
                object.getPlan().setId(id);
            }
            long val=db.insert(object.getTable().getTableName(), null, object.getContentValues());

            this.addOrUpdateActivitiesFromPlan(id, object.getPlan().getActivities(), object.getPlan().getActivitiesGallery(), object.getPlan().getActivitiesBreak());

        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

    }

    @Override
    public PlanDto read(PlanDto object) {

        Cursor cursor = null;
        try {
            cursor = db.query(object.getTable().getTableName(), object.getColumnsToRead(), object.getSelectionString(), object.getSelectionArgs(), object.groupBy(), object.having(),object.orderBy());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        Plan plan = new Plan();
        cursor.moveToFirst();
        //TODO
        PlanDto planDto = new PlanDto();
        planDto.setPlan(plan);
        cursor.close();
        return null;
    }

    @Override
    public void update(PlanDto object) {
        try {
            db.update(object.getTable().getTableName(), object.getContentValues(), object.getCOLUMN_ID() + "='" + object.getID()+"'", null);
            db.delete(AK_PL.TABLE_NAME, AK_PL.ID_PL + " = '" + object.getPlan().getId()+"'" , null);
            this.addOrUpdateActivitiesFromPlan(object.getPlan().getId(), object.getPlan().getActivities(), object.getPlan().getActivitiesGallery(), object.getPlan().getActivitiesBreak());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(PlanDto objBaseDto) {
        try {
            db.delete(AK_PL.TABLE_NAME, AK_PL.ID_PL + " = '" + objBaseDto.getPlan().getId()+"'" , null);
            db.delete(objBaseDto.getTable().getTableName(), objBaseDto.getCOLUMN_ID() + "='" + objBaseDto.getID()+"'" , null);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Plan getAktualnyPlan(String systemAktualnyPlanId) {
        Cursor cursor = null;
        try {
            cursor=db.query(com.przyjaznyplan.sqlCreate.Plan.TABLE_NAME,
               com.przyjaznyplan.sqlCreate.Plan.PLAN_ALL_COLUMNS, com.przyjaznyplan.sqlCreate.Plan.ID+ " = '" + systemAktualnyPlanId+"'" , null ,null, null, null, null);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        Plan plan = cursorToPlan(cursor);
        cursor.close();
        return plan;
    }

    private Plan cursorToPlan(Cursor cursor){
        ActivityDao adao = new ActivityDao(MySQLiteHelper.getDb());
        Plan p = new Plan();
        String planId = cursor.getString(cursor.getColumnIndex(com.przyjaznyplan.sqlCreate.Plan.ID));
        p.setId(planId);
        p.setTitle(cursor.getString(cursor.getColumnIndex(com.przyjaznyplan.sqlCreate.Plan.NAZWA)));
        p.setActivities(adao.getActivitiesAndTempAGFromPlan(planId));
        p.setActivitiesBreak(adao.getBreaksFromPlan(planId, 0));
        p.setActivitiesGallery(adao.getActivityGalleryFromPlan(planId, 0));
        return p;
    }

}
