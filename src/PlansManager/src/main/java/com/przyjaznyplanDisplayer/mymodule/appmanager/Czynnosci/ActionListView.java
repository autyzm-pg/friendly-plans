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
import android.widget.Toast;

import com.przyjaznyplan.models.Slide;
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.SlidesAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActionListView extends Activity {
    private SlidesAdapter listAdapter ;
    private com.przyjaznyplan.models.Activity activity;
    private ListView mainListView;
    private MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_list);
        activity = (com.przyjaznyplan.models.Activity)getIntent().getExtras().get("ACTIVITY");
        mainListView = (ListView) findViewById(R.id.actionsListView);
        initList();
    }

    @Override
    protected void onResume(){
        super.onResume();
        initList();
    }

    public void initList(){
        activity = ActivityRepository.getActivityById(activity.getId());
        if(activity.getSlides() == null)
            activity.setSlides(new ArrayList<Slide>());
        listAdapter = new SlidesAdapter(this, R.layout.row_list_layout,R.id.label, activity.getSlides());
        mainListView.setAdapter(listAdapter);
    }


    public void addNewAction(View v) {
        Intent intent = new Intent(this, ActionAddEditView.class);
        intent.putExtra("ACTIVITY", activity);
        startActivityForResult(intent, RequestCodes.ACTION_ADD_NEW);
    }

    public void saveTemplate(View v) {
        ActivityRepository.updateWithActions(this.activity);
        Intent intent = new Intent();
        intent.putExtra("ACTIVITY", this.activity);
        setResult(RequestCodes.ACTIVITY_MANAGEMENT, intent);

        super.finish();
    }

    public void editAction(View v){
        int position = Integer.parseInt(v.getTag().toString());
        Intent intent = new Intent(this, ActionAddEditView.class);
        intent.putExtra("SLIDE", activity.getSlides().get(position));
        intent.putExtra("ACTIVITY", activity);
        startActivityForResult(intent, RequestCodes.ACTION_EDIT);
    }

    public void removeAction(View v){
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
                Toast.makeText(this, R.string.play_timer_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        initList();
    }
}
