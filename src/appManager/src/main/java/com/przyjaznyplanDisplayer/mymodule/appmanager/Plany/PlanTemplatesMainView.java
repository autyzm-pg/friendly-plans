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

import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

public class PlanTemplatesMainView extends Activity {
    ArrayAdapter<String> adapter;
    ListView patternsListView;

    /**
     * Method run on creating view. Fills ListView with menu items and show it in the view
     *
     * @param savedInstanceState    android variable
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_patterns_main_view);
        patternsListView = (ListView) findViewById(R.id.patternsListView);
        ArrayList<String> patternsListArray = new ArrayList<String>();
        patternsListArray.add(getResources().getString(R.string.add_plan));
        patternsListArray.add(getResources().getString(R.string.edit_plan));
        patternsListArray.add(getResources().getString(R.string.delete_plan));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, patternsListArray);
        patternsListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        patternsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position == 0) addPlan();
                else if (position == 1) editPlan();
                else if (position == 2) removePlan();
            }
        });
    }

    /**
     * Changes the view to the add new template view
     */
    private void addPlan() {
        Intent intent = new Intent(this, PlanEditView.class);
        startActivityForResult(intent, RequestCodes.PLAN_ADD_NEW);
    }

    /**
     * Changes the view to the edit template view
     */
    private void editPlan() {
        Intent intent = new Intent(this, PlanFindView.class);
        intent.putExtra("REQUESTCODE",RequestCodes.PLAN_EDIT);
        startActivityForResult(intent,RequestCodes.PLAN_EDIT);
    }

    /**
     * Changes the view to the remove template view
     */
    private void removePlan() {
        Intent intent = new Intent(this, PlanFindView.class);
        intent.putExtra("REQUESTCODE",RequestCodes.PLAN_REMOVE);
        startActivityForResult(intent,RequestCodes.PLAN_REMOVE);
    }
}