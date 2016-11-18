package com.przyjaznyplanDisplayer.mymodule.appmanager.PlanActivities;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.widget.ListView;

import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci.ActivityAssignmentView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.TestUtils;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.ActivityAssignmentAdapter;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;


import static junit.framework.Assert.assertEquals;

public class ActivityAssignmentTest {

    private static final int NUMBER_OF_ACTIVITIES = 4;
    private static final int NUMBER_OF_ACTION = 3;
    @Rule
    public ActivityTestRule<ActivityAssignmentView> activityRule = new ActivityTestRule<>(ActivityAssignmentView.class, true, false);

    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        TestUtils.createActivitiesWithActions(NUMBER_OF_ACTIVITIES, NUMBER_OF_ACTION, "ACTIVITY", "ACTION", "/path/to/audio", "path/to/image");
    }

    @Test
    public void showPlanActivityListTest(){

        Intent intent = new Intent();
        intent.putExtra("REQUESTCODE", RequestCodes.PLAN_ACTIVITY);
        activityRule.launchActivity(intent);

        ListView listView = (ListView) activityRule.getActivity().findViewById(R.id.activityAssignmentListView);

        ActivityAssignmentAdapter activitiesAdapter = (ActivityAssignmentAdapter) listView.getAdapter();
        assertEquals("Should all activities", activitiesAdapter.getCount(), NUMBER_OF_ACTIVITIES);

    }


}
