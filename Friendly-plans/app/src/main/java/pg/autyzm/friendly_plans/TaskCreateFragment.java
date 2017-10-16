package pg.autyzm.friendly_plans;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import database.repository.AssetRepository;
import database.repository.TaskTemplateRepository;
import java.io.File;
import java.io.IOException;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.validation.TaskValidation;
import pg.autyzm.friendly_plans.validation.Utils;

public class TaskCreateFragment extends Fragment {

    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";
    private static final String TASK_ID = "task_id";

    @Inject
    public FilePickerProxy filePickerProxy;
    @Inject
    TaskValidation taskValidation;
    @Inject
    TaskTemplateRepository taskTemplateRepository;
    @Inject
    AssetRepository assetRepository;
    @Inject
    MediaPlayer mp;

    private Animation rotation;
    private TextView labelTaskName;
    private TextView labelDurationTime;
    private EditText taskName;
    private EditText taskPicture;
    private EditText taskSound;
    private EditText taskDurTime;
    private Button taskNext;
    private Button selectPicture;
    private Button selectSound;
    private ImageButton clearSound;
    private ImageButton clearPicture;
    private Button playSound;
    private ImageView picturePreview;
    private ImageView playSoundIcon;
    private Long pictureId;
    private Long soundId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        return inflater.inflate(R.layout.fragment_task_create, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        registerViews(view);
    }

    private long addTaskOnDb() {
        return taskTemplateRepository.create(taskName.getText().toString(),
                Integer.valueOf(taskDurTime.getText().toString()),
                pictureId,
                soundId);
    }

    private void registerViews(View view) {

        labelTaskName = (TextView) view.findViewById(R.id.id_tv_task_name_label);
        Utils.markFieldMandatory(labelTaskName);
        labelDurationTime = (TextView) view.findViewById(R.id.id_tv_task_duration_time);
        Utils.markFieldMandatory(labelDurationTime);
        taskName = (EditText) view.findViewById(R.id.id_et_task_name);
        taskPicture = (EditText) view.findViewById(R.id.id_et_task_picture);
        taskSound = (EditText) view.findViewById(R.id.id_et_task_sound);
        taskDurTime = (EditText) view.findViewById(R.id.id_et_task_duration_time);
        taskNext = (Button) view.findViewById(R.id.id_btn_task_next);
        selectPicture = (Button) view.findViewById(R.id.id_btn_select_task_picture);
        picturePreview = (ImageView) view.findViewById(R.id.iv_picture_preview);
        selectSound = (Button) view.findViewById(R.id.id_btn_select_task_sound);
        playSound = (Button) view.findViewById(R.id.id_btn_play_sound);
        clearSound = (ImageButton) view.findViewById(R.id.id_ib_clear_sound_btn);
        clearPicture = (ImageButton) view.findViewById(R.id.id_ib_clear_img_btn);
        playSoundIcon = (ImageView) view.findViewById(R.id.id_iv_play_sound_icon);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

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

        playSound.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                playStopSound();
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
                stopSound();
                stopBtnAnimation();
            }
        });

        taskNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                stopSound();
                stopBtnAnimation();
                if (taskValidation.isValid(taskName, taskDurTime)) {
                    long taskId = addTaskOnDb();
                    showToastMessage(R.string.task_saved_message);

                    showStepsList(taskId);
                }
            }
        });
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer player) {
                mp.reset();
                stopBtnAnimation();
            }
        });
    }

    private void showStepsList(long taskId) {
        StepListFragment fragment = new StepListFragment();
        Bundle args = new Bundle();
        args.putLong(TASK_ID, taskId);
        fragment.setArguments(args);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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

    private void playStopSound() {
        boolean isNameEmpty = taskSound.getText().toString().isEmpty();
        if (!isNameEmpty) {
            if (!mp.isPlaying()) {
                playSound(retrieveSoundFile());
                runBtnAnimation();
            } else {
                stopSound();
                stopBtnAnimation();
            }
        } else {
            showToastMessage(R.string.no_file_to_play_error);
        }
    }

    private void playSound(String pathToFile) {
        try {
            mp.reset();
            mp.setDataSource(pathToFile);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            showToastMessage(R.string.playing_file_error);
        }
    }

    private String retrieveSoundFile() {
        String soundFileName = assetRepository.get(soundId).getFilename();
        String fileDir = getActivity().getApplicationContext().getFilesDir().toString();
        return fileDir + File.separator + soundFileName;
    }

    private String retrieveImageFile() {
        String imageFileName = assetRepository.get(pictureId).getFilename();
        String fileDir = getActivity().getApplicationContext().getFilesDir().toString();
        return fileDir + File.separator + imageFileName;
    }

    private void stopSound() {
        mp.stop();
        mp.reset();
    }

    private void runBtnAnimation() {
        playSoundIcon.setImageResource(R.drawable.ic_playing_sound_2);
        rotation = AnimationUtils
                .loadAnimation(getActivity().getApplicationContext(),
                        R.anim.ic_play_sound_animation);
        playSoundIcon.startAnimation(rotation);
    }

    private void stopBtnAnimation() {
        playSoundIcon.clearAnimation();
        playSoundIcon.setImageResource(R.drawable.ic_play_sound_1);
    }

    private void showToastMessage(int resourceStringId) {
        ToastUserNotifier.displayNotifications(
            resourceStringId,
            getActivity().getApplicationContext());
    }
}

