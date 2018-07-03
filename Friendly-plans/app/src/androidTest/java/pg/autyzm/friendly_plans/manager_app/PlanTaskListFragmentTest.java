package pg.autyzm.friendly_plans.manager_app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import database.repository.PlanTemplateRepository;
import database.repository.TaskTemplateRepository;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.manager_app.view.plan_create.PlanCreateActivity;
import pg.autyzm.friendly_plans.manager_app.view.plan_create_task_list.PlanTaskListFragment;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.resource.PlanTemplateRule;
import pg.autyzm.friendly_plans.resource.TaskTemplateRule;
import pg.autyzm.friendly_plans.view_actions.ViewClicker;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pg.autyzm.friendly_plans.matcher.RecyclerViewMatcher.withRecyclerView;

@RunWith(AndroidJUnit4.class)
public class PlanTaskListFragmentTest {

    private static final String PLAN_ID = "PLAN_ID";
    private static final String TASK_NAME = "TASK_NAME";
    private static final String PLAN_NAME = "PLAN NAME";
    private static final String DELETE_TEST_TASK = "DELETE TEST TASK";

    private  PlanTemplateRepository planTemplateRepository;
    private  TaskTemplateRepository taskTemplateRepository;
    private long planId;

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<PlanCreateActivity> activityRule = new ActivityTestRule<>(
            PlanCreateActivity.class, true, true);

    @Rule
    public PlanTemplateRule planTemplateRule = new PlanTemplateRule(daoSessionResource,
            activityRule);

    @Rule
    public TaskTemplateRule taskTemplateRule = new TaskTemplateRule(daoSessionResource,
            activityRule);

    @Before
    public void setUp() {
        Context context = activityRule.getActivity().getApplicationContext();
        planTemplateRepository = new PlanTemplateRepository(daoSessionResource.getSession(context));
        taskTemplateRepository = new TaskTemplateRepository(daoSessionResource.getSession(context));

        long taskId = taskTemplateRepository.create(DELETE_TEST_TASK, 1, null, null);
        planId = planTemplateRule.createPlan(PLAN_NAME);
        planTemplateRepository.setTasksWithThisPlan(planId, taskId);
        taskTemplateRule.createTask(TASK_NAME);

        PlanTaskListFragment fragment = new PlanTaskListFragment();

        Bundle args = new Bundle();
        args.putLong(PLAN_ID, planId);
        fragment.setArguments(args);

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

    @Test
    public void whenAddingNewTaskToPlanExpectShowTaskOnList() {
        onView(withId(R.id.id_btn_create_plan_add_tasks))
            .perform(click());

        RecyclerView recyclerView = (RecyclerView) activityRule.getActivity().findViewById(R.id.rv_create_plan_add_tasks);
        int lastPosition = recyclerView.getAdapter().getItemCount() - 1;

        onView(withId(R.id.rv_create_plan_add_tasks)).perform(
                RecyclerViewActions.scrollToPosition(lastPosition));

        onView(withId(R.id.rv_create_plan_add_tasks)).perform(
                RecyclerViewActions
                        .actionOnItemAtPosition(lastPosition, click()));

        onView(withId(R.id.id_btn_add_tasks_to_plan))
                .perform(click());

        recyclerView = (RecyclerView) activityRule.getActivity().findViewById(R.id.rv_create_plan_task_list);
        lastPosition = recyclerView.getAdapter().getItemCount() - 1;

        onView(withRecyclerView(R.id.rv_create_plan_task_list)
                .atPosition(lastPosition))
                .check(matches(hasDescendant(withText(TASK_NAME))));
    }

    @Test
    public void whenRemoveIconClickedExpectTaskToNoLongerBeInThisPlan(){
        final int testedTaskPosition = 0;

        onView(withId(R.id.rv_create_plan_task_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(testedTaskPosition,
                                new ViewClicker(R.id.id_remove_task)));

        onView(withId(R.id.rv_create_plan_task_list)).perform(
                RecyclerViewActions.scrollToPosition(testedTaskPosition));

        onView(withRecyclerView(R.id.rv_create_plan_task_list)
                .atPosition(testedTaskPosition)).check(doesNotExist());
    }
}
