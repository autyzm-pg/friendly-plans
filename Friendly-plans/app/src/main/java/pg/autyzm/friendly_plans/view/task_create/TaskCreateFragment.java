package pg.autyzm.friendly_plans.view.task_create;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import database.entities.Asset;
import database.entities.TaskTemplate;
import database.repository.AssetRepository;
import database.repository.TaskTemplateRepository;
import java.io.File;
import java.io.IOException;
import java.lang.RuntimeException;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.databinding.FragmentTaskCreateBinding;
import pg.autyzm.friendly_plans.view.components.SoundComponent;
import pg.autyzm.friendly_plans.validation.ValidationResult;
import pg.autyzm.friendly_plans.validation.ValidationStatus;
import pg.autyzm.friendly_plans.view.main_screen.MainActivity;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.validation.TaskValidation;
import pg.autyzm.friendly_plans.validation.Utils;
import pg.autyzm.friendly_plans.view.step_list.StepListFragment;

public class TaskCreateFragment extends Fragment {

    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";

    @Inject
    public FilePickerProxy filePickerProxy;
    @Inject
    TaskValidation taskValidation;
    @Inject
    TaskTemplateRepository taskTemplateRepository;
    @Inject
    AssetRepository assetRepository;

    private Animation rotation;
    private TextView labelTaskName;
    private TextView labelDurationTime;
    private EditText taskName;
    private EditText taskPicture;
    private EditText taskSound;
    private EditText taskDurationTime;
    private Button taskSteps;
    private Button saveAndFinish;
    private Button selectPicture;
    private Button selectSound;
    private ImageButton clearSound;
    private ImageButton clearPicture;
    private ImageView picturePreview;
    private Long pictureId;
    private Long soundId;
    private Long taskId;
    private SoundComponent soundComponent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        FragmentTaskCreateBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_create, container, false);
        View view = binding.getRoot();
        ImageView playSoundIcon = (ImageView) view.findViewById(R.id.id_iv_play_sound_icon);

        AppComponent appComponent = ((App) getActivity().getApplication()).getAppComponent();
        soundComponent = SoundComponent.getSoundComponent(
            soundId, playSoundIcon, getActivity().getApplicationContext(), appComponent);
        appComponent.inject(this);

        binding.setSoundComponent(soundComponent);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        registerViews(view);
        Bundle arguments = getArguments();
        if (arguments != null) {
            Long taskId = (Long) arguments.get(ActivityProperties.TASK_ID);

            if (taskId != null) {
                initTaskForm(taskId);
            }
        }
    }

    private void registerViews(View view) {
        labelTaskName = (TextView) view.findViewById(R.id.id_tv_task_name_label);
        Utils.markFieldMandatory(labelTaskName);
        labelDurationTime = (TextView) view.findViewById(R.id.id_tv_task_duration_time);
        Utils.markFieldMandatory(labelDurationTime);
        taskName = (EditText) view.findViewById(R.id.id_et_task_name);
        taskPicture = (EditText) view.findViewById(R.id.id_et_task_picture);
        taskSound = (EditText) view.findViewById(R.id.id_et_task_sound);
        taskDurationTime = (EditText) view.findViewById(R.id.id_et_task_duration_time);
        taskSteps = (Button) view.findViewById(R.id.id_btn_steps);
        saveAndFinish = (Button) view.findViewById(R.id.id_btn_save_and_finish);
        selectPicture = (Button) view.findViewById(R.id.id_btn_select_task_picture);
        picturePreview = (ImageView) view.findViewById(R.id.iv_picture_preview);
        selectSound = (Button) view.findViewById(R.id.id_btn_select_task_sound);
        clearSound = (ImageButton) view.findViewById(R.id.id_ib_clear_sound_btn);
        clearPicture = (ImageButton) view.findViewById(R.id.id_ib_clear_img_btn);

        selectPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filePickerProxy.openFilePicker(TaskCreateFragment.this, AssetType.PICTURE);
            }
        });

        picturePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ImagePreviewDialog preview = new ImagePreviewDialog();
                Bundle args = new Bundle();
                args.putString("imgPath", retrieveImageFile());
                preview.setArguments(args);
                preview.show(getFragmentManager(), "preview");
            }
        });

        selectSound.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filePickerProxy.openFilePicker(TaskCreateFragment.this, AssetType.SOUND);
            }
        });


        clearPicture.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                taskPicture.setText("");
                picturePreview.setImageResource(0);
                clearPicture.setVisibility(View.INVISIBLE);
            }
        });

        clearSound.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                taskSound.setText("");
                clearSound.setVisibility(View.INVISIBLE);
                soundComponent.stopActions();
            }
        });

        taskSteps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Long taskId = saveOrUpdate();
                if (taskId != null) {
                    showStepsList(taskId);
                }
            }
        });
        saveAndFinish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Long taskId = saveOrUpdate();
                if (taskId != null) {
                    showMainMenu();
                }
            }
        });
    }

    private Long saveOrUpdate() {
        soundComponent.stopActions();
        if (taskId != null) {
            if (validateName(taskId, taskName) && validateDuration(taskDurationTime)) {
                return updateTask(taskId);
            }
        } else {
            if (validateName(taskName) && validateDuration(taskDurationTime)) {
                return addTask();
            }
        }
        return null;
    }

    private Long addTask() {
        try {
            long taskId = taskTemplateRepository.create(taskName.getText().toString(),
                    Integer.valueOf(taskDurationTime.getText().toString()),
                    pictureId,
                    soundId);
            showToastMessage(R.string.task_saved_message);

            return taskId;

        } catch (RuntimeException exception) {
            return handleSavingError(exception);
        }
    }

    private Long updateTask(Long taskId) {
        try {
            taskTemplateRepository.update(taskId,
                    taskName.getText().toString(),
                    Integer.valueOf(taskDurationTime.getText().toString()),
                    pictureId,
                    soundId);
            showToastMessage(R.string.task_saved_message);

            return taskId;

        } catch (RuntimeException exception) {
            return handleSavingError(exception);
        }
    }

    @Nullable
    private Long handleSavingError(RuntimeException exception) {
        Log.e("Task Create View", "Error saving task", exception);
        showToastMessage(R.string.save_task_error_message);
        return null;
    }

    private boolean validateName(Long taskId, EditText taskName) {
        ValidationResult validationResult = taskValidation
                .isUpdateNameValid(taskId, taskName.getText().toString());
        return handleInvalidResult(taskName, validationResult);
    }

    private boolean validateName(EditText taskName) {
        ValidationResult validationResult = taskValidation
                .isNewNameValid(taskName.getText().toString());
        return handleInvalidResult(taskName, validationResult);
    }

    private boolean validateDuration(EditText duration) {
        ValidationResult validationResult = taskValidation
                .isDurationValid(duration.getText().toString());
        return handleInvalidResult(duration, validationResult);
    }

    private boolean handleInvalidResult(EditText editText, ValidationResult validationResult) {
        if (validationResult.getValidationStatus().equals(ValidationStatus.INVALID)) {
            editText.setError(validationResult.getValidationInfo());
            return false;
        }

        return true;
    }

    private void initTaskForm(long taskId) {
        this.taskId = taskId;

        TaskTemplate task = taskTemplateRepository.get(taskId);
        taskName.setText(task.getName());
        taskDurationTime.setText(String.valueOf(task.getDurationTime()));
        Asset picture = task.getPicture();
        Asset sound = task.getSound();
        if (picture != null) {
            setAssetValue(AssetType.PICTURE, picture.getFilename(), picture.getId());
        }
        if (sound != null) {
            setAssetValue(AssetType.SOUND, sound.getFilename(), sound.getId());
        }
    }

    private void showStepsList(final long taskId) {
        StepListFragment fragment = new StepListFragment();
        Bundle args = new Bundle();
        args.putLong(ActivityProperties.TASK_ID, taskId);
        fragment.setArguments(args);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showMainMenu() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
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

    private void setAssetValue(AssetType assetType, String assetName, Long assetId) {

        assetName = assetName.replaceAll(REGEX_TRIM_NAME, "");

        if (assetType.equals(AssetType.PICTURE)) {
            taskPicture.setText(assetName);
            clearPicture.setVisibility(View.VISIBLE);
            pictureId = assetId;
            showPreview();
        } else {
            taskSound.setText(assetName);
            soundId = assetId;
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

    private void showToastMessage(int resourceStringId) {
        ToastUserNotifier.displayNotifications(
                resourceStringId,
                getActivity().getApplicationContext());
    }
}

