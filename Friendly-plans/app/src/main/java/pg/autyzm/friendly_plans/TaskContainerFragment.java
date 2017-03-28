package pg.autyzm.friendly_plans;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import database.repository.TaskTemplateRepository;
import javax.inject.Inject;

public class TaskContainerFragment extends Fragment {

    @Inject
    TaskTemplateRepository taskTemplateRepository;

    private EditText taskName;
    private EditText taskPicture;
    private EditText taskSound;
    private EditText taskDurTime;
    private Button taskNext;
    private Button selectPicture;

    public FilePickerProxy filePickerProxy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
//        ((App) getActivity().getApplication()).getRepositoryComponent().inject(this);
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        return inflater.inflate(R.layout.fragment_task_container, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        filePickerProxy = new FilePickerProxy();

        taskName = (EditText) view.findViewById(R.id.id_et_task_name);
        taskPicture = (EditText) view.findViewById(R.id.id_et_task_picture);
        taskSound = (EditText) view.findViewById(R.id.id_et_task_sound);
        taskDurTime = (EditText) view.findViewById(R.id.id_et_task_duration_time);
        taskNext = (Button) view.findViewById(R.id.id_btn_task_next);

        taskNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("taskName :", "{" + taskName.getText().toString() + "}");
                Log.i("taskPicture :", "{" + taskPicture.getText().toString() + "}");
                Log.i("taskSound :", "{" + taskSound.getText().toString() + "}");
                Log.i("taskDurTime :", "{" + taskDurTime.getText().toString() + "}");

                long id = taskTemplateRepository.create(taskName.getText().toString(),
                        Integer.valueOf(taskDurTime.getText().toString()));
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
            taskPicture.setText(filePickerProxy.getFilePath(data));
        }
    }



}

