package pg.autyzm.friendly_plans.manager_app;

import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pg.autyzm.friendly_plans.R;
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

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<TaskListActivity> activityRule = new ActivityTestRule<>(
            TaskListActivity.class, true, true);

    @Rule
    public TaskTemplateRule taskTemplateRule = new TaskTemplateRule(daoSessionResource,
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
        final int testedTaskPosition = 4;
        onView(withId(R.id.id_btn_list_of_tasks)).perform(click());
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedTaskPosition));
        onView(withRecyclerView(R.id.rv_task_list)
                .atPosition(testedTaskPosition))
                .check(matches(hasDescendant(withText(expectedNameTask
                        + testedTaskPosition))));
    }

    @Test
    public void whenPrizeIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {
        final int testedTaskPosition = 4;
        onView(withId(R.id.id_btn_list_of_prizes)).perform(click());
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedTaskPosition));
        onView(withRecyclerView(R.id.rv_task_list)
                .atPosition(testedTaskPosition))
                .check(matches(hasDescendant(withText(expectedNamePrize
                        + testedTaskPosition))));
    }

    @Test
    public void whenInteractionIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {
        final int testedTaskPosition = 4;
        onView(withId(R.id.id_btn_list_of_interactions)).perform(click());
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
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedTaskPosition));
        onView(withRecyclerView(R.id.rv_task_list)
                .atPosition(testedTaskPosition))
                .check(matches(not(hasDescendant(withText(expectedNameTask
                        + testedTaskPosition)))));
    }
}
