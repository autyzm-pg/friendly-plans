package pg.autyzm.friendly_plans.child_app.view.common;


import android.media.MediaPlayer;
import android.view.View;

public class SoundIconListener implements View.OnClickListener {
    private MediaPlayer sound;
    public SoundIconListener(MediaPlayer sound){
        this.sound = sound;
    }

    @Override
    public void onClick(View view) {
        sound.start();
    }
}
