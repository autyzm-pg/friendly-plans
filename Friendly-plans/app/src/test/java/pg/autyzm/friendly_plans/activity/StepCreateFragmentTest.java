package pg.autyzm.friendly_plans.activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageButton;

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

import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.BuildConfig;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.test_helpers.AppComponentBuilder;
import pg.autyzm.friendly_plans.test_helpers.AppComponentInjector;
import pg.autyzm.friendly_plans.view.step_create.StepCreateFragment;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateActivity;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class StepCreateFragmentTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    private ToastUserNotifier toastUserNotifier;

    private TaskCreateActivity activity;
    private StepCreateFragment fragment;

    @Before
    public void setUp() {
        final AppComponent appComponent = AppComponentBuilder.builder()
                .toastUserNotifier(toastUserNotifier)
                .buildAppComponent();
        AppComponentInjector.injectIntoApp(appComponent);
        activity = Robolectric.setupActivity(TaskCreateActivity.class);
        fragment = new StepCreateFragment();
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.commit();
    }

    @Test
    public void whenClickPlayButtonWithoutSoundChosenExpectToastToBeDisplayed() {
        ImageButton playSound = (ImageButton) activity.findViewById(R.id.id_btn_play_step_sound);
        playSound.performClick();
        verify(toastUserNotifier).displayNotifications(
                eq(R.string.no_file_to_play_error), any(Context.class));
    }

    @Test
    public void whenClickSaveButton() {
        Button logData = (Button) activity.findViewById(R.id.id_btn_save_step);
        logData.performClick();
        verify(toastUserNotifier).displayNotifications(
                eq(R.string.step_saved_message), any(Context.class));
    }
}
