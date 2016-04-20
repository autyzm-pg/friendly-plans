/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.mymodule.appmanager.Czynnosci;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.przyjaznyplan.models.Slide;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.SlidesAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActionList extends Activity {
    private SlidesAdapter listAdapter ;
    private com.przyjaznyplan.models.Activity activity;
    private int activityMode;
    private ListView mainListView;
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_list);
        activity = (com.przyjaznyplan.models.Activity)getIntent().getExtras().get("ACTIVITY");
        mainListView = (ListView) findViewById(R.id.listView);
        if(this.activity.getSlides()==null){
            List<Slide> ls = new ArrayList<Slide>();
            this.activity.setSlides(ls);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        initList();
    }

    public void initList(){
        listAdapter = new SlidesAdapter(this, R.layout.row_list_layout,R.id.label, activity.getSlides());
        mainListView.setAdapter(listAdapter);
    }


    public void dodajNowaCzynnoscClick(View v) {
        Intent intent = new Intent(this, ActionAddEditView.class);
        startActivityForResult(intent, RequestCodes.ACTION_ADD_NEW);
    }

    public void zapisz(View v) {
        Intent intent = new Intent();
        intent.putExtra("ACTIVITY", this.activity);
        setResult(RequestCodes.ACTIVITY_MANAGEMENT, intent);
        super.finish();
    }

    public void edytujSlajd(View v){
        Intent intent = new Intent(this, ActionAddEditView.class);
        int position = Integer.parseInt(v.getTag().toString());
        intent.putExtra("SLIDE",activity.getSlides().get(position));
        intent.putExtra("POSITION",position);
        startActivityForResult(intent,RequestCodes.ACTION_EDIT);
    }

    public void usunSlajd(View v){
        int position = Integer.parseInt(v.getTag().toString());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==RequestCodes.ACTION_EDIT && resultCode == RequestCodes.SLIDE_EDITED){
            activity.getSlides().set(Integer.parseInt(data.getExtras().get("POSITION").toString()),(Slide)data.getExtras().get("SLIDE"));
        }
        if(requestCode==RequestCodes.ACTION_ADD_NEW && resultCode == RequestCodes.SLIDE_ADDED){
            activity.getSlides().add((Slide)data.getExtras().get("SLIDE"));
        }
        initList();
    }
}
