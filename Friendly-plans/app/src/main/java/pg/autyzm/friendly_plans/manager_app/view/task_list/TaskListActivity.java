package pg.autyzm.friendly_plans.manager_app.view.task_list;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import database.entities.TaskTemplate;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import database.entities.PlanTemplate;
import database.repository.PlanTemplateRepository;
import database.repository.TaskTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.ActivityTaskListBinding;
import pg.autyzm.friendly_plans.manager_app.view.task_create.TaskCreateActivity;
import pg.autyzm.friendly_plans.notifications.DialogUserNotifier;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class TaskListActivity extends AppCompatActivity implements TaskListActivityEvents {

    @Inject
    TaskTemplateRepository taskTemplateRepository;
    @Inject
    PlanTemplateRepository planTemplateRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;

    SearchView searchView;

    private TaskRecyclerViewAdapter taskListAdapter;

    private List<TaskTemplate> taskItemList = new ArrayList<TaskTemplate>();
    private Integer selectedTypeId = 1;


    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener =
            new TaskRecyclerViewAdapter.TaskItemClickListener() {

                @Override
                public void onRemoveTaskClick(final int position){
                    List<PlanTemplate> relatedPlans = planTemplateRepository.getPlansWithThisTask(
                            taskListAdapter.getTaskItem(position).getId());
                    if(relatedPlans.isEmpty()) {
                        DialogUserNotifier dialog = new DialogUserNotifier(
                                TaskListActivity.this,
                                getResources().getString(R.string.task_removal_confirmation_title),
                                getResources().getString(R.string.task_removal_confirmation_message)
                        );
                        dialog.setPositiveButton(
                                getResources().getString(R.string.task_removal_confirmation_positive_button),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        removeTask(position);
                                        dialog.dismiss();
                                    }
                                });
                        dialog.setNegativeButton(
                                getResources().getString(R.string.task_removal_confirmation_negative_button),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog.showDialog();
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
        ActivityTaskListBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_task_list);
        binding.setEvents(this);
        setUpViews();
        taskItemList = taskTemplateRepository.getByTypeId(1);
        taskListAdapter.setTaskItems(taskItemList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.plan_list_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                refreshList(query, selectedTypeId);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                refreshList(newText, selectedTypeId);
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        taskListAdapter.setTaskItems(taskItemList);
    }

    private void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_task_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskListAdapter = new TaskRecyclerViewAdapter(taskItemClickListener);
        recyclerView.setAdapter(taskListAdapter);
    }

    @Override
    public void eventShowListOfTasks(View view) {
        showSelectedList(view, 1);
    }

    @Override
    public void eventShowListOfPrizes(View view) {
        showSelectedList(view, 2);
    }

    @Override
    public void eventShowListOfInteractions(View view) {
        showSelectedList(view, 3);
    }

    private void showSelectedList(View view, Integer typeId) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setFocusableInTouchMode(false);
        selectedTypeId = typeId;
        taskItemList = taskTemplateRepository.getByTypeId(typeId);
        taskListAdapter.setTaskItems(taskItemList);
    }

    private void removeTask(int position){
        taskTemplateRepository.delete(taskListAdapter.getTaskItem(position).getId());
        refreshList(searchView.getQuery().toString(), selectedTypeId);
        toastUserNotifier.displayNotifications(
                R.string.task_removed_message,
                getApplicationContext());
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
                        + " " + relatedPlansNamesJoined + ".");
        dialog.setPositiveButton(getResources().getString(R.string.task_cannot_be_removed_dialog_close_button),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.showDialog();
    }

    private void refreshList(String searchedValue, Integer typeId) {
        taskListAdapter.setTaskItems(taskTemplateRepository.getFilteredByNameAndType(searchedValue, typeId));
    }
}
