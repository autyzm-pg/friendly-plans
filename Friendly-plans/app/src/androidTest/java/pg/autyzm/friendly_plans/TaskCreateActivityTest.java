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

import android.content.Context;
import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import database.entities.Asset;
import database.entities.TaskTemplate;
import database.repository.AssetRepository;
import database.repository.TaskTemplateRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;

@RunWith(AndroidJUnit4.class)
public class TaskCreateActivityTest {

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<TaskCreateActivity> activityRule = new ActivityTestRule<>(
            TaskCreateActivity.class, true, true);

    private static final String EXPECTED_NAME = "TEST TASK";
    private static final String TEST_PICTURE_NAME = "picture.jpg";

    private TaskTemplateRepository taskTemplateRepository;
    private AssetRepository assetRepository;

    private File internalStorage;
    private Long idToDelete;
    private List<File> testFiles;

    @Before
    public void setUp() {
        testFiles = new ArrayList<>();
        Context context = activityRule.getActivity().getApplicationContext();
        taskTemplateRepository = new TaskTemplateRepository(daoSessionResource.getSession(context));
        assetRepository = new AssetRepository(daoSessionResource.getSession(context));
        internalStorage = context.getFilesDir();
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
        removeTestFiles();
        removeTestAssets();
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

    @Test
    public void When_SettingPicture_Expect_PictureNameIsDisplayed()
            throws InterruptedException, IOException {
        setTestPicture();
        List<Asset> assets = assetRepository.getAll();

        onView(withId(R.id.id_et_task_picture))
                .check(matches(withText(assets.get(0).getFilename())));
    }

    @Test
    public void When_AddingNewTaskWithPicture_Expect_NewTaskAddedToDB()
            throws InterruptedException, IOException {
        onView(withId(R.id.id_et_task_name))
                .perform(replaceText(EXPECTED_NAME));
        closeKeyboard();

        onView(withId(R.id.id_et_task_duration_time))
                .perform(replaceText("1"));
        closeKeyboard();

        setTestPicture();

        onView(withId(R.id.id_btn_task_next))
                .perform(click());

        List<Asset> assets = assetRepository.getAll();
        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(EXPECTED_NAME);
        idToDelete = taskTemplates.get(0).getId();

        assertThat(assets.size(), is(1));
        assertThat(taskTemplates.size(), is(1));
        assertThat(taskTemplates.get(0).getName(), is(EXPECTED_NAME));
        assertThat(taskTemplates.get(0).getDurationTime(), is(1));
        assertThat(taskTemplates.get(0).getPictureId(), is(assets.get(0).getId()));
    }

    private void closeKeyboard() throws InterruptedException {
        closeSoftKeyboard();
        Thread.sleep(1000);
    }

    private void setTestPicture() throws IOException, InterruptedException {
        File testPicture = createTestPicture();
        chooseTestPicture(testPicture);
        addSafeCopyToTestFiles();
    }

    private File createTestPicture() throws IOException {
        File testPicture = new File(internalStorage, TEST_PICTURE_NAME);
        FileUtils.writeStringToFile(testPicture, "Test");
        testFiles.add(testPicture);
        return testPicture;
    }

    private void chooseTestPicture(File testPicture) throws InterruptedException {
        final Intent data = new Intent();
        data.putExtra(FilePickerActivity.RESULT_FILE_PATH, testPicture.getAbsolutePath());
        final TaskContainerFragment fragment = getFragment();
        activityRule.getActivity().runOnUiThread(new Runnable() {
            public void run() {
                fragment.onActivityResult(FilePickerProxy.PICK_FILE_REQUEST,
                        FilePickerActivity.RESULT_OK, data);
            }
        });
        Thread.sleep(1000);
    }

    private void addSafeCopyToTestFiles() {
        List<Asset> assets = assetRepository.getAll();
        String fileName = assets.get(0).getFilename();
        testFiles.add(new File(internalStorage, fileName));
    }

    private TaskContainerFragment getFragment() {
        return (TaskContainerFragment) activityRule.getActivity().getFragmentManager()
                .findFragmentById(R.id.task_container);
    }

    private void removeTestFiles() {
        for (File testFile : testFiles) {
            try {
                if (testFile.exists()) {
                    FileUtils.forceDelete(testFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void removeTestAssets() {
        for (Asset asset : assetRepository.getAll()) {
            assetRepository.delete(asset.getId());
        }
    }
}
