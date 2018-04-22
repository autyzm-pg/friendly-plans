package pg.autyzm.friendly_plans;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pg.autyzm.friendly_plans.view.plan_create.PlanCreateActivity;

@RunWith(AndroidJUnit4.class)
public class PlanCreateActivityTest {

    @Rule
    public ActivityTestRule<PlanCreateActivity> activityRule = new ActivityTestRule<>(
            PlanCreateActivity.class, true, true);

    @Before
    public void unlockScreen() {
        final PlanCreateActivity activity = activityRule.getActivity();
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
    public void whenPlanCreateActivityExpectHeaderAndEmptyField() {
        onView(withId(R.id.id_plan_create_description))
                .check(matches(withText(R.string.plan_create_description)));
        onView(withId(R.id.id_et_plan_name))
                .check(matches(withText("")));
    }
}
