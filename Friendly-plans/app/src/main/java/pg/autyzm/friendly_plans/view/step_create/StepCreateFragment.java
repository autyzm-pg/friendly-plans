package pg.autyzm.friendly_plans.view.step_create;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import database.repository.AssetRepository;
import database.repository.StepTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.FragmentStepCreateBinding;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.view.components.SoundComponent;

public class StepCreateFragment extends Fragment implements StepCreateEvents.StepData {

    @Inject
    MediaPlayer mediaPlayer;
    @Inject
    StepTemplateRepository stepTemplateRepository;
    @Inject
    AssetRepository assetRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;

    private ImageView playSoundIcon;
    private EditText stepName;
    private Button save;
    private Long soundId;
    private Long taskId;

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
        stepName = (EditText) view.findViewById(R.id.id_et_step_name);
        save = (Button) view.findViewById(R.id.id_btn_save_step);
        Bundle arguments = getArguments();
        if (arguments != null) {
            taskId = (Long) arguments.get(ActivityProperties.TASK_ID);
        }
    }

    private Long addStepToTask(String stepName, int order) {
        try {
            long stepId = stepTemplateRepository.create(stepName, order,
                    (Long) null,
                    (Long) null, taskId);
            showToastMessage(R.string.step_saved_message);
            return stepId;

        } catch (RuntimeException exception) {
            return handleSavingError(exception);
        }
    }


    @Override
    public void saveStepData(StepCreateData stepCreateData) {
        String name = stepCreateData.getStepName();
        String picture = stepCreateData.getPictureName();
        String sound = stepCreateData.getSoundName();
        Long stepId = addStepToTask(name, 0);
        Log.i("step data", name + " " + picture + " " + sound+ " " + stepId);
    }


    private void showToastMessage(int resourceStringId) {
        toastUserNotifier.displayNotifications(
                resourceStringId,
                getActivity().getApplicationContext());
    }

    @Nullable
    private Long handleSavingError(RuntimeException exception) {
        Log.e("Step Create View", "Error saving step", exception);
        showToastMessage(R.string.save_step_error_message);
        return null;
    }
}
