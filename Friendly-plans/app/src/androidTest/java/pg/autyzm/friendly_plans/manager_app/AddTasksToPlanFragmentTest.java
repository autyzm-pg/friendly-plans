package pg.autyzm.friendly_plans.manager_app;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;
import static pg.autyzm.friendly_plans.manager_app.PlanCreateActivityTest.daoSessionResource;
import static pg.autyzm.friendly_plans.matcher.RecyclerViewMatcher.withRecyclerView;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import database.entities.TaskTemplate;
import database.repository.PlanTemplateRepository;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.manager_app.view.plan_create.PlanCreateActivity;
import pg.autyzm.friendly_plans.manager_app.view.plan_create_add_tasks.AddTasksToPlanFragment;
import pg.autyzm.friendly_plans.resource.PlanTemplateRule;
import pg.autyzm.friendly_plans.resource.TaskTemplateRule;


@RunWith(AndroidJUnit4.class)
public class AddTasksToPlanFragmentTest {

    private final int numberOfTasks = 6;
    private static final String expectedNameTask = "TEST TASK ";
    private static final String expectedNamePrize = "TEST PRIZE ";
    private static final String expectedNameInteraction = "TEST INTERACTION ";
    private static final String PLAN_ID = "PLAN_ID";
    private static final String TYPE_ID = "TYPE_ID";
    private static final String PLAN_NAME = "PLAN NAME";

    private PlanTemplateRepository planTemplateRepository;
    private Long planId;

    @Rule
    public ActivityTestRule<PlanCreateActivity> activityRule = new ActivityTestRule<>(
            PlanCreateActivity.class, true, true);

    @Rule
    public TaskTemplateRule taskTemplateRule = new TaskTemplateRule(daoSessionResource,
            activityRule);

    @Rule
    public PlanTemplateRule planTemplateRule = new PlanTemplateRule(daoSessionResource,
            activityRule);

    @Before
    public void setUp() {
        for (int taskNumber = 0; taskNumber < numberOfTasks; taskNumber++) {
            taskTemplateRule
                    .createTask(expectedNameTask + taskNumber, 1);
            taskTemplateRule
                    .createTask(expectedNamePrize + taskNumber, 2);
            taskTemplateRule
                    .createTask(expectedNameInteraction + taskNumber, 3);
        }

        Context context = activityRule.getActivity().getApplicationContext();
        planTemplateRepository = new PlanTemplateRepository(
                daoSessionResource.getSession(context));
        planId = planTemplateRule.createPlan(PLAN_NAME);
    }

    @After
    public void deleteTasks () {
        taskTemplateRule.deleteAll();
    }

    public void setView (Integer typeId) {
        AddTasksToPlanFragment fragment = new AddTasksToPlanFragment();
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
    public void whenAddTasksToPlanFragmentExpectHeaderAndButton() {
        setView(1);
        onView(withId(R.id.id_tv_add_tasks_to_plan_info))
                .check(matches(withText(R.string.create_plan_add_tasks_info_type_1)));
        onView(withId(R.id.id_btn_add_tasks_to_plan)).check(matches(isDisplayed()));
    }

    @Test
    public void whenAddPrizesToPlanFragmentExpectHeaderAndButton() {
        setView(2);
        onView(withId(R.id.id_tv_add_tasks_to_plan_info))
                .check(matches(withText(R.string.create_plan_add_tasks_info_type_2)));
        onView(withId(R.id.id_btn_add_tasks_to_plan)).check(matches(isDisplayed()));
    }

    @Test
    public void whenAddInteractionsToPlanFragmentExpectHeaderAndButton() {
        setView(3);
        onView(withId(R.id.id_tv_add_tasks_to_plan_info))
                .check(matches(withText(R.string.create_plan_add_tasks_info_type_3)));
        onView(withId(R.id.id_btn_add_tasks_to_plan)).check(matches(isDisplayed()));
    }

    @Test
    public void whenTaskIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {
        checkListView(1, expectedNameTask);
    }

    @Test
    public void whenPrizeIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {
        checkListView(2, expectedNamePrize);
    }

    @Test
    public void whenInteractionIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {
        checkListView(3, expectedNameInteraction);
    }

    @Test
    public void whenTaskPositionIsClickedExpectAddedTaskToPlan() {
        checkIsAddedToPlan(1, expectedNameTask);
    }

    @Test
    public void whenPrizePositionIsClickedExpectAddedTaskToPlan() {
        checkIsAddedToPlan(2, expectedNamePrize);
    }

    @Test
    public void whenInteractionPositionIsClickedExpectAddedTaskToPlan() {
        checkIsAddedToPlan(3, expectedNameInteraction);
    }

    public void checkListView (Integer typeId, String EXPECTED_NAME) {
        setView(typeId);
        final int testedTaskPosition = 3;

        onView(withId(R.id.rv_create_plan_add_tasks)).perform(scrollToPosition(testedTaskPosition));
        onView(withRecyclerView(R.id.rv_create_plan_add_tasks)
                .atPosition(testedTaskPosition))
                .check(matches(hasDescendant(withText(EXPECTED_NAME
                        + testedTaskPosition))));
    }

    public void checkIsAddedToPlan (Integer typeId, String EXPECTED_NAME){
        setView(typeId);
        final int testedTaskPosition = 3;

        onView(withId(R.id.rv_create_plan_add_tasks)).perform(scrollToPosition(testedTaskPosition));
        onView(withId(R.id.rv_create_plan_add_tasks))
                .perform(RecyclerViewActions.actionOnItemAtPosition(testedTaskPosition, click()));
        List<TaskTemplate> taskTemplates = planTemplateRepository.getTaskWithThisPlanByTypeId(planId, typeId);

        assertThat(taskTemplates.size(), is(1));
        assertThat(taskTemplates.get(0).getName(), is(EXPECTED_NAME + testedTaskPosition));
    }

}
