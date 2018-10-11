package pg.autyzm.friendly_plans.manager_app.view.plan_create_add_tasks;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import java.util.List;

import javax.inject.Inject;

import database.entities.TaskTemplate;
import database.repository.PlanTemplateRepository;
import database.repository.TaskTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.FragmentAddTasksToPlanBinding;
import pg.autyzm.friendly_plans.manager_app.view.task_list.TaskRecyclerViewAdapter;

public class AddTasksToPlanFragment extends Fragment implements AddTasksToPlanEvents{

    public enum TaskType {
        TASK(R.string.create_plan_add_tasks_info_type_1),
        PRIZE(R.string.create_plan_add_tasks_info_type_2),
        INTERACTION(R.string.create_plan_add_tasks_info_type_3);

        private final int infoLabel;

        TaskType(Integer infoLabel) {
            this.infoLabel = infoLabel;
        }
        public Integer getInfoLabel() {
            return this.infoLabel;
        }
    }

    private TaskType taskType;
    private TaskRecyclerViewAdapter taskListAdapter;

    @Inject
    TaskTemplateRepository taskTemplateRepository;

    @Inject
    PlanTemplateRepository planTemplateRepository;

    Long planId;
    Integer typeId;

    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener =
            new TaskRecyclerViewAdapter.TaskItemClickListener() {
                @Override
                public void onTaskItemClick(int position) {
                    planTemplateRepository.setTasksWithThisPlan(planId, taskListAdapter.getTaskItem(position).getId());
                    taskListAdapter.removeListItem(position);
                }

                @Override
                public void onRemoveTaskClick(int position){
                    /*Item remove TODO*/
                }
            };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getAppComponent().inject(this);

        FragmentAddTasksToPlanBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_add_tasks_to_plan, container, false);

        binding.setAddTasksToPlanEvents(this);

        View view = binding.getRoot();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(ActivityProperties.PLAN_ID) && arguments.containsKey(ActivityProperties.TYPE_ID)) {
            planId = (Long) arguments.get(ActivityProperties.PLAN_ID);
            typeId = (Integer) arguments.get(ActivityProperties.TYPE_ID);
        }

        getTaskType(typeId);
        TextView labelInfo = (TextView) view.findViewById(R.id.id_tv_add_tasks_to_plan_info);
        labelInfo.setText(taskType.getInfoLabel());

        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv_create_plan_add_tasks);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        taskListAdapter = new TaskRecyclerViewAdapter(taskItemClickListener);
        recyclerView.setAdapter(taskListAdapter);

        /* Show only tasks that are not already added to current plan */
        List<TaskTemplate> tasksWithThisPlan = planTemplateRepository.getTaskWithThisPlanByTypeId(planId, typeId);
        List<TaskTemplate> tasks =  taskTemplateRepository.getByTypeId(typeId);

        if(!tasksWithThisPlan.isEmpty()) {
            tasks.removeAll(tasksWithThisPlan);
        }

        taskListAdapter.setTaskItems(tasks);
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
    @Override
    public void eventDoneAddingTasksToPlan(View view) {
        getFragmentManager().popBackStack();
    }
}
