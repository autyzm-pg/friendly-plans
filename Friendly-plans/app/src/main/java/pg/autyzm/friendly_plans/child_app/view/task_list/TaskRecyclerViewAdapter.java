package pg.autyzm.friendly_plans.child_app.view.task_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import database.entities.TaskTemplate;
import pg.autyzm.friendly_plans.R;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskRecyclerViewHolder> {
    private List<TaskTemplate> tasks;
    private TaskItemClickListener taskItemClickListener;

    protected interface TaskItemClickListener {
        void stepsIconListener(int position);
    }

    static class TaskRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView taskDuration;
        ImageView actionPicture;
        TaskItemClickListener taskItemClickListener;
        boolean hasTimer = false;

        TaskRecyclerViewHolder(View itemView, final TaskItemClickListener taskItemClickListener) {
            super(itemView);
            this.taskName = (TextView) itemView.findViewById(R.id.id_tv_task_name);
            this.actionPicture = (ImageView) itemView.findViewById(R.id.id_task_activity_icon);
            this.taskDuration = (TextView) itemView.findViewById(R.id.id_tv_task_duration_time);
            this.actionPicture.setOnClickListener(actionIconListener);
            this.taskItemClickListener = taskItemClickListener;
        }

        View.OnClickListener actionIconListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasTimer){
                    // todo
                }
                else
                    taskItemClickListener.stepsIconListener(getAdapterPosition());
            }
        };

        protected void setUpHolder(TaskTemplate task){
            taskName.setText(task.getName());

            if (!task.getStepTemplates().isEmpty()) {
                actionPicture.setImageResource(R.drawable.ksiazki);
                taskDuration.setVisibility(View.INVISIBLE);
            }
            else if (task.getDurationTime() != null) {
                actionPicture.setImageResource(R.drawable.timer);
                taskDuration.setText(String.format("%ss", task.getDurationTime().toString()));
                hasTimer = true;
            }
            else {
                actionPicture.setVisibility(View.INVISIBLE);
                taskDuration.setVisibility(View.INVISIBLE);
            }
        }
    }

    TaskRecyclerViewAdapter(List<TaskTemplate> tasks, TaskItemClickListener taskItemClickListener) {
        this.tasks = tasks;
        this.taskItemClickListener = taskItemClickListener;
    }

    @Override
    public TaskRecyclerViewAdapter.TaskRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                             int viewType) {
        View taskView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_app_item_task, parent, false);
        return new TaskRecyclerViewHolder(taskView, taskItemClickListener);
    }

    @Override
    public void onBindViewHolder(TaskRecyclerViewHolder holder, int position) {
        if (tasks != null && !tasks.isEmpty()) {
            TaskTemplate task = tasks.get(position);
            holder.setUpHolder(task);
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public TaskTemplate getTaskItem(int position) {
        return tasks.get(position);
    }
}
