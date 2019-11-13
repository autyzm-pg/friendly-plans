package pg.autyzm.friendly_plans.child_app.utility;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

import javax.inject.Inject;

import database.repository.AssetRepository;
import database.repository.ChildPlanRepository;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.asset.AssetsHelper;

public class SoundHelper {
    @Inject
    ChildPlanRepository childPlanRepository;
    @Inject
    AssetsHelper assetsHelper;
    @Inject
    AssetRepository assetRepository;
    String loopSoundPath;
    public static SoundHelper getSoundHelper(AppComponent appComponent)
    {
        SoundHelper soundHelper = new SoundHelper();
        appComponent.inject(soundHelper);
        soundHelper.setLoopSoundPath();
        return soundHelper;
    }

    void setLoopSoundPath() {
        Long soundId = childPlanRepository.getActivePlan().getChild().getTimerSoundId();
        loopSoundPath = assetsHelper.getFileFullPath(assetRepository.get(soundId));

    }

    public MediaPlayer prepareLoopSound() {
        MediaPlayer sound = new MediaPlayer();
        try {
            sound.setDataSource(loopSoundPath);
            sound.setAudioStreamType(AudioManager.STREAM_MUSIC);
            sound.setLooping(true);
            sound.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sound;
    }

    public void resetLoopSound(MediaPlayer sound){
        sound.reset();
        try {
            sound.setDataSource(loopSoundPath);
            sound.setAudioStreamType(AudioManager.STREAM_MUSIC);
            sound.setLooping(true);
            sound.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
