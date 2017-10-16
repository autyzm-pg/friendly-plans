package pg.autyzm.friendly_plans;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static matcher.RecyclerViewMatcher.withRecyclerView;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    public void When_TaskHasSteps_Expect_StepsToBeDisplayed() {
        checkStepDisplayed(0, STEP_NAME_1);
        checkStepDisplayed(1, STEP_NAME_2);
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

    private void checkStepDisplayed(int position, String stepName) {
        onView(withRecyclerView(R.id.rv_step_list)
                .atPosition(position))
                .check(selectedDescendantsMatch(withId(R.id.id_tv_step_name),
                        withText(stepName)));
    }
}
