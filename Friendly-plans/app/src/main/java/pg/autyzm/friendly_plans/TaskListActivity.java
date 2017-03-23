package pg.autyzm.friendly_plans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import database.entities.TaskTemplate;
import database.repository.TaskTemplateRepository;

;

/**
 * Created by Mateusz on 2017-03-08.
 */

public class TaskListActivity extends AppCompatActivity {

    @Inject
    TaskTemplateRepository taskTemplateRepository;

    private TaskRecyclerViewAdapter taskListAdapter;
    private static final String TAG = "TaskListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ((App) getApplication()).getRepositoryComponent().inject(this);
        setContentView(R.layout.activity_task_list);
        setUpViews();
        loadTasksToadapter(taskTemplateRepository.getAll());


    }


    private void setUpViews() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_task_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskListAdapter = new TaskRecyclerViewAdapter(taskItemClickListener);
        recyclerView.setAdapter(taskListAdapter);



    }

    //For early "test" purposes
    private void setUpAndLoadMockList() {
        List<TaskTemplate> mockTaskList = new ArrayList<>();
        TaskTemplate mockTaskWithoutMedia = new TaskTemplate(1L, "name without media", "", "", 0);
        mockTaskList.add(new TaskTemplate(1L, "picture, no sound", "picture", "", 0));
        mockTaskList.add(new TaskTemplate(2L, "sound, no picture", "", "sound", 0));
        for (int i = 0; i < 10; i++) {
            mockTaskList.add(new TaskTemplate((long) i, "name " + i, "picture", "sound", i));
            mockTaskList.add(mockTaskWithoutMedia);
        }

        loadTasksToadapter(mockTaskList);

    }

    private void loadTasksToadapter(List<TaskTemplate> taskTemplates) {

        taskListAdapter.setTaskItems(taskTemplates);

    }


    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener = new TaskRecyclerViewAdapter.TaskItemClickListener() {
        @Override
        public void onTaskItemClick(int position) {
            TaskTemplate taskTemplate = taskListAdapter.getTaskItem(position);
            Log.d(TAG, "onTaskItemClick: task position : " + position + " task name: " + taskTemplate.getName());
        }
    };

}
