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
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.ActivitySimpleAdapter;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

public class PlanyDodajWybraneView extends Activity implements AdapterView.OnItemClickListener {

    private Plan plan;
    private ArrayList<com.przyjaznyplan.models.Activity> wybrane;
    private ArrayList<com.przyjaznyplan.models.Activity> wybranePrzerwy;
    private ArrayList<com.przyjaznyplan.models.Activity> wybraneGA;
    private ActivitySimpleAdapter listAdapter;
    private ListView mainListView;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plandodajwybraneview);
        try {
            this.plan = (Plan)getIntent().getExtras().get("PLAN");
            this.wybrane = new ArrayList<com.przyjaznyplan.models.Activity>();
            this.wybranePrzerwy = new ArrayList<com.przyjaznyplan.models.Activity>();
            this.wybraneGA = new ArrayList<com.przyjaznyplan.models.Activity>();
            if(this.plan.getActivities()==null){
                this.plan.setActivities(new ArrayList<com.przyjaznyplan.models.Activity>());
            }
            mainListView = (ListView) findViewById(R.id.listView);
            TextView tv =(TextView) findViewById(R.id.nazwaPlanu);
            if( tv!= null){
                tv.setText(this.plan.getTitle());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            super.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initList();
    }

    public void initList(){
        listAdapter = new ActivitySimpleAdapter(this, R.layout.row_list_layout_activity_simple,R.id.label, this.plan.getActivities());
        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        com.przyjaznyplan.models.Activity aktywnosc = (com.przyjaznyplan.models.Activity) mainListView.getAdapter().getItem(i);
        if(aktywnosc.getStatus()==null || !aktywnosc.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())) {
            if (aktywnosc.getTypeFlag() != null && aktywnosc.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString())) {
            } else {
                aktywnosc.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString());
                wybrane.add(aktywnosc);
                listAdapter.notifyDataSetChanged();
            }
        } else if (aktywnosc.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
            wybrane.remove(aktywnosc);
            aktywnosc.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
            listAdapter.notifyDataSetChanged();
        }
    }

    public void wybierzGA (View v){
        Intent intent = new Intent(this, PlanyDodawanieGAPrzerwy.class);
        intent.putExtra("ACTIVITIES", (ArrayList)this.plan.getActivitiesGallery());
        startActivityForResult(intent,RequestCodes.ADD_FROM_PLAN_GA);
    }

    public void wybierzPrzerwy (View v){
        Intent intent = new Intent(this, PlanyDodawanieGAPrzerwy.class);
        intent.putExtra("ACTIVITIES", (ArrayList)this.plan.getActivitiesBreak());
        startActivityForResult(intent,RequestCodes.ADD_FROM_PLAN_BREAK);
    }

    public void zapisz (View v){
        Intent intent = new Intent();
        for(com.przyjaznyplan.models.Activity ac : wybrane){
            ac.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
        }
        intent.putExtra("ACTIVITIES", wybrane);
        for(com.przyjaznyplan.models.Activity ac : this.plan.getActivitiesGallery()){
            if(ac.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
                ac.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
                wybraneGA.add(ac);
            }
        }
        for(com.przyjaznyplan.models.Activity ac : this.plan.getActivitiesBreak()){
            if(ac.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
                ac.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
                wybranePrzerwy.add(ac);
            }
        }
        intent.putExtra("BREAKACTIVITIES", wybranePrzerwy);
        intent.putExtra("GAACTIVITIES", wybraneGA);
        setResult(1, intent);
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null && data.getExtras().get("ACTIVITIES") != null){
            if(requestCode == RequestCodes.ADD_FROM_PLAN_GA) {
                this.plan.getActivitiesGallery().clear();
                this.plan.getActivitiesGallery().addAll((ArrayList<com.przyjaznyplan.models.Activity>)data.getExtras().get("ACTIVITIES"));
            } else if (requestCode == RequestCodes.ADD_FROM_PLAN_BREAK){
                this.plan.getActivitiesBreak().clear();
                this.plan.getActivitiesBreak().addAll((ArrayList<com.przyjaznyplan.models.Activity>)data.getExtras().get("ACTIVITIES"));
            }
        }
    }
}
