package pg.autyzm.friendly_plans;

import android.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import database.repository.TaskTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.utils.Utils;
import pg.autyzm.friendly_plans.validation.TaskValidation;

public class TaskContainerFragment extends Fragment {

    @Inject
    TaskTemplateRepository taskTemplateRepository;

    private TextView labelTaskName;
    private EditText taskName;
    private EditText taskPicture;
    private EditText taskSound;
    private EditText taskDurTime;
    private Button taskNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getRepositoryComponent().inject(this);
        return inflater.inflate(R.layout.fragment_task_container, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        registerViews(view);
        taskNext.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                if (TaskValidation.isAllValidationOK(taskName, taskDurTime)) {
                    goToNextPage();   // and start next activity
                }
                }
            }
        );

    }

    private void goToNextPage() {
        // create intent and start new activity
        // TODO: implement new intent and its extras
        //Toast is here for testing purposes.

        Toast toast = Toast
                .makeText(getActivity(), "All GOOD! Go to next page.", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    private void registerViews(View view) {

        labelTaskName = (TextView) view.findViewById(R.id.id_tv_task_name_label);
        Utils.markFieldMandatory(labelTaskName);// red (*) is here

        taskName = (EditText) view.findViewById(R.id.id_et_task_name);
        taskPicture = (EditText) view.findViewById(R.id.id_et_task_picture);
        taskSound = (EditText) view.findViewById(R.id.id_et_task_sound);
        taskDurTime = (EditText) view.findViewById(R.id.id_et_task_duration_time);
        taskNext = (Button) view.findViewById(R.id.id_btn_task_next);
    }
}

