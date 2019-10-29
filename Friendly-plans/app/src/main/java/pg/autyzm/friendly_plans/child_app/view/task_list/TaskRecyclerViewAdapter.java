package pg.autyzm.friendly_plans.child_app.view.task_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import database.entities.TaskTemplate;
import pg.autyzm.friendly_plans.R;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskRecyclerViewHolder> {
    private List<TaskTemplate> tasks;
    private TaskItemClickListener taskItemClickListener;
    private String imageDirectory;

    protected interface TaskItemClickListener {
        void stepsIconListener(int position);
    }

    static class TaskRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView taskDuration;
        ImageView taskImage;
        ImageView actionPicture;
        TaskItemClickListener taskItemClickListener;
        String imageDirectory;
        boolean hasTimer = false;

        TaskRecyclerViewHolder(
            View itemView,
            final TaskItemClickListener taskItemClickListener,
            final String imageDirectory
        ) {
            super(itemView);
            this.taskName = (TextView) itemView.findViewById(R.id.id_tv_task_name);
            this.actionPicture = (ImageView) itemView.findViewById(R.id.id_task_activity_icon);
            this.taskDuration = (TextView) itemView.findViewById(R.id.id_tv_task_duration_time);
            this.taskImage = (ImageView) itemView.findViewById(R.id.id_iv_task_image);
            this.actionPicture.setOnClickListener(actionIconListener);
            this.taskItemClickListener = taskItemClickListener;
            this.imageDirectory = imageDirectory;
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

        void setUpHolder(TaskTemplate task){
            taskName.setText(task.getName());

            if (task.getPicture() != null) {
                String imageName = task.getPicture().getFilename();
                Picasso.get()
                        .load(new File(imageDirectory + File.separator + imageName))
                        .into(taskImage);
            }
            else {
                taskImage.setVisibility(View.INVISIBLE);
            }

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

    TaskRecyclerViewAdapter(
        List<TaskTemplate> tasks,
        TaskItemClickListener taskItemClickListener,
        final String imageDirectory
    ) {
        this.tasks = tasks;
        this.taskItemClickListener = taskItemClickListener;
        this.imageDirectory = imageDirectory;
    }

    @Override
    public TaskRecyclerViewAdapter.TaskRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                             int viewType) {
        View taskView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_app_item_task, parent, false);
        return new TaskRecyclerViewHolder(taskView, taskItemClickListener, imageDirectory);
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

    TaskTemplate getTaskItem(int position) {
        return tasks.get(position);
    }
}
