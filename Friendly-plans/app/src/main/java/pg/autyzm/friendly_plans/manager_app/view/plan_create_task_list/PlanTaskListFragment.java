package pg.autyzm.friendly_plans.manager_app.view.plan_create_task_list;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import database.entities.PlanTaskTemplate;
import database.entities.PlanTemplate;
import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.FragmentPlanTaskListBinding;
import pg.autyzm.friendly_plans.item_touch_helper.SimpleItemTouchHelperCallback;
import pg.autyzm.friendly_plans.manager_app.view.main_screen.MainActivity;
import pg.autyzm.friendly_plans.manager_app.view.plan_create_add_tasks.AddTasksToPlanFragment;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class PlanTaskListFragment extends Fragment implements PlanTaskListEvents {

    private PlanCreateTaskListRecyclerViewAdapter taskListAdapter;
    private Long planId;

    @Inject
    PlanTemplateRepository planTemplateRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;

    PlanCreateTaskListRecyclerViewAdapter.TaskItemClickListener taskItemClickListener =
            new PlanCreateTaskListRecyclerViewAdapter.TaskItemClickListener() {

                private boolean removedTask = false;

                @Override
                public void onRemoveTaskClick(long itemId) {
                    planTemplateRepository.removeTaskFromPlan(planId, itemId);
                    PlanTemplate planTemplate = planTemplateRepository.get(planId);
                    taskListAdapter
                            .setTaskItems(planTemplate.getPlanTaskTemplates());
                    toastUserNotifier.displayNotifications(
                            R.string.step_removed_message,
                            getActivity().getApplicationContext());
                    removedTask = true;
                }

                @Override
                public void onMoveItem() {
                    Boolean reordered = false;
                    for(int i = 0; i < taskListAdapter.getItemCount(); i++){
                        PlanTaskTemplate planTaskItem =  taskListAdapter.getPlanTaskTemplate(i);
                        if(i != planTaskItem.getOrder()) {
                            planTemplateRepository.removeTaskFromPlan(planId, planTaskItem.getId());
                            planTemplateRepository.setTaskWithPlan(planId, planTaskItem.getTaskTemplateId(), i);
                            reordered = true;
                        }
                        if(!removedTask && reordered){
                            toastUserNotifier.displayNotifications(
                                    R.string.steps_reordered_message,
                                    getActivity().getApplicationContext());
                        }
                        taskListAdapter.notifyDataSetChanged();
                    }
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getAppComponent().inject(this);

        FragmentPlanTaskListBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_plan_task_list, container, false);

        binding.setPlanTaskListEvents(this);
        View view = binding.getRoot();
        return view;
    }

    @Override
    public void eventAddTasksToPlan(View view) {
        AddTasksToPlanFragment fragment = new AddTasksToPlanFragment();

        Bundle args = new Bundle();
        args.putLong(ActivityProperties.PLAN_ID, planId);
        fragment.setArguments(args);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.plan_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void eventSaveAndFinish(View view) {
        toastUserNotifier.displayNotifications(
                R.string.plan_saved_message,
                getActivity().getApplicationContext());
        showMainMenu();
    }

    private void showMainMenu() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ActivityProperties.PLAN_ID)) {
            planId = (Long) arguments.get(ActivityProperties.PLAN_ID);
        }

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_create_plan_task_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        taskListAdapter = new PlanCreateTaskListRecyclerViewAdapter(taskItemClickListener);
        recyclerView.setAdapter(taskListAdapter);

        PlanTemplate planTemplate = planTemplateRepository.get(planId);
        taskListAdapter.setTaskItems(planTemplate.getPlanTaskTemplates());


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(taskListAdapter);
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void onResume() {
        PlanTemplate planTemplate = planTemplateRepository.get(planId);
        taskListAdapter.setTaskItems(planTemplate.getPlanTaskTemplates());
        taskListAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
