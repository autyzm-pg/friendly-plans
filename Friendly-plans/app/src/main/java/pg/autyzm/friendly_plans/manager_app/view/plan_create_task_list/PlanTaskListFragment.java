package pg.autyzm.friendly_plans.manager_app.view.plan_create_task_list;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import database.entities.PlanTemplate;
import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.FragmentPlanTaskListBinding;
import pg.autyzm.friendly_plans.manager_app.view.plan_create_add_tasks.AddTasksToPlanFragment;
import pg.autyzm.friendly_plans.manager_app.view.task_list.TaskRecyclerViewAdapter;

public class PlanTaskListFragment extends Fragment implements PlanTaskListEvents {

    private TaskRecyclerViewAdapter taskListAdapter;
    private Long planId;

    @Inject
    PlanTemplateRepository planTemplateRepository;

    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener =
            new TaskRecyclerViewAdapter.TaskItemClickListener() {
                @Override
                public void onTaskItemClick(int position) {
                    /* What to do after click? Remove task? Edit? */
                }

                @Override
                public void onRemoveTaskClick(int position){

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ActivityProperties.PLAN_ID)) {
            planId = (Long) arguments.get(ActivityProperties.PLAN_ID);
        }

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_create_plan_task_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        taskListAdapter = new TaskRecyclerViewAdapter(taskItemClickListener);
        recyclerView.setAdapter(taskListAdapter);

        PlanTemplate planTemplate = planTemplateRepository.get(planId);
        taskListAdapter.setTaskItems(planTemplate.getTasksWithThisPlan());
    }

    public void onResume() {
        PlanTemplate planTemplate = planTemplateRepository.get(planId);
        taskListAdapter.setTaskItems(planTemplate.getTasksWithThisPlan());
        taskListAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
