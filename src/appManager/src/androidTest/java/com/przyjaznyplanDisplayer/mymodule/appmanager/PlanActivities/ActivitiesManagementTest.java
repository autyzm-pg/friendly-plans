package com.przyjaznyplanDisplayer.mymodule.appmanager.PlanActivities;


import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.widget.ListView;

import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci.ActivityManagementView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.TestUtils;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.ActivitySimpleAdapter;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.core.AllOf.allOf;

public class ActivitiesManagementTest {

    private static final int NUMBER_OF_ACTION = 1;
    private static final int NUMBER_OF_ACTIVITIES = 3;
    @Rule
    public ActivityTestRule<ActivityManagementView> activityRule = new ActivityTestRule<>(ActivityManagementView.class, true, false);

    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        List<Activity> activities = TestUtils.createActivitiesWithActions(NUMBER_OF_ACTIVITIES, NUMBER_OF_ACTION, "ACTIVITY", "ACTION", "/path/to/audio", "path/to/image");
    }

    @Test
    public void showPlanActivityListTest(){

        Intent intent = new Intent();
        intent.putExtra("REQUESTCODE", RequestCodes.ACTIVITY_EDIT);
        activityRule.launchActivity(intent);

        ListView listView = (ListView) activityRule.getActivity().findViewById(R.id.listView);

        ActivitySimpleAdapter activitiesAdapter = (ActivitySimpleAdapter) listView.getAdapter();
        assertEquals("Should all activities", activitiesAdapter.getCount(), NUMBER_OF_ACTIVITIES);

    }

    @Test
    public void filterPlanActivityListTest(){

        Intent intent = new Intent();
        intent.putExtra("REQUESTCODE", RequestCodes.ACTIVITY_EDIT);
        activityRule.launchActivity(intent);

        onView(withId(R.id.editText)).perform(clearText(),typeText("ACTIVITY0"));
        onView(withId(R.id.button)).perform(click());

        ListView listView = (ListView) activityRule.getActivity().findViewById(R.id.listView);

        ActivitySimpleAdapter activitiesAdapter = (ActivitySimpleAdapter) listView.getAdapter();
        assertEquals("Should show filtered activities", activitiesAdapter.getCount(), 1);

    }

    @Test
    public void deleteActivityTest(){

        Intent intent = new Intent();
        intent.putExtra("REQUESTCODE", RequestCodes.ACTIVITY_EDIT);
        activityRule.launchActivity(intent);

        onView(allOf(withId(R.id.removeButton), hasSibling(withText("ACTIVITY0")))).perform(click());
        onView(withText(R.string.yes)).perform(click());

        List<Activity> activites = ActivityRepository.getActivityByTitle("ACTION0");

        assertEquals("Activity should be deleted",0 ,activites.size());

    }

    @Test
    public void editActivityButtonTest(){
        Intent intent = new Intent();
        intent.putExtra("REQUESTCODE", RequestCodes.ACTIVITY_EDIT);
        activityRule.launchActivity(intent);

        onView(allOf(withId(R.id.editButton), hasSibling(withText("ACTIVITY0")))).perform(click());
    }


}
