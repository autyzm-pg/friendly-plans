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

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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
    public void editActionInitValuesTest(){
        runActivity(activity);

        onView(withId(R.id.editText)).check(matches(withText("ACTIVITY")));

        onView(withId(R.id.imageView4)).check(matches(not(isDisplayed())));
        onView(withId(R.id.imageView)).check(matches(not(isDisplayed())));

        onView(withId(R.id.textView5)).check(matches(withText(NUMBER_OF_ACTION_TEXT)));

        onView(withId(R.id.imageView2)).check(matches(isDisplayed()));
        onView(withId(R.id.imageView3)).check(matches(isDisplayed()));
    }

    @Test
    public void editActionWithoutAudioTest(){
        runActivity(activityWithoutAudio);

        onView(withId(R.id.editText)).check(matches(withText("ACTIVITY")));

        onView(withId(R.id.imageView4)).check(matches(not(isDisplayed())));
        onView(withId(R.id.imageView)).check(matches(not(isDisplayed())));

        onView(withId(R.id.textView5)).check(matches(withText(NO_ACTIONS_TEXT)));

        onView(withId(R.id.imageView2)).check(matches(not(isDisplayed())));
        onView(withId(R.id.imageView3)).check(matches(not(isDisplayed())));
    }

    @Test
    public void editActionTest(){
        runActivity(activity);

        onView(withId(R.id.editText)).perform(clearText(), typeText(NEW_ACTIVITY_TITLE));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.button4)).perform(click());

        Activity editedActivity = ActivityRepository.getActivityById(activity.getId());
        assertEquals("Activity should have been changed", NEW_ACTIVITY_TITLE, editedActivity.getTitle());
    }

    private void runActivity(Activity activity) {
        Intent intent = new Intent();
        intent.putExtra("ACTIVITY", activity);
        activityRule.launchActivity(intent);
    }

    @After
    public void tearDown() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
    }
}
