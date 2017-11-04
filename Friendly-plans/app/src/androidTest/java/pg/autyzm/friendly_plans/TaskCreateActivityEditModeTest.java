package pg.autyzm.friendly_plans;


import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import database.repository.TaskTemplateRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateActivity;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateFragment;

public class TaskCreateActivityEditModeTest {

    private static final String TASK_NAME = "TEST TASK";
    private static final String TASK_DURATION_TXT = "3";
    private static final int TASK_DURATION = 3;

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<TaskCreateActivity> activityRule = new ActivityTestRule<>(
        TaskCreateActivity.class, true, true);

    private TaskTemplateRepository taskTemplateRepository;
    private List<Long> idsToDelete = new ArrayList<>();
    private Long editTaskId;

    @Before
    public void setUp() {
        Context context = activityRule.getActivity().getApplicationContext();
        taskTemplateRepository = new TaskTemplateRepository(daoSessionResource.getSession(context));
        editTaskId = taskTemplateRepository.create(TASK_NAME, TASK_DURATION, null, null);
        idsToDelete.add(editTaskId);

    }

    @After
    public void tearDown() {
        for(Long id : idsToDelete) {
            taskTemplateRepository.delete(id);
        }
    }

    @Test
    public void whenOpenedWithTaskIdExpectFormFillWithTaskData() {
        openCreateTaskFragmentWithTaskId();
        onView(withId(R.id.id_et_task_name))
            .check(matches(withText(TASK_NAME)));
        onView(withId(R.id.id_et_task_picture))
            .check(matches(withText("")));
        onView(withId(R.id.id_et_task_sound))
            .check(matches(withText("")));
        onView(withId(R.id.id_et_task_duration_time))
            .check(matches(withText(TASK_DURATION_TXT)));
    }

    private void openCreateTaskFragmentWithTaskId() {
        TaskCreateFragment fragment = new TaskCreateFragment();
        Bundle args = new Bundle();
        args.putLong(ActivityProperties.TASK_ID, editTaskId);
        fragment.setArguments(args);

        FragmentManager manager = activityRule.getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.commit();
    }

}
