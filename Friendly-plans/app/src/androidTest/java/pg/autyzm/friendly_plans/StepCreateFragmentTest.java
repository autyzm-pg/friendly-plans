package pg.autyzm.friendly_plans;


import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.entities.Asset;
import database.entities.StepTemplate;
import database.repository.AssetRepository;
import database.repository.StepTemplateRepository;
import database.repository.TaskTemplateRepository;
import pg.autyzm.friendly_plans.resource.AssetTestRule;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.view.step_create.StepCreateFragment;
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
import static junit.framework.Assert.assertNull;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
public class StepCreateFragmentTest {

    private static final String EXPECTED_NAME = "TEST STEP";
    private static final String TASK_EXPECTED_NAME = "TEST TASK";
    private static final String TASK_EXPECTED_DURATION_TXT = "1";

    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";
    
    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<TaskCreateActivity> activityRule = new ActivityTestRule<TaskCreateActivity>(
            TaskCreateActivity.class, true, true);

    @Rule
    public AssetTestRule assetTestRule = new AssetTestRule(daoSessionResource, activityRule);

    private AssetRepository assetRepository;
    private StepTemplateRepository stepTemplateRepository;
    private TaskTemplateRepository taskTemplateRepository;

    private List<Long> stepIdsToDelete = new ArrayList<>();
    
    private Long taskId;

    @Before
    public void setUp() {
        Context context = activityRule.getActivity().getApplicationContext();
        stepTemplateRepository = new StepTemplateRepository(daoSessionResource.getSession(context));
        taskTemplateRepository = new TaskTemplateRepository(daoSessionResource.getSession(context));
        assetRepository = new AssetRepository(daoSessionResource.getSession(context));

        taskId = taskTemplateRepository.create(TASK_EXPECTED_NAME,
                Integer.valueOf(TASK_EXPECTED_DURATION_TXT),
                null,
                null);

        StepCreateFragment fragment = new StepCreateFragment();
        Bundle args = new Bundle();
        args.putLong(ActivityProperties.TASK_ID, taskId);
        fragment.setArguments(args);
        FragmentTransaction transaction = activityRule.getActivity().getFragmentManager().beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
        taskTemplateRepository.delete(taskId);
    }

    @Test
    public void whenStepCreateFragmentDisplayedExpectHeaderAndEmptyFields() {
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
    public void whenAddingANewStepWithoutSoundExpectStepToBeAddedWithoutSound()
            throws IOException, InterruptedException {
        onView(withId(R.id.id_et_step_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();


        onView(withId(R.id.id_btn_save_step))
                .perform(click());

        List<StepTemplate> stepTemplates = stepTemplateRepository.get(EXPECTED_NAME);
        storeStepsToDelete(stepTemplates);

        assertThat(stepTemplates.size(), is(1));
        assertNull(stepTemplates.get(0).getSoundId());
        assertThat(stepTemplates.get(0).getName(), is(EXPECTED_NAME));
    }

    @Test
    public void whenAddingANewStepWithSoundExpectStepToBeAddedWithSound()
            throws IOException, InterruptedException {
        onView(withId(R.id.id_et_step_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        assetTestRule.setTestSound();

        onView(withId(R.id.id_btn_save_step))
                .perform(click());

        List<Asset> assets = assetRepository.getAll();
        List<StepTemplate> stepTemplates = stepTemplateRepository.get(EXPECTED_NAME);
        storeStepsToDelete(stepTemplates);

        assertThat(stepTemplates.size(), is(1));
        assertThat(stepTemplates.get(0).getSoundId(), is(assets.get(0).getId()));
    }

    @Test
    public void whenAddingNewStepWithPictureExpectNewStepAddedToDB() throws IOException, InterruptedException {
        onView(withId(R.id.id_et_step_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        assetTestRule.setTestPicture();

        onView(withId(R.id.id_btn_save_step))
                .perform(click());

        List<Asset> assets = assetRepository.getAll();
        List<StepTemplate> stepTemplates = stepTemplateRepository.get(EXPECTED_NAME);
        storeStepsToDelete(stepTemplates);

        assertThat(assets.size(), is(1));
        assertThat(stepTemplates.size(), is(1));
        assertThat(stepTemplates.get(0).getName(), is(EXPECTED_NAME));
        assertThat(stepTemplates.get(0).getPictureId(), is(assets.get(0).getId()));
    }

    @Ignore
    @Test
    public void whenSettingSoundExpectSoundNameAndBtnsAreDisplayed()
             throws IOException, InterruptedException {
        assetTestRule.setTestSound();
        List<Asset> assets = assetRepository.getAll();

        String fileName = (assets.get(0).getFilename()).replaceAll(REGEX_TRIM_NAME, "");

        closeSoftKeyboard();

        onView(withId(R.id.id_et_step_sound))
            .check(matches(withText(fileName)));

        onView(withId(R.id.id_btn_play_step_sound))
                .check(matches(isDisplayed()));
        onView(withId(R.id.id_ib_clear_step_sound_btn))
                .check(matches(isDisplayed()));
    }

    @Test
    public void whenSoundCrossBtnIsPressedPlayAndCrossBtnsAreNotDisplayed()
            throws IOException, InterruptedException {

        assetTestRule.setTestSound();

        closeSoftKeyboard();

        onView(withId(R.id.id_ib_clear_step_sound_btn))
                .perform(click());

        onView(withId(R.id.id_btn_play_step_sound))
                .check(matches(not(isDisplayed())));
        onView(withId(R.id.id_ib_clear_step_sound_btn))
                .check(matches(not(isDisplayed())));
    }


    private void storeStepsToDelete(List<StepTemplate> stepTemplates){
        for (StepTemplate storedStep : stepTemplates) {
            stepIdsToDelete.add(storedStep.getId());
        }
    }
}
