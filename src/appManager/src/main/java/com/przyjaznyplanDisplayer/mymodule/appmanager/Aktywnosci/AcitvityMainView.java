/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

public class AcitvityMainView extends Activity {
    ArrayAdapter<String> adapter;
    ListView activityListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        activityListView = (ListView) findViewById(R.id.activityListView);
        ArrayList<String> activityListArray = new ArrayList<String>();
        activityListArray.add(getResources().getString(R.string.add_activity));
        activityListArray.add(getResources().getString(R.string.edit_activity));
        activityListArray.add(getResources().getString(R.string.delete_activity));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activityListArray);
        activityListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if(position == 0) addActivity();
                else if(position == 1) editActivity();
                else if(position == 2) deleteActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void addActivity(){
        Intent intent = new Intent(this, ActivityEditView.class);
        startActivityForResult(intent, RequestCodes.ACTIVITY_ADD_NEW);
    }

    private void editActivity(){
        Intent intent = new Intent(this, ActivityFindView.class);
        intent.putExtra("REQUESTCODE",RequestCodes.ACTIVITY_EDIT);
        startActivityForResult(intent,RequestCodes.ACTIVITY_EDIT);
    }

    private void deleteActivity(){
        Intent intent = new Intent(this, ActivityFindView.class);
        intent.putExtra("REQUESTCODE",RequestCodes.ACTIVITY_REMOVE);
        startActivityForResult(intent,RequestCodes.ACTIVITY_REMOVE);
    }

}
