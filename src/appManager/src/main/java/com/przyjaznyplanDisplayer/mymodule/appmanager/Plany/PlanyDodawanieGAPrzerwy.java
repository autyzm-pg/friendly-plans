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
import android.widget.Toast;

import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.ActivitySimpleAdapter;

import java.util.ArrayList;

public class PlanyDodawanieGAPrzerwy extends Activity implements AdapterView.OnItemClickListener {
    private ArrayList<com.przyjaznyplan.models.Activity> wybrane;
    private ActivitySimpleAdapter listAdapter;
    private ListView mainListView;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plandodajwybranegaprzerwyview);
        try {
            this.wybrane = (ArrayList<com.przyjaznyplan.models.Activity>)getIntent().getExtras().get("ACTIVITIES");
            mainListView = (ListView) findViewById(R.id.listView);
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
        listAdapter = new ActivitySimpleAdapter(this, R.layout.row_list_layout_activity_simple,R.id.label, this.wybrane);
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
            aktywnosc.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString());
            listAdapter.notifyDataSetChanged();
        } else if (aktywnosc.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
            aktywnosc.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
            listAdapter.notifyDataSetChanged();
        }
    }

    public void zapisz (View v){
        Intent intent = new Intent();
        intent.putExtra("ACTIVITIES", wybrane);
        setResult(1, intent);
        super.finish();
    }
}
