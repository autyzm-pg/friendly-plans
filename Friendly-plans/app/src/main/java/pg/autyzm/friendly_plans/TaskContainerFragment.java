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
import pg.autyzm.friendly_plans.utils.Validation;

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
                                            if (isAllValidationOK()) {
                                                createTaskInDb(); // fulfill the method to create task on db
                                                goToNextPage();   // and start next activity
                                            }
                                        }
                                    }
        );

    }


    private boolean isAllValidationOK() {
        if (isFormOk()) {
            if (isNameFree()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    //every mandatory field in the form is validated here
    private boolean isFormOk() {
        boolean ret = true;

        if (!Validation.hasText(taskName)) {
            ret = false;
        }
        if (!Validation.isNameOk(taskName, true)) {
            ret = false;
        }
        if (!Validation.isNumber(taskDurTime, false)) {
            ret = false;
        }
        // add validation on other fields if required
        return ret;
    }

    // search name in db
    private boolean isNameFree() {
        //TODO: implement getSingleNameFromBd(name) method on repository!
        int namesCount = taskTemplateRepository.get(taskName.getText().toString()).size();

        if (!(namesCount > 0)) {
            Validation.isNameEmpty(taskName, false);
            return false;
        } else {
            return true;
        }
    }

    private void createTaskInDb() {

        /***
         * create record on db
         * TODO: implement save new task in table functionality
         */
    }

    private void goToNextPage() {
        /***
         * create intent and start new activity
         * TODO: implement new intent and its extras
         */
    }

    private void registerViews(View view) {

        labelTaskName = (TextView) view.findViewById(R.id.id_tv_task_name_label);
        Utils.markFieldtMandatory(labelTaskName);

        taskName = (EditText) view.findViewById(R.id.id_et_task_name);
        taskPicture = (EditText) view.findViewById(R.id.id_et_task_picture);
        taskSound = (EditText) view.findViewById(R.id.id_et_task_sound);
        taskDurTime = (EditText) view.findViewById(R.id.id_et_task_duration_time);
        taskNext = (Button) view.findViewById(R.id.id_btn_task_next);
    }
}

