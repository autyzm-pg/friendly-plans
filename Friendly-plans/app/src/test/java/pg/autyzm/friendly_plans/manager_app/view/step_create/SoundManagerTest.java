package pg.autyzm.friendly_plans.manager_app.view.step_create;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import database.entities.Asset;
import database.entities.StepTemplate;
import database.repository.AssetRepository;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.test_helpers.AppComponentBuilder;
import pg.autyzm.friendly_plans.test_helpers.RandomGenerator;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AnimationUtils.class)
public class SoundManagerTest {

    private static final String FILE_PATH = "path";
    private static final Long SOUND_ID = RandomGenerator.getId();

    private SoundManager soundManager;

    @Mock
    MediaPlayer mediaPlayer;
    @Mock
    AssetRepository assetRepository;
    @Mock
    ToastUserNotifier toastUserNotifier;
    @Mock
    AssetsHelper assetsHelper;

    @Mock
    private ImageButton playSoundIcon;
    @Mock
    private Context context;
    @Mock
    private ImageButton clearSound;
    @Mock
    private StepCreateData stepData;

    @Before
    public void setUp() {
        final AppComponent appComponent = AppComponentBuilder.builder()
                .assetRepository(assetRepository)
                .assetsHelper(assetsHelper)
                .toastUserNotifier(toastUserNotifier)
                .mediaPlayer(mediaPlayer)
                .buildAppComponent();
        soundManager = SoundManager.getSoundManager(stepData, playSoundIcon, clearSound, context, appComponent);
    }

    @Test
    public void whenClearingSoundExpectAllActionsStoppedAndPlayIconsInvisible() {
        soundManager.clearSound();

        verify(stepData).setSound(null);
        verify(playSoundIcon).setVisibility(View.INVISIBLE);
        verify(clearSound).setVisibility(View.INVISIBLE);

        verify(playSoundIcon).clearAnimation();
        verify(playSoundIcon).setImageResource(R.drawable.ic_play_sound);
        verify(mediaPlayer).stop();
        verify(mediaPlayer).reset();
    }

    @Test
    public void whenStoppingSoundActionExpectMediaPlayerStopped() {
        soundManager.stopActions();

        verify(playSoundIcon).clearAnimation();
        verify(playSoundIcon).setImageResource(R.drawable.ic_play_sound);
        verify(mediaPlayer).stop();
        verify(mediaPlayer).reset();
    }

    @Test
    public void whenShowingSoundAndSoundSetExpectFieldsVisible() {
        StepTemplate stepTemplate = new StepTemplate();
        stepTemplate.setSoundId(SOUND_ID);
        when(stepData.getStepTemplate()).thenReturn(stepTemplate);

        soundManager.showSound();

        verify(playSoundIcon).setVisibility(View.VISIBLE);
        verify(clearSound).setVisibility(View.VISIBLE);
    }

    @Test
    public void whenShowingSoundAndSoundNotSetExpectFieldsVisible() {
        StepTemplate stepTemplate = new StepTemplate();
        stepTemplate.setSoundId(null);
        when(stepData.getStepTemplate()).thenReturn(stepTemplate);

        soundManager.showSound();

        verify(playSoundIcon).setVisibility(View.INVISIBLE);
        verify(clearSound).setVisibility(View.INVISIBLE);
    }

    @Test
    public void whenPlayButtonClickAndMusicIsNotPlayingExpectMusicPlayed() throws IOException {
        StepTemplate stepTemplate = new StepTemplate();
        stepTemplate.setSoundId(SOUND_ID);
        when(stepData.getStepTemplate()).thenReturn(stepTemplate);
        when(mediaPlayer.isPlaying()).thenReturn(false);
        Asset sound = new Asset();
        when(assetRepository.get(SOUND_ID)).thenReturn(sound);
        when(assetsHelper.getFileFullPath(sound)).thenReturn(FILE_PATH);

        PowerMockito.mockStatic(AnimationUtils.class);
        AnimationSet animation = new AnimationSet(true);
        PowerMockito.when(AnimationUtils.loadAnimation(any(Context.class), eq(R.anim.ic_play_sound_animation)))
                .thenReturn(animation);

        soundManager.onPlayStopSoundClick(null);

        verify(mediaPlayer).reset();
        verify(mediaPlayer).setDataSource(FILE_PATH);
        verify(mediaPlayer).prepare();
        verify(mediaPlayer).start();

        verify(playSoundIcon).setImageResource(R.drawable.ic_playing_sound);
        verify(playSoundIcon).startAnimation(any(Animation.class));
    }

    @Test
    public void whenPlayButtonClickAndMusicIsPlayingExpectMusicStopped() {
        StepTemplate stepTemplate = new StepTemplate();
        stepTemplate.setSoundId(SOUND_ID);
        when(stepData.getStepTemplate()).thenReturn(stepTemplate);
        when(mediaPlayer.isPlaying()).thenReturn(true);

        soundManager.onPlayStopSoundClick(null);

        verify(playSoundIcon).clearAnimation();
        verify(playSoundIcon).setImageResource(R.drawable.ic_play_sound);
        verify(mediaPlayer).stop();
        verify(mediaPlayer).reset();
    }

    @Test
    public void whenPlayButtonClickAndSoundIdNotSetExpectErrorMessage() {
        StepTemplate stepTemplate = new StepTemplate();
        stepTemplate.setSoundId(null);
        when(stepData.getStepTemplate()).thenReturn(stepTemplate);

        soundManager.onPlayStopSoundClick(null);

        verify(toastUserNotifier).displayNotifications(R.string.no_file_to_play_error, context);
    }
}
