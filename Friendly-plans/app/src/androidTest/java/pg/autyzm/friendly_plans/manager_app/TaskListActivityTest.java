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

import database.repository.TaskTemplateRepository;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.manager_app.view.task_list.TaskListActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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

    @Before
    public void setUp() {
        final int numberOfTasks = 5;
        TaskTemplateRepository taskTemplateRepository = new TaskTemplateRepository(
                daoSessionResource.getSession(activityRule.getActivity().getApplicationContext()));
        taskTemplateRepository.deleteAll();
        for (int taskNumber = 0; taskNumber < numberOfTasks; taskNumber++) {
            taskTemplateRepository
                    .create(expectedNameTask + taskNumber,
                            taskNumber, (long) taskNumber,
                            (long) taskNumber,
                            1);
            taskTemplateRepository
                    .create(expectedNamePrize + taskNumber,
                            taskNumber, (long) taskNumber,
                            (long) taskNumber,
                            2);
            taskTemplateRepository
                    .create(expectedNameInteraction + taskNumber,
                            taskNumber, (long) taskNumber,
                            (long) taskNumber,
                            3);
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
}
