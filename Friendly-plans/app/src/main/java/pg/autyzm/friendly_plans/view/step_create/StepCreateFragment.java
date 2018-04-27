package pg.autyzm.friendly_plans.view.step_create;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.MediaPlayer;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import database.entities.Asset;
import database.entities.StepTemplate;
import database.repository.AssetRepository;
import database.repository.StepTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.databinding.FragmentStepCreateBinding;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.view.components.SoundComponent;
import pg.autyzm.friendly_plans.view.task_create.ImagePreviewDialog;

public class StepCreateFragment extends Fragment implements StepCreateEvents.StepData {

    @Inject
    MediaPlayer mediaPlayer;
    @Inject
    StepTemplateRepository stepTemplateRepository;
    @Inject
    AssetRepository assetRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;
    @Inject
    public FilePickerProxy filePickerProxy;

    private Long soundId;
    private Long taskId;
    private Long stepId;
    private StepCreateData stepData;
    private StepTemplate step;
    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";
    private Long pictureId;
    private EditText stepPicture;
    private ImageButton clearPicture;
    private EditText stepSound;
    private ImageButton clearSound;
    private ImageView picturePreview;
    private SoundComponent soundComponent;

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

        String stepName = "";

        Bundle arguments = getArguments();
        if (arguments != null) {
            if(arguments.containsKey(ActivityProperties.TASK_ID)) {
                taskId = (Long) arguments.get(ActivityProperties.TASK_ID);
            }
            if(arguments.containsKey(ActivityProperties.STEP_ID)) {
                stepId = (Long) arguments.get(ActivityProperties.STEP_ID);
                step = stepTemplateRepository.get(stepId);
                stepName = step.getName();
            }
        }

        stepData = new StepCreateData(stepName, "", "");
        binding.setStepData(stepData);
        binding.setStepDataClick(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        stepPicture = (EditText) view.findViewById(R.id.id_et_step_picture);
        clearPicture = (ImageButton) view.findViewById(R.id.id_ib_step_clear_img_btn);
        picturePreview = (ImageView) view.findViewById(R.id.iv_step_picture_preview);
        stepSound = (EditText) view.findViewById(R.id.id_et_step_sound);
        clearSound = (ImageButton) view.findViewById(R.id.id_ib_clear_step_sound_btn);

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

    private Long addOrUpdateStepToTask(String stepName, int order) {
        try {
            if(stepId != null) {
                stepTemplateRepository.update(stepId, stepName, order,
                        pictureId,
                        soundId, taskId);
                showToastMessage(R.string.step_saved_message);
                return stepId;
            } else {
                long stepId = stepTemplateRepository.create(stepName, order,
                        pictureId,
                        soundId, taskId);
                showToastMessage(R.string.step_saved_message);
                return stepId;
            }
        } catch (RuntimeException exception) {
            return handleSavingError(exception);
        }
    }

    @Override
    public void selectStepPicture() {
        filePickerProxy.openFilePicker(StepCreateFragment.this, AssetType.PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (filePickerProxy.isFilePicked(resultCode)) {
            if (filePickerProxy.isPickFileRequested(requestCode, AssetType.PICTURE)) {
                handleAssetSelecting(data, AssetType.PICTURE);
            } else if (filePickerProxy.isPickFileRequested(requestCode, AssetType.SOUND)) {
                handleAssetSelecting(data, AssetType.SOUND);
            }
        }
    }

    @Override
    public void cleanStepPicture() {
        stepPicture.setText("");
        picturePreview.setImageResource(0);
        clearPicture.setVisibility(View.INVISIBLE);
        pictureId = null;
    }

    private void setAssetValue(AssetType assetType, String assetName, Long assetId) {

        String assetNameTrimed = assetName.replaceAll(REGEX_TRIM_NAME, "");

        if (assetType.equals(AssetType.PICTURE)) {
            stepData.setPictureName(assetNameTrimed);
            clearPicture.setVisibility(View.VISIBLE);
            pictureId = assetId;
            showPreview();
        } else {
            stepData.setSoundName(assetNameTrimed);
            soundId = assetId;
            soundComponent.setSoundId(assetId);
            clearSound.setVisibility(View.VISIBLE);
        }
    }

    private void showPreview() {
        Picasso.with(getActivity().getApplicationContext())
                .load(new File(retrieveImageFile()))
                .into(picturePreview);
    }

    private String retrieveImageFile() {
        String imageFileName = assetRepository.get(pictureId).getFilename();
        String fileDir = getActivity().getApplicationContext().getFilesDir().toString();
        return fileDir + File.separator + imageFileName;
    }


    private void handleAssetSelecting(Intent data, AssetType assetType) {
        Context context = getActivity().getApplicationContext();
        String filePath = filePickerProxy.getFilePath(data);
        AssetsHelper assetsHelper = new AssetsHelper(context);
        try {
            String assetName = assetsHelper.makeSafeCopy(filePath);
            Long assetId = assetRepository
                    .create(AssetType.getTypeByExtension(assetName), assetName);
            setAssetValue(assetType, assetName, assetId);
        } catch (IOException e) {
            showToastMessage(R.string.picking_file_error);
        }
    }

    @Override
    public void showPicture() {
        ImagePreviewDialog preview = new ImagePreviewDialog();
        Bundle args = new Bundle();
        args.putString("imgPath", retrieveImageFile());
        preview.setArguments(args);
        preview.show(getFragmentManager(), "preview");
    }

    @Override
    public void selectStepSound() {
        filePickerProxy.openFilePicker(StepCreateFragment.this, AssetType.SOUND);
    }

    @Override
    public void clearStepSound() {
        stepSound.setText("");
        soundId = null;
        clearSound.setVisibility(View.INVISIBLE);
        soundComponent.setSoundId(null);
        soundComponent.stopActions();

    }

    @Override
    public void saveStepData(StepCreateData stepCreateData) {
        String name = stepCreateData.getStepName();
        String picture = stepCreateData.getPictureName();
        String sound = stepCreateData.getSoundName();
        Long stepId = addOrUpdateStepToTask(name, 0);
        Log.i("step data", name + " " + picture + " " + sound+ " " + stepId);
        if(stepId != null) getFragmentManager().popBackStack();
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
