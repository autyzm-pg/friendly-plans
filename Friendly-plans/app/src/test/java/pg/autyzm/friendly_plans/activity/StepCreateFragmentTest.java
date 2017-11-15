package pg.autyzm.friendly_plans.activity;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import android.app.FragmentTransaction;
import android.media.MediaPlayer;
import android.widget.Button;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import pg.autyzm.friendly_plans.BuildConfig;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.test_helpers.AppComponentDaggerRule;
import pg.autyzm.friendly_plans.test_helpers.PowerAppComponentDaggerRule;
import pg.autyzm.friendly_plans.view.step_create.StepCreateFragment;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateActivity;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class StepCreateFragmentTest {

    @Rule
    public final PowerAppComponentDaggerRule daggerRule = new PowerAppComponentDaggerRule();

    private TaskCreateActivity activity;
    private StepCreateFragment fragment;


    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(TaskCreateActivity.class);
        fragment = new StepCreateFragment();
        FragmentTransaction transaction = activity.getFragmentManager().beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.commit();
    }

    @Test
    public void whenClickPlayButtonWithoutSoundChosenExpectToastToBeDisplayed() {
        Button playSound = (Button) activity.findViewById(R.id.id_btn_play_step_sound);
        playSound.performClick();
        String expectedMessage = activity.getApplicationContext().getString(
                R.string.no_file_to_play_error);
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo(expectedMessage));
    }
}
