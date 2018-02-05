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
import database.repository.AssetRepository;
import database.repository.StepTemplateRepository;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
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

    private ImageView playSoundIcon;
    private Long soundId;
    private Long taskId;
    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";
    private Long pictureId;
    private EditText stepPicture;
    private ImageButton clearPicture;
    private ImageView picturePreview;


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
        stepPicture = (EditText) view.findViewById(R.id.id_et_step_picture);
        clearPicture = (ImageButton) view.findViewById(R.id.id_ib_step_clear_img_btn);
        playSoundIcon = (ImageView) view.findViewById(R.id.id_iv_play_step_sound_icon);
        picturePreview = (ImageView) view.findViewById(R.id.iv_step_picture_preview);
        Bundle arguments = getArguments();
        if (arguments != null) {
            taskId = (Long) arguments.get(ActivityProperties.TASK_ID);
        }
    }

    private Long addStepToTask(String stepName, int order) {
        try {
            long stepId = stepTemplateRepository.create(stepName, order,
                    pictureId,
                    (Long) null, taskId);
            showToastMessage(R.string.step_saved_message);
            return stepId;

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
            }
        }
    }

    @Override
    public void cleanStepPicture() {
        stepPicture.setText("");
        picturePreview.setImageResource(0);
        clearPicture.setVisibility(View.INVISIBLE);
    }

    private void setAssetValue(AssetType assetType, String assetName, Long assetId) {

        assetName = assetName.replaceAll(REGEX_TRIM_NAME, "");

        if (assetType.equals(AssetType.PICTURE)) {
            stepPicture.setText(assetName);
            clearPicture.setVisibility(View.VISIBLE);
            pictureId = assetId;
            showPreview();
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
    public void saveStepData(StepCreateData stepCreateData) {
        String name = stepCreateData.getStepName();
        String picture = stepCreateData.getPictureName();
        String sound = stepCreateData.getSoundName();
        Long stepId = addStepToTask(name, 0);
        Log.i("step data", name + " " + picture + " " + sound + " " + stepId);
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
