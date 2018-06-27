package pg.autyzm.friendly_plans.manager_app.view.task_create;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import database.entities.Asset;
import database.entities.TaskTemplate;
import database.repository.TaskTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.databinding.FragmentTaskCreateBinding;
import pg.autyzm.friendly_plans.manager_app.validation.TaskValidation;
import pg.autyzm.friendly_plans.manager_app.validation.Utils;
import pg.autyzm.friendly_plans.manager_app.validation.ValidationResult;
import pg.autyzm.friendly_plans.manager_app.view.components.SoundComponent;
import pg.autyzm.friendly_plans.manager_app.view.main_screen.MainActivity;
import pg.autyzm.friendly_plans.manager_app.view.step_list.StepListFragment;
import pg.autyzm.friendly_plans.manager_app.view.view_fragment.CreateFragment;

public class TaskCreateFragment extends CreateFragment implements TaskCreateActivityEvents {

    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";

    @Inject
    TaskValidation taskValidation;
    @Inject
    TaskTemplateRepository taskTemplateRepository;

    private TextView labelTaskName;
    private EditText taskName;
    private EditText taskDurationTime;
    private Long taskId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        FragmentTaskCreateBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_create, container, false);
        binding.setEvents(this);

        View view = binding.getRoot();
        ImageButton playSoundIcon = (ImageButton) view.findViewById(R.id.id_btn_play_sound);

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
        view.post(new Runnable() {  // Set assets only when the layout is completely built
            @Override
            public void run() {
                Bundle arguments = getArguments();
                if (arguments != null) {
                    Long taskId = (Long) arguments.get(ActivityProperties.TASK_ID);

                    if (taskId != null) {
                        initTaskForm(taskId);
                    }
                }
            }
        });
    }

    private void registerViews(View view) {
        labelTaskName = (TextView) view.findViewById(R.id.id_tv_task_name_label);
        Utils.markFieldMandatory(labelTaskName);
        taskName = (EditText) view.findViewById(R.id.id_et_task_name);
        pictureFileName = (EditText) view.findViewById(R.id.id_et_task_picture);
        soundFileName = (EditText) view.findViewById(R.id.id_et_task_sound);
        taskDurationTime = (EditText) view.findViewById(R.id.id_et_task_duration_time);
        picturePreview = (ImageView) view.findViewById(R.id.iv_picture_preview);
        clearSound = (ImageButton) view.findViewById(R.id.id_ib_clear_sound_btn);
        clearPicture = (ImageButton) view.findViewById(R.id.id_ib_clear_img_btn);
    }

    private Long saveOrUpdate() {
        soundComponent.stopActions();
        try {
            if (taskId != null) {
                if (validateName(taskId, taskName) && validateDuration(taskDurationTime)) {
                    Integer duration = getDuration();
                    taskTemplateRepository.update(taskId,
                            taskName.getText().toString(),
                            duration,
                            pictureId,
                            soundId);
                    showToastMessage(R.string.task_saved_message);

                    return taskId;
                }
            } else {
                if (validateName(taskName) && validateDuration(taskDurationTime)) {
                    Integer duration = getDuration();
                    long taskId = taskTemplateRepository.create(taskName.getText().toString(),
                            duration,
                            pictureId,
                            soundId);
                    showToastMessage(R.string.task_saved_message);

                    return taskId;
                }
            }
        } catch (RuntimeException exception) {
            Log.e("Task Create View", "Error saving task", exception);
            showToastMessage(R.string.save_task_error_message);
        }
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

    private void initTaskForm(long taskId) {
        this.taskId = taskId;

        TaskTemplate task = taskTemplateRepository.get(taskId);
        taskName.setText(task.getName());
        if (task.getDurationTime() != null){
            taskDurationTime.setText(String.valueOf(task.getDurationTime()));
        }
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
    protected void setAssetValue(AssetType assetType, String assetName, Long assetId) {

        String assetNameTrimmed = assetName.replaceAll(REGEX_TRIM_NAME, "");

        if (assetType.equals(AssetType.PICTURE)) {
            pictureFileName.setText(assetNameTrimmed);
            clearPicture.setVisibility(View.VISIBLE);
            pictureId = assetId;
            showPreview(pictureId, picturePreview);
        } else {
            soundFileName.setText(assetNameTrimmed);
            clearSound.setVisibility(View.VISIBLE);
            soundId = assetId;
            soundComponent.setSoundId(soundId);
        }
    }

    private Integer getDuration() {
        if (!taskDurationTime.getText().toString().isEmpty() &&
                !taskDurationTime.getText().toString().equals("0")) {
            return Integer.valueOf(taskDurationTime.getText().toString());
        }
        return null;
    }

    @Override
    public void eventListStep(View view) {
        taskId = saveOrUpdate();
        if (taskId != null) {
            showStepsList(taskId);
        }
    }

    @Override
    public void eventSelectPicture(View view) {
        filePickerProxy.openFilePicker(TaskCreateFragment.this, AssetType.PICTURE);
    }

    @Override
    public void eventSelectSound(View view) {
        filePickerProxy.openFilePicker(TaskCreateFragment.this, AssetType.SOUND);
    }

    @Override
    public void eventClearPicture(View view) {
        clearPicture();
    }

    @Override
    public void eventClearSound(View view) {
        clearSound();
    }

    @Override
    public void eventClickPreviewPicture(View view) {
        showPicture(pictureId);

    }

    @Override
    public void eventSaveAndFinish(View view) {
        Long taskId = saveOrUpdate();
        if (taskId != null) {
            showMainMenu();
        }
    }
}

