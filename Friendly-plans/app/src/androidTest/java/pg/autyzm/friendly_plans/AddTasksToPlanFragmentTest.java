package pg.autyzm.friendly_plans;

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
import pg.autyzm.friendly_plans.view.plan_create.PlanCreateActivity;
import pg.autyzm.friendly_plans.view.plan_create_add_tasks.AddTasksToPlanFragment;


@RunWith(AndroidJUnit4.class)
public class AddTasksToPlanFragmentTest {

    @Rule
    public ActivityTestRule<PlanCreateActivity> activityRule = new ActivityTestRule<>(
            PlanCreateActivity.class, true, true);

    @Before
    public void setUp() {
        AddTasksToPlanFragment fragment = new AddTasksToPlanFragment();

        FragmentManager manager = activityRule.getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.plan_container, fragment);
        transaction.commit();
    }

    @Test
    public void whenAddTasksToPlanFragmentExpectHeaderAndButton() {
        onView(withId(R.id.id_tv_add_tasks_to_plan_info))
                .check(matches(withText(R.string.create_plan_add_tasks_info)));
        onView(withId(R.id.id_btn_add_tasks_to_plan)).check(matches(isDisplayed()));
    }

}
