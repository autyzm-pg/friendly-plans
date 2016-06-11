/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.example.przyjaznyplan.R;
import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplanDisplayer.Utils.PlanActivityAdapter;


public class ActivitiesListTest extends ActivityInstrumentationTestCase2<PlanActivityView> {

    private final int ACTIVITIES_NUMBER = 4;

    public ActivitiesListTest() {
        super(PlanActivityView.class);
    }

    protected void setUp() throws Exception {

        DatabaseUtils.rebuildDatabaseWithInitData();
        TestUtils.setUpCurrentPlanWithActivities(ACTIVITIES_NUMBER);
    }

    protected void tearDown() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
    }

    public void testActivitiesListDisplay() {
        Activity mainActivity = this.getActivity();
        ListView listActivities = (ListView) mainActivity.findViewById(R.id.list);
        PlanActivityAdapter planActivityAdapter = (PlanActivityAdapter) listActivities.getAdapter();
        assertEquals(ACTIVITIES_NUMBER, planActivityAdapter.getCount());
    }

}
