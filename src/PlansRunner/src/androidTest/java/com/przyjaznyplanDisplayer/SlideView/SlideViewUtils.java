/*
 *
 *  * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 *  * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *  *
 *  * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 *
 */

package com.przyjaznyplanDisplayer.SlideView;

import android.support.test.espresso.matcher.ViewMatchers;

import com.example.przyjaznyplan.R;
import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplanDisplayer.Utils.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.is;

public class SlideViewUtils {

    public static void assertIsOnActivitiesFirstPage() {
        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTIVITY_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.INVISIBLE)));

        Activity activity = ActivityRepository.getActivityByTitleFromCurrentPlan(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTION_NUMBER);
        assertIsActivityNotDone(activity);
    }

    public static void assertIsOnActivitiesSecondPage() {
        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTIVITY_BASE_NAME + TestUtils.SECOND_ACTIVITY_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(isDisplayed()));

        Activity activity = ActivityRepository.getActivityByTitleFromCurrentPlan(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTION_NUMBER);
        assertIsActivityDone(activity);
    }

    public static void assertIsOnActionsFirstPage() {
        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTION_BASE_NAME + TestUtils.FIRST_ACTION_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(isDisplayed()));

        Activity activity = ActivityRepository.getActivityByTitleFromCurrentPlan(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTION_NUMBER);
        assertIsActivityNotDone(activity);
    }

    public static void assertIsOnActionsSecondPage() {
        onView(withId(R.id.basicview_description)).check(matches(withText(TestUtils.ACTION_BASE_NAME + TestUtils.SECOND_ACTION_NUMBER)));
        onView(withId(R.id.basivview_greenbutton)).check(matches(isDisplayed()));
        onView(withId(R.id.basivview_redbutton)).check(matches(isDisplayed()));

        Activity activity = ActivityRepository.getActivityByTitleFromCurrentPlan(TestUtils.ACTIVITY_BASE_NAME + TestUtils.FIRST_ACTION_NUMBER);
        assertIsActivityNotDone(activity);
    }

    public static void assertIsOnFinishedPage() {
        onView(withId(R.id.imageView)).check(matches(isDisplayed()));
    }

    public static void assertIsActivityDone(Activity activity) {
        assertThat("Activity should be done", activity.getStatus(), is(equalTo(Activity.ActivityStatus.FINISHED.toString())));
    }

    public static void assertIsActivityNotDone(Activity activity) {
        assertThat("Activity should not be done", activity.getStatus(), is(equalTo(Activity.ActivityStatus.NEW.toString())));
    }
}
