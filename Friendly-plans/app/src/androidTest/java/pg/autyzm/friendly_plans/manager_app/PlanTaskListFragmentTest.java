package pg.autyzm.friendly_plans.manager_app;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.manager_app.view.plan_create.PlanCreateActivity;
import pg.autyzm.friendly_plans.manager_app.view.plan_create_task_list.PlanTaskListFragment;

@RunWith(AndroidJUnit4.class)
public class PlanTaskListFragmentTest {

    @Rule
    public ActivityTestRule<PlanCreateActivity> activityRule = new ActivityTestRule<>(
            PlanCreateActivity.class, true, true);

    @Before
    public void setUp() {
        PlanTaskListFragment fragment = new PlanTaskListFragment();

        FragmentManager manager = activityRule.getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.plan_container, fragment);
        transaction.commit();
    }
    @Test
    public void whenPlanTaskListFragmentExpectHeaderAndButtons() {
        onView(withId(R.id.id_tv_plan_tasks_list_info))
                .check(matches(withText(R.string.create_plan_tasks_list_info)));
        onView(withId(R.id.id_btn_create_plan_add_tasks)).check(matches(isDisplayed()));
        onView(withId(R.id.id_btn_save_plan_tasks)).check(matches(isDisplayed()));
    }
}
