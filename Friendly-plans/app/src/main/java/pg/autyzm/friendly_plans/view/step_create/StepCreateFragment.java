package pg.autyzm.friendly_plans.view.step_create;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import database.repository.AssetRepository;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.FragmentStepCreateBinding;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class StepCreateFragment extends Fragment implements StepCreateEvents {

    @Inject
    MediaPlayer mediaPlayer;
    @Inject
    AssetRepository assetRepository;

    private EditText stepSound;
    private ImageView playSoundIcon;
    private Long soundId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        FragmentStepCreateBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_create, container,false);
        View view = binding.getRoot();
        binding.setEvents(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        stepSound = (EditText) view.findViewById(R.id.id_et_step_sound);
        playSoundIcon = (ImageView) view.findViewById(R.id.id_iv_play_step_sound_icon);
    }

    @Override
    public void onPlayStopSoundClick(View view) {
        playStopSound();
    }

    private void playStopSound() {
        boolean isNameEmpty = stepSound.getText().toString().isEmpty();
        if (!isNameEmpty) {
            if (!mediaPlayer.isPlaying()) {
                playSound(retrieveSoundFile());
                runBtnAnimation();
            } else {
                stopSound();
                stopBtnAnimation();
            }
        } else {
            showToastMessage(R.string.no_file_to_play_error);
        }
    }

    private void playSound(String pathToFile) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(pathToFile);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            showToastMessage(R.string.playing_file_error);
        }
    }

    private String retrieveSoundFile() {
        String soundFileName = assetRepository.get(soundId).getFilename();
        String fileDir = getActivity().getApplicationContext().getFilesDir().toString();
        return fileDir + File.separator + soundFileName;
    }

    private void runBtnAnimation() {
        playSoundIcon.setImageResource(R.drawable.ic_playing_sound_2);
        Animation rotation = AnimationUtils
            .loadAnimation(getActivity().getApplicationContext(),
                R.anim.ic_play_sound_animation);
        playSoundIcon.startAnimation(rotation);
    }

    private void stopBtnAnimation() {
        playSoundIcon.clearAnimation();
        playSoundIcon.setImageResource(R.drawable.ic_play_sound_1);
    }

    private void stopSound() {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    private void showToastMessage(int resourceStringId) {
        ToastUserNotifier.displayNotifications(
                resourceStringId,
                getActivity().getApplicationContext());
    }
}
