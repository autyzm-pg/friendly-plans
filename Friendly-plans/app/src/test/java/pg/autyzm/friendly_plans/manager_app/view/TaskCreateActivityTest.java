package pg.autyzm.friendly_plans.manager_app.view;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Fragment;
import android.content.Intent;
import android.widget.Button;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import database.repository.TaskTemplateRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.BuildConfig;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.test_helpers.AppComponentInjector;
import pg.autyzm.friendly_plans.test_helpers.AppComponentBuilder;
import pg.autyzm.friendly_plans.manager_app.validation.TaskValidation;
import pg.autyzm.friendly_plans.manager_app.validation.ValidationResult;
import pg.autyzm.friendly_plans.manager_app.validation.ValidationStatus;
import pg.autyzm.friendly_plans.manager_app.view.task_create.TaskCreateActivity;
import pg.autyzm.friendly_plans.manager_app.view.task_create.TaskCreateFragment;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class TaskCreateActivityTest {

    private static final String TEST_FILE_PATH = "Test";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private FilePickerProxy filePickerProxy;
    @Mock
    private TaskTemplateRepository taskTemplateRepository;
    @Mock
    private TaskValidation taskValidation;

    private ToastUserNotifier toastUserNotifier;
    private TaskCreateActivity activity;
    private Fragment fragment;

    @Before
    public void setUp() {
        setUpMocks();
        setUpAppComponent();
        activity = Robolectric.setupActivity(TaskCreateActivity.class);
        fragment = activity.getFragmentManager().findFragmentById(R.id.task_container);
    }

    private void setUpMocks() {
        toastUserNotifier = new ToastUserNotifier();
        when(filePickerProxy.isPickFileRequested(any(int.class), any(AssetType.class)))
                .thenReturn(true);
        when(filePickerProxy.getFilePath(any(Intent.class)))
                .thenReturn(TEST_FILE_PATH);
        when(filePickerProxy.isFilePicked(any(int.class))).thenReturn(true);
    }

    private void setUpAppComponent() {

        final AppComponent appComponent = AppComponentBuilder.builder()
                    .filePickerProxy(filePickerProxy)
                    .taskTemplateRepository(taskTemplateRepository)
                    .taskValidation(taskValidation)
                    .toastUserNotifier(toastUserNotifier)
                    .buildAppComponent();
        AppComponentInjector.injectIntoApp(appComponent);
    }

    @Test
    public void whenClickingSelectPictureExpectOpenFilePickerBeCalled() throws Exception {
        Button selectPicture = (Button) activity.findViewById(R.id.id_btn_select_task_picture);
        selectPicture.performClick();

        verify(filePickerProxy)
                .openFilePicker(any(TaskCreateFragment.class), any(AssetType.class));
    }

    @Test
    public void whenActivityResultIsCalledWithNonExistingPictureDataExpectToastWithErrorMessageIsShown() {
        fragment.onActivityResult(
                AssetType.PICTURE.ordinal(),
                FilePickerActivity.RESULT_OK,
                new Intent()
        );
        String expectedMessage = activity.getResources().getString(R.string.picking_file_error);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(expectedMessage));
    }

    @Test
    public void whenClickingSelectSoundExpectOpenFilePickerBeCalled() throws Exception {
        Button selectSound = (Button) activity.findViewById(R.id.id_btn_select_task_sound);
        selectSound.performClick();

        verify(filePickerProxy)
                .openFilePicker(any(TaskCreateFragment.class), any(AssetType.class));
    }

    @Test
    public void whenActivityResultIsCalledWithNonExistingSoundDataExpectToastWithErrorMessageIsShown() {
        fragment.onActivityResult(
                AssetType.SOUND.ordinal(),
                FilePickerActivity.RESULT_OK,
                new Intent()
        );
        String expectedMessage = activity.getResources().getString(R.string.picking_file_error);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(expectedMessage));
    }

    @Test
    public void whenAddingTaskCreatesAnErrorExpectUserNotifier() {
        checkRuntimeException(R.id.id_btn_save_and_finish);
    }

    @Test
    public void whenGoingToStepsErrorExpectUserNotifier() {
        checkRuntimeException(R.id.id_btn_steps);
    }

    private void checkRuntimeException(int buttonId) {
        when(taskValidation
                .isNewNameValid(any(String.class)))
                .thenReturn(new ValidationResult(ValidationStatus.VALID));

        when(taskValidation
                .isDurationValid(any(String.class)))
                .thenReturn(new ValidationResult(ValidationStatus.VALID));

        when(taskTemplateRepository
                .create(any(String.class), any(Integer.class), any(Long.class), any(Long.class), any(Integer.class)))
                .thenThrow(new RuntimeException());
        Button button = (Button) activity.findViewById(buttonId);
        button.performClick();
        String expectedMessage = activity.getResources()
                .getString(R.string.save_task_error_message);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(expectedMessage));
    }
}

