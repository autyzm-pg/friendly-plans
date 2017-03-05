package pg.autyzm.friendly_plans;

import android.os.Bundle;
import dao.DaggerDaoSessionComponent;
import dao.DaoSessionComponent;
import dao.DaoSessionModule;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private DaoSessionComponent daoSessionComponent;

    private EditText taskName;
    private EditText taskPicture;
    private EditText taskSound;
    private EditText taskDurTime;
    private Button taskNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        daoSessionComponent = DaggerDaoSessionComponent.builder()
                .daoSessionModule(new DaoSessionModule(this))
                .build();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_management);

        taskName = (EditText) findViewById(R.id.id_et_task_name);
        taskPicture = (EditText) findViewById(R.id.id_et_task_picture);
        taskSound = (EditText) findViewById(R.id.id_et_task_sound);
        taskDurTime = (EditText) findViewById(R.id.id_et_task_duration_time);
        taskNext = (Button) findViewById(R.id.id_btn_task_next);

        taskNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // temporary solution
                Log.i("taskName :", "{" + taskName.getText().toString() + "}");
                Log.i("taskPicture :", "{" + taskPicture.getText().toString() + "}");
                Log.i("taskSound :", "{" + taskSound.getText().toString() + "}");
                Log.i("taskDurTime :", "{" + taskDurTime.getText().toString() + "}");
            }
        });
    }
}
