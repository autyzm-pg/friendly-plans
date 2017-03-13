package pg.autyzm.friendly_plans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import database.TaskTemplate;

/**
 * Created by Mateusz on 2017-03-08.
 */

public class TaskListActivity extends AppCompatActivity {

    private TaskRecyclerViewAdapter taskListAdapter;
    private static final String TAG = "TaskListActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        setUpViews();
        setUpMockList();



    }


    private void setUpViews() {

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_task_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        taskListAdapter = new TaskRecyclerViewAdapter(taskItemClickListener);
        recyclerView.setAdapter(taskListAdapter);


    }

    private void setUpMockList() {
        List<TaskTemplate> mockTaskList = new ArrayList<>();
        TaskTemplate mockTaskWithoutMedia = new TaskTemplate(1, "name without media", "", "", "");
        mockTaskList.add(new TaskTemplate(1, "picture, no sound", "picture", "", ""));
        mockTaskList.add(new TaskTemplate(2, "sound, no picture", "", "sound", ""));
        for (int i = 0; i < 10; i++) {
            mockTaskList.add(new TaskTemplate(i, "name " + i, "picture", "sound", i+":00"));
            mockTaskList.add(mockTaskWithoutMedia);
        }



        taskListAdapter.setTaskItems(mockTaskList);


    }


    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener = new TaskRecyclerViewAdapter.TaskItemClickListener() {
        @Override
        public void onTaskItemClick(int position) {
            TaskTemplate taskTemplate = taskListAdapter.getTaskItem(position);
            Log.d(TAG, "onTaskItemClick: task position : " + position + " task name: " + taskTemplate.getName());
        }
    };

}
