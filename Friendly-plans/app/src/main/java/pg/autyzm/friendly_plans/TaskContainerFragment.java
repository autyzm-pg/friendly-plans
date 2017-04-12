package pg.autyzm.friendly_plans;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import database.repository.AssetRepository;
import database.repository.TaskTemplateRepository;
import java.io.IOException;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.validation.TaskValidation;
import pg.autyzm.friendly_plans.validation.Utils;

public class TaskContainerFragment extends Fragment {

    @Inject
    TaskValidation taskValidation;

    @Inject
    TaskTemplateRepository taskTemplateRepository;

    @Inject
    AssetRepository assetRepository;

    @Inject
    public FilePickerProxy filePickerProxy;

    private TextView labelTaskName;
    private TextView labelDurationTime;
    private EditText taskName;
    private EditText taskPicture;
    private EditText taskSound;
    private EditText taskDurTime;
    private Button taskNext;
    private Button selectPicture;
    private Long pictureId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        return inflater.inflate(R.layout.fragment_task_container, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        registerViews(view);
        taskNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (taskValidation.isValid(taskName, taskDurTime)) {
                    addTaskOnDb();
                    goToNextPage();
                }
            }
        });
    }

    private void addTaskOnDb() {
        taskTemplateRepository.create(taskName.getText().toString(),
                Integer.valueOf(taskDurTime.getText().toString()), pictureId);
    }

    private void goToNextPage() {
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

        selectPicture.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                filePickerProxy.openImageFilePicker(TaskContainerFragment.this);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (isSelectPictureResult(requestCode, resultCode)) {
            handlePictureSelecting(data);
        }
    }

    private boolean isSelectPictureResult(int requestCode, int resultCode) {
        return filePickerProxy.isPickFileRequested(requestCode) && filePickerProxy
                .isFilePicked(resultCode);
    }

    private void handlePictureSelecting(Intent data) {
        Context context = getActivity().getApplicationContext();
        String filePath = filePickerProxy.getFilePath(data);
        AssetsHelper assetsHelper = new AssetsHelper(context);
        try {
            String assetName = assetsHelper.makeSafeCopy(filePath);
            pictureId = assetRepository
                    .create(AssetType.getTypeByExtension(assetName), assetName);
            taskPicture.setText(assetName);

        } catch (IOException e) {
            String pickingFileError = getResources().getString(R.string.picking_file_error);
            Toast errorToast = Toast.makeText(context, pickingFileError, Toast.LENGTH_LONG);
            errorToast.show();
        }
    }


}

