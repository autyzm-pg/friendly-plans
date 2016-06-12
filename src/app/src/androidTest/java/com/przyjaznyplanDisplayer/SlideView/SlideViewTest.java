/*
 *
 *  * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 *  * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *  *
 *  * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 *
 */

package com.przyjaznyplanDisplayer.SlideView;

import android.content.Intent;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.przyjaznyplan.R;
import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplanDisplayer.PlanActivityView;
import com.przyjaznyplanDisplayer.Utils.TestUtils;
import com.przyjaznyplanDisplayer.Utils.Matcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SlideViewTest {

    private final int ACTIVITIES_NUMBER = 6;

    @Rule
    public ActivityTestRule<PlanActivityView> activityRule = new ActivityTestRule<>(PlanActivityView.class, true, false);

    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        TestUtils.setUpCurrentPlanWithActivities(ACTIVITIES_NUMBER);
        TestUtils.setCurrentUserPreferences(
                TypyWidokuPlanuAktywnosci.slide,
                TestUtils.DONT_CHANGE_ACTIVITY_VIEW_TYPE,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
    }

    @After
    public void tearDown() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
    }

    @Test
    public void testDisplayingActivity() {
        runActivity();
        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTIVITY_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void testSwitchingActivityForward() {
        runActivity();
        onView(withId(R.id.basivview_greenbutton)).perform(click());

        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTIVITY_BASE_NAME + TestUtils.SECOND_ACTIVITY_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(isDisplayed()));
    }

    @Test
    public void testSwitchingActivityBackward() {
        runActivity();
        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_redbutton)).perform(click());

        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTIVITY_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));
    }

    @Test
    public void testSwitchingAllActivities() {
        runActivity();
        for (int activityNumber = 0; activityNumber < ACTIVITIES_NUMBER; activityNumber++)
            onView(withId(R.id.basivview_greenbutton)).perform(click());

        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }

    @Test
    public void testSwitchingActivitiesWithBigNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.big,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_redbutton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_redbutton)).perform(click());

        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTIVITY_BASE_NAME + TestUtils.SECOND_ACTIVITY_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.BIG_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testSwitchingActivitiesWithMediumNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.medium,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_redbutton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_redbutton)).perform(click());

        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTIVITY_BASE_NAME + TestUtils.SECOND_ACTIVITY_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.MEDIUM_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testSwitchingActivitiesWithSmallNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.small,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_redbutton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_redbutton)).perform(click());

        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTIVITY_BASE_NAME + TestUtils.SECOND_ACTIVITY_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.SMALL_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    private void runActivity() {
        activityRule.launchActivity(new Intent());
    }

}
