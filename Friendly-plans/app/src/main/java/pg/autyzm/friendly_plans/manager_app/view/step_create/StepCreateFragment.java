package pg.autyzm.friendly_plans.manager_app.view.step_create;

import android.annotation.TargetApi;
import android.app.Fragment;
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

import android.widget.TextView;
import database.entities.StepTemplate;
import javax.inject.Inject;

import database.repository.StepTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.databinding.FragmentStepCreateBinding;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.manager_app.validation.StepValidation;
import pg.autyzm.friendly_plans.manager_app.validation.ValidationResult;
import pg.autyzm.friendly_plans.manager_app.validation.ValidationStatus;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class StepCreateFragment extends Fragment implements StepCreateEvents.StepData {
    @Inject
    MediaPlayer mediaPlayer;
    @Inject
    StepTemplateRepository stepTemplateRepository;
    @Inject
    StepValidation stepValidation;
    @Inject
    ToastUserNotifier toastUserNotifier;
    @Inject
    FilePickerProxy filePickerProxy;

    private TextView soundFileName;
    private ImageButton clearSound;
    private TextView pictureFileName;
    private ImageView picturePreview;
    private ImageButton clearPicture;
    private EditText stepNameView;
    private EditText stepDurationView;
    private ImageButton playSoundIcon;

    private StepCreateData stepData;
    private SoundManager soundManager;
    private PictureManager pictureManager;

    @TargetApi(VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        FragmentStepCreateBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_create, container, false);

        AppComponent appComponent = ((App) getActivity().getApplication()).getAppComponent();
        View view = binding.getRoot();

        ImageButton playSoundIcon = (ImageButton) view.findViewById(R.id.id_btn_play_step_sound);
        appComponent.inject(this);
        registerViews(view);

        stepData = parseArguments();

        soundManager = SoundManager.getSoundManager(
                stepData,
                playSoundIcon,
                clearPicture,
                getActivity().getApplicationContext(),
                appComponent);

        pictureManager = PictureManager.getPictureManager(
                stepData,
                clearPicture,
                picturePreview,
                getActivity().getApplicationContext(),
                appComponent);

        binding.setSoundManager(soundManager);
        binding.setStepData(stepData);
        binding.setStepDataClick(this);

        return view;
    }

    private StepCreateData parseArguments() {
        Bundle arguments = getArguments();

        StepCreateData stepData = new StepCreateData(
                (Long) arguments.get(ActivityProperties.TASK_ID));
        if (arguments.containsKey(ActivityProperties.STEP_ID)) {
            Long stepId = (Long) arguments.get(ActivityProperties.STEP_ID);
            stepData.setStepTemplate(stepTemplateRepository.get(stepId));
        }

        return stepData;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (filePickerProxy.isFilePicked(resultCode)) {
            if (filePickerProxy.isPickFileRequested(requestCode, AssetType.PICTURE)) {
                pictureManager.handleAssetSelecting(filePickerProxy.getFilePath(data));
            } else if (filePickerProxy.isPickFileRequested(requestCode, AssetType.SOUND)) {
                soundManager.handleAssetSelecting(filePickerProxy.getFilePath(data));
            }
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        view.post(new Runnable() {
            @Override
            public void run() {
                if (stepData != null && stepData.getStepTemplate() != null) {
                    pictureManager.showPicture();
                    soundManager.showSound();
                }
            }
        });
    }

    public void selectStepPicture() {
        filePickerProxy.openFilePicker(StepCreateFragment.this, AssetType.PICTURE);
    }

    public void selectStepSound() {
        filePickerProxy.openFilePicker(StepCreateFragment.this, AssetType.SOUND);
    }

    public void cleanStepPicture() {
        pictureManager.clearPicture();
    }

    public void clearStepSound() {
        soundManager.clearSound();
    }

    public void showPicture() {
        pictureManager.showPicturePreview(
                stepData.getStepTemplate(),
                getActivity().getApplicationContext(),
                getFragmentManager()
        );
    }

    public void saveStepData(StepCreateData stepCreateData) {
        soundManager.stopActions();

        try {
            StepTemplate stepTemplate = stepCreateData.getStepTemplate();
            if (stepTemplate.getId() != null) {
                updateStepTemplate(stepTemplate);
            } else {
                createStepTemplate(stepCreateData);
            }
        } catch (RuntimeException exception) {
            Log.e("Step Create View", "Error saving step", exception);
            showToastMessage(R.string.save_step_error_message);
        }
    }

    private void registerViews(View view) {
        stepNameView = (EditText) view.findViewById(R.id.id_et_step_name);
        pictureFileName = (TextView) view.findViewById(R.id.id_et_step_picture);
        clearPicture = (ImageButton) view.findViewById(R.id.id_ib_step_clear_img_btn);
        picturePreview = (ImageView) view.findViewById(R.id.iv_step_picture_preview);
        soundFileName = (TextView) view.findViewById(R.id.id_et_step_sound);
        clearSound = (ImageButton) view.findViewById(R.id.id_ib_clear_step_sound_btn);
        stepDurationView = (EditText) view.findViewById(R.id.id_et_step_duration);
        playSoundIcon = (ImageButton) view.findViewById(R.id.id_btn_play_step_sound);
    }

    private void createStepTemplate(StepCreateData stepCreateData) {
        if (validateName(stepNameView, stepCreateData)) {
            StepTemplate stepTemplate = stepCreateData.getStepTemplate();
            stepTemplate.setOrder(countStepOrder(stepCreateData.getTaskId()));
            long stepId = stepTemplateRepository.create(stepTemplate);

            stepTemplate.setId(stepId);

            showToastMessage(R.string.step_saved_message);
            getFragmentManager().popBackStack();
        }
    }

    private boolean validateName(EditText stepName, StepCreateData stepCreateData) {
        ValidationResult validationResult = stepValidation
                .isNewNameValid(stepCreateData.getTaskId(), stepCreateData.getStepName());
        return handleInvalidResult(stepName, validationResult);
    }

    private void updateStepTemplate(StepTemplate stepTemplate) {
        if (validateName(stepNameView, stepTemplate)) {
            stepTemplateRepository.update(stepTemplate);
            showToastMessage(R.string.step_saved_message);
            getFragmentManager().popBackStack();
        }
    }

    private boolean validateName(EditText stepName, StepTemplate stepTemplate) {
        ValidationResult validationResult = stepValidation
                .isUpdateNameValid(stepTemplate.getId(),
                        stepTemplate.getTaskTemplateId(),
                        stepTemplate.getName());
        return handleInvalidResult(stepName, validationResult);
    }

    private boolean handleInvalidResult(EditText editText, ValidationResult validationResult) {
        if (validationResult.getValidationStatus().equals(ValidationStatus.INVALID)) {
            editText.setError(validationResult.getValidationInfo());
            return false;
        }
        return true;
    }

    private void showToastMessage(int resourceStringId) {
        toastUserNotifier.displayNotifications(
                resourceStringId,
                getActivity().getApplicationContext());
    }

    private int countStepOrder(Long taskId) {
        return stepTemplateRepository.getAll(taskId).size();
    }
}
