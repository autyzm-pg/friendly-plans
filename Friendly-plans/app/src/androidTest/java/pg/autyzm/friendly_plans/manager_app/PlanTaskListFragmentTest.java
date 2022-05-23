package pg.autyzm.friendly_plans.manager_app;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.manager_app.view.plan_create.PlanCreateActivity;
import pg.autyzm.friendly_plans.manager_app.view.plan_create_task_list.PlanTaskListFragment;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.resource.PlanTemplateRule;
import pg.autyzm.friendly_plans.resource.TaskTemplateRule;
import pg.autyzm.friendly_plans.view_actions.ViewClicker;

import static pg.autyzm.friendly_plans.matcher.RecyclerViewMatcher.withRecyclerView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class PlanTaskListFragmentTest {

    private static final String expectedNameTask = "TEST TASK ";
    private static final String expectedNamePrize = "TEST PRIZE ";
    private static final String expectedNameInteraction = "TEST INTERACTION ";
    private static final String PLAN_ID = "PLAN_ID";
    private static final String TYPE_ID = "TYPE_ID";
    private static final String PLAN_NAME = "PLAN NAME";
    private static final String DELETE_TEST_TASK = "DELETE TEST TASK";
    private static final String DELETE_TEST_PRIZE = "DELETE TEST PRIZE";
    private static final String DELETE_TEST_INTERACTION = "DELETE TEST INTERACTION";

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

    private Long planId;
    @Before
    public void setUp() {
        Context context = activityRule.getActivity().getApplicationContext();
        PlanTemplateRepository planTemplateRepository = new PlanTemplateRepository(daoSessionResource.getSession(context));

        planId = planTemplateRule.createPlan(PLAN_NAME);
        long taskId = taskTemplateRule.createTask(DELETE_TEST_TASK, 1);
        long prizeId = taskTemplateRule.createTask(DELETE_TEST_PRIZE, 2);
        long interactionId = taskTemplateRule.createTask(DELETE_TEST_INTERACTION, 3);

        planTemplateRepository.setTasksWithThisPlan(planId, taskId);
        planTemplateRepository.setTasksWithThisPlan(planId, prizeId);
        planTemplateRepository.setTasksWithThisPlan(planId, interactionId);

        taskTemplateRule.createTask(expectedNameTask, 1);
        taskTemplateRule.createTask(expectedNamePrize, 2);
        taskTemplateRule.createTask(expectedNameInteraction, 3);
    }

    public void setView (Integer typeId) {
        PlanTaskListFragment fragment = new PlanTaskListFragment();
        Bundle args = new Bundle();
        args.putLong(PLAN_ID, planId);
        args.putInt(TYPE_ID, typeId);
        fragment.setArguments(args);

        FragmentManager manager = activityRule.getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.plan_container, fragment);
        transaction.commit();
    }

    @Test
    public void whenPlanTaskListFragmentExpectHeaderAndButtons() {
        setView(1);
        onView(withId(R.id.id_tv_plan_tasks_list_info))
                .check(matches(withText(R.string.create_plan_tasks_list_info_type_1)));
        onView(withId(R.id.id_btn_create_plan_add_tasks))
                .check(matches(withText(R.string.create_plan_add_tasks_type_1)));
    }

    @Test
    public void whenAddingNewTaskToPlanExpectShowTaskOnList() {
        checkShowTaskOnList(1, expectedNameTask);
    }

    @Test
    public void whenRemoveIconClickedExpectTaskToNoLongerBeInThisPlan(){
        checkIsItemRemoved(1);
    }

    @Test
    public void whenPlanPrizeListFragmentExpectHeaderAndButtons() {
        setView(2);
        onView(withId(R.id.id_tv_plan_tasks_list_info))
                .check(matches(withText(R.string.create_plan_tasks_list_info_type_2)));
        onView(withId(R.id.id_btn_create_plan_add_tasks))
                .check(matches(withText(R.string.create_plan_add_tasks_type_2)));
}

    @Test
    public void whenAddingNewPrizeToPlanExpectShowPrizeOnList() {
        checkShowTaskOnList(2, expectedNamePrize);
    }

    @Test
    public void whenRemoveIconClickedExpectPrizeToNoLongerBeInThisPlan(){
        checkIsItemRemoved(2);
    }

    @Test
    public void whenPlanInteractionListFragmentExpectHeaderAndButtons() {
        setView(3);
        onView(withId(R.id.id_tv_plan_tasks_list_info))
                .check(matches(withText(R.string.create_plan_tasks_list_info_type_3)));
        onView(withId(R.id.id_btn_create_plan_add_tasks))
                .check(matches(withText(R.string.create_plan_add_tasks_type_3)));
    }

    @Test
    public void whenAddingNewInteractionToPlanExpectShowInteractionOnList() {
        checkShowTaskOnList(3, expectedNameInteraction);
    }

    @Test
    public void whenRemoveIconClickedExpectInteractionToNoLongerBeInThisPlan(){
        checkIsItemRemoved(3);
    }

    public void checkIsItemRemoved (Integer typeId) {
        setView(typeId);
        final int testedInteractionPosition = 0;

        onView(withId(R.id.rv_create_plan_task_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(testedInteractionPosition,
                                (ViewAction) new ViewClicker(R.id.id_remove_task)));

        onView(withId(R.id.rv_create_plan_task_list)).perform(
                RecyclerViewActions.scrollToPosition(testedInteractionPosition));

        onView(withRecyclerView(R.id.rv_create_plan_task_list)
                .atPosition(testedInteractionPosition)).check(doesNotExist());
    }

    public void checkShowTaskOnList (Integer typeId, String EXPECTED_NAME) {
        setView(typeId);
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
                .check(matches(hasDescendant(withText(EXPECTED_NAME))));
    }
}
