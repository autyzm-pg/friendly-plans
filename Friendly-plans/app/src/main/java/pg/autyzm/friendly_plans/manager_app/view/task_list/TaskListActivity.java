package pg.autyzm.friendly_plans.manager_app.view.task_list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import database.entities.PlanTaskTemplate;
import database.repository.TaskTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.manager_app.view.task_create.TaskCreateActivity;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class TaskListActivity extends AppCompatActivity {

    @Inject
    TaskTemplateRepository taskTemplateRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;

    private TaskRecyclerViewAdapter taskListAdapter;

    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener =
            new TaskRecyclerViewAdapter.TaskItemClickListener() {

                @Override
                public void onRemoveTaskClick(int position){
                    List<PlanTaskTemplate> planTasks = taskTemplateRepository.getPlansWithThisTask(
                            taskListAdapter.getTaskItem(position).getId());
                    if(planTasks.isEmpty()) {
                        taskTemplateRepository.delete(taskListAdapter.getTaskItem(position).getId());
                        toastUserNotifier.displayNotifications(
                                R.string.task_removed_message,
                                getApplicationContext());
                        taskListAdapter.setTaskItems(taskTemplateRepository.getAll());
                    }
                    else{
                        displayTaskCannotBeRemovedAlert(planTasks);
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

    private void displayTaskCannotBeRemovedAlert(List<PlanTaskTemplate> planTasks) {
        String planNames = "";
        for (PlanTaskTemplate planTask : planTasks){
            String asd = planTask.getPlanTemplate().getName();
            planNames = planNames + asd + ", ";
        }
        planNames = planNames.substring(0, planNames.length() - 2);
        planNames += ".";
        AlertDialog alertDialog = new AlertDialog.Builder(
                TaskListActivity.this).create();
        alertDialog.setTitle(R.string.break_time_tasks);
        alertDialog.setMessage(
                getResources().getString(R.string.task_cannot_be_removed_message)
                        + " " + planNames);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                getResources().getString(R.string.task_cannot_be_removed_dialog_close_button_text),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
