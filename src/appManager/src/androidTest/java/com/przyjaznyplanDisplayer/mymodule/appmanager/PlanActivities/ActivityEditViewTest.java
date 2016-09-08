package com.przyjaznyplanDisplayer.mymodule.appmanager.PlanActivities;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci.ActivityEditView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.TestUtils;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ActivityEditViewTest {

    public static final int NUMBER_OF_ACTION = 2;
    public static final String NUMBER_OF_ACTION_TEXT = "2";
    public static final String NEW_ACTIVITY_TITLE = "NEW ACTIVITY";
    private static final int NO_ACTIONS = 0;
    private static final String NO_ACTIONS_TEXT = "0";

    @Rule
    public ActivityTestRule<ActivityEditView> activityRule = new ActivityTestRule<>(ActivityEditView.class, true, false);
    private Activity activity;
    private Activity activityWithoutAudio;


    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        activity = TestUtils.createActivityWithActions(NUMBER_OF_ACTION, "ACTIVITY", "ACTION","path/to/audio", "");
        activityWithoutAudio = TestUtils.createActivityWithActions(NO_ACTIONS, "ACTIVITY", "ACTION","", "");
    }

    @Test
    public void addPlanActivityWithoutActionsTest(){
        runActivity();

        onView(withId(R.id.activityTitle)).perform(clearText(), typeText(NEW_ACTIVITY_TITLE));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.saveActivityButton)).perform(click());

        List<Activity> addedActivities = ActivityRepository.getActivityByTitle(NEW_ACTIVITY_TITLE);

        assertEquals("New activity should be added", 1, addedActivities.size());
        Activity addedActivity = addedActivities.get(0);
        assertEquals("New activity should have expected title", NEW_ACTIVITY_TITLE, addedActivity.getTitle());
        assertEquals("New activity should not have actions", NO_ACTIONS, addedActivity.getSlides().size());

    }

    @Test
    public void addEmptyPlanActivityTest(){
        runActivity();

        onView(withId(R.id.saveActivityButton)).perform(click());
        onView(withText(R.string.missing_title_field)).inRoot(withDecorView(Matchers.not(is(activityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
    }

    @Test
    public void editPlanActivityInitValuesTest(){
        runActivity(activity);

        onView(withId(R.id.activityTitle)).check(matches(withText("ACTIVITY")));

        onView(withId(R.id.removePictureIcon)).check(matches(not(isDisplayed())));
        onView(withId(R.id.picture)).check(matches(not(isDisplayed())));

        onView(withId(R.id.activityNumber)).check(matches(withText(NUMBER_OF_ACTION_TEXT)));

        onView(withId(R.id.playSoundIcon)).check(matches(isDisplayed()));
        onView(withId(R.id.removeSoundIcon)).check(matches(isDisplayed()));
    }

    @Test
    public void editPlanActivityWithoutAudioTest(){
        runActivity(activityWithoutAudio);

        onView(withId(R.id.activityTitle)).check(matches(withText("ACTIVITY")));

        onView(withId(R.id.removePictureIcon)).check(matches(not(isDisplayed())));
        onView(withId(R.id.picture)).check(matches(not(isDisplayed())));

        onView(withId(R.id.activityNumber)).check(matches(withText(NO_ACTIONS_TEXT)));

        onView(withId(R.id.playSoundIcon)).check(matches(not(isDisplayed())));
        onView(withId(R.id.removeSoundIcon)).check(matches(not(isDisplayed())));
    }

    @Test
    public void editPlanActivityTest(){
        runActivity(activity);

        onView(withId(R.id.activityTitle)).perform(clearText(), typeText(NEW_ACTIVITY_TITLE));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.saveActivityButton)).perform(click());

        Activity editedActivity = ActivityRepository.getActivityById(activity.getId());
        assertEquals("Activity should have been changed", NEW_ACTIVITY_TITLE, editedActivity.getTitle());
    }

    private void runActivity(Activity activity) {
        Intent intent = new Intent();
        intent.putExtra("ACTIVITY", activity);
        activityRule.launchActivity(intent);
    }

    private void runActivity() {
        Intent intent = new Intent();
        activityRule.launchActivity(intent);
    }

    @After
    public void tearDown() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
    }
}
