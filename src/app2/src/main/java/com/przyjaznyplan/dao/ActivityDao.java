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

import com.przyjaznyplan.DbHelper.PKGen;
import com.przyjaznyplan.dto.ActivityDto;
import com.przyjaznyplan.dto.SlideDto;
import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.Slide;
import com.przyjaznyplan.sqlCreate.AK_PL;
import com.przyjaznyplan.sqlCreate.Aktywnosc;
import com.przyjaznyplan.sqlCreate.CZ_AK;
import com.przyjaznyplan.sqlCreate.Czynnosc;
import com.przyjaznyplan.utils.BusinessLogic;

import java.util.ArrayList;
import java.util.List;

public class ActivityDao extends AbstractDao<ActivityDto> {

    public ActivityDao(SQLiteDatabase db){
        super(db);
    }

    @Override
    public void create(ActivityDto object) {
        try {
            object.createMode = true;
            String id = PKGen.GenPK();
            if(object.getContentValues().get(Aktywnosc.ID)==null) {

                object.getActivity().setId(id);
                object.getContentValues().put(Aktywnosc.ID, id);
            }
            long val=db.insert(object.getTable().getTableName(), null, object.getContentValues());

            this.addOrUpdateSlidesFromActivity(object.getActivity().getSlides(),object.getActivity().getId());
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    private void findAndRemove(List<Slide> oldList, String id){
        for(int i=0;i<oldList.size();i++){
            if(oldList.get(i).getId().equals(id)){
                oldList.remove(i);
            }
        }
    }

    public void addOrUpdateSlidesFromActivity(List<Slide> ls, String activityId){
        SlideDao sdao = new SlideDao(db);
        long newSlideId;
        if(ls == null){
            return;
        }
        List<Slide> valuesToDelete = sdao.getAllActivitySlides(activityId);
        for(int i=0;i<ls.size();i++){
            try{
                SlideDto sdto = new SlideDto();
                Slide s=ls.get(i);
                s.setIdActivity(activityId);
                s.setPosition(i);
                sdto.setSlide(s);
                if(s.getId() == null || s.getId().equals("")){
                    sdto.createMode = true;
                    sdao.create(sdto);
                } else {
                    findAndRemove(valuesToDelete,s.getId());
                    sdto.createMode =false;
                    sdao.update(sdto);
                }
            } catch(Exception e){

            }
        }
        for(int i=0;i<valuesToDelete.size();i++){
            sdao.deleteSlideAndCzAk(activityId, valuesToDelete.get(i).getId());
        }
    }

    @Override
    public ActivityDto read(ActivityDto object) {
        Cursor cursor = null;
        try {

            cursor = db.query(object.getTable().getTableName(), object.getColumnsToRead(), object.getSelectionString(), object.getSelectionArgs(), object.groupBy(), object.having(),object.orderBy());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        cursor.moveToFirst();
        Activity activity = cursorToActivity(cursor);

        object.setActivity(activity);
        return object;
    }

    @Override
    public void update(ActivityDto object) {
        try {
            db.update(object.getTable().getTableName(), object.getContentValues(), object.getCOLUMN_ID() + "='" + object.getID()+"'", null);
            this.addOrUpdateSlidesFromActivity(object.getActivity().getSlides(), object.getActivity().getId());
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(ActivityDto objBaseDto) {
        SlideDao sdao = new SlideDao(db);
        String activityId = objBaseDto.getActivity().getId();
        List<Slide> valuesToDelete = sdao.getAllActivitySlides(activityId +"");
        try {
            //delete all cz_ak
            db.delete(CZ_AK.TABLE_NAME,CZ_AK.ID_AK + "='"+ activityId+"'",null);
            //delete all slides
            for(Slide s : valuesToDelete){
                db.delete(Czynnosc.TABLE_NAME,Czynnosc.ID+ "='"+ s.getId()+"'",null);
            }
            //delete activity from plans
            deleteactivityFromPlan(activityId);
            //delete activity
            db.delete(objBaseDto.getTable().getTableName(), objBaseDto.getCOLUMN_ID() + "='" + objBaseDto.getID() + "'", null);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void deleteactivityFromPlan(String activityId){
        Cursor cursor = null, cursor2 = null;
        int howManyActivityGaleriesToDelete = 0;
        try {
            cursor=db.query(true,AK_PL.TABLE_NAME,
                    new String[]{AK_PL.ID_PL}, AK_PL.ID_AK + " = '" + activityId + "'", null, AK_PL.ID_PL , null, null, null);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex(AK_PL.ID_PL));
            howManyActivityGaleriesToDelete = 0;
            try {
                cursor2=db.query(AK_PL.TABLE_NAME,
                        AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_AK + " = '" + activityId + "')and("+ AK_PL.TYP_AK +"='" +Activity.TypeFlag.ACTIVITY_GALLERY+"')and("+AK_PL.ID_PL+"='"+ id +"')", null, null, null, AK_PL.TYP_AK);
                cursor2.moveToFirst();
                howManyActivityGaleriesToDelete+=cursor2.getCount();
                cursor2=db.query(AK_PL.TABLE_NAME,
                        AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_AK + " = '" + activityId + "')and("+ AK_PL.TYP_AK +"='" +Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY+"')and("+AK_PL.ID_PL+"='"+ id +"')", null, null, null, AK_PL.TYP_AK);
                cursor2.moveToFirst();
                howManyActivityGaleriesToDelete-=cursor2.getCount();
                db.delete(AK_PL.TABLE_NAME,"("+AK_PL.ID_AK + "='"+ activityId+"')and("+AK_PL.ID_PL + "='"+ id+"')",null);
                cursor2=db.query(AK_PL.TABLE_NAME,
                        AK_PL.ALL_COLUMNS,"("+ AK_PL.TYP_AK +"='" +Activity.TypeFlag.TEMP_ACTIVITY_GALLERY +"')and("+AK_PL.ID_PL+"='"+ id +"')", null, null, null, null);
                cursor2.moveToFirst();
                for(int i=0; i< howManyActivityGaleriesToDelete; i++){
                    String id2 = cursor2.getString(cursor2.getColumnIndex(AK_PL.ID));
                    db.delete(AK_PL.TABLE_NAME,AK_PL.ID+ "='"+ id2+"'",null);
                    cursor2.moveToNext();
                }
                cursor2.close();
            }catch (Exception e){
                System.out.println(e.getMessage());
            }
            cursor.moveToNext();
        }
        cursor.close();
    }

    public List<String> getPlanIdsOfSelectedActivity(String activityId){
        Cursor cursor = null;
        List<String> ids = new ArrayList<String>();
        try {
            cursor=db.query(AK_PL.TABLE_NAME,
                    AK_PL.ALL_COLUMNS, AK_PL.ID_AK + " = '" + activityId + "'", null, null, null, AK_PL.POZ_AK);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String id = cursor.getString(cursor.getColumnIndex(AK_PL.ID_PL));
            ids.add(id);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return ids;
    }

    public Activity getChosenChildActivity() {
        Cursor cursor = db.query(AK_PL.TABLE_NAME,
                AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + BusinessLogic.SYSTEM_CURRENT_PLAN_ID + "')and("+ AK_PL.STATUS_AK + " = '" + Activity.ActivityStatus.CHOSEN+ "')", null, null, null, AK_PL.POZ_AK);
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            Activity childActivity = getActivityById(cursor.getString(cursor.getColumnIndex(AK_PL.ID_AK)));
            setExternalAttributesFromPlan(cursor,childActivity);
            cursor.close();
            return childActivity;
        }
        else{
            return null;
        }
    }

    public List <Activity> getActivitiesByTitle (String title){
        List<Activity> activities = new ArrayList<Activity>();

        Cursor cursor = null;
        try {
            cursor=db.query(Aktywnosc.TABLE_NAME,
                    Aktywnosc.COLUMN_CHILD_ACTIVITY_ALL_COLUMNS, Aktywnosc.COLUMN_TITLE + " LIKE ? and " + Aktywnosc.ID +"!='"+BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID +"'", new String[] {"%"+ title+ "%" } ,null, null, Aktywnosc.COLUMN_TITLE + " ASC", null);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Activity childActivity = cursorToActivity(cursor);
            activities.add(childActivity);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return activities;
    }

    private Activity getActivityById(String activityId){
        Cursor cursor = null;
        try {
            cursor = db.query(Aktywnosc.TABLE_NAME, Aktywnosc.COLUMN_CHILD_ACTIVITY_ALL_COLUMNS,Aktywnosc.ID +" = '"+ activityId +"'", null, null, null, null);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        Activity activity = cursorToActivity(cursor);
        cursor.close();
        return activity;
    }

    public List<Activity> getActivitiesAndTempAGFromPlan(String planId){
        List<Activity> list = new ArrayList<Activity>();
        Cursor cursor = null;
        try {
            cursor=db.query(AK_PL.TABLE_NAME,
                    AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + planId + "')and("+ AK_PL.TYP_AK + " = '" + Activity.TypeFlag.ACTIVITY + "' or " + AK_PL.TYP_AK  + " = '" + Activity.TypeFlag.TEMP_ACTIVITY_GALLERY + "' or " + AK_PL.TYP_AK + " = '" + Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY + "')", null, null, null, AK_PL.POZ_AK);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Activity childActivity = getActivityById(cursor.getString(cursor.getColumnIndex(AK_PL.ID_AK)));
            setExternalAttributesFromPlan(cursor, childActivity);
            list.add(childActivity);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return list;
    }

    public List<Activity> getActivityGalleryFromPlan(String planId, Integer limit){
        List<Activity> list = new ArrayList<Activity>();
        Cursor cursor = null;
        try {
            if(limit==0){
                cursor=db.query(AK_PL.TABLE_NAME,
                        AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + planId + "')and("+ AK_PL.TYP_AK + " = '" + Activity.TypeFlag.ACTIVITY_GALLERY + "')", null, null, null, AK_PL.POZ_AK);
            } else {
                cursor=db.query(AK_PL.TABLE_NAME,
                        AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + planId + "')and("+ AK_PL.TYP_AK + " = '" + Activity.TypeFlag.ACTIVITY_GALLERY + "')and("+AK_PL.STATUS_AK+"!='"+Activity.ActivityStatus.FINISHED.toString()+"')", null, null, null, AK_PL.POZ_AK, limit.toString());
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Activity childActivity = getActivityById(cursor.getString(cursor.getColumnIndex(AK_PL.ID_AK)));
            setExternalAttributesFromPlan(cursor, childActivity);
            list.add(childActivity);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return list;
    }

    public List<Activity> getBreaksFromPlan(String planId, Integer limit){
        List<Activity> list = new ArrayList<Activity>();
        Cursor cursor = null;
        try {
            if(limit==0){
                cursor=db.query(AK_PL.TABLE_NAME,
                        AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + planId + "')and("+ AK_PL.TYP_AK + " = '" + Activity.TypeFlag.BREAK+ "')", null, null, null, AK_PL.POZ_AK);
            } else {
                cursor=db.query(AK_PL.TABLE_NAME,
                        AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + planId + "')and("+ AK_PL.TYP_AK + " = '" + Activity.TypeFlag.BREAK+ "')and("+AK_PL.STATUS_AK+"!='"+Activity.ActivityStatus.FINISHED.toString()+"')", null, null, null, AK_PL.POZ_AK, limit.toString());
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Activity childActivity = getActivityById(cursor.getString(cursor.getColumnIndex(AK_PL.ID_AK)));
            setExternalAttributesFromPlan(cursor, childActivity);
            list.add(childActivity);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return list;
    }

    public Activity getActivityGallery(){
        Activity aktywnosc;
        Cursor cursor = null;
        try {
            cursor=db.query(Aktywnosc.TABLE_NAME,
                    Aktywnosc.COLUMN_CHILD_ACTIVITY_ALL_COLUMNS, Aktywnosc.ID+ " = '" + BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID +"'" , null ,null, null, null, null);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        aktywnosc = cursorToActivity(cursor);
        cursor.close();
        return aktywnosc;
    }

    //cursor from AK_PL
    private Activity setExternalAttributesFromPlan(Cursor cursor, Activity childActivity){
        childActivity.setTypeFlag(cursor.getString(cursor.getColumnIndex(AK_PL.TYP_AK)));
        childActivity.setNumber(cursor.getInt(cursor.getColumnIndex(AK_PL.POZ_AK)));
        childActivity.setStatus(cursor.getString(cursor.getColumnIndex(AK_PL.STATUS_AK)));
        return childActivity;
    }

    public void setChosenStatusOnActivityGallery(Activity aktywnoscGA, String planId){
        Cursor cursor = null;
        try {
            cursor=db.query(AK_PL.TABLE_NAME,
                    AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + planId + "')and("+ AK_PL.POZ_AK + " = " + aktywnoscGA.getNumber() + ")", null, null, null, AK_PL.POZ_AK);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        ContentValues values = new ContentValues();
        values.put(AK_PL.STATUS_AK, Activity.ActivityStatus.CHOSEN.toString());
        String id = cursor.getString(cursor.getColumnIndex(AK_PL.ID));
        int val=db.update(AK_PL.TABLE_NAME, values, AK_PL.ID + "='" + id+"'", null);
        cursor.close();
    }

    public void changeGAToActivity(Activity aktywnoscGA, String planId, Activity aktywnosc){
        Cursor cursor = null;
        try {
            cursor=db.query(AK_PL.TABLE_NAME,
                    AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + planId + "')and("+ AK_PL.POZ_AK + " = " + aktywnoscGA.getNumber() + ")and("+ AK_PL.TYP_AK + " = '" + aktywnoscGA.getTypeFlag() + "')", null, null, null, AK_PL.POZ_AK);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        ContentValues values = new ContentValues();
        values.put(AK_PL.ID_AK, aktywnosc.getId());
        values.put(AK_PL.STATUS_AK, Activity.ActivityStatus.FINISHED.toString());
        values.put(AK_PL.TYP_AK, Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString());
        String id = cursor.getString(cursor.getColumnIndex(AK_PL.ID));
        int val=db.update(AK_PL.TABLE_NAME, values, AK_PL.ID + "='" + id+"'", null);
        /*
        TODO
        change also done in actywnosc
         */
        cursor.close();
    }

    public void setActivityGaleryAsUndone(String planId, int position, String activityId){
        Cursor cursor = null;
        try {
            cursor=db.query(AK_PL.TABLE_NAME,
                    AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + planId + "')and("+ AK_PL.POZ_AK + " = " + position + ")and("+ AK_PL.TYP_AK + " = '" + Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY + "')", null, null, null, AK_PL.POZ_AK);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        ContentValues values = new ContentValues();
        String id = cursor.getString(cursor.getColumnIndex(AK_PL.ID));
        values.put(AK_PL.ID_AK,BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID);
        values.put(AK_PL.ID_PL,cursor.getString(cursor.getColumnIndex(AK_PL.ID_PL)));
        values.put(AK_PL.TYP_AK,Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString());
        values.put(AK_PL.POZ_AK,cursor.getInt(cursor.getColumnIndex(AK_PL.POZ_AK)));
        values.put(AK_PL.STATUS_AK, Activity.ActivityStatus.NEW.toString());
        db.update(AK_PL.TABLE_NAME, values, AK_PL.ID + "='" + id+"'", null);

        try {
            cursor=db.query(AK_PL.TABLE_NAME,
                    AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + planId + "')and("+ AK_PL.STATUS_AK + " = '" + Activity.ActivityStatus.FINISHED.toString() + "')and("+ AK_PL.TYP_AK + " = '" + Activity.TypeFlag.ACTIVITY_GALLERY + "')and("+ AK_PL.ID_AK + " = '" + activityId + "')", null, null, null, AK_PL.POZ_AK);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        values = new ContentValues();
        values.put(AK_PL.STATUS_AK, Activity.ActivityStatus.NEW.toString());
        id = cursor.getString(cursor.getColumnIndex(AK_PL.ID));
        db.update(AK_PL.TABLE_NAME, values, AK_PL.ID + "='" + id+"'", null);
        cursor.close();
    }

    public void setActivityAsUndone(String planId, int position, String activityType){
        Cursor cursor = null;
        try {
            cursor=db.query(AK_PL.TABLE_NAME,
                    AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + planId + "')and("+ AK_PL.POZ_AK + " = " + position + ")and("+ AK_PL.TYP_AK + " = '" + activityType + "')", null, null, null, AK_PL.POZ_AK);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        ContentValues values = new ContentValues();
        String id = cursor.getString(cursor.getColumnIndex(AK_PL.ID));
        values.put(AK_PL.ID,id);
        values.put(AK_PL.ID_AK,cursor.getString(cursor.getColumnIndex(AK_PL.ID_AK)));
        values.put(AK_PL.ID_PL,cursor.getString(cursor.getColumnIndex(AK_PL.ID_PL)));
        values.put(AK_PL.TYP_AK,cursor.getString(cursor.getColumnIndex(AK_PL.TYP_AK)));
        values.put(AK_PL.POZ_AK,cursor.getInt(cursor.getColumnIndex(AK_PL.POZ_AK)));
        values.put(AK_PL.STATUS_AK, Activity.ActivityStatus.NEW.toString());
        db.update(AK_PL.TABLE_NAME, values, AK_PL.ID + "='" + id+"'", null);
        cursor.close();
    }

    public void setActivityAsDone(String planId, int position, String activityType){
        Cursor cursor = null;
        try {
            cursor=db.query(AK_PL.TABLE_NAME,
                    AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + planId + "')and("+ AK_PL.POZ_AK + " = " + position + ")and("+ AK_PL.TYP_AK + " = '" + activityType + "')", null, null, null, AK_PL.POZ_AK);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        cursor.moveToFirst();
        ContentValues values = new ContentValues();
        String id = cursor.getString(cursor.getColumnIndex(AK_PL.ID));
        values.put(AK_PL.STATUS_AK, Activity.ActivityStatus.FINISHED.toString());
        db.update(AK_PL.TABLE_NAME, values, AK_PL.ID + "='" + id+"'", null);
        cursor.close();
    }

    private Activity cursorToActivity(Cursor cursor) {
        Activity childActivity = new Activity();
        //if you want to change some column, please add the line
        childActivity.setId(cursor.getString(cursor.getColumnIndex(Aktywnosc.ID)));
        childActivity.setTitle(cursor.getString(cursor.getColumnIndex(Aktywnosc.COLUMN_TITLE)));
        childActivity.setSlides(new SlideDao(db).getAllActivitySlides(childActivity.getId()+""));
        childActivity.setLastSlideNumber(cursor.getInt(cursor.getColumnIndex(Aktywnosc.COLUMN_ACTIVITY_LAST_SLIDE_NUMBER)));
        childActivity.setAudioPath(cursor.getString(cursor.getColumnIndex(Aktywnosc.COLUMN_AUDIO)));
        childActivity.setIconPath(cursor.getString(cursor.getColumnIndex(Aktywnosc.COLUMN_ICON)));
        childActivity.setTime(cursor.getInt(cursor.getColumnIndex(Aktywnosc.COLUMN_TIME)));
        return childActivity;
    }

    public void changeStatusOfChosenActivity() {
        Cursor cursor = db.query(AK_PL.TABLE_NAME,
                AK_PL.ALL_COLUMNS,"(" + AK_PL.ID_PL + " = '" + BusinessLogic.SYSTEM_CURRENT_PLAN_ID + "')and("+ AK_PL.STATUS_AK + " = '" + Activity.ActivityStatus.CHOSEN+ "')", null, null, null, AK_PL.POZ_AK);
        if(cursor.getCount() == 1) {
            cursor.moveToFirst();
            ContentValues values = new ContentValues();
            String id = cursor.getString(cursor.getColumnIndex(AK_PL.ID));
            values.put(AK_PL.STATUS_AK, Activity.ActivityStatus.NEW.toString());
            db.update(AK_PL.TABLE_NAME, values, AK_PL.ID + "='" + id+"'", null);
            cursor.close();
        }
    }
}
