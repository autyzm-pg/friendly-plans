package com.przyjaznyplanDisplayer.mymodule.appmanager.PlanActions;


import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.Slide;
import com.przyjaznyplan.repositories.ActionRepository;
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Czynnosci.ActionAddEditView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.TestUtils;

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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditActionTest {

    public static final String PATH_TO_AUDIO = "/path/to/audio";
    @Rule
    public ActivityTestRule<ActionAddEditView> activityRule = new ActivityTestRule<>(ActionAddEditView.class, true, false);
    private Activity activity;
    private String expectedActionTitle;
    private Activity activityToChange;

    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        activity = TestUtils.createActivityWithActions(0, "ACTIVITY", "ACTION", "/path/to/audio", "path/to/image");
        activityToChange = TestUtils.createActivityWithActions(1, "ACTIVITY", "ACTION", PATH_TO_AUDIO, "path/to/image");
        expectedActionTitle = "NEW ACTION";
    }

    @Test
    public void addNewActionToActivityTest(){
        runActivity(activity);

        onView(withId(R.id.editText)).perform(clearText(), typeText(expectedActionTitle));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.saveButton)).perform(click());

        List<Slide> actions = ActionRepository.getActionsByActivityId(activity.getId());
        assertEquals("New action should be added", 1, actions.size());

    }

    @Test
    public void editActionTest(){
        runActivity(activityToChange, activityToChange.getSlides().get(0));

        onView(withId(R.id.editText)).perform(clearText(), typeText(expectedActionTitle));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.soundPath)).check(matches(withText(PATH_TO_AUDIO)));
        onView(withId(R.id.deleteSound)).check(matches(isDisplayed()));
        onView(withId(R.id.playSound)).check(matches((isDisplayed())));

        onView(withId(R.id.saveButton)).perform(click());

        List<Slide> actions = ActionRepository.getActionsByActivityId(activityToChange.getId());
        assertEquals("Action should be changed", expectedActionTitle, actions.get(0).getText());

    }

    @Test
    public void addTwoActionsTest(){
        runActivity(activity);

        onView(withId(R.id.editText)).perform(clearText(), typeText("FIRST ACTION"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.saveButton)).perform(click());

        activity = ActivityRepository.getActivityById(activity.getId());
        runActivity(activity);

        onView(withId(R.id.editText)).perform(clearText(), typeText("SECOND ACTION"));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.saveButton)).perform(click());

        List<Slide> actions = ActionRepository.getActionsByActivityId(activity.getId());
        assertEquals("Activity should have two added", 2, actions.size());
    }

    private void runActivity(Activity activity) {
        Intent intent = new Intent();
        intent.putExtra("ACTIVITY", activity);
        activityRule.launchActivity(intent);
    }

    private void runActivity(Activity activity, Slide action) {
        Intent intent = new Intent();
        intent.putExtra("ACTIVITY", activity);
        intent.putExtra("SLIDE", action);
        activityRule.launchActivity(intent);
    }

    @After
    public void tearDown() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
    }

}
