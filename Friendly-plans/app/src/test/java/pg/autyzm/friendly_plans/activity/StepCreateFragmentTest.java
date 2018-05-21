package pg.autyzm.friendly_plans.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.widget.Button;
import android.widget.ImageButton;

import com.nbsp.materialfilepicker.ui.FilePickerActivity;

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

import database.repository.StepTemplateRepository;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.BuildConfig;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.test_helpers.AppComponentBuilder;
import pg.autyzm.friendly_plans.test_helpers.AppComponentInjector;
import pg.autyzm.friendly_plans.validation.StepValidation;
import pg.autyzm.friendly_plans.validation.ValidationResult;
import pg.autyzm.friendly_plans.validation.ValidationStatus;
import pg.autyzm.friendly_plans.view.step_create.StepCreateFragment;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateActivity;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateFragment;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class StepCreateFragmentTest {

    private static final String TEST_FILE_PATH = "Test";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ToastUserNotifier toastUserNotifier;

    @Mock
    private FilePickerProxy filePickerProxy;

    @Mock
    private StepTemplateRepository stepTemplateRepository;
    @Mock
    private StepValidation stepValidation;


    private TaskCreateActivity activity;
    private StepCreateFragment fragment;

    @Before
    public void setUp() {
        setUpMocks();
        final AppComponent appComponent = AppComponentBuilder.builder()
                .filePickerProxy(filePickerProxy)
                .stepTemplateRepository(stepTemplateRepository)
                .stepValidation(stepValidation)
                .toastUserNotifier(toastUserNotifier)
                .buildAppComponent();
        AppComponentInjector.injectIntoApp(appComponent);
        activity = Robolectric.setupActivity(TaskCreateActivity.class);
        fragment = new StepCreateFragment();
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.commit();
    }


    private void setUpMocks() {
        toastUserNotifier = new ToastUserNotifier();
        when(filePickerProxy.isPickFileRequested(any(int.class), any(AssetType.class)))
                .thenReturn(true);
        when(filePickerProxy.getFilePath(any(Intent.class)))
                .thenReturn(TEST_FILE_PATH);
        when(filePickerProxy.isFilePicked(any(int.class))).thenReturn(true);
    }

    @Test
    public void whenClickPlayButtonWithoutSoundChosenExpectToastToBeDisplayed() {
        ImageButton playSound = (ImageButton) activity.findViewById(R.id.id_btn_play_step_sound);
        playSound.performClick();

        String expectedMessage = activity.getResources().getString(R.string.no_file_to_play_error);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(expectedMessage));
    }

    @Test
    public void whenClickingSelectPictureExpectOpenFilePickerBeCalled() throws Exception {
        Button selectPicture = (Button) activity.findViewById(R.id.id_btn_select_step_picture);
        selectPicture.performClick();

        verify(filePickerProxy)
                .openFilePicker(any(StepCreateFragment.class), any(AssetType.class));
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
        Button selectSound = (Button) activity.findViewById(R.id.id_btn_select_step_sound);
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
    public void whenAddingStepCreatesAnErrorExpectUserNotifier() {
        checkRuntimeException(R.id.id_btn_save_step);
    }

    private void checkRuntimeException(int buttonId) {
        when(stepValidation
                .isNewNameValid(any(Long.class), any(String.class)))
                .thenReturn(new ValidationResult(ValidationStatus.VALID));

        when(stepTemplateRepository
                .create(any(String.class), any(Integer.class), any(Long.class), any(Long.class), any(Long.class)))
                .thenThrow(new RuntimeException());
        Button button = (Button) activity.findViewById(buttonId);
        button.performClick();
        String expectedMessage = activity.getResources()
                .getString(R.string.save_step_error_message);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(expectedMessage));
    }
}
