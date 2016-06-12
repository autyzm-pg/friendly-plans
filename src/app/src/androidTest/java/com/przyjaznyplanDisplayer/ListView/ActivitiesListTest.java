/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.ListView;

import android.content.Intent;
import android.graphics.Paint;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.rule.ActivityTestRule;
import android.test.suitebuilder.annotation.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.is;

import com.example.przyjaznyplan.R;
import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplanDisplayer.PlanActivityView;
import com.przyjaznyplanDisplayer.Utils.TestUtils;
import com.przyjaznyplanDisplayer.Utils.Matcher;

import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivitiesListTest {

    private final int ACTIVITIES_NUMBER = 4;

    @Rule
    public ActivityTestRule<PlanActivityView> activityRule = new ActivityTestRule<>(PlanActivityView.class, true, false);

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
        runActivity();
        onView(withId(R.id.list)).check(matches(isDisplayed()));
        for (int activityNumber = 0; activityNumber < ACTIVITIES_NUMBER; activityNumber++)
            getActivitiesListElement(activityNumber).check(matches(isDisplayed()));
    }

    @Test
    public void testDoingSomeActivities() {
        runActivity();
        getActivitiesListElement(TestUtils.FIRST_ACTIVITY_NUMBER).perform(click());
        getActivitiesListElement(TestUtils.SECOND_ACTIVITY_NUMBER).perform(click());

        getActivitiesListLabelElement(TestUtils.FIRST_ACTIVITY_NUMBER).check(isActivityDone());
        getActivitiesListLabelElement(TestUtils.SECOND_ACTIVITY_NUMBER).check(isActivityDone());

        Activity activity = ActivityRepository.getActivityByTitleFromCurrentPlan(TestUtils.ACTIVITY_BASE_NAME + "1");
        assertThat("Activity should exists in database", activity, is(notNullValue()));
        assertThat("Activity should be done", activity.getStatus(), is(equalTo(Activity.ActivityStatus.FINISHED.toString())));
    }

    @Test
    public void testDoingAllActivities() {
        runActivity();
        for (int activityNumber = 0; activityNumber < ACTIVITIES_NUMBER; activityNumber++)
            getActivitiesListElement(activityNumber).perform(click());
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        List<Activity> activities = ActivityRepository.getAllActivitiesFromCurrentPlan();
        for (Activity activity : activities)
            assertThat("Activity should be done",
                    activity.getStatus(), is(Matchers.equalTo(Activity.ActivityStatus.FINISHED.toString())));
    }

    @Test
    public void testDisplayingBigActivitiesNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.big,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();
        DataInteraction firstActivityLabel = getActivitiesListLabelElement(TestUtils.FIRST_ACTIVITY_NUMBER);
        firstActivityLabel.check(matches(Matcher.withTextSize(TestUtils.BIG_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testDisplayingMediumActivitiesNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.medium,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();
        DataInteraction firstActivityLabel = getActivitiesListLabelElement(TestUtils.FIRST_ACTIVITY_NUMBER);
        firstActivityLabel.check(matches(Matcher.withTextSize(TestUtils.MEDIUM_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testDisplayingSmallActivitiesNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.small,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();
        DataInteraction firstActivityLabel = getActivitiesListLabelElement(TestUtils.FIRST_ACTIVITY_NUMBER);
        firstActivityLabel.check(matches(Matcher.withTextSize(TestUtils.SMALL_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    private void runActivity() {
        activityRule.launchActivity(new Intent());
    }

    private DataInteraction getActivitiesListElement(int elementNumber) {
        return onData(hasToString(startsWith(TestUtils.ACTIVITY_BASE_NAME)))
                .inAdapterView(withId(R.id.list)).atPosition(elementNumber);
    }

    private DataInteraction getActivitiesListLabelElement(int elementNumber) {
        return getActivitiesListElement(elementNumber).onChildView(withId(R.id.label));
    }

    private ViewAssertion isActivityDone() {
        return matches(Matcher.withTextPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG));
    }
}
