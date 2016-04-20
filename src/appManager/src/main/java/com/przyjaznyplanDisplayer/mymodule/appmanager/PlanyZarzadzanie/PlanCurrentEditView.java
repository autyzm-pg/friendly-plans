/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.mymodule.appmanager.PlanyZarzadzanie;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.dao.PlanDao;
import com.przyjaznyplan.dto.PlanDto;
import com.przyjaznyplan.models.Plan;
import com.przyjaznyplan.utils.BusinessLogic;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci.ActivityEditView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Plany.PlanAddActivityView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Plany.PlanBrowseActivity;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Plany.PlanFindView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.ActivityAdapter;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

public class PlanCurrentEditView extends Activity implements AdapterView.OnItemClickListener {
    private Plan plan;
    private PlanDao planDao;
    private ActivityAdapter listAdapter;
    private ListView mainListView;
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_current_edit);
        try {
            planDao = new PlanDao(MySQLiteHelper.getDb());
            this.plan = planDao.getAktualnyPlan(BusinessLogic.SYSTEM_CURRENT_PLAN_ID);
        }catch (Exception e){
            System.out.println(e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            super.finish();
        }
        mainListView = (ListView) findViewById(R.id.listView);
        if(this.plan.getActivities()==null){
            this.plan.setActivities(new ArrayList<com.przyjaznyplan.models.Activity>());
        }
        if(this.plan.getActivitiesGallery()==null){
            this.plan.setActivitiesGallery(new ArrayList<com.przyjaznyplan.models.Activity>());
        }
        if(this.plan.getActivitiesBreak()==null){
            this.plan.setActivitiesBreak(new ArrayList<com.przyjaznyplan.models.Activity>());
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        initList();
    }

    public void initList(){
        listAdapter = new ActivityAdapter(this, R.layout.row_list_layout,R.id.label, this.plan.getActivities());
        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemClickListener(this);
    }

    public void addActivityClick(View v) {
        Intent intent = new Intent(this, PlanAddActivityView.class);
        intent.putExtra("REQUESTCODE", RequestCodes.PLAN_ADD_ACTIVITY);
        startActivityForResult(intent,RequestCodes.PLAN_ADD_ACTIVITY);
    }

    public void slideEdit(View v){
        Intent intent = new Intent(this, ActivityEditView.class);
        int position = Integer.parseInt(v.getTag().toString());
        com.przyjaznyplan.models.Activity activity = plan.getActivities().get(position);
        if(activity.getId().equals(BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID)|| (activity.getTypeFlag()!=null && activity.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString()))){
            galleryActivityBrowseClick(v);
        }else {
            intent.putExtra("ACTIVITY", activity);
            startActivityForResult(intent,RequestCodes.ACTIVITY_EDITED);
        }
    }

    public void slideRemove(View v){
        int position = Integer.parseInt(v.getTag().toString());
        com.przyjaznyplan.models.Activity activity = plan.getActivities().get(position);
        if(activity.getId().equals(BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID) || (activity.getTypeFlag()!=null && activity.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString())) ){
            galleryActivityBrowseClick(v);
        }else {
            listAdapter.remove(position);
        }
    }

    public void playSound(View v){
        String audioPath=(String)v.getTag();
        if(audioPath!=null && !audioPath.equals("")){
            try {
                if(this.mp==null||!this.mp.isPlaying()) {
                    this.mp = MediaPlayer.create(this, Uri.parse(audioPath));
                    mp.start();
                } else {
                    if(this.mp!=null&&this.mp.isPlaying()){
                        mp.stop();
                    }
                }
            }catch(Exception e){

            }
        }
    }

    public void breakActivityBrowseClick(View v) {
        Intent intent = new Intent(this, PlanBrowseActivity.class);
        intent.putExtra("PLAN", this.plan);
        intent.putExtra("EDIT_EXECUTION", true);
        intent.putExtra("REQUESTCODE", RequestCodes.BREAK_BROWSE);
        startActivityForResult(intent,RequestCodes.BREAK_BROWSE);
    }

    public void galleryActivityBrowseClick(View v) {
        Intent intent = new Intent(this, PlanBrowseActivity.class);
        intent.putExtra("PLAN", this.plan);
        intent.putExtra("EDIT_EXECUTION", true);
        intent.putExtra("REQUESTCODE", RequestCodes.GA_BROWSE);
        startActivityForResult(intent,RequestCodes.GA_BROWSE);
    }

    private void refreshAllTheSameActivities(com.przyjaznyplan.models.Activity editedActivity){
        for(int i = 0;i<this.plan.getActivities().size();i++){
            if(this.plan.getActivities().get(i).getId().equals(editedActivity.getId())){
                this.plan.getActivities().set(i,editedActivity);
            }
        }
        for(int i = 0;i<this.plan.getActivitiesGallery().size();i++){
            if(this.plan.getActivitiesGallery().get(i).getId().equals(editedActivity.getId())){
                this.plan.getActivitiesGallery().set(i,editedActivity);
            }
        }
        for(int i = 0;i<this.plan.getActivitiesBreak().size();i++){
            if(this.plan.getActivitiesBreak().get(i).getId().equals(editedActivity.getId())){
                this.plan.getActivitiesBreak().set(i,editedActivity);
            }
        }
    }

    public void activityAddFromTemplateClick(View v){
        Intent intent = new Intent(this, PlanFindView.class);
        intent.putExtra("REQUESTCODE",RequestCodes.ACTIVITY_ADD_FROM_PATTERN);
        startActivityForResult(intent,RequestCodes.ACTIVITY_ADD_FROM_PATTERN);
    }

    public void planClearClick(View v){
        this.plan.setActivities(new ArrayList<com.przyjaznyplan.models.Activity>());
        this.plan.setActivitiesGallery(new ArrayList<com.przyjaznyplan.models.Activity>());
        this.plan.setActivitiesBreak(new ArrayList<com.przyjaznyplan.models.Activity>());
        initList();
    }

    private void activityAddFromTemplate(Plan selectedPlan){
        for(int i = 0;i<selectedPlan.getActivities().size();i++){
            this.plan.getActivities().add(selectedPlan.getActivities().get(i));
        }
        for(int i = 0;i<selectedPlan.getActivitiesGallery().size();i++){
            this.plan.getActivitiesGallery().add(selectedPlan.getActivitiesGallery().get(i));
        }
        for(int i = 0;i<selectedPlan.getActivitiesBreak().size();i++){
            this.plan.getActivitiesBreak().add(selectedPlan.getActivitiesBreak().get(i));
        }
        String message = getResources().getString(R.string.activities_added)+":\n";
        message+=selectedPlan.getActivities().size() + " "+getResources().getString(R.string.activities)+"\n";
        message+=selectedPlan.getActivitiesBreak().size() + " "+getResources().getString(R.string.activities_break)+"\n";
        message+=selectedPlan.getActivitiesGallery().size() + " "+getResources().getString(R.string.activities_gallery);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void save(View v){
        PlanDto pdto = new PlanDto();
        pdto.setPlan(this.plan);
        try{
            planDao.update((pdto));
            super.finish();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void cleanFullExecution(View v){
        com.przyjaznyplan.models.Activity activity;
        for(int i = 0;i<this.plan.getActivities().size();i++){
            activity = this.plan.getActivities().get(i);
            if(activity.getStatus()!=null && activity.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
                if(activity.getTypeFlag()!=null && activity.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString())){
                    activity = new com.przyjaznyplan.models.Activity();
                    activity.setId(BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID);
                    activity.setTitle(getResources().getString(R.string.activity_gallery));
                    activity.setTypeFlag(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString());
                    this.plan.getActivities().set(i,activity);
                } else {
                    activity.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
                }
            }
        }
        for(int i = 0;i<this.plan.getActivitiesGallery().size();i++){
            activity = this.plan.getActivitiesGallery().get(i);
            if(activity.getStatus()!=null && activity.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
                activity.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
            }
        }
        for(int i = 0;i<this.plan.getActivitiesBreak().size();i++){
            activity = this.plan.getActivitiesBreak().get(i);
            if(activity.getStatus()!=null && activity.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
                activity.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
            }
        }
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        com.przyjaznyplan.models.Activity activity = null;
        if(data!=null && data.getExtras()!=null && data.getExtras().get("ACTIVITY")!=null){
            activity = (com.przyjaznyplan.models.Activity)data.getExtras().get("ACTIVITY");
            if(requestCode == RequestCodes.PLAN_ADD_ACTIVITY && resultCode ==  RequestCodes.PLAN_NEW_ACTIVITY){
                Toast.makeText(this, R.string.activity_added, Toast.LENGTH_LONG).show();
                if(activity!=null){
                    this.plan.getActivities().add(activity);
                }
            }
            if(requestCode == RequestCodes.PLAN_ADD_ACTIVITY && resultCode ==  RequestCodes.PLAN_ACTIVITY){
                Toast.makeText(this, R.string.activity_added, Toast.LENGTH_LONG).show();
                if(activity!=null){
                    this.plan.getActivities().add(activity);
                }
            }
            if(requestCode == RequestCodes.PLAN_ADD_ACTIVITY && resultCode ==  RequestCodes.PLAN_BREAK){
                Toast.makeText(this, R.string.activity_break_added, Toast.LENGTH_LONG).show();
                if(activity!=null){
                    this.plan.getActivitiesBreak().add(activity);
                }
            }
            if(requestCode == RequestCodes.PLAN_ADD_ACTIVITY && resultCode ==  RequestCodes.PLAN_GALLERY){
                Toast.makeText(this, R.string.activity_gallery_added, Toast.LENGTH_LONG).show();
                if(activity!=null){
                    this.plan.getActivitiesGallery().add(activity);
                    com.przyjaznyplan.models.Activity gA = new com.przyjaznyplan.models.Activity();
                    gA.setId(BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID);
                    gA.setTitle(getResources().getString(R.string.activity_gallery));
                    gA.setTypeFlag(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString());
                    this.plan.getActivities().add(gA);
                }
            }
            if(requestCode == RequestCodes.ACTIVITY_EDITED && resultCode ==  RequestCodes.ACTIVITY_EDITED){
                refreshAllTheSameActivities(activity);
                Toast.makeText(this, R.string.activity_edited, Toast.LENGTH_LONG).show();
            }
        }
        if(data!=null && data.getExtras()!=null && data.getExtras().get("PLAN")!=null){
            Plan p = (Plan) data.getExtras().get("PLAN");
            if((requestCode == RequestCodes.BREAK_BROWSE && resultCode ==  RequestCodes.BREAK_BROWSE)
                    ||(requestCode == RequestCodes.GA_BROWSE && resultCode ==  RequestCodes.GA_BROWSE)){
                this.plan = p;
            }
            if(requestCode == RequestCodes.ACTIVITY_ADD_FROM_PATTERN && resultCode ==  RequestCodes.ACTIVITY_ADD_FROM_PATTERN){
                activityAddFromTemplate(p);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        com.przyjaznyplan.models.Activity activity = (com.przyjaznyplan.models.Activity) mainListView.getAdapter().getItem(i);
        if(activity.getStatus()!=null && activity.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
            if(activity.getTypeFlag()!=null && activity.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString())){
                activity = new com.przyjaznyplan.models.Activity();
                activity.setId(BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID);
                activity.setTitle(getResources().getString(R.string.activity_gallery));
                activity.setTypeFlag(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString());
                com.przyjaznyplan.models.Activity aktywnosc2 = (com.przyjaznyplan.models.Activity) mainListView.getAdapter().getItem(i);
                for(int j = 0;j<this.plan.getActivitiesGallery().size();j++) {
                    if(this.plan.getActivitiesGallery().get(j).getId().equals(aktywnosc2.getId()) && aktywnosc2.getStatus()!= null && aktywnosc2.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
                        aktywnosc2.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
                        this.plan.getActivitiesGallery().set(j,aktywnosc2);
                        break;
                    }
                }
                this.plan.getActivities().set(i,activity);
            } else {
                activity.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
            }
        } else {
            if(activity.getStatus()==null || !activity.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())) {
                if (activity.getTypeFlag() != null && activity.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString())) {
                    galleryActivityBrowseClick(view);
                } else {
                    activity.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString());
                }
            }
        }
        listAdapter.notifyDataSetChanged();
    }
}
