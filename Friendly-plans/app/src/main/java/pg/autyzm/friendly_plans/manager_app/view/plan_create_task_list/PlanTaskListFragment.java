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

import android.widget.Button;
import android.widget.TextView;
import javax.inject.Inject;

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
    private Integer typeId;

    public enum TaskType {
        TASK(R.string.create_plan_tasks_list_info_type_1, R.string.create_plan_add_tasks_type_1),
        PRIZE(R.string.create_plan_tasks_list_info_type_2, R.string.create_plan_add_tasks_type_2),
        INTERACTION(R.string.create_plan_tasks_list_info_type_3, R.string.create_plan_add_tasks_type_3);

        private final int infoLabel;
        private final int addLabel;

        TaskType(Integer infoLabel, Integer addLabel) {
            this.infoLabel = infoLabel;
            this.addLabel = addLabel;
        }

        public Integer getInfoLabel() {
            return this.infoLabel;
        }

        public Integer getAddLabel() {
            return this.addLabel;
        }
    }

    private TaskType taskType;

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
                    planTemplateRepository.deleteTaskFromThisPlan(
                            planId,
                            taskListAdapter.getTaskItem(position).getId());
                    taskListAdapter.removeListItem(position);
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
        args.putInt(ActivityProperties.TYPE_ID, typeId);
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
        if (arguments != null && arguments.containsKey(ActivityProperties.PLAN_ID) && arguments.containsKey(ActivityProperties.TYPE_ID)) {
            planId = (Long) arguments.get(ActivityProperties.PLAN_ID);
            typeId = (Integer) arguments.get(ActivityProperties.TYPE_ID);
        }

        getTaskType(typeId);
        Button addButton = (Button) view.findViewById(R.id.id_btn_create_plan_add_tasks);
        TextView labelInfo = (TextView) view.findViewById(R.id.id_tv_plan_tasks_list_info);
        addButton.setText(taskType.getAddLabel());
        labelInfo.setText(taskType.getInfoLabel());

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_create_plan_task_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        taskListAdapter = new TaskRecyclerViewAdapter(taskItemClickListener);
        recyclerView.setAdapter(taskListAdapter);

        taskListAdapter.setTaskItems(planTemplateRepository.getTaskWithThisPlanByTypeId(planId, typeId));
    }

    public void getTaskType(Integer typeId){
        switch(typeId){
            case 1:
                taskType = TaskType.TASK;
                break;
            case 2:
                taskType = TaskType.PRIZE;
                break;
            default:
                taskType = TaskType.INTERACTION;
                break;
        }
    }

    public void onResume() {
        taskListAdapter.setTaskItems(planTemplateRepository.getTaskWithThisPlanByTypeId(planId, typeId));
        taskListAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
