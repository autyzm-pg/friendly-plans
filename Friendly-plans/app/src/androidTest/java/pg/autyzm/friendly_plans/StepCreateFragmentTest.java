package pg.autyzm.friendly_plans;


import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import database.entities.StepTemplate;
import database.entities.TaskTemplate;
import database.repository.StepTemplateRepository;
import database.repository.TaskTemplateRepository;
import pg.autyzm.friendly_plans.matcher.ToastMatcher;
import pg.autyzm.friendly_plans.resource.AssetTestRule;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateActivity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;

<<<<<<< HEAD
@RunWith(AndroidJUnit4.class)
public class StepCreateFragmentTest {

    private static final String EXPECTED_NAME = "TEST STEP 11";
//    private static final String EXPECTED_NAME_OF_PICTURE = "TEST PICTURE";
//    private static final String EXPECTED_NAME_OF_SOUND = "TEST SOUND";
    private static final String TASK_EXPECTED_NAME = "TEST TASK 100";
    private static final String TASK_EXPECTED_DURATION_TXT = "1";
=======
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;
import database.entities.Asset;
import database.entities.StepTemplate;
import database.repository.AssetRepository;
import database.repository.StepTemplateRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pg.autyzm.friendly_plans.matcher.ToastMatcher;
import pg.autyzm.friendly_plans.resource.AssetTestRule;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.view.step_create.StepCreateData;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateActivity;
import pg.autyzm.friendly_plans.view.step_create.StepCreateFragment;

@RunWith(AndroidJUnit4.class)
public class StepCreateFragmentTest {

    private static final String EXPECTED_NAME = "TEST STEP";
    private static final String EXPECTED_NAME_OF_PICTURE = "TEST PICTURE";
    private static final String EXPECTED_NAME_OF_SOUND = "TEST SOUND";
    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";
>>>>>>> 7dcd184... #132: Tests for add picture to step

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<TaskCreateActivity> activityRule = new ActivityTestRule<TaskCreateActivity>(
            TaskCreateActivity.class, true, true);

    @Rule
    public AssetTestRule assetTestRule = new AssetTestRule(daoSessionResource, activityRule);

    private StepTemplateRepository stepTemplateRepository;
<<<<<<< HEAD
    private TaskTemplateRepository taskTemplateRepository;

    private List<Long> taskIdsToDelete = new ArrayList<>();
    private List<Long> stepIdsToDelete = new ArrayList<>();
=======
    public AssetTestRule assetTestRule = new AssetTestRule(daoSessionResource, activityRule);
    private AssetRepository assetRepository;
    private List<Long> idsToDelete = new ArrayList<>();
>>>>>>> 7dcd184... #132: Tests for add picture to step

    @Before
    public void setUp() {
        Context context = activityRule.getActivity().getApplicationContext();
        stepTemplateRepository = new StepTemplateRepository(daoSessionResource.getSession(context));
        taskTemplateRepository = new TaskTemplateRepository(daoSessionResource.getSession(context));
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
        for (Long id : stepIdsToDelete) {
            stepTemplateRepository.delete(id);
        }
        for (Long id : taskIdsToDelete) {
            taskTemplateRepository.delete(id);
        }
    }

    @Test
    public void whenStepCreateFragmentDisplayedExpectHeaderAndEmptyFields() {
        openStepCreate();
        onView(withId(R.id.id_step_create_description))
                .check(matches(withText(R.string.create_step_description)));
        onView(withId(R.id.id_et_step_name))
                .check(matches(withText("")));
        onView(withId(R.id.id_et_step_picture))
                .check(matches(withText("")));
        onView(withId(R.id.id_et_step_sound))
                .check(matches(withText("")));
    }

    @Test
    public void whenAddingNewStepExpectNewStepAddedToDB() {

        openStepCreate();

        onView(withId(R.id.id_et_step_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_save_step))
                .perform(click());

        List<StepTemplate> stepTemplates = stepTemplateRepository.get(EXPECTED_NAME);
        stepIdsToDelete.add(stepTemplates.get(0).getId());

        assertThat(stepTemplates.size(), is(1));
        assertThat(stepTemplates.get(0).getName(), is(EXPECTED_NAME));
        onView(withText(R.string.step_saved_message)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }
    @Test
    public void whenSettingPictureExpectPictureNameIsDisplayed()
            throws InterruptedException, IOException {
        assetTestRule.setTestPicture();
        List<Asset> assets = assetRepository.getAll();

        String fileName = (assets.get(0).getFilename()).replaceAll(REGEX_TRIM_NAME, "");
        onView(withId(R.id.id_et_step_picture))
                .check(matches(withText(fileName)));
    }
    @Test
    public void whenAddingNewTaskWithPictureExpectNewTaskAddedToDB()
            throws InterruptedException, IOException {
        onView(withId(R.id.id_et_step_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        assetTestRule.setTestPicture();

        onView(withId(R.id.id_btn_save_step))
                .perform(click());

        List<Asset> assets = assetRepository.getAll();
        List<StepTemplate> stepTemplates = stepTemplateRepository.get(EXPECTED_NAME);
        idsToDelete.add(stepTemplates.get(0).getId());

        assertThat(assets.size(), is(1));
        assertThat(stepTemplates.size(), is(1));
        assertThat(stepTemplates.get(0).getName(), is(EXPECTED_NAME));
        assertThat(stepTemplates.get(0).getPictureId(), is(assets.get(0).getId()));
        onView(withText(R.string.step_saved_message)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    private void openStepCreate(){
        onView(withId(R.id.id_et_task_name))
                .perform(replaceText(TASK_EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
                .perform(replaceText(TASK_EXPECTED_DURATION_TXT));
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_steps))
                .perform(click());

        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(TASK_EXPECTED_NAME);
        taskIdsToDelete.add(taskTemplates.get(0).getId());

        onView(withId(R.id.id_btn_create_step))
                .perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
