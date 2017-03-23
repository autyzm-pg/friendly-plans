package pg.autyzm.friendly_plans;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;

import database.repository.TaskTemplateRepository;
import database.entities.TaskTemplate;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class TaskCreateActivityTest {

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<TaskCreateActivity> activityRule = new ActivityTestRule<>(
        TaskCreateActivity.class, true, true);

    private static final String EXPECTED_NAME = "TEST TASK";

    private TaskTemplateRepository taskTemplateRepository;
    private Long idToDelete;

    @Before
    public void setUp() {
        taskTemplateRepository = new TaskTemplateRepository(
            daoSessionResource.getSession(activityRule.getActivity().getApplicationContext()));
    }

    @Before
    public void unlockScreen() {
        final TaskCreateActivity activity = activityRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }

    @After
    public void tearDown() {
        if (idToDelete != null) {
            taskTemplateRepository.delete(idToDelete);
        }
    }

    @Test
    public void When_TaskCreateActivity_Expect_HeaderAndEmptyFields() {
        onView(withId(R.id.id_task_create_description))
            .check(matches(withText(R.string.task_create_description)));
        onView(withId(R.id.id_et_task_name))
            .check(matches(withText("")));
        onView(withId(R.id.id_et_task_picture))
            .check(matches(withText("")));
        onView(withId(R.id.id_et_task_sound))
            .check(matches(withText("")));
        onView(withId(R.id.id_et_task_duration_time))
            .check(matches(withText("")));
    }

    @Test
    public void When_AddingNewTask_Expect_NewTaskAddedToDB() throws InterruptedException {

        onView(withId(R.id.id_et_task_name))
            .perform(replaceText(EXPECTED_NAME));
        closeKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
            .perform(replaceText("1"));
        closeKeyboard();

        onView(withId(R.id.id_btn_task_next))
            .perform(click());

        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(EXPECTED_NAME);
        idToDelete = taskTemplates.get(0).getId();

        assertThat(taskTemplates.size(), is(1));
        assertThat(taskTemplates.get(0).getName(), is(EXPECTED_NAME));
        assertThat(taskTemplates.get(0).getDurationTime(), is(1));
    }

    private void closeKeyboard() throws InterruptedException {
        closeSoftKeyboard();
        Thread.sleep(1000);
    }
}
