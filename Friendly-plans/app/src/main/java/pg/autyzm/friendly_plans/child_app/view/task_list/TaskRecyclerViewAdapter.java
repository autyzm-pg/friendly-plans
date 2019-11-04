package pg.autyzm.friendly_plans.child_app.view.task_list;

import android.graphics.Color;
import android.graphics.Paint;
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
import pg.autyzm.friendly_plans.child_app.utility.Consts;
import pg.autyzm.friendly_plans.child_app.view.common.ChildActivityList;
import pg.autyzm.friendly_plans.child_app.view.common.ChildActivityState;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskRecyclerViewHolder> implements ChildActivityList {
    private List<TaskTemplate> tasks;
    private TaskItemClickListener taskItemClickListener;
    private String imageDirectory;

    private Integer currentTaskPosition = 0;
    Integer getCurrentTaskPosition() { return currentTaskPosition; }
    private ChildActivityState currentTaskState = ChildActivityState.PENDING_START;
    ChildActivityState getCurrentTaskState() { return currentTaskState; }
    void setCurrentTaskState(ChildActivityState state) { this.currentTaskState = state; }

    protected interface TaskItemClickListener {
        void stepsIconListener(int position);
        void timerIconClickListener(int position, final TextView durationLabel);
        void blankChildActivityListener(int position);
    }

    static class TaskRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView taskName;
        TextView taskDuration;
        ImageView taskImage;
        ImageView actionPicture;
        TaskItemClickListener taskItemClickListener;
        String imageDirectory;
        boolean hasTimer = false;
        boolean hasSteps = false;

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
            itemView.setOnClickListener(actionIconListener);
            this.taskItemClickListener = taskItemClickListener;
            this.imageDirectory = imageDirectory;
        }

        View.OnClickListener actionIconListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasTimer){
                    taskItemClickListener.timerIconClickListener(getAdapterPosition(), taskDuration);
                }
                else if (hasSteps)
                    taskItemClickListener.stepsIconListener(getAdapterPosition());
                else
                    taskItemClickListener.blankChildActivityListener(getAdapterPosition());
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
                hasSteps = true;
            }
            else if (task.getDurationTime() != null) {
                if (taskDuration.getText().toString().equals("0s"))
                    return; // task is already completed

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
        holder.itemView.setBackgroundColor(Color.TRANSPARENT);

        if (position == currentTaskPosition)
            holder.itemView.setBackgroundColor(Color.parseColor(Consts.GREEN));
        else if (position < currentTaskPosition) {
            holder.taskName.setPaintFlags(holder.taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.itemView.setBackgroundColor(Color.parseColor(Consts.GREY));
        }
    }

    @Override
    public void activityCompleted() {
        currentTaskState = ChildActivityState.FINISHED;
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    void moveToNextTask(){
        currentTaskPosition++;
        currentTaskState = ChildActivityState.PENDING_START;
        notifyDataSetChanged();
    }

    void setCurrentTask(int position){
        currentTaskPosition = position;
        currentTaskState = ChildActivityState.PENDING_START;
        notifyDataSetChanged();
    }

    TaskTemplate getTaskItem(int position) {
        return tasks.get(position);
    }
}
