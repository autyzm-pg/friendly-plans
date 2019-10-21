package pg.autyzm.friendly_plans.child_app.view.task_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import database.entities.TaskTemplate;
import pg.autyzm.friendly_plans.R;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskRecyclerViewHolder> {
    private List<TaskTemplate> tasks;

    static class TaskRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;

        TaskRecyclerViewHolder(View itemView) {
            super(itemView);
            this.taskName = (TextView) itemView.findViewById(R.id.id_tv_task_name);
        }
    }

    TaskRecyclerViewAdapter(List<TaskTemplate> tasks) {
        this.tasks = tasks;
    }

    @Override
    public TaskRecyclerViewAdapter.TaskRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                             int viewType) {
        View taskView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_app_item_task, parent, false);
        return new TaskRecyclerViewHolder(taskView);
    }

    @Override
    public void onBindViewHolder(TaskRecyclerViewHolder holder, int position) {
        if (tasks != null && !tasks.isEmpty()) {
            TaskTemplate task = tasks.get(position);
            holder.taskName.setText(task.getName());
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
