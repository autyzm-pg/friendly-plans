package pg.autyzm.friendly_plans;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import database.entities.Asset;
import database.entities.AssetType;
import database.repository.AssetRepository;
import database.repository.TaskTemplateRepository;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.inject.Inject;

public class TaskContainerFragment extends Fragment {

    private String PICKING_FILE_ERROR;

    @Inject
    TaskTemplateRepository taskTemplateRepository;

    @Inject
    AssetRepository assetRepository;

    @Inject
    public FilePickerProxy filePickerProxy;

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
        PICKING_FILE_ERROR = getResources().getString(R.string.picking_file_error);
        return inflater.inflate(R.layout.fragment_task_container, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        taskName = (EditText) view.findViewById(R.id.id_et_task_name);
        taskPicture = (EditText) view.findViewById(R.id.id_et_task_picture);
        taskSound = (EditText) view.findViewById(R.id.id_et_task_sound);
        taskDurTime = (EditText) view.findViewById(R.id.id_et_task_duration_time);
        taskNext = (Button) view.findViewById(R.id.id_btn_task_next);

        taskNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long id = taskTemplateRepository.create(taskName.getText().toString(),
                        Integer.valueOf(taskDurTime.getText().toString()), pictureId);
                Log.i("id :", "{" + String.valueOf(id) + "}");
            }
        });

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

        if(filePickerProxy.isPickFileRequested(requestCode) && filePickerProxy.isFilePicked(resultCode)) {
            Context context = getActivity().getApplicationContext();
            String filePath = filePickerProxy.getFilePath(data);
            AssetsHelper assetsHelper = new AssetsHelper(context);
            try {
                String assetName = assetsHelper.makeSafeCopy(filePath);
                pictureId = assetRepository.create(AssetType.getTypeByExtension(assetName), assetName);
                taskPicture.setText(assetName);

            } catch (IOException e) {
                Toast errorToast = Toast.makeText(context, PICKING_FILE_ERROR, Toast.LENGTH_LONG);
                errorToast.show();
            }

        }
    }



}

