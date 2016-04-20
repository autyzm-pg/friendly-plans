/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.mymodule.appmanager.Plany;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci.ActivityEditView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci.ActivityFindView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

public class PlanAddActivityView extends Activity {
    private Integer mode;
    ArrayAdapter<String> adapter;
    ListView activityListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_add_activity);
        mode = (Integer)getIntent().getExtras().get("REQUESTCODE");
        activityListView = (ListView) findViewById(R.id.activityListView);
        ArrayList<String> activityListArray = new ArrayList<String>();
        activityListArray.add(getResources().getString(R.string.add_activity));
        activityListArray.add(getResources().getString(R.string.add_existing_activity));
        activityListArray.add(getResources().getString(R.string.add_break_activity));
        activityListArray.add(getResources().getString(R.string.add_gallery_activity));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activityListArray);
        activityListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if(position == 0) addActivity();
                else if(position == 1) addExistingActivity();
                else if(position == 2) addBreakActivity();
                else if(position == 3) addGalleryActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void addActivity() {
        Intent intent = new Intent(this, ActivityEditView.class);
        intent.putExtra("REQUESTCODE", RequestCodes.PLAN_NEW_ACTIVITY);
        startActivityForResult(intent,RequestCodes.PLAN_NEW_ACTIVITY);
    }

    public void addExistingActivity() {
        Intent intent = new Intent(this, ActivityFindView.class);
        intent.putExtra("REQUESTCODE", RequestCodes.PLAN_ACTIVITY);
        startActivityForResult(intent,RequestCodes.PLAN_ACTIVITY);
    }

    public void addBreakActivity() {
        Intent intent = new Intent(this, ActivityFindView.class);
        intent.putExtra("REQUESTCODE", RequestCodes.PLAN_BREAK);
        startActivityForResult(intent,RequestCodes.PLAN_BREAK);
    }

    public void addGalleryActivity() {
        Intent intent = new Intent(this, ActivityFindView.class);
        intent.putExtra("REQUESTCODE", RequestCodes.PLAN_GALLERY);
        startActivityForResult(intent,RequestCodes.PLAN_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();
        if(data!= null && data.getExtras()!=null && data.getExtras().get("ACTIVITY")!=null){
            intent.putExtra("ACTIVITY", (com.przyjaznyplan.models.Activity)data.getExtras().get("ACTIVITY"));
        }
        setResult(requestCode, intent);
        super.finish();
    }
}
