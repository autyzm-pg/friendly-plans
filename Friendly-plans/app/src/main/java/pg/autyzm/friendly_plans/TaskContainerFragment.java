package pg.autyzm.friendly_plans;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class TaskContainerFragment extends Fragment {

    private EditText taskName;
    private EditText taskPicture;
    private EditText taskSound;
    private EditText taskDurTime;
    private Button taskNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
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
                Log.i("taskName :", "{" + taskName.getText().toString() + "}");
                Log.i("taskPicture :", "{" + taskPicture.getText().toString() + "}");
                Log.i("taskSound :", "{" + taskSound.getText().toString() + "}");
                Log.i("taskDurTime :", "{" + taskDurTime.getText().toString() + "}");
            }
        });
    }
}

