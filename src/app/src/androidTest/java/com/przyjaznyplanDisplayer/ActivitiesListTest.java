/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer;

import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.internal.util.Checks;
import android.support.test.rule.ActivityTestRule;
import android.test.suitebuilder.annotation.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
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
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplan.repositories.DatabaseUtils;

import java.util.List;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivitiesListTest {

    private final int ACTIVITIES_NUMBER = 4;
    private final int FIRST_ACTIVITY_NUMBER = 0;
    private final int SECOND_ACTIVITY_NUMBER = 1;


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
        switchFromFinalScreenToActivitiesList();
        onView(withId(R.id.list)).check(matches(isDisplayed()));
        for (int activityNumber = 0; activityNumber < ACTIVITIES_NUMBER; activityNumber++)
            getActivitiesListElement(activityNumber).check(matches(isDisplayed()));
    }

    @Test
    public void testDoingSomeActivities() {
        switchFromFinalScreenToActivitiesList();
        getActivitiesListElement(FIRST_ACTIVITY_NUMBER).perform(click());
        getActivitiesListElement(SECOND_ACTIVITY_NUMBER).perform(click());

        getActivitiesListLabelElement(FIRST_ACTIVITY_NUMBER).check(isActivityDone());
        getActivitiesListLabelElement(SECOND_ACTIVITY_NUMBER).check(isActivityDone());

        Activity activity = ActivityRepository.getActivityByTitleFromCurrentPlan(TestUtils.ACTIVITY_BASE_NAME + "1");
        assertThat("Activity should exists in database", activity, is(notNullValue()));
        assertThat("Activity should be done", activity.getStatus(), is(equalTo(Activity.ActivityStatus.FINISHED.toString())));
    }

    @Test
    public void testDoingAllActivities() {
        switchFromFinalScreenToActivitiesList();
        for (int activityNumber = 0; activityNumber < ACTIVITIES_NUMBER; activityNumber++)
            getActivitiesListElement(activityNumber).perform(click());
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
        List<Activity> activities = ActivityRepository.getAllActivitiesFromCurrentPlan();
        for (Activity activity : activities)
            assertThat("Activity should be done", activity.getStatus(), is(Matchers.equalTo(Activity.ActivityStatus.FINISHED.toString())));
    }

    private void switchFromFinalScreenToActivitiesList() {
        onView(withId(R.id.imageView)).perform(click());
        onView(withId(R.id.imageView)).perform(click());
        onView(withId(R.id.imageView)).perform(click());
    }

    private DataInteraction getActivitiesListElement(int elementNumber) {
        return onData(hasToString(startsWith(TestUtils.ACTIVITY_BASE_NAME)))
                .inAdapterView(withId(R.id.list)).atPosition(elementNumber);
    }

    private DataInteraction getActivitiesListLabelElement(int elementNumber) {
        return getActivitiesListElement(elementNumber).onChildView(withId(R.id.label));
    }

    private ViewAssertion isActivityDone() {
        return matches(withTextPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG));
    }

    public static Matcher<View> withTextPaintFlags(final int paintFlag) {
        Checks.checkNotNull(paintFlag);

        return new BoundedMatcher<View, TextView>(TextView.class) {

            @Override
            public boolean matchesSafely(TextView label) {
                return (paintFlag & label.getPaintFlags()) > 0;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with paint flag: ");
            }
        };
    }
}
