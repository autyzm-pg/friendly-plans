package pg.autyzm.friendly_plans;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static matcher.RecyclerViewMatcher.withRecyclerView;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pg.autyzm.friendly_plans.matchers.ToastMatcher;

@RunWith(AndroidJUnit4.class)
public class StepListFragmentTest {

    public static final String STEP_NAME_1 = "STEP_NAME_1";
    public static final String STEP_NAME_2 = "STEP_NAME_2";
    public static final String TASK_NAME_1 = "TASK_NAME_1";
    public static final String TASK_ID = "task_id";

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<TaskCreateActivity> activityRule = new ActivityTestRule<>(
            TaskCreateActivity.class, true, true);

    @Rule
    public TaskTemplateRule taskTemplateRule = new TaskTemplateRule(daoSessionResource,
            activityRule);

    @Before
    public void setUp() {
        long taskId = taskTemplateRule.createTaskWithSteps(TASK_NAME_1, STEP_NAME_1, STEP_NAME_2);

        openStepsListFragment(taskId);
    }

    @Test
    public void WhenTaskHasStepsExpectStepsToBeDisplayed() {
        long taskId = taskTemplateRule.createTaskWithSteps(TASK_NAME_1, STEP_NAME_1, STEP_NAME_2);
        openStepsListFragment(taskId);

        assertStepDisplayed(0, STEP_NAME_1);
        assertStepDisplayed(1, STEP_NAME_2);
    }

    @Test
    public void WhenTaskHasNoStepsExpectNoStepsToBeDisplayed() {
        long taskId = taskTemplateRule.createTask(TASK_NAME_1);
        openStepsListFragment(taskId);

        onView(withId(R.id.rv_step_list)).check(matches(withSize(0)));
    }

    @Test
    public void WhenSaveAndFinishClickedTaskSavedShouldBeDisplayed() {
        long taskId = taskTemplateRule.createTaskWithSteps(TASK_NAME_1, STEP_NAME_1, STEP_NAME_2);
        openStepsListFragment(taskId);
        onView(withId(R.id.id_btn_next))
            .perform(click());

        onView(withText(R.string.task_with_steps_saved_message)).inRoot(new ToastMatcher())
            .check(matches(isDisplayed()));
    }

    private void openStepsListFragment(long taskId) {
        StepListFragment fragment = new StepListFragment();
        Bundle args = new Bundle();
        args.putLong(TASK_ID, taskId);
        fragment.setArguments(args);

        FragmentManager manager = activityRule.getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.commit();
    }

    private void assertStepDisplayed(int position, String stepName) {
        onView(withRecyclerView(R.id.rv_step_list)
                .atPosition(position))
                .check(selectedDescendantsMatch(withId(R.id.id_tv_step_name),
                        withText(stepName)));
    }

    static Matcher<View> withSize(final int expectedSize) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(final View view) {
                final int listSize = ((RecyclerView) view).getAdapter().getItemCount();
                return listSize == expectedSize;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("RecyclerView should have " + expectedSize + " items");
            }
        };
    }
}
