package pg.autyzm.friendly_plans;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateActivity;
import pg.autyzm.friendly_plans.view.step_create.StepCreateFragment;

@RunWith(AndroidJUnit4.class)
public class StepCreateFragmentTest {

    @Rule
    public ActivityTestRule<TaskCreateActivity> activityRule = new ActivityTestRule<TaskCreateActivity>(
        TaskCreateActivity.class, true, true);

    @Before
    public void setUp() {
        openStepCreateFragment();
    }

    @Before
    public void unlockScreen() {
        final TaskCreateActivity activity = activityRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }

    @Test
    public void whenStepCreateFragmentDisplayedExpectHeaderAndEmptyFields() {
        onView(withId(R.id.id_step_create_description))
            .check(matches(withText(R.string.create_step_description)));
        onView(withId(R.id.id_et_step_name))
            .check(matches(withText("")));
        onView(withId(R.id.id_et_step_picture))
            .check(matches(withText("")));
        onView(withId(R.id.id_et_step_sound))
            .check(matches(withText("")));
    }

    private void openStepCreateFragment() {
        StepCreateFragment fragment = new StepCreateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        FragmentManager manager = activityRule.getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.commit();
    }

}
