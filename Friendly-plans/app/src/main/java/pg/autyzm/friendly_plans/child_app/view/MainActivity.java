package pg.autyzm.friendly_plans.child_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import database.entities.ChildPlan;
import database.repository.ChildPlanRepository;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.child_app.utility.Consts;
import pg.autyzm.friendly_plans.child_app.view.task_list.TaskListActivity;
import pg.autyzm.friendly_plans.child_app.view.task_slides.TaskSlidesActivity;

public class MainActivity extends AppCompatActivity {

    @Inject
    ChildPlanRepository childPlanRepository;

    ChildPlan activePlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        activePlan = childPlanRepository.getActivePlan();
        if (activePlan != null) {
            Intent tasksDisplay;
            if (childPlanRepository.getActivePlan().getChild().getTasksDisplayMode().equals("Slide"))
                tasksDisplay = new Intent(MainActivity.this, TaskSlidesActivity.class);
            else
                tasksDisplay = new Intent(MainActivity.this, TaskListActivity.class);
            startActivityForResult(tasksDisplay, Consts.RETURN_MESSAGE_CODE);
        }
        setUpView();
    }

    private void setUpView() {
        setContentView(R.layout.child_app_activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null){
            String returnMessage = data.getStringExtra(Consts.RETURN_MESSAGE_KEY);
            if (returnMessage != null){
                if (returnMessage.equals(Consts.MESSAGE_TASKS_COMPLETED))
                    setContentView(R.layout.activity_child_app_finish_screen);
                else
                    finish();
            }
        }
    }
}
