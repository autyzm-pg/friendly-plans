package pg.autyzm.friendly_plans.manager_app.view.task_list;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import database.entities.TaskTemplate;
import database.repository.TaskTemplateRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.ActivityTaskListBinding;
import pg.autyzm.friendly_plans.manager_app.view.task_create.TaskCreateActivity;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class TaskListActivity extends AppCompatActivity implements TaskListActivityEvents {

    @Inject
    TaskTemplateRepository taskTemplateRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;

    private TaskRecyclerViewAdapter taskListAdapter;
    private List<TaskTemplate> taskItemList = new ArrayList<TaskTemplate>();
    private List<TaskTemplate> prizeItemList = new ArrayList<TaskTemplate>();
    private List<TaskTemplate> interactionItemList = new ArrayList<TaskTemplate>();
    private List<TaskTemplate> selectedList;

    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener =
            new TaskRecyclerViewAdapter.TaskItemClickListener() {

                @Override
                public void onRemoveTaskClick(int position){
                    taskTemplateRepository.delete(taskListAdapter.getTaskItem(position).getId());
                    toastUserNotifier.displayNotifications(
                            R.string.task_removed_message,
                            getApplicationContext());
                    taskListAdapter.setTaskItems(taskTemplateRepository.getAll());
                }

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
        ActivityTaskListBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_task_list);
        binding.setEvents(this);
        setUpViews();
        setElementsLists(taskTemplateRepository.getAll());
        selectedList = taskItemList;
        taskListAdapter.setTaskItems(selectedList);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        taskListAdapter.setTaskItems(selectedList);
    }

    private void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_task_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskListAdapter = new TaskRecyclerViewAdapter(taskItemClickListener);
        recyclerView.setAdapter(taskListAdapter);
    }

    private void setElementsLists(List<TaskTemplate> taskTemplateList) {
        for (TaskTemplate item : taskTemplateList) {
            switch (item.getTypeId().toString()) {
                case "1":
                    taskItemList.add(item);
                    break;
                case "2":
                    prizeItemList.add(item);
                    break;
                case "3":
                    interactionItemList.add(item);
                    break;
            }
        }
    }


    @Override
    public void eventShowListOfTasks(View view) {
        showSelectedList(view, taskItemList);
    }

    @Override
    public void eventShowListOfPrizes(View view) {
        showSelectedList(view, prizeItemList);
    }

    @Override
    public void eventShowListOfInteractions(View view) {
        showSelectedList(view, interactionItemList);
    }

    private void showSelectedList(View view, List<TaskTemplate> typeOfList) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setFocusableInTouchMode(false);
        selectedList = typeOfList;
        taskListAdapter.setTaskItems(selectedList);
    }
}
