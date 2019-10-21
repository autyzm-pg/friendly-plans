package pg.autyzm.friendly_plans.child_app.view.task_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import database.entities.PlanTask;
import database.entities.PlanTemplate;
import database.entities.TaskTemplate;
import pg.autyzm.friendly_plans.R;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskRecyclerViewHolder> {
    private List<TaskTemplate> tasks;
    private TextView planName;

    static class TaskRecyclerViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView taskName;
        private List<TaskTemplate> tasks;

        TaskRecyclerViewHolder(View itemView, List<TaskTemplate> tasks) {
            super(itemView);
            this.taskName = (TextView) itemView.findViewById(R.id.id_tv_task_name);
            this.tasks = tasks;
        }
    }

    TaskRecyclerViewAdapter(List<TaskTemplate> tasks) {
        this.tasks = tasks;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TaskRecyclerViewAdapter.TaskRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                             int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_app_item_task, parent, false);
        TaskRecyclerViewHolder viewHolder = new TaskRecyclerViewHolder(v, tasks);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(TaskRecyclerViewHolder holder, int position) {
        if (tasks != null && !tasks.isEmpty()) {
            TaskTemplate task = tasks.get(position);
            holder.taskName.setText(task.getName());
            holder.tasks = tasks;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
