package pg.autyzm.friendly_plans;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pg.autyzm.friendly_plans.matcher.RecyclerViewMatcher.withRecyclerView;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;
import database.repository.PlanTemplateRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.view.child_settings.ChildSettingsActivity;

@RunWith(AndroidJUnit4.class)
public class ChildSettingsActivityTest {

    private static final String expectedName = "TEST PLAN ";
    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

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

    @Before
    public void setUp() {
        final int numberOfPlans = 7;
        PlanTemplateRepository planTemplateRepository = new PlanTemplateRepository(
                daoSessionResource.getSession(activityRule.getActivity().getApplicationContext()));
        planTemplateRepository.deleteAll();
        for (int planNumber = 0; planNumber < numberOfPlans; planNumber++) {
            planTemplateRepository
                    .create(expectedName + planNumber);
        }
        activityRule.launchActivity(new Intent());
    }

    @Test
    public void whenPlanIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {
        final int testedPlanPosition = 5;
        onView(withId(R.id.rv_child_settings_plan_list)).perform(scrollToPosition(testedPlanPosition));
        onView(withRecyclerView(R.id.rv_child_settings_plan_list)
                .atPosition(testedPlanPosition))
                .check(matches(hasDescendant(withText(expectedName
                        + testedPlanPosition))));
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
