package pg.autyzm.friendly_plans.child_app.view.task_list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import database.entities.ChildPlan;
import database.entities.PlanTask;
import database.entities.TaskTemplate;
import database.repository.ChildPlanRepository;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;

public class TaskListActivity extends AppCompatActivity {
    private android.support.v7.widget.RecyclerView recyclerView;
    private TaskRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ChildPlan activePlan;

    @Inject
    ChildPlanRepository childPlanRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.child_app_task_list);
        recyclerView = (RecyclerView) findViewById(R.id.rv_child_app_task_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        activePlan = childPlanRepository.getActivePlan();
        activePlan.getPlanTasks();
        List<TaskTemplate> tasks = activePlan.getPlanTemplate().getTasksWithThisPlan();
        activePlan.getPlanTasks();
        mAdapter = new TaskRecyclerViewAdapter(tasks);
        recyclerView.setAdapter(mAdapter);
    }
}
