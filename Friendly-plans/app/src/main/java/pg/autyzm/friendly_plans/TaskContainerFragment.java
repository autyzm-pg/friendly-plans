package pg.autyzm.friendly_plans;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import database.repository.TaskTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.utils.Utils;
import pg.autyzm.friendly_plans.validation.TaskValidation;

public class TaskContainerFragment extends Fragment {

    @Inject
    TaskValidation taskValidation;

    @Inject
    TaskTemplateRepository taskTemplateRepository;

    private TextView labelTaskName;
    private TextView labelDurationTime;
    private EditText taskName;
    private EditText taskPicture;
    private EditText taskSound;
    private EditText taskDurTime;
    private Button taskNext;

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
                Integer.valueOf(taskDurTime.getText().toString()));
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
    }
}

