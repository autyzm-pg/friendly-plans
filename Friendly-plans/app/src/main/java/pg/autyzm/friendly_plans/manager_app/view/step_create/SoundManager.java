package pg.autyzm.friendly_plans.manager_app.view.step_create;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import database.entities.Asset;
import database.repository.AssetRepository;
import java.io.IOException;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public final class SoundManager {

    @Inject
    MediaPlayer mediaPlayer;
    @Inject
    AssetRepository assetRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;
    @Inject
    AssetsHelper assetsHelper;

    private ImageButton playSoundIcon;
    private Context context;
    private ImageButton clearSound;
    private StepCreateData stepData;

    public static SoundManager getSoundManager(StepCreateData stepData, ImageButton playSoundIcon,
            ImageButton clearSound,
            Context context, AppComponent appComponent) {
        final SoundManager soundManager = new SoundManager(stepData, playSoundIcon, clearSound,
                context);
        appComponent.inject(soundManager);
        soundManager.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        soundManager.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                soundManager.handlePlayingCompletion();
            }
        });
        return soundManager;
    }

    private SoundManager(StepCreateData stepData, ImageButton playSoundIcon,
            ImageButton clearSound, Context context) {
        this.stepData = stepData;
        this.clearSound = clearSound;
        this.playSoundIcon = playSoundIcon;
        this.context = context;
    }

    public void onPlayStopSoundClick(View view) {
        if (isSoundSet()) {
            handleSoundPlaying();
        } else {
            toastUserNotifier.displayNotifications(R.string.no_file_to_play_error, context);
        }
    }

    public void stopActions() {
        stopSound();
        stopAnimation();
    }

    public void clearSound() {
        stepData.setSound(null);
        changeSoundSelected(false);
        stopActions();
    }

    public void showSound() {
        changeSoundSelected(isSoundSet());
    }

    public void handleAssetSelecting(String filePath) {
        AssetsHelper assetsHelper = new AssetsHelper(context);
        try {
            String assetName = assetsHelper.makeSafeCopy(filePath);
            Long soundId = assetRepository
                    .create(AssetType.getTypeByExtension(assetName), assetName);
            stepData.setSound(assetRepository.get(soundId));
        } catch (IOException e) {
            toastUserNotifier.displayNotifications(R.string.picking_file_error, context);
        }
    }

    private void changeSoundSelected(Boolean isSelected) {
        if (isSelected) {
            playSoundIcon.setVisibility(View.VISIBLE);
            clearSound.setVisibility(View.VISIBLE);
        } else {
            playSoundIcon.setVisibility(View.INVISIBLE);
            clearSound.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isSoundSet() {
        return stepData.getStepTemplate().getSoundId() != null;
    }

    private void handleSoundPlaying() {
        if (!mediaPlayer.isPlaying()) {
            Asset sound = assetRepository.get(stepData.getStepTemplate().getSoundId());
            playSound(sound);
            runAnimation();
        } else {
            stopActions();
        }
    }

    private void playSound(Asset sound) {
        try {
            String soundPath = assetsHelper.getFileFullPath(sound);
            mediaPlayer.reset();
            mediaPlayer.setDataSource(soundPath);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            toastUserNotifier.displayNotifications(R.string.playing_file_error, context);
        }
    }

    private void runAnimation() {
        playSoundIcon.setImageResource(R.drawable.ic_playing_sound);
        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.ic_play_sound_animation);
        playSoundIcon.startAnimation(rotation);
    }

    private void stopAnimation() {
        playSoundIcon.clearAnimation();
        playSoundIcon.setImageResource(R.drawable.ic_play_sound);
    }

    private void stopSound() {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    private void handlePlayingCompletion() {
        mediaPlayer.reset();
        stopAnimation();
    }
}
