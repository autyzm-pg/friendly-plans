/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.dao.ActivityDao;
import com.przyjaznyplan.dto.ActivityDto;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.ActivitySimpleAdapter;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.List;

public class ActivityFindView extends Activity implements AdapterView.OnItemClickListener {
    private ActivitySimpleAdapter listAdapter;
    private ListView mainListView;
    private MediaPlayer mp;
    private List<com.przyjaznyplan.models.Activity> list;
    private ActivityDao activityDao;
    private Integer mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_view);
        try {
            mainListView = (ListView) findViewById(R.id.listView);
            activityDao = new ActivityDao(MySQLiteHelper.getDb());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        list = activityDao.getActivitiesByTitle("");

        listAdapter = new ActivitySimpleAdapter(this, R.layout.row_list_layout_activity_simple,R.id.label, list);

        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        mode = (Integer)getIntent().getExtras().get("REQUESTCODE");
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    public void find(View v){
        String name="";
        EditText etName = (EditText) findViewById(R.id.editText);
        try {
            name = etName.getText().toString();
        } catch (Exception e){
        }

        Toast.makeText(v.getContext(), getResources().getString(R.string.searching_for)+": " + name, Toast.LENGTH_LONG).show();
        list = activityDao.getActivitiesByTitle(name);
        listAdapter = new ActivitySimpleAdapter(this, R.layout.row_list_layout_activity_simple,R.id.label, list);
        mainListView.setAdapter(listAdapter);
        mainListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


    private class DialogInterfaceOnClickHelper implements DialogInterface.OnClickListener{
        private com.przyjaznyplan.models.Activity activityToRemove;
        private Activity actvity;
        private List<String> ids;

        public void setActivityToRemoveId(com.przyjaznyplan.models.Activity act, Activity actvity){
            this.activityToRemove = act;
            this.actvity = actvity;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    ActivityDto adto = new ActivityDto();
                    adto.setActivity(activityToRemove);
                    activityDao.delete(adto);
                    this.actvity.finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        com.przyjaznyplan.models.Activity item = (com.przyjaznyplan.models.Activity) mainListView.getAdapter().getItem(i);
        if(mode==RequestCodes.ACTIVITY_REMOVE){
            List <String> ids = activityDao.getPlanIdsOfSelectedActivity(item.getId());
            DialogInterfaceOnClickHelper dialogClickListener = new DialogInterfaceOnClickHelper();
            dialogClickListener.setActivityToRemoveId(item, this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.sure_to_remove)+"\""+ item.getTitle() +"\"? "+getResources().getString(R.string.activity_is_used) +" "+ ids.size() +" "+getResources().getString(R.string.activity_context_remove)+"\n\n\n")
                    .setPositiveButton(R.string.yes, dialogClickListener)
                    .setNegativeButton(R.string.no, dialogClickListener)
                    .show();
        } else{
            Intent intent = new Intent(this, ActivityEditView.class);
            intent.putExtra("ACTIVITY",item);
            if(mode==RequestCodes.ACTIVITY_EDIT){
                startActivityForResult(intent, RequestCodes.ACTIVITY_EDITED);
            }
            if(mode==RequestCodes.PLAN_BREAK || mode==RequestCodes.PLAN_ACTIVITY || mode==RequestCodes.PLAN_GALLERY){
                setResult(mode, intent);
                super.finish();
            }
        }
    }

    public void playSound(View v) {
        String audioPath=(String)v.getTag();
        if(audioPath!=null && !audioPath.equals("")){
            try {
                if(this.mp==null||!this.mp.isPlaying()) {
                    this.mp = MediaPlayer.create(this, Uri.parse(audioPath));
                    mp.start();
                }
            }catch(Exception e){

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.finish();
    }
}