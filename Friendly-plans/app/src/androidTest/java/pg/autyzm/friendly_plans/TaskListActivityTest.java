package pg.autyzm.friendly_plans;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import database.repository.TaskTemplateRepository;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static matcher.RecyclerViewMatcher.withRecyclerView;

@RunWith(AndroidJUnit4.class)
public class TaskListActivityTest {
    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();
    @Rule
    public ActivityTestRule<TaskListActivity> activityRule = new ActivityTestRule<>(
            TaskListActivity.class, true, true);
    private static final String EXPECTED_NAME = "TEST TASK";

    @Before
    public void setUp() {
        int numberOfTasks = 10;
        TaskTemplateRepository taskTemplateRepository = new TaskTemplateRepository(
                daoSessionResource.getSession(activityRule.getActivity().getApplicationContext()));
        for (int taskNumber = 0; taskNumber < numberOfTasks; taskNumber++) {
            taskTemplateRepository
                    .create(EXPECTED_NAME + " " + taskNumber, taskNumber, (long) taskNumber);
        }
        activityRule.launchActivity(new Intent());
    }

    @Test
    public void checkIfItemsAreClickable() {
        Espresso.onView(withId(R.id.rv_task_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
    }

    @Test
    public void checkIfTaskIsAddedToDBAndProperlyDisplayedOnRecyclerView() {
        int testedPosition = 5;
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedPosition));
        onView(withRecyclerView(R.id.rv_task_list)
                .atPosition(testedPosition))
                .check(matches(hasDescendant(withText(EXPECTED_NAME + " " + (testedPosition)))));
    }
}
