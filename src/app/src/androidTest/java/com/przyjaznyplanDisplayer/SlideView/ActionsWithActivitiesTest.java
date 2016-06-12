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

import com.example.przyjaznyplan.R;
import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplanDisplayer.PlanActivityView;
import com.przyjaznyplanDisplayer.Utils.Matcher;
import com.przyjaznyplanDisplayer.Utils.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ActionsWithActivitiesTest {

    private final int ACTIVITIES_NUMBER = 3;
    private final int ACTIONS_NUMBER = 3;

    @Rule
    public ActivityTestRule<PlanActivityView> activityRule = new ActivityTestRule<>(PlanActivityView.class, true, false);

    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        TestUtils.setUpCurrentPlanWithActivitiesAndActions(ACTIVITIES_NUMBER, ACTIONS_NUMBER);
        TestUtils.setCurrentUserPreferences(
                TypyWidokuPlanuAktywnosci.slide,
                TestUtils.DONT_CHANGE_ACTIVITY_VIEW_TYPE,
                TypyWidokuCzynnosci.basic
        );
    }

    @After
    public void tearDown() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
    }

    @Test
    public void testDisplayingActions() {
        runActivity();
        onView(withId(R.id.basivview_greenbutton)).perform(click());

        assertIsOnActionsFirstPage();
    }

    @Test
    public void testSwitchingActionsForward() {
        runActivity();
        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_greenbutton)).perform(click());

        assertIsOnActionsSecondPage();
    }

    @Test
    public void testSwitchingActionsBackward() {
        runActivity();
        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_redbutton)).perform(click());

        assertIsOnActionsFirstPage();
    }

    @Test
    public void testSwitchingActionsMultiTimesForwardAndBackward() {
        runActivity();
        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_redbutton)).perform(click());

        onView(withId(R.id.basivview_redbutton)).perform(click());

        assertIsOnActivitiesFirstPage();

        onView(withId(R.id.basivview_greenbutton)).perform(click());

        assertIsOnActionsFirstPage();
    }

    @Test
    public void testSwitchingAllActions() {
        runActivity();
        onView(withId(R.id.basivview_greenbutton)).perform(click());
        for (int actionNumber = 0; actionNumber < ACTIONS_NUMBER; actionNumber++)
            onView(withId(R.id.basivview_greenbutton)).perform(click());

        assertIsOnActivitiesSecondPage();
    }

    @Test
    public void testSwitchingAllActionsAndSwitchingBackward() {
        runActivity();
        onView(withId(R.id.basivview_greenbutton)).perform(click());
        for (int actionNumber = 0; actionNumber < ACTIONS_NUMBER; actionNumber++)
            onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_redbutton)).perform(click());

        assertIsOnActivitiesFirstPage();
    }

    @Test
    public void testSwitchingAllActionsAndActivities() {
        runActivity();
        for (int activityNumber = 0; activityNumber < ACTIVITIES_NUMBER; activityNumber++) {
            onView(withId(R.id.basivview_greenbutton)).perform(click());
            for (int actionNumber = 0; actionNumber < ACTIONS_NUMBER; actionNumber++)
                onView(withId(R.id.basivview_greenbutton)).perform(click());
        }
        assertIsOnLastPage();
    }

    @Test
    public void testSwitchingForwardActionsWithBigNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.big,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_greenbutton)).perform(click());

        assertIsOnActionsSecondPage();
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.BIG_ACTION_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testSwitchingAllActionsWithBigNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.big,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        for (int actionNumber = 0; actionNumber < ACTIONS_NUMBER; actionNumber++)
            onView(withId(R.id.basivview_greenbutton)).perform(click());

        assertIsOnActivitiesSecondPage();
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.BIG_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testSwitchingAllActionsAndSwitchingBackwardWithBigNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.big,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        for (int actionNumber = 0; actionNumber < ACTIONS_NUMBER; actionNumber++)
            onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_redbutton)).perform(click());

        assertIsOnActivitiesFirstPage();
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.BIG_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testSwitchingForwardActionsWithMediumNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.medium,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_greenbutton)).perform(click());

        assertIsOnActionsSecondPage();
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.MEDIUM_ACTION_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testSwitchingAllActionsWithMediumNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.medium,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        for (int actionNumber = 0; actionNumber < ACTIONS_NUMBER; actionNumber++)
            onView(withId(R.id.basivview_greenbutton)).perform(click());

        assertIsOnActivitiesSecondPage();
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.MEDIUM_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testSwitchingAllActionsAndSwitchingBackwardWithMediumNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.medium,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        for (int actionNumber = 0; actionNumber < ACTIONS_NUMBER; actionNumber++)
            onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_redbutton)).perform(click());

        assertIsOnActivitiesFirstPage();
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.MEDIUM_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testSwitchingForwardActionsWithSmallNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.small,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_greenbutton)).perform(click());

        assertIsOnActionsSecondPage();
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.SMALL_ACTION_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testSwitchingAllActionsWithSmallNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.small,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        for (int actionNumber = 0; actionNumber < ACTIONS_NUMBER; actionNumber++)
            onView(withId(R.id.basivview_greenbutton)).perform(click());

        assertIsOnActivitiesSecondPage();
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.SMALL_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    @Test
    public void testSwitchingAllActionsAndSwitchingBackwardWithSmallNames() {
        TestUtils.setCurrentUserPreferences(
                TestUtils.DONT_CHANGE_PLAN_VIEW_TYPE,
                TypyWidokuAktywnosci.small,
                TestUtils.DONT_CHANGE_ACTION_VIEW_TYPE
        );
        runActivity();

        onView(withId(R.id.basivview_greenbutton)).perform(click());
        for (int actionNumber = 0; actionNumber < ACTIONS_NUMBER; actionNumber++)
            onView(withId(R.id.basivview_greenbutton)).perform(click());
        onView(withId(R.id.basivview_redbutton)).perform(click());

        assertIsOnActivitiesFirstPage();
        onView(withId(R.id.basicview_description)).check(matches(Matcher.withTextSize(TestUtils.SMALL_ACTIVITY_NAMES_TEXT_SIZE)));
    }

    private void runActivity() {
        activityRule.launchActivity(new Intent());
    }

    private void assertIsOnActionsFirstPage() {
        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTION_BASE_NAME + TestUtils.FIRST_ACTION_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(isDisplayed()));

        Activity activity = ActivityRepository.getActivityByTitleFromCurrentPlan(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTION_NUMBER);
        assertIsActivityNotDone(activity);
    }

    private void assertIsOnActionsSecondPage() {
        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTION_BASE_NAME + TestUtils.SECOND_ACTION_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(isDisplayed()));

        Activity activity = ActivityRepository.getActivityByTitleFromCurrentPlan(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTION_NUMBER);
        assertIsActivityNotDone(activity);
    }

    private void assertIsOnActivitiesFirstPage() {
        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTIVITY_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        Activity activity = ActivityRepository.getActivityByTitleFromCurrentPlan(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTION_NUMBER);
        assertIsActivityNotDone(activity);
    }

    private void assertIsOnActivitiesSecondPage() {
        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTIVITY_BASE_NAME + TestUtils.SECOND_ACTIVITY_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(isDisplayed()));

        Activity activity = ActivityRepository.getActivityByTitleFromCurrentPlan(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTION_NUMBER);
        assertIsActivityDone(activity);
    }

    private void assertIsOnLastPage() {
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }

    private void assertIsActivityDone(Activity activity) {
        assertThat("Activity should be done", activity.getStatus(), is(equalTo(Activity.ActivityStatus.FINISHED.toString())));
    }

    private void assertIsActivityNotDone(Activity activity) {
        assertThat("Activity should not be done", activity.getStatus(), is(equalTo(Activity.ActivityStatus.NEW.toString())));
    }

}
