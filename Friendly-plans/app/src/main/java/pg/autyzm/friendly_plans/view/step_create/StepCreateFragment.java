package pg.autyzm.friendly_plans.view.step_create;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import database.repository.AssetRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.FragmentStepCreateBinding;
import pg.autyzm.friendly_plans.view.components.SoundComponent;

public class StepCreateFragment extends Fragment {

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
        AppComponent appComponent = ((App) getActivity().getApplication()).getAppComponent();
        appComponent.inject(this);
        SoundComponent soundComponent = SoundComponent.getSoundComponent(
                soundId, playSoundIcon, getActivity().getApplicationContext(), appComponent);

        FragmentStepCreateBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_create, container, false);
        View view = binding.getRoot();
        binding.setSoundComponent(soundComponent);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        stepSound = (EditText) view.findViewById(R.id.id_et_step_sound);
        playSoundIcon = (ImageView) view.findViewById(R.id.id_iv_play_step_sound_icon);
    }


}
