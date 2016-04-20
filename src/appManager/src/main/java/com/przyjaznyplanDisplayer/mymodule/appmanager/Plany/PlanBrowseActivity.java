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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.przyjaznyplan.models.Plan;
import com.przyjaznyplan.utils.BusinessLogic;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci.ActivityEditView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.ActivityAdapter;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.List;

public class PlanBrowseActivity extends Activity implements AdapterView.OnItemClickListener {

    private ActivityAdapter listAdapter;
    private ListView mainListView;
    private MediaPlayer mp;
    private List<com.przyjaznyplan.models.Activity> aList;
    private Integer mode;
    private Boolean changeStatusEnabled = false;
    private Plan plan;
    private int editedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.planprzegladanieaktywnoosci);

        mode = (Integer) getIntent().getExtras().get("REQUESTCODE");
        plan = (Plan) getIntent().getExtras().get("PLAN");
        if(getIntent().getExtras().get("EDIT_EXECUTION")!=null){
            changeStatusEnabled = true;
        }
        TextView text = (TextView) (findViewById(R.id.textView));
        if(mode == RequestCodes.BREAK_BROWSE){
            text.setText(R.string.break_activities);
            this.aList = plan.getActivitiesBreak();
        } else {
            text.setText(R.string.gallery_activities);
            this.aList = plan.getActivitiesGallery();
        }
        mainListView = (ListView) findViewById(R.id.listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
    }

    public void initList(){
        listAdapter = new ActivityAdapter(this, R.layout.row_list_layout,R.id.label, aList);
        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemClickListener(this);
    }

    public void edytujSlajd(View v){
        Intent intent = new Intent(this, ActivityEditView.class);
        int position = Integer.parseInt(v.getTag().toString());
        editedPosition = position;
        com.przyjaznyplan.models.Activity aktywnosc = aList.get(position);
        intent.putExtra("ACTIVITY", aktywnosc);
        startActivityForResult(intent,RequestCodes.ACTIVITY_EDITED);
    }

    public void usunSlajd(View v){
        int position = Integer.parseInt(v.getTag().toString());
        com.przyjaznyplan.models.Activity aktywnosc = aList.get(position);
        boolean option = aktywnosc.getStatus()!=null && aktywnosc.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString());
        if(mode == RequestCodes.GA_BROWSE){
            for(com.przyjaznyplan.models.Activity act:plan.getActivities()){
                if(option){
                    if(act.getId().equals(aktywnosc.getId())&& act.getTypeFlag()!=null && act.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString())){
                        plan.getActivities().remove(act);
                        break;
                    }
                } else {
                    if(act.getId().equals( BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID)){
                        plan.getActivities().remove(act);
                        break;
                    }
                }
            }
        }
        listAdapter.remove(position);
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
    @Override
    public void finish(){
        if(mode == RequestCodes.BREAK_BROWSE){
           plan.setActivitiesBreak(this.aList);
        } else {
           plan.setActivitiesGallery(this.aList);
        }
        Intent intent = new Intent();
        intent.putExtra("PLAN", this.plan);
        setResult(mode , intent);
        super.finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null && data.getExtras()!=null && data.getExtras().get("ACTIVITY")!=null) {
            if(requestCode == RequestCodes.ACTIVITY_EDITED && resultCode ==  RequestCodes.ACTIVITY_EDITED){
                com.przyjaznyplan.models.Activity editedActivity = (com.przyjaznyplan.models.Activity)data.getExtras().get("ACTIVITY");
                refreshAllTheSameActivities(editedActivity);
                Toast.makeText(this, R.string.activity_edited, Toast.LENGTH_LONG).show();
            }
        }

        editedPosition = 0;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(changeStatusEnabled ==null || changeStatusEnabled == false){
            return;
        }
        com.przyjaznyplan.models.Activity aktywnosc = (com.przyjaznyplan.models.Activity) mainListView.getAdapter().getItem(i);
        if(aktywnosc.getStatus()!=null && aktywnosc.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())) {
            aktywnosc.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
            if(mode == RequestCodes.GA_BROWSE){
                for(int j = 0;i<this.plan.getActivities().size();j++){
                    if(this.plan.getActivities().get(j).getId().equals(aktywnosc.getId()) && this.plan.getActivities().get(j).getTypeFlag()!= null && this.plan.getActivities().get(j).getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString())){
                        aktywnosc = new com.przyjaznyplan.models.Activity();
                        aktywnosc.setId(BusinessLogic.SYSTEM_ACTIVITY_GALLERY_ID);
                        aktywnosc.setTitle(getResources().getString(R.string.activity_gallery));
                        aktywnosc.setTypeFlag(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString());
                        this.plan.getActivities().set(j,aktywnosc);
                        break;
                    }
                }
            }
        } else {
            if(aktywnosc.getStatus()== null || !aktywnosc.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
                aktywnosc.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString());
                if(mode == RequestCodes.GA_BROWSE){
                    for(int j = 0;i<this.plan.getActivities().size();j++){
                        if(this.plan.getActivities().get(j).getTypeFlag()!= null && this.plan.getActivities().get(j).getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString())){
                            this.plan.getActivities().set(j, aktywnosc);
                            this.plan.getActivities().get(j).setTypeFlag(com.przyjaznyplan.models.Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString());
                            break;
                        }
                    }
                }
            }
        }
        listAdapter.notifyDataSetChanged();

    }
}
