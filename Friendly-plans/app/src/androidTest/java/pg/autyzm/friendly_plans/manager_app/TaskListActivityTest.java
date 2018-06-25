package pg.autyzm.friendly_plans.manager_app;

import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.resource.ChildViewClicker;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.manager_app.view.task_list.TaskListActivity;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
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

    private static final String expectedName = "TEST TASK ";
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
        final int numberOfTasks = 10;
        for (int taskNumber = 0; taskNumber < numberOfTasks; taskNumber++) {
            taskTemplateRule.createTask(expectedName + taskNumber);
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
        final int testedTaskPosition = 5;
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedTaskPosition));
        onView(withRecyclerView(R.id.rv_task_list)
                .atPosition(testedTaskPosition))
                .check(matches(hasDescendant(withText(expectedName
                        + testedTaskPosition))));
    }

    @Test
    public void whenTaskIsRemovedExpectTaskIsNotOnTheList() {
        final int testedTaskPosition = 3;
        onView(withId(R.id.rv_task_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(testedTaskPosition,
                                new ChildViewClicker(R.id.id_remove_task)));
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedTaskPosition));
        onView(withRecyclerView(R.id.rv_task_list)
                .atPosition(testedTaskPosition))
                .check(matches(not(hasDescendant(withText(expectedName
                        + testedTaskPosition)))));
    }
}
