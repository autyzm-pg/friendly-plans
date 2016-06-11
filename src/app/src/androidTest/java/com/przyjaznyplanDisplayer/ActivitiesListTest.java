/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer;

import android.support.test.espresso.DataInteraction;
import android.support.test.rule.ActivityTestRule;
import android.test.suitebuilder.annotation.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasToString;

import com.example.przyjaznyplan.R;
import com.przyjaznyplan.repositories.DatabaseUtils;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivitiesListTest {

    private final int ACTIVITIES_NUMBER = 4;

    @Rule
    public ActivityTestRule<PlanActivityView> mActivityRule = new ActivityTestRule<>(PlanActivityView.class);

    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        TestUtils.setUpCurrentPlanWithActivities(ACTIVITIES_NUMBER);
    }

    @After
    public void tearDown() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
    }

    @Test
    public void testActivitiesListDisplay() {
        onView(withId(R.id.imageView)).perform(click());
        onView(withId(R.id.imageView)).perform(click());
        onView(withId(R.id.imageView)).perform(click());
        onView(withId(R.id.list)).check(matches(isDisplayed()));
        for (int activityNumber = 0; activityNumber < ACTIVITIES_NUMBER; activityNumber++)
            getActivitiesListElement(activityNumber).check(matches(isDisplayed()));
    }

    private DataInteraction getActivitiesListElement(int elementNumber) {
        return onData(hasToString(startsWith(TestUtils.ACTIVITY_BASE_NAME)))
                .inAdapterView(withId(R.id.list)).atPosition(elementNumber);
    }
}
