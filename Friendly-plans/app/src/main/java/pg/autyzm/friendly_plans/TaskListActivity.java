package pg.autyzm.friendly_plans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import database.repository.TaskTemplateRepository;
import javax.inject.Inject;

public class TaskListActivity extends AppCompatActivity {

    private static final String TAG = "TaskListActivity";
    @Inject
    TaskTemplateRepository taskTemplateRepository;

    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener =
            new TaskRecyclerViewAdapter.TaskItemClickListener() {
                @Override
                public void onTaskItemClick(int position) {
                    Log.d(TAG, "onTaskItemClick: " + position);
                }
            };
    private TaskRecyclerViewAdapter taskListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_task_list);
        setUpViews();
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
