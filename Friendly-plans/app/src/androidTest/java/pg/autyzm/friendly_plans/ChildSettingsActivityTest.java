package pg.autyzm.friendly_plans;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pg.autyzm.friendly_plans.view.child_settings.ChildSettingsActivity;

@RunWith(AndroidJUnit4.class)
public class ChildSettingsActivityTest {

    @Rule
    public ActivityTestRule<ChildSettingsActivity> activityRule = new ActivityTestRule<>(
            ChildSettingsActivity.class, true, true);

    @Before
    public void unlockScreen() {
        final ChildSettingsActivity activity = activityRule.getActivity();
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
    public void whenChildSettingActivityExpectHeadersAndOtherElementsOfView() {
        onView(withId(R.id.id_child_settings_change_description))
                .check(matches(withText(R.string.child_settings_change)));
        onView(withId(R.id.id_child_settings_select_plan_description))
                .check(matches(withText(R.string.child_settings_select_plan)));
        onView(withId(R.id.id_et_child_name))
                .check(matches(withText("")));
        onView(withId(R.id.id_et_child_surname))
                .check(matches(withText("")));
        onView(withId(R.id.id_et_timer_sound))
                .check(matches(withText("")));
        onView(withId(R.id.id_btn_select_timer_sound)).check(matches(isDisplayed()));
        onView(withId(R.id.id_rb_font_big)).check(matches(isNotChecked()));
        onView(withId(R.id.id_rb_font_medium)).check(matches(isNotChecked()));
        onView(withId(R.id.id_rb_font_small)).check(matches(isNotChecked()));
        onView(withId(R.id.id_rb_step_list)).check(matches(isNotChecked()));
        onView(withId(R.id.id_rb_step_slide)).check(matches(isNotChecked()));
        onView(withId(R.id.id_rb_task_list)).check(matches(isNotChecked()));
        onView(withId(R.id.id_rb_task_slide)).check(matches(isNotChecked()));
    }
}
