package pg.autyzm.friendly_plans.manager_app.view.step_create;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import database.entities.StepTemplate;
import database.repository.AssetRepository;
import java.io.File;
import javax.inject.Inject;

import database.entities.Asset;
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
import pg.autyzm.friendly_plans.manager_app.view.components.SoundComponent;
import pg.autyzm.friendly_plans.manager_app.view.task_create.ImagePreviewDialog;
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
    @Inject
    AssetRepository assetRepository;

    private EditText soundFileName;
    private ImageButton clearSound;
    private EditText pictureFileName;
    private ImageView picturePreview;
    private ImageButton clearPicture;
    private EditText stepNameView;
    private EditText stepDurationView;
    private ImageButton playSoundIcon;

    private SoundComponent soundComponent;
    private StepCreateData stepData;

    @TargetApi(VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        FragmentStepCreateBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_step_create, container, false);

        AppComponent appComponent = ((App) getActivity().getApplication()).getAppComponent();

        stepData = parseArguments();

        soundComponent = SoundComponent.getSoundComponent(
                getSoundId(stepData.getStepTemplate()), playSoundIcon, getActivity().getApplicationContext(), appComponent);
        appComponent.inject(this);

        View view = binding.getRoot();
        binding.setSoundComponent(soundComponent);
        binding.setStepData(stepData);
        binding.setStepDataClick(this);

        return view;
    }

    private static Long getSoundId(StepTemplate stepTemplate) {
        if (stepTemplate == null) {
            return null;
        }

        return stepTemplate.getSoundId();
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        registerViews(view);
        view.post(new Runnable() {
            @Override
            public void run() {
                if (stepData != null && stepData.getStepTemplate() != null) {
                    StepTemplate stepTemplate = stepData.getStepTemplate();

                    Asset picture = stepTemplate.getPicture();
                    if (picture != null) {
                        clearPicture.setVisibility(View.VISIBLE);
                        showPreview(picture.getId(), picturePreview);
                    }

                    Asset sound = stepTemplate.getSound();
                    if (sound != null) {
                        clearSound.setVisibility(View.VISIBLE);
                    }

                }
            }
        });
    }

    @Override
    public void selectStepPicture() {
        filePickerProxy.openFilePicker(StepCreateFragment.this, AssetType.PICTURE);
    }

    @Override
    public void selectStepSound() {
        filePickerProxy.openFilePicker(StepCreateFragment.this, AssetType.SOUND);
    }

    @Override
    public void cleanStepPicture() {
        stepData.setPictureName(null);
        picturePreview.setImageDrawable(null);
        clearPicture.setVisibility(View.INVISIBLE);
    }

    @Override
    public void clearStepSound() {
        stepData.setPictureName(null);
        clearSound.setVisibility(View.INVISIBLE);
        soundComponent.setSoundId(null);
        soundComponent.stopActions();
    }

    @Override
    public void showPicture() {
        ImagePreviewDialog preview = new ImagePreviewDialog();
        Bundle args = new Bundle();
        args.putString("imgPath", retrieveImageFile(stepData.getStepTemplate().getPictureId()));
        preview.setArguments(args);
        preview.show(getFragmentManager(), "preview");
    }

    @Override
    public void saveStepData(StepCreateData stepCreateData) {
        soundComponent.stopActions();

        try {
            StepTemplate stepTemplate = stepCreateData.getStepTemplate();
            if (stepTemplate != null) {
                updateStepTemplate(stepTemplate);
            } else {
                createStepTemplate(stepCreateData);
            }
        } catch (RuntimeException exception) {
            Log.e("Step Create View", "Error saving step", exception);
            showToastMessage(R.string.save_step_error_message);
        }
    }

    private String retrieveImageFile(Long pictureId) {
        String imageFileName = assetRepository.get(pictureId).getFilename();
        String fileDir = getActivity().getApplicationContext().getFilesDir().toString();
        return fileDir + File.separator + imageFileName;
    }

    private void registerViews(View view) {
        stepNameView = (EditText) view.findViewById(R.id.id_et_step_name);
        pictureFileName = (EditText) view.findViewById(R.id.id_et_step_picture);
        clearPicture = (ImageButton) view.findViewById(R.id.id_ib_step_clear_img_btn);
        picturePreview = (ImageView) view.findViewById(R.id.iv_step_picture_preview);
        soundFileName = (EditText) view.findViewById(R.id.id_et_step_sound);
        clearSound = (ImageButton) view.findViewById(R.id.id_ib_clear_step_sound_btn);
        stepDurationView = (EditText) view.findViewById(R.id.id_et_step_duration);
        playSoundIcon = (ImageButton) view.findViewById(R.id.id_btn_play_step_sound);
    }

    private void showPreview(Long pictureId, ImageView picturePreview) {
        Picasso.with(getActivity().getApplicationContext())
                .load(new File(retrieveImageFile(pictureId)))
                .resize(0, picturePreview.getHeight())
                .into(picturePreview);
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
