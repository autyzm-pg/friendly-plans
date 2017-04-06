package pg.autyzm.friendly_plans.activity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
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
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import pg.autyzm.friendly_plans.BuildConfig;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.TaskContainerFragment;
import pg.autyzm.friendly_plans.TaskCreateActivity;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.test_helpers.AppComponentDaggerRule;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
public class TaskCreateActivityTest {

    @Rule
    public final AppComponentDaggerRule rule = new AppComponentDaggerRule();

    @Mock
    private FilePickerProxy filePickerProxy;

    @Mock
    private TaskTemplateRepository taskTemplateRepository;

    private TaskCreateActivity activity;

    @Before
    public void setUp() {
        doNothing().when(filePickerProxy).openImageFilePicker(any(TaskContainerFragment.class));
        activity = Robolectric.setupActivity(TaskCreateActivity.class);
    }

    @Test
    public void When_ClickingSelectPicture_Expect_OpenFilePickerBeCalled() throws Exception {
        Button selectPicture = (Button) activity.findViewById(R.id.id_btn_select_task_picture);
        selectPicture.performClick();

        verify(filePickerProxy).openImageFilePicker(any(TaskContainerFragment.class));
    }

    @Test
    public void When_ActivityResultIsCalledWithNonExistingPictureData_Expect_ToastWithErrorMessageIsShown() {
        String TEST_FILE_PATH = "Test";
        when(filePickerProxy.isPickFileRequested(any(int.class))).thenReturn(true);
        when(filePickerProxy.isFilePicked(any(int.class))).thenReturn(true);
        when(filePickerProxy.getFilePath(any(Intent.class))).thenReturn(TEST_FILE_PATH);

        Fragment fragment = activity.getFragmentManager().findFragmentById(R.id.task_container);
        fragment.onActivityResult(
                FilePickerProxy.PICK_FILE_REQUEST,
                FilePickerActivity.RESULT_OK,
                new Intent()
        );
        String expectedMessage = activity.getResources().getString(R.string.picking_file_error);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(expectedMessage));
    }
    
}

