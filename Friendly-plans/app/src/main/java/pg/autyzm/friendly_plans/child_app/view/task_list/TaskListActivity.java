package pg.autyzm.friendly_plans.child_app.view.task_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import database.entities.ChildPlan;
import database.entities.TaskTemplate;
import database.repository.ChildPlanRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.child_app.view.step_list.StepListActivity;

public class TaskListActivity extends AppCompatActivity {

    @Inject
    ChildPlanRepository childPlanRepository;
    private TaskRecyclerViewAdapter taskRecyclerViewAdapter;

    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener =
            new TaskRecyclerViewAdapter.TaskItemClickListener() {

                @Override
                public void stepsIconListener(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(ActivityProperties.TASK_ID,
                            taskRecyclerViewAdapter.getTaskItem(position).getId());

                    Intent intent = new Intent(TaskListActivity.this, StepListActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.child_app_task_list);
        setRecyclerView();
    }

    void setRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_child_app_task_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ChildPlan activePlan = childPlanRepository.getActivePlan();
        List<TaskTemplate> tasks = activePlan.getPlanTemplate().getTasksWithThisPlan();

        taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(tasks, taskItemClickListener);

        recyclerView.setAdapter(taskRecyclerViewAdapter);
    }
}
