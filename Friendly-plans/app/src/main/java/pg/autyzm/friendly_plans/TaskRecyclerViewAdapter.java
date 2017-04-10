package pg.autyzm.friendly_plans;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.entities.TaskTemplate;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskListViewHolder> {

    private List<TaskTemplate> taskItemList;
    private TaskItemClickListener taskItemClickListener;
    private static final int ICON_PLACEHOLDER_ID = R.drawable.ic_placeholder;

    public TaskRecyclerViewAdapter(TaskItemClickListener taskItemClickListener) {
        this.taskItemClickListener = taskItemClickListener;
        this.taskItemList = new ArrayList<TaskTemplate>();
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskListViewHolder(view, taskItemClickListener);
    }

    @Override
    public void onBindViewHolder(TaskListViewHolder holder, int position) {

        if (taskItemList != null && taskItemList.size() != 0) {
            TaskTemplate taskItem = taskItemList.get(position);
            holder.taskName.setText(taskItem.getName());
            //condition only for mocking purposes -> needs to be changed later
            if (taskItem.getPicture() != null && !taskItem.getPicture().getFilename().isEmpty()) {
                holder.taskPicture.setImageResource(ICON_PLACEHOLDER_ID);
                holder.taskPicture.setVisibility(View.VISIBLE);
            } else {
                holder.taskPicture.setVisibility(View.GONE);
            }
            if (taskItem.getSound() != null && !taskItem.getSound().getFilename().isEmpty()) {
                holder.taskSoundIcon.setImageResource(ICON_PLACEHOLDER_ID);
                holder.taskSoundIcon.setVisibility(View.VISIBLE);
            } else {
                holder.taskSoundIcon.setVisibility(View.GONE);
            }
            if (taskItem.getDurationTime() != 0) {
                holder.taskDurationIcon.setImageResource(ICON_PLACEHOLDER_ID);
                holder.taskDurationTime.setText(String.valueOf(taskItem.getDurationTime()));
                holder.taskDurationIcon.setVisibility(View.VISIBLE);
            } else {
                holder.taskDurationTime.setText("");
                holder.taskDurationIcon.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return ((taskItemList != null) && (taskItemList.size() != 0) ? taskItemList.size() : 0);
    }

    public interface TaskItemClickListener {
        void onTaskItemClick(int position);
    }

    public void setTaskItems(List<TaskTemplate> taskItemList) {
        this.taskItemList = taskItemList;
        notifyDataSetChanged();
    }

    public TaskTemplate getTaskItem(int position) {
        return taskItemList.get(position);
    }

    static class TaskListViewHolder extends RecyclerView.ViewHolder {

        TextView taskName = null;
        ImageView taskPicture = null;
        ImageView taskSoundIcon = null;
        ImageView taskDurationIcon = null;
        TextView taskDurationTime = null;

        public TaskListViewHolder(View itemView, final TaskItemClickListener taskItemClickListener) {
            super(itemView);
            this.taskName = (TextView) itemView.findViewById(R.id.id_tv_task_name);
            this.taskPicture = (ImageView) itemView.findViewById(R.id.id_iv_task_picture);
            this.taskSoundIcon = (ImageView) itemView.findViewById(R.id.id_iv_task_sound_icon);
            this.taskDurationIcon = (ImageView) itemView.findViewById(R.id.id_iv_task_duration_icon);
            this.taskDurationTime = (TextView) itemView.findViewById(R.id.id_tv_task_duration_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskItemClickListener.onTaskItemClick(getAdapterPosition());
                }
            });
        }
    }
}
