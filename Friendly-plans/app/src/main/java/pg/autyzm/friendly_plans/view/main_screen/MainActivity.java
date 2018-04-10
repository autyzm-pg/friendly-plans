package pg.autyzm.friendly_plans.view.main_screen;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.ActivityMainBinding;
import pg.autyzm.friendly_plans.view.plan_create.PlanCreateActivity;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateActivity;
import pg.autyzm.friendly_plans.view.task_list.TaskListActivity;

public class MainActivity extends AppCompatActivity implements MainActivityEvents {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setEvents(this);
    }

    @Override
    public void eventCreateTask(View view) {
        Intent intent = new Intent(this, TaskCreateActivity.class);
        startActivity(intent);
    }

    @Override
    public void eventShowTaskList(View view) {
        Intent intent = new Intent(this, TaskListActivity.class);
        startActivity(intent);
    }

    @Override
    public void eventCreatePlan(View view) {
        Intent intent = new Intent(this, PlanCreateActivity.class);
        startActivity(intent);
    }
}
