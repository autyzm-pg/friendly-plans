/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.mymodule.appmanager.PlanyZarzadzanie;

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

public class UserPlansMainView extends Activity {
    ArrayAdapter<String> adapter;
    ListView usersPlansListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_plans_main_view);
        usersPlansListView = (ListView) findViewById(R.id.plansListView);
        ArrayList<String> patternsListArray = new ArrayList<String>();
        patternsListArray.add(getResources().getString(R.string.edit_user_plan));
        // TODO: patternsListArray.add(getResources().getString(R.string.show_user_plan));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patternsListArray);
        usersPlansListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        usersPlansListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position == 0) editUserPlan();
                else if (position == 1) showPlan();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void editUserPlan() {
        Intent intent = new Intent(this, PlanCurrentEditView.class);
        startActivityForResult(intent, RequestCodes.CURRENT_PLAN_EDIT);
    }

    public void showPlan() {
        Intent intent = new Intent(this, PrzegladajAktualnyPlan.class);
        startActivityForResult(intent, RequestCodes.CURRENT_PLAN_BROWSE);
    }

}
