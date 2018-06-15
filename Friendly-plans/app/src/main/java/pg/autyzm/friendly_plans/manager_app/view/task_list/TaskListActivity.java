package pg.autyzm.friendly_plans.manager_app.view.task_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import database.repository.TaskTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.manager_app.view.task_create.TaskCreateActivity;

public class TaskListActivity extends AppCompatActivity {

    @Inject
    TaskTemplateRepository taskTemplateRepository;

    private TaskRecyclerViewAdapter taskListAdapter;

    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener =
            new TaskRecyclerViewAdapter.TaskItemClickListener() {
                @Override
                public void onTaskItemClick(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(ActivityProperties.TASK_ID,
                            taskListAdapter.getTaskItem(position).getId());

                    Intent intent = new Intent(TaskListActivity.this, TaskCreateActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_task_list);
        setUpViews();
        taskListAdapter.setTaskItems(taskTemplateRepository.getAll());
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        taskListAdapter.setTaskItems(taskTemplateRepository.getAll());
    }

    private void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_task_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskListAdapter = new TaskRecyclerViewAdapter(taskItemClickListener);
        recyclerView.setAdapter(taskListAdapter);
    }
}
