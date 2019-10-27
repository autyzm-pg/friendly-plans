package pg.autyzm.friendly_plans.child_app.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;

import database.entities.ChildPlan;
import database.repository.ChildPlanRepository;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.child_app.view.task_list.TaskListActivity;

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
            Intent taskList = new Intent(MainActivity.this, TaskListActivity.class);
            startActivity(taskList);
        }
        else
            setUpView();
    }

    private void setUpView() {
        setContentView(R.layout.child_app_activity_main);
    }

}
