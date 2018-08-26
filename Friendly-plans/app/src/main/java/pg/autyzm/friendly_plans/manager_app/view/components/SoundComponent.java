package pg.autyzm.friendly_plans.manager_app.view.components;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import java.io.IOException;

import javax.inject.Inject;

import database.entities.Asset;
import database.repository.AssetRepository;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public final class SoundComponent {

    @Inject
    MediaPlayer mediaPlayer;
    @Inject
    AssetRepository assetRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;
    @Inject
    AssetsHelper assetsHelper;

    private Long soundId;
    private ImageButton playSoundIcon;
    private Context context;

    public static SoundComponent getSoundComponent(Long soundId, ImageButton playSoundIcon,
            Context context, AppComponent appComponent) {
        final SoundComponent soundComponent = new SoundComponent(soundId, playSoundIcon, context);
        appComponent.inject(soundComponent);
        soundComponent.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        soundComponent.mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                soundComponent.handlePlayingCompletion();
            }
        });
        return soundComponent;
    }

    private SoundComponent(Long soundId, ImageButton playSoundIcon,
            Context context) {
        this.soundId = soundId;
        this.playSoundIcon = playSoundIcon;
        this.context = context;
    }

    public void onPlayStopSoundClick(View view) {
        if (soundId != null) {
            handleSoundPlaying();
        } else {
            toastUserNotifier.displayNotifications(R.string.no_file_to_play_error, context);
        }
    }

    public void stopActions() {
        stopSound();
        stopAnimation();
    }

    public void setSoundId(Long soundId){
        this.soundId = soundId;
        if(soundId == null) {
            playSoundIcon.setVisibility(View.INVISIBLE);
        } else {
            playSoundIcon.setVisibility(View.VISIBLE);
        }
    }

    private void handleSoundPlaying() {
        if (!mediaPlayer.isPlaying()) {
            Asset sound = assetRepository.get(soundId);
            playSound(sound);
            runAnimation();
        } else {
            stopSound();
            stopAnimation();
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
        playSoundIcon.setImageResource( R.drawable.ic_playing_sound);
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
