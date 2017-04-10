package pg.autyzm.friendly_plans;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import database.entities.TaskTemplate;
import database.repository.TaskTemplateRepository;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static matcher.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.CoreMatchers.is;

@RunWith(AndroidJUnit4.class)
public class TaskListActivityTest {
    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();
    @Rule
    public ActivityTestRule<TaskListActivity> activityRule = new ActivityTestRule<>(
            TaskListActivity.class, true, true);
    private TaskTemplateRepository taskTemplateRepository;
    private static final String EXPECTED_NAME = "TEST TASK";
    private List<Long> testTasks;

    @Before
    public void setUp() {
        taskTemplateRepository = new TaskTemplateRepository(
                daoSessionResource.getSession(activityRule.getActivity().getApplicationContext()));
        long id;
        testTasks = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            id = taskTemplateRepository.create(EXPECTED_NAME + " " + i, i, 1l);
            testTasks.add(id);
        }
        activityRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() {
        if (testTasks != null && testTasks.size() != 0) {
            for (long idToDelete : testTasks) {
                taskTemplateRepository.delete(idToDelete);
            }
        }
        testTasks = null;
    }

    @Test
    public void checkIfTasksAreAddedToDB() {
        int numberOfTestTasksAdded = 10;
        List<TaskTemplate> testTasks = taskTemplateRepository.getAll();
        for (int i = 0; i < 10; i++) {
            assertThat(testTasks.get(i).getName(), is(EXPECTED_NAME + " " + i));
        }
        assertThat(testTasks.size(), is(numberOfTestTasksAdded));
    }

    @Test
    public void checkIfItemsAreClickable() {
        Espresso.onView(withId(R.id.rv_task_list)).perform(RecyclerViewActions.actionOnItemAtPosition(3, click()));
    }

    @Test
    public void checkIfTaskIsAddedToDBAndProperlyDisplayedOnRecyclerView() {

        int testedPosition = 5;
        onView(withId(R.id.rv_task_list)).perform(scrollToPosition(testedPosition));
        onView(withRecyclerView(R.id.rv_task_list).atPosition(testedPosition)).check(matches(hasDescendant(withText(EXPECTED_NAME + " " + (testedPosition)))));
    }
}
