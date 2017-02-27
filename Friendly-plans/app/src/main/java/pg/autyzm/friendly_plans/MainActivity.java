package pg.autyzm.friendly_plans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity.java";

  EditText taskName;
  EditText taskPicture;
  EditText taskSound;
  EditText taskDurTime;
  Button taskNext;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    
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
        taskName.getText().toString();
        Log.i("taskName :", "{" + taskName.getText().toString() + "}");
        Log.i("taskPicture :", "{" + taskSound.getText().toString() + "}");
        Log.i("taskSound :", "{" + taskDurTime.getText().toString() + "}");
        Log.i("taskDurTime :", "{" + taskDurTime.getText().toString() + "}");
      }
    });
  }
}
