package pg.autyzm.friendly_plans.view.step_create;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import database.repository.AssetRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.databinding.FragmentStepCreateBinding;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.view.components.SoundComponent;

public class StepCreateFragment extends Fragment implements StepCreateEvents.StepData {

    @Inject
    MediaPlayer mediaPlayer;
    @Inject
    AssetRepository assetRepository;
    @Inject
    public FilePickerProxy filePickerProxy;

    private ImageView playSoundIcon;
    private Long soundId;

    @TargetApi(VERSION_CODES.M)
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
        StepCreateData stepData = new StepCreateData("", "", "");
        binding.setStepData(stepData);
        binding.setStepDataClick(this);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        playSoundIcon = (ImageView) view.findViewById(R.id.id_iv_play_step_sound_icon);
    }

    @Override
    public void logStepName(StepCreateData stepCreateData) {
        String name = stepCreateData.getStepName();
        String picture = stepCreateData.getPictureName();
        String sound = stepCreateData.getSoundName();
        Log.i("step data", name + " " + picture + " " + sound);
        Toast.makeText(getActivity(), name + " " + picture + " " + sound, Toast.LENGTH_SHORT)
                .show();
    }


}
