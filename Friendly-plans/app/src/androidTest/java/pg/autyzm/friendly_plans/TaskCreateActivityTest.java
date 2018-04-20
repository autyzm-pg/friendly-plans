package pg.autyzm.friendly_plans;

import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import database.entities.Asset;
import database.entities.TaskTemplate;
import database.repository.AssetRepository;
import database.repository.TaskTemplateRepository;
import pg.autyzm.friendly_plans.matcher.ToastMatcher;
import pg.autyzm.friendly_plans.resource.AssetTestRule;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateActivity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertNull;
import static org.hamcrest.core.Is.is;
import static pg.autyzm.friendly_plans.matcher.ErrorTextMatcher.hasErrorText;

@RunWith(AndroidJUnit4.class)
public class TaskCreateActivityTest {

    private static final String EXPECTED_NAME = "TEST TASK";
    private static final String EXPECTED_DURATION_TXT = "1";
    private static final int EXPECTED_DURATION = 1;
    private static final String BAD_TASK_NAME = "Bad task name!@";
    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<TaskCreateActivity> activityRule = new ActivityTestRule<>(
            TaskCreateActivity.class, true, true);
    @Rule
    public AssetTestRule assetTestRule = new AssetTestRule(daoSessionResource, activityRule);
    private TaskTemplateRepository taskTemplateRepository;
    private AssetRepository assetRepository;

    private List<Long> idsToDelete = new ArrayList<>();

    @Before
    public void setUp() {
        Context context = activityRule.getActivity().getApplicationContext();
        taskTemplateRepository = new TaskTemplateRepository(daoSessionResource.getSession(context));
        assetRepository = new AssetRepository(daoSessionResource.getSession(context));
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
        for (Long id : idsToDelete) {
            taskTemplateRepository.delete(id);
        }
    }

