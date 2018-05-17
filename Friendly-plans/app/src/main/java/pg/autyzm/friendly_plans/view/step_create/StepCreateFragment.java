package pg.autyzm.friendly_plans.view.step_create;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import javax.inject.Inject;

import database.entities.Asset;
import database.entities.StepTemplate;
import database.repository.StepTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.databinding.FragmentStepCreateBinding;
import pg.autyzm.friendly_plans.validation.StepValidation;
import pg.autyzm.friendly_plans.validation.ValidationResult;
import pg.autyzm.friendly_plans.view.components.SoundComponent;
import pg.autyzm.friendly_plans.view.view_fragment.CreateFragment;

public class StepCreateFragment extends CreateFragment implements StepCreateEvents.StepData {

    @Inject
    MediaPlayer mediaPlayer;
    @Inject
    StepTemplateRepository stepTemplateRepository;
    @Inject
    StepValidation stepValidation;


    private Long taskId;
    private Long stepId;
    private StepCreateData stepData;
    private StepTemplate step;
    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";
    private EditText stepNameView;

    @TargetApi(VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        FragmentStepCreateBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_create, container, false);

        View view = binding.getRoot();
        ImageButton playSoundIcon = (ImageButton) view.findViewById(R.id.id_btn_play_step_sound);

        AppComponent appComponent = ((App) getActivity().getApplication()).getAppComponent();
        soundComponent = SoundComponent.getSoundComponent(
                soundId, playSoundIcon, getActivity().getApplicationContext(), appComponent);
        appComponent.inject(this);

        binding.setSoundComponent(soundComponent);

        String initialStepName = "";

        Bundle arguments = getArguments();
        if (arguments != null) {
            if(arguments.containsKey(ActivityProperties.TASK_ID)) {
                taskId = (Long) arguments.get(ActivityProperties.TASK_ID);
            }
            if(arguments.containsKey(ActivityProperties.STEP_ID)) {
                stepId = (Long) arguments.get(ActivityProperties.STEP_ID);
                step = stepTemplateRepository.get(stepId);
                initialStepName = step.getName();
            }
        }

        stepData = new StepCreateData(initialStepName, "", "");
        binding.setStepData(stepData);
        binding.setStepDataClick(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        registerViews(view);
        if(step != null) {
            Asset picture = step.getPicture();
            Asset sound = step.getSound();
            if (picture != null) {
                setAssetValue(AssetType.PICTURE, picture.getFilename(), picture.getId());
            }
            if (sound != null) {
                setAssetValue(AssetType.SOUND, sound.getFilename(), sound.getId());
            }
        }
    }

    private void registerViews(View view) {
        stepNameView = (EditText) view.findViewById(R.id.id_et_step_name);
        pictureFileName = (EditText) view.findViewById(R.id.id_et_step_picture);
        clearPicture = (ImageButton) view.findViewById(R.id.id_ib_step_clear_img_btn);
        picturePreview = (ImageView) view.findViewById(R.id.iv_step_picture_preview);
        soundFileName = (EditText) view.findViewById(R.id.id_et_step_sound);
        clearSound = (ImageButton) view.findViewById(R.id.id_ib_clear_step_sound_btn);
    }

        private Long addOrUpdateStepToTask(String stepName, int order) {
        soundComponent.stopActions();
        try {
            if (stepId != null) {
                if(validateName(stepId, stepNameView)) {

                    stepTemplateRepository.update(stepId, stepName, order,
                            pictureId,
                            soundId, taskId);
                    showToastMessage(R.string.step_saved_message);
                    return stepId;
                }
            } else {
                if(validateName(stepNameView)) {
                    long stepId = stepTemplateRepository.create(stepName, order,
                            pictureId,
                            soundId, taskId);
                    showToastMessage(R.string.step_saved_message);
                    return stepId;
                }
            }
        } catch (RuntimeException exception) {
            return handleSavingError(exception);
        }
        return null;
    }

    private boolean validateName(EditText stepName) {
        ValidationResult validationResult = stepValidation
                .isNewNameValid(taskId, stepName.getText().toString());
        return handleInvalidResult(stepName, validationResult);
    }

    private boolean validateName(Long stepId, EditText stepName) {
        ValidationResult validationResult = stepValidation
                .isUpdateNameValid(stepId, taskId, stepName.getText().toString());
        return handleInvalidResult(stepName, validationResult);
    }

    @Override
    public void selectStepPicture() {
        filePickerProxy.openFilePicker(StepCreateFragment.this, AssetType.PICTURE);
    }

    @Override
    protected void setAssetValue(AssetType assetType, String assetName, Long assetId) {

        String assetNameTrimed = assetName.replaceAll(REGEX_TRIM_NAME, "");

        if (assetType.equals(AssetType.PICTURE)) {
            stepData.setPictureName(assetNameTrimed);
            clearPicture.setVisibility(View.VISIBLE);
            pictureId = assetId;
            showPreview(pictureId, picturePreview);
        } else {
            stepData.setSoundName(assetNameTrimed);
            soundId = assetId;
            soundComponent.setSoundId(assetId);
            clearSound.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void selectStepSound() {
        filePickerProxy.openFilePicker(StepCreateFragment.this, AssetType.SOUND);
    }

    @Override
    public void cleanStepPicture() {
        clearPicture();
    }

    @Override
    public void clearStepSound() {
        clearSound();
    }

    @Override
    public void showPicture() {
        showPicture(pictureId);
    }

    @Override
    public void saveStepData(StepCreateData stepCreateData) {
        String name = stepCreateData.getStepName();
        String picture = stepCreateData.getPictureName();
        String sound = stepCreateData.getSoundName();
        int order = stepTemplateRepository.getAll(taskId).size();
        if(stepId != null) order = step.getOrder();
        Long stepId = addOrUpdateStepToTask(name, order);
        Log.i("step data", name + " " + picture + " " + sound+ " " + stepId);
        if(stepId != null) getFragmentManager().popBackStack();
    }
}
