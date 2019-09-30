package pg.autyzm.friendly_plans.manager_app.view.components;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;

import database.entities.Asset;
import database.repository.AssetRepository;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.test_helpers.AppComponentBuilder;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(AnimationUtils.class)
public class SoundComponentTest {

    private static final Long NO_SOUND_SELECTED_ID = null;
    private static final Long SOUND_SELECTED_ID = 5L;
    private static final String SOUND_FULL_PATH = "/test/sound/file/path.mp3";

    @Mock
    private ImageButton playSoundIcon;
    @Mock
    private Context context;
    @Mock
    private View onClickView;
    @Mock
    private Asset sound;
    @Mock
    private Animation rotation;
    @Mock
    private ToastUserNotifier toastUserNotifier;
    @Mock
    private MediaPlayer mediaPlayer;
    @Mock
    private AssetRepository assetRepository;
    @Mock
    private AssetsHelper assetsHelper;

    private AppComponent appComponent;

    @Before
    public void setUp() {
        PowerMockito.mockStatic(AnimationUtils.class);
        when(AnimationUtils.loadAnimation(context, R.anim.ic_play_sound_animation))
                .thenReturn(rotation);
        appComponent = AppComponentBuilder.builder()
                    .toastUserNotifier(toastUserNotifier)
                    .mediaPlayer(mediaPlayer)
                    .assetRepository(assetRepository)
                    .assetsHelper(assetsHelper)
                    .buildAppComponent();
    }

    @Test
    public void whenSoundIdIsNullAndPlayClickedShouldShowToast() {
        SoundComponent soundComponent = SoundComponent.getSoundComponent(NO_SOUND_SELECTED_ID,
                playSoundIcon, context, appComponent);

        soundComponent.onPlayStopSoundClick(onClickView);

        verify(toastUserNotifier).displayNotifications(
                R.string.no_file_to_play_error, context);

    }

    @Test
    public void whenPlayClickedAndSoundIsPlayingAnimationAndPlayingShouldStop() {
        when(mediaPlayer.isPlaying()).thenReturn(true);
        SoundComponent soundComponent = SoundComponent.getSoundComponent(SOUND_SELECTED_ID,
                playSoundIcon, context, appComponent);

        soundComponent.onPlayStopSoundClick(onClickView);

        verifySoundStopped(mediaPlayer);
        verifyAnimationStopped();
    }

    @Test
    public void whenPlayClickedAndSoundIsNotPlayingAnimationAndPlayingShouldStart()
        throws IOException {
        when(mediaPlayer.isPlaying()).thenReturn(false);
        when(assetRepository.get(SOUND_SELECTED_ID)).thenReturn(sound);
        when(assetsHelper.getFileFullPath(sound)).thenReturn(SOUND_FULL_PATH);
        SoundComponent soundComponent = SoundComponent.getSoundComponent(SOUND_SELECTED_ID,
                playSoundIcon, context, appComponent);

        soundComponent.onPlayStopSoundClick(onClickView);

        verifySoundStarted(mediaPlayer);
        verifyAnimationStarted();
    }

    @Test
    public void onStopActionAnimationAndPlayingSoundShouldStop()
        throws IOException {
        SoundComponent soundComponent = SoundComponent.getSoundComponent(SOUND_SELECTED_ID,
                playSoundIcon, context, appComponent);

        soundComponent.stopActions();

        verifySoundStopped(mediaPlayer);
        verifyAnimationStopped();
    }

    private void verifySoundStopped(MediaPlayer mediaPlayer) {
        verify(mediaPlayer).stop();
        verify(mediaPlayer).reset();
    }

    private void verifyAnimationStopped() {
        verify(playSoundIcon).clearAnimation();
        verify(playSoundIcon).setImageResource( R.drawable.ic_play_sound);
    }

    private void verifySoundStarted(MediaPlayer mediaPlayer) throws IOException {
        verify(mediaPlayer).reset();
        verify(mediaPlayer).setDataSource(SOUND_FULL_PATH);
        verify(mediaPlayer).prepare();
        verify(mediaPlayer).start();
    }

    private void verifyAnimationStarted() {
        verify(playSoundIcon).setImageResource( R.drawable.ic_playing_sound);
        verify(playSoundIcon).startAnimation(rotation);
    }

}
