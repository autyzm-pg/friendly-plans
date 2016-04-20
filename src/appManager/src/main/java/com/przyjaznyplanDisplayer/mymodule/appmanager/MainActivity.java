/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.mymodule.appmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci.AcitvityMainView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Informacje.AboutView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Plany.PlanTemplatesMainView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.PlanyZarzadzanie.UserPlansMainView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Users.UserListView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

/**
 * Root view class for FriendlyPlanManager app.
 * It displays list with all possible options.
 *
 */
public class MainActivity extends Activity {
    ArrayAdapter<String> adapter;
    ListView activityListView;

    /**
     * Method run on creating view. Fills ListView with menu items and show it in the view
     *
     * @param savedInstanceState    android variable
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityListView = (ListView) findViewById(R.id.activityListView);
        ArrayList<String> activityListArray = new ArrayList<String>();
        activityListArray.add(getResources().getString(R.string.plans_pattern));
        activityListArray.add(getResources().getString(R.string.activity_management));
        activityListArray.add(getResources().getString(R.string.user_plans_management));
        activityListArray.add(getResources().getString(R.string.user_management));
        activityListArray.add(getResources().getString(R.string.about_app));
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, activityListArray);
        activityListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        activityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if(position == 0) plansPatterns();
                else if(position == 1) activityManagement();
                else if(position == 2) usersPlanManagement();
                else if(position == 3) usersManagement();
                else if(position == 4) aboutPlan();
            }
        });

    }

    /**
     * Changes the view to the plan template view
     */
    private void plansPatterns(){
        Intent intent = new Intent(this, PlanTemplatesMainView.class);
        startActivityForResult(intent, RequestCodes.PLANS_PATTERN);
    }

    /**
     * Changes the view to the activity management view
     */
    private void activityManagement() {
        Intent intent = new Intent(this, AcitvityMainView.class);
        startActivityForResult(intent, RequestCodes.ACTIVITY_MANAGEMENT_2);
    }

    /**
     * Changes the view to about app view
     */
    private void aboutPlan() {
        Intent intent = new Intent(this, AboutView.class);
        startActivityForResult(intent, 8642);
    }

    /**
     * Changes the view to the user plans management patterns view
     */
    private void usersPlanManagement(){
        Intent intent = new Intent(this, UserPlansMainView.class);
        startActivityForResult(intent, RequestCodes.USER_PLAN_MANAGEMENT);
    }

    /**
     * Changes the view to the users management view
     */
    private void usersManagement() {

        Intent intent = new Intent(this,UserListView.class);
        startActivity(intent);
    }


}