    @Test
    public void whenTaskCreateActivityExpectHeaderAndEmptyFields() {
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
    public void whenAddingNewTaskExpectNewTaskAddedToDB() {
        onView(withId(R.id.id_et_task_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
                .perform(replaceText(EXPECTED_DURATION_TXT));
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_steps))
                .perform(click());

        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(EXPECTED_NAME);
        idsToDelete.add(taskTemplates.get(0).getId());

        assertThat(taskTemplates.size(), is(1));
        assertThat(taskTemplates.get(0).getName(), is(EXPECTED_NAME));
        assertThat(taskTemplates.get(0).getDurationTime(), is(EXPECTED_DURATION));
        onView(withText(R.string.task_saved_message)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void whenAddingNewTaskAndFinishExpectNewTaskAddedToDB() {
        onView(withId(R.id.id_et_task_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
                .perform(replaceText(EXPECTED_DURATION_TXT));
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_save_and_finish))
                .perform(click());

        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(EXPECTED_NAME);
        idsToDelete.add(taskTemplates.get(0).getId());

        assertThat(taskTemplates.size(), is(1));
        assertThat(taskTemplates.get(0).getName(), is(EXPECTED_NAME));
        assertThat(taskTemplates.get(0).getDurationTime(), is(EXPECTED_DURATION));
        onView(withId(R.id.button_createPlan)).check(matches(isDisplayed()));
    }

    @Test
    public void whenSettingPictureExpectPictureNameIsDisplayed()
            throws InterruptedException, IOException {
        assetTestRule.setTestPicture();
        List<Asset> assets = assetRepository.getAll();

        String fileName = (assets.get(0).getFilename()).replaceAll(REGEX_TRIM_NAME, "");
        onView(withId(R.id.id_et_task_picture))
                .check(matches(withText(fileName)));
    }

    @Test
    public void whenSettingSoundExpectSoundNameIsDisplayed()
            throws IOException, InterruptedException {
        assetTestRule.setTestSound();
        List<Asset> assets = assetRepository.getAll();

        String fileName = (assets.get(0).getFilename()).replaceAll(REGEX_TRIM_NAME, "");
        onView(withId(R.id.id_et_task_sound))
                .check(matches(withText(fileName)));
    }

    @Test
    public void whenAddingNewTaskWithPictureExpectNewTaskAddedToDB()
            throws InterruptedException, IOException {
        onView(withId(R.id.id_et_task_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
                .perform(replaceText(EXPECTED_DURATION_TXT));
        closeSoftKeyboard();

        assetTestRule.setTestPicture();

        onView(withId(R.id.id_btn_steps))
                .perform(click());

        List<Asset> assets = assetRepository.getAll();
        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(EXPECTED_NAME);
        idsToDelete.add(taskTemplates.get(0).getId());

        assertThat(assets.size(), is(1));
        assertThat(taskTemplates.size(), is(1));
        assertThat(taskTemplates.get(0).getName(), is(EXPECTED_NAME));
        assertThat(taskTemplates.get(0).getDurationTime(), is(EXPECTED_DURATION));
        assertThat(taskTemplates.get(0).getPictureId(), is(assets.get(0).getId()));
    }

    @Test
    public void whenAddingANewTaskWithoutSoundExpectTaskToBeAddedWithoutSound()
            throws IOException, InterruptedException {
        onView(withId(R.id.id_et_task_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
                .perform(replaceText(EXPECTED_DURATION_TXT));
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_steps))
                .perform(click());

        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(EXPECTED_NAME);
        idsToDelete.add(taskTemplates.get(0).getId());

        assertThat(taskTemplates.size(), is(1));
        assertNull(taskTemplates.get(0).getSoundId());
    }

    @Test
    public void whenAddingANewTaskWithSoundExpectTaskToBeAddedWithSound()
            throws IOException, InterruptedException {
        onView(withId(R.id.id_et_task_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
                .perform(replaceText(EXPECTED_DURATION_TXT));
        closeSoftKeyboard();

        assetTestRule.setTestSound();

        onView(withId(R.id.id_btn_steps))
                .perform(click());

        List<Asset> assets = assetRepository.getAll();
        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(EXPECTED_NAME);
        idsToDelete.add(taskTemplates.get(0).getId());

        assertThat(taskTemplates.size(), is(1));
        assertThat(taskTemplates.get(0).getSoundId(), is(assets.get(0).getId()));
    }

    @Test
    public void whenAddingNewTaskAndNameIsEmptyExpectWarning() {
        closeSoftKeyboard();
        onView(withId(R.id.id_btn_steps))
                .perform(click());

        onView(withId(R.id.id_et_task_name))
                .check(matches(hasErrorText(
                        activityRule.getActivity().getString(R.string.not_empty_msg))));
    }

    @Test
    public void whenAddingNewTaskAndDurationIsEmptyExpectWarning() {
        onView(withId(R.id.id_et_task_name))
                .perform(typeText(EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_steps))
                .perform(scrollTo());

        onView(withId(R.id.id_btn_steps))
                .perform(click());

        onView(withId(R.id.id_et_task_duration_time))
                .check(matches(hasErrorText(
                        activityRule.getActivity().getString(R.string.not_empty_msg))));
    }

    @Test
    public void whenAddingNewTaskAndForbiddenNameExpectWarning() {
        onView(withId(R.id.id_et_task_name))
                .perform(typeText(BAD_TASK_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_steps))
                .perform(scrollTo());

        onView(withId(R.id.id_btn_steps))
                .perform(click());

        onView(withId(R.id.id_et_task_name))
                .check(matches(hasErrorText(
                        activityRule.getActivity().getString(R.string.only_letters_msg))));
    }

    @Test
    public void whenAddTaskWithExistingNameExpectWarning() {
        idsToDelete.add(taskTemplateRepository
                .create(EXPECTED_NAME, Integer.valueOf(EXPECTED_DURATION_TXT), null, null));

        onView(withId(R.id.id_et_task_name))
                .perform(typeText(EXPECTED_NAME));

        closeSoftKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
                .perform(replaceText(EXPECTED_DURATION_TXT))
                .perform(scrollTo());

        closeSoftKeyboard();

        onView(withId(R.id.id_btn_steps))
                .perform(click())
                .perform(scrollTo());

        onView(withId(R.id.id_et_task_name))
                .check(matches(hasErrorText(
                        activityRule.getActivity().getString(R.string.name_exist_msg))));
    }

    @Test
    public void whenFieldsEmptyAndPlayBtnPressedThenToastExpected() {
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_play_sound))
                .perform(click());
        onView(withText(R.string.no_file_to_play_error)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void whenImageSelectedAndPlayBtnPressedThenToastExpected()
            throws IOException, InterruptedException {

        assetTestRule.setTestPicture();
        closeSoftKeyboard();
        onView(withId(R.id.id_btn_play_sound))
                .perform(click());
        closeSoftKeyboard();
        onView(withText(R.string.no_file_to_play_error)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void whenImageSelectedAndCrossBtnPressedAndSaveExpectedSaveTaskWithoutImage()
            throws IOException, InterruptedException {

        onView(withId(R.id.id_et_task_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
                .perform(replaceText(EXPECTED_DURATION_TXT));
        closeSoftKeyboard();

        assetTestRule.setTestPicture();
        closeSoftKeyboard();

        onView(withId(R.id.id_ib_clear_img_btn))
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_steps))
                .perform(click());

        List<Asset> assets = assetRepository.getAll();
        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(EXPECTED_NAME);
        idsToDelete.add(taskTemplates.get(0).getId());

        assertThat(taskTemplates.size(), is(1));
        assertNull(taskTemplates.get(0).getPictureId());
    }

    @Test
    public void whenImageSelectedAndCrossBtnPressedExpectedClearPreviewAndEditText()
            throws IOException, InterruptedException {

        assetTestRule.setTestPicture();
        closeSoftKeyboard();

        onView(withId(R.id.id_ib_clear_img_btn))
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.id_iv_task_picture))
                .check(doesNotExist());

        onView(withId(R.id.id_et_task_picture))
                .check(matches(isDisplayed()));
    }

    @Test
    public void whenSoundSelectedAndCrossBtnPressedAndSaveExpectedSaveTaskWithoutSound()
            throws IOException, InterruptedException {

        onView(withId(R.id.id_et_task_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
                .perform(replaceText(EXPECTED_DURATION_TXT));
        closeSoftKeyboard();

        assetTestRule.setTestSound();
        closeSoftKeyboard();

        onView(withId(R.id.id_ib_clear_sound_btn))
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_steps))
                .perform(click());

        List<Asset> assets = assetRepository.getAll();
        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(EXPECTED_NAME);
        idsToDelete.add(taskTemplates.get(0).getId());

        assertThat(taskTemplates.size(), is(1));
        assertNull(taskTemplates.get(0).getSoundId());
    }

    @Test
    public void whenSoundSelectedAndCrossBtnPressedExpectedClearEditText()
            throws IOException, InterruptedException {

        assetTestRule.setTestSound();
        closeSoftKeyboard();

        onView(withId(R.id.id_ib_clear_sound_btn))
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.id_et_task_sound))
                .check(matches(isDisplayed()));
    }

    @Test
    public void whenSoundSelectedAndCrossBtnPressedAndPlayBtnPressedThenToastExpected()
            throws IOException, InterruptedException {

        assetTestRule.setTestSound();
        closeSoftKeyboard();

        onView(withId(R.id.id_ib_clear_sound_btn))
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_play_sound))
                .perform(click());

        onView(withText(R.string.no_file_to_play_error)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));
    }

    @Test
    public void whenMovingBackFromStepListNewTaskCanBeUpdated() {
        onView(withId(R.id.id_et_task_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
                .perform(replaceText(EXPECTED_DURATION_TXT));
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_steps))
                .perform(click());

        Espresso.pressBack();

        closeSoftKeyboard();

        onView(withId(R.id.id_btn_save_and_finish))
                .perform(click());

        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(EXPECTED_NAME);
        idsToDelete.add(taskTemplates.get(0).getId());

        assertThat(taskTemplates.size(), is(1));
        assertThat(taskTemplates.get(0).getName(), is(EXPECTED_NAME));
        assertThat(taskTemplates.get(0).getDurationTime(), is(EXPECTED_DURATION));
        onView(withId(R.id.button_createPlan)).check(matches(isDisplayed()));
    }
}
