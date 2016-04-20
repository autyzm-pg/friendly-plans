/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.mymodule.appmanager.Plany;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.dao.PlanDao;
import com.przyjaznyplan.dto.PlanDto;
import com.przyjaznyplan.models.Plan;
import com.przyjaznyplan.utils.BusinessLogic;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci.ActivityEditView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.ActivityAdapter;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

public class PlanEditView extends Activity  {
    private int mode;

    private Plan plan;
    private PlanDao planDao;
    private ActivityAdapter listAdapter;
    private ListView mainListView;
    private MediaPlayer mp;

    /**
     * Method run on creating view. Fills ListView with menu items and show it in the view
     *
     * @param savedInstanceState    android variable
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_edit_view);
        if(getIntent().getExtras()!=null && getIntent().getExtras().get("PLAN")!=null){
            mode = RequestCodes.PLAN_EDITED;
            this.plan = (Plan)getIntent().getExtras().get("PLAN");
            EditText etName = (EditText) findViewById(R.id.editText);
            etName.setText(this.plan.getTitle());
        }
        else {
            mode = RequestCodes.PLAN_ADD_NEW;
            this.plan = new Plan();
        }
        try {
            planDao = new PlanDao(MySQLiteHelper.getDb());
        }catch (Exception e){
            System.out.println(e.getMessage());
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
    }

    public void addActivityClick(View v) {
        Intent intent = new Intent(this, PlanAddActivityView.class);
        intent.putExtra("REQUESTCODE", RequestCodes.PLAN_ADD_ACTIVITY);
        startActivityForResult(intent,RequestCodes.PLAN_ADD_ACTIVITY);
    }

    public void editSlide(View v){
        Intent intent = new Intent(this, ActivityEditView.class);
        int position = Integer.parseInt(v.getTag().toString());
        com.przyjaznyplan.models.Activity aktywnosc = plan.getActivities().get(position);
        if(aktywnosc.getId().equals(BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID)){
            browseGalleryActivityClick(v);
        }else {
            intent.putExtra("ACTIVITY", aktywnosc);
            startActivityForResult(intent,RequestCodes.ACTIVITY_EDITED);
        }
    }

    public void removeSlide(View v){
        int position = Integer.parseInt(v.getTag().toString());
        com.przyjaznyplan.models.Activity aktywnosc = plan.getActivities().get(position);
        if(aktywnosc.getId().equals(BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID)){
            browseGalleryActivityClick(v);
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

    public void browseBreakActivityClick(View v) {
        Intent intent = new Intent(this, PlanBrowseActivity.class);
        intent.putExtra("PLAN", this.plan);
        intent.putExtra("REQUESTCODE", RequestCodes.BREAK_BROWSE);
        startActivityForResult(intent,RequestCodes.BREAK_BROWSE);
    }

    public void browseGalleryActivityClick(View v) {
        Intent intent = new Intent(this, PlanBrowseActivity.class);
        intent.putExtra("PLAN", this.plan);
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

    public void saveTemplate(View v){
        EditText etName = (EditText) findViewById(R.id.editText);
        String name = etName.getText().toString();
        if(name==null || name==""){
            Toast.makeText(this, R.string.set_up_template_name, Toast.LENGTH_LONG).show();
            return;
        }
        this.plan.setTitle(name);
        PlanDto pdto = new PlanDto();
        pdto.setPlan(this.plan);
        try{
            if(mode == RequestCodes.PLAN_ADD_NEW){
                planDao.create(pdto);
            } else {
                planDao.update((pdto));
            }
            super.finish();
        } catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
    }

    public void addFromPlanClick(View v) {
        Intent intent = new Intent(this, PlanFindView.class);
        intent.putExtra("REQUESTCODE",RequestCodes.ADD_FROM_PLAN_SEARCH);
        startActivityForResult(intent,RequestCodes.ADD_FROM_PLAN_SEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        com.przyjaznyplan.models.Activity aktywnosc = null;
        if(data!=null && data.getExtras()!=null && data.getExtras().get("ACTIVITY")!=null){
            aktywnosc = (com.przyjaznyplan.models.Activity)data.getExtras().get("ACTIVITY");
            if(requestCode == RequestCodes.PLAN_ADD_ACTIVITY && resultCode ==  RequestCodes.PLAN_NEW_ACTIVITY){
                Toast.makeText(this, R.string.activity_added, Toast.LENGTH_LONG).show();
                if(aktywnosc!=null){
                    this.plan.getActivities().add(aktywnosc);
                }
            }
            if(requestCode == RequestCodes.PLAN_ADD_ACTIVITY && resultCode ==  RequestCodes.PLAN_ACTIVITY){
                Toast.makeText(this, R.string.activity_added, Toast.LENGTH_LONG).show();
                if(aktywnosc!=null){
                    this.plan.getActivities().add(aktywnosc);
                }
            }
            if(requestCode == RequestCodes.PLAN_ADD_ACTIVITY && resultCode ==  RequestCodes.PLAN_BREAK){
                Toast.makeText(this, R.string.activity_break_added, Toast.LENGTH_LONG).show();
                if(aktywnosc!=null){
                    this.plan.getActivitiesBreak().add(aktywnosc);
                }
            }
            if(requestCode == RequestCodes.PLAN_ADD_ACTIVITY && resultCode ==  RequestCodes.PLAN_GALLERY){
                Toast.makeText(this, R.string.activity_gallery_added, Toast.LENGTH_LONG).show();
                if(aktywnosc!=null){
                    this.plan.getActivitiesGallery().add(aktywnosc);
                    com.przyjaznyplan.models.Activity gA = new com.przyjaznyplan.models.Activity();
                    gA.setId(BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID);
                    gA.setTitle(getResources().getString(R.string.activity_gallery));
                    gA.setTypeFlag(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString());
                    this.plan.getActivities().add(gA);
                }
            }
            if(requestCode == RequestCodes.ACTIVITY_EDITED && resultCode ==  RequestCodes.ACTIVITY_EDITED){
                refreshAllTheSameActivities(aktywnosc);
                Toast.makeText(this, R.string.activity_edited, Toast.LENGTH_LONG).show();
            }
        }
        if(data!=null && data.getExtras()!=null && data.getExtras().get("PLAN")!=null){
            if((requestCode == RequestCodes.BREAK_BROWSE && resultCode ==  RequestCodes.BREAK_BROWSE)
                ||(requestCode == RequestCodes.GA_BROWSE && resultCode ==  RequestCodes.GA_BROWSE)){
                this.plan = (Plan) data.getExtras().get("PLAN");
            }
        }
        if(data!=null && data.getExtras()!=null && data.getExtras().get("ACTIVITIES")!=null && requestCode == RequestCodes.ADD_FROM_PLAN_SEARCH){
            for(com.przyjaznyplan.models.Activity ac : (ArrayList<com.przyjaznyplan.models.Activity>)data.getExtras().get("ACTIVITIES")){
                this.plan.getActivities().add(ac);
            }
            for(com.przyjaznyplan.models.Activity ac : (ArrayList<com.przyjaznyplan.models.Activity>)data.getExtras().get("BREAKACTIVITIES")){
                this.plan.getActivitiesBreak().add(ac);
            }
            for(com.przyjaznyplan.models.Activity ac : (ArrayList<com.przyjaznyplan.models.Activity>)data.getExtras().get("GAACTIVITIES")){
                this.plan.getActivitiesGallery().add(ac);
                com.przyjaznyplan.models.Activity gA = new com.przyjaznyplan.models.Activity();
                gA.setId(BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID);
                gA.setTitle(getResources().getString(R.string.activity_gallery));
                gA.setTypeFlag(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString());
                this.plan.getActivities().add(gA);
            }
        }

    }
}
