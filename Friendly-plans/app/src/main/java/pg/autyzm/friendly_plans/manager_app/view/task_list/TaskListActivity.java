package pg.autyzm.friendly_plans.manager_app.view.task_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import database.entities.PlanTemplate;
import database.repository.PlanTemplateRepository;
import database.repository.TaskTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.manager_app.view.task_create.TaskCreateActivity;
import pg.autyzm.friendly_plans.notifications.DialogUserNotifier;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class TaskListActivity extends AppCompatActivity {

    @Inject
    TaskTemplateRepository taskTemplateRepository;
    @Inject
    PlanTemplateRepository planTemplateRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;

    private TaskRecyclerViewAdapter taskListAdapter;

    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener =
            new TaskRecyclerViewAdapter.TaskItemClickListener() {

                @Override
                public void onRemoveTaskClick(int position){
                    List<PlanTemplate> relatedPlans = planTemplateRepository.getPlansWithThisTask(
                            taskListAdapter.getTaskItem(position).getId());
                    if(relatedPlans.isEmpty()) {
                        taskTemplateRepository.delete(taskListAdapter.getTaskItem(position).getId());
                        toastUserNotifier.displayNotifications(
                                R.string.task_removed_message,
                                getApplicationContext());
                        taskListAdapter.setTaskItems(taskTemplateRepository.getAll());
                    }
                    else{
                        displayTaskCannotBeRemovedAlert(relatedPlans);
                    }
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

    private void displayTaskCannotBeRemovedAlert(List<PlanTemplate> relatedPlans) {
        List<String> relatedPlansNames = new ArrayList<>();
        for (PlanTemplate plan : relatedPlans){
            relatedPlansNames.add(plan.getName());
        }
        String relatedPlansNamesJoined = TextUtils.join(", " , relatedPlansNames);

        DialogUserNotifier dialog = new DialogUserNotifier(
                TaskListActivity.this,
                getResources().getString(R.string.task_cannot_be_removed),
                getResources().getString(R.string.task_cannot_be_removed_message)
                        + " " + relatedPlansNamesJoined + ".",
                getResources().getString(R.string.task_cannot_be_removed_dialog_close_button_text)
                );
        dialog.showDialog();
    }
}
