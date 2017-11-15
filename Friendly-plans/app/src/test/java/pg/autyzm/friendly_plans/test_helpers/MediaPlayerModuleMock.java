package pg.autyzm.friendly_plans.test_helpers;

import android.media.MediaPlayer;
import org.mockito.Mockito;
import pg.autyzm.friendly_plans.MediaPlayerModule;


public class MediaPlayerModuleMock extends MediaPlayerModule {

    private MediaPlayer mediaPlayer;

    public MediaPlayerModuleMock() {
        mediaPlayer = Mockito.mock(MediaPlayer.class);
    }

    @Override
    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }
}
