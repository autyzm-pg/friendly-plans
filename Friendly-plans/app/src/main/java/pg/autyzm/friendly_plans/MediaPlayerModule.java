package pg.autyzm.friendly_plans;

import android.media.MediaPlayer;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class MediaPlayerModule {

    @Provides
    @Singleton
    public MediaPlayer getMediaPlayer() {
        return new MediaPlayer();
    }
}
