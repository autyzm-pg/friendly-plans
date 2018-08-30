package pg.autyzm.friendly_plans.manager_app;

import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.resource.PlanTemplateRule;
import pg.autyzm.friendly_plans.view_actions.ViewClicker;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.manager_app.view.task_list.TaskListActivity;
import pg.autyzm.friendly_plans.resource.TaskTemplateRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static pg.autyzm.friendly_plans.matcher.RecyclerViewMatcher.withRecyclerView;

@RunWith(AndroidJUnit4.class)
public class TaskListActivityTest {

    private static final String expectedNameTask = "TEST TASK ";
    private static final String expectedNamePrize = "TEST PRIZE ";
    private static final String expectedNameInteraction = "TEST INTERACTION ";
    private static final String PLAN_WITH_TASK_NAME = "PLAN WITH TASK";
    private static final String TASK_THAT_IS_ADDED_TO_PLAN_NAME = "TASK IN PLAN";

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<TaskListActivity> activityRule = new ActivityTestRule<>(
            TaskListActivity.class, true, true);

    @Rule
    public TaskTemplateRule taskTemplateRule = new TaskTemplateRule(daoSessionResource,
            activityRule);

    @Rule
    public PlanTemplateRule planTemplateRule = new PlanTemplateRule(daoSessionResource,
            activityRule);

    @Before
    public void setUp() {
        final int numberOfTasks = 5;
        taskTemplateRule.deleteAll();

        for (int taskNumber = 0; taskNumber < numberOfTasks; taskNumber++) {
            taskTemplateRule
                    .createTask(expectedNameTask + taskNumber, 1);
            taskTemplateRule
                    .createTask(expectedNamePrize + taskNumber, 2);
            taskTemplateRule
                    .createTask(expectedNameInteraction + taskNumber, 3);
        }

        Context context = activityRule.getActivity().getApplicationContext();
        PlanTemplateRepository planTemplateRepository = new PlanTemplateRepository(
                daoSessionResource.getSession(context));
        long taskId = taskTemplateRule.createTask(TASK_THAT_IS_ADDED_TO_PLAN_NAME, 1);
        long planId = planTemplateRule.createPlan(PLAN_WITH_TASK_NAME);
        planTemplateRepository.setTasksWithThisPlan(planId, taskId);

        activityRule.launchActivity(new Intent());
    }

    @Test
    public void checkIfItemsAreClickable() {
        final int testedTaskPosition = 3;
        onView(withId(R.id.rv_task_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(testedTaskPosition, click()));
    }

    @Test
    public void whenTaskIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {
        final int testedTaskPosition = 3;
        onView(withId(R.id.id_btn_list_of_tasks))
                .perform(click());
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedTaskPosition));
        onView(withRecyclerView(R.id.rv_task_list)
                .atPosition(testedTaskPosition))
                .check(matches(hasDescendant(withText(expectedNameTask
                        + testedTaskPosition))));
    }

    @Test
    public void whenPrizeIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {
        final int testedTaskPosition = 3;
        onView(withId(R.id.id_btn_list_of_prizes))
                .perform(click());
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedTaskPosition));
        onView(withRecyclerView(R.id.rv_task_list)
                .atPosition(testedTaskPosition))
                .check(matches(hasDescendant(withText(expectedNamePrize
                        + testedTaskPosition))));
    }

    @Test
    public void whenInteractionIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {
        final int testedTaskPosition = 3;
        onView(withId(R.id.id_btn_list_of_interactions))
                .perform(click());
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedTaskPosition));
        onView(withRecyclerView(R.id.rv_task_list)
                .atPosition(testedTaskPosition))
                .check(matches(hasDescendant(withText(expectedNameInteraction
                        + testedTaskPosition))));
    }

    @Test
    public void whenTaskIsRemovedExpectTaskIsNotOnTheList() {
        final int testedTaskPosition = 3;
        onView(withId(R.id.rv_task_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(testedTaskPosition,
                                new ViewClicker(R.id.id_remove_task)));
        onView(withText(R.string.task_removal_confirmation_positive_button)).perform(click());
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedTaskPosition));
        onView(withRecyclerView(R.id.rv_task_list)
                .atPosition(testedTaskPosition))
                .check(matches(not(hasDescendant(withText(expectedNameTask
                        + testedTaskPosition)))));
    }

    @Test
    public void whenTaskWhichIsInAPlanIsRemovedExpectTaskNotRemoved() {
        final int testedTaskPosition = 5;
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedTaskPosition));
        onView(withId(R.id.rv_task_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(testedTaskPosition,
                                new ViewClicker(R.id.id_remove_task)));
        onView(withText(R.string.task_cannot_be_removed_dialog_close_button)).perform(click());
        onView(withRecyclerView(R.id.rv_task_list)
                .atPosition(testedTaskPosition))
                .check(matches(hasDescendant(withText(TASK_THAT_IS_ADDED_TO_PLAN_NAME))));
    }
}
