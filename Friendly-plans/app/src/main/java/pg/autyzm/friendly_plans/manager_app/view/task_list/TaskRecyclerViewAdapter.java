package pg.autyzm.friendly_plans.manager_app.view.task_list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import database.entities.TaskTemplate;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetsHelper;

public class TaskRecyclerViewAdapter
        extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskListViewHolder> {

    private static final int ICON_PLACEHOLDER_PICTURE_ID = R.drawable.ic_placeholder;
    private static final int ICON_PLACEHOLDER_SOUND_ID = R.drawable.ic_playing_sound;
    private static final int ICON_PLACEHOLDER_TIME_ID = R.drawable.ic_placeholder_time;
    private List<TaskTemplate> taskItemList;
    private TaskItemClickListener taskItemClickListener;
    private AssetsHelper assetsHelper;

    public TaskRecyclerViewAdapter(TaskItemClickListener taskItemClickListener) {
        this.taskItemClickListener = taskItemClickListener;
        this.taskItemList = Collections.emptyList();
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        assetsHelper = new AssetsHelper(parent.getContext());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskListViewHolder(view, taskItemClickListener);
    }

    @Override
    public void onBindViewHolder(TaskListViewHolder holder, int position) {
        if (taskItemList != null && !taskItemList.isEmpty()) {
            TaskTemplate taskItem = taskItemList.get(position);
            holder.taskName.setText(taskItem.getName());
            setPicture(holder, taskItem);
            setSound(holder, taskItem);
            setDurationTime(holder, taskItem);
        }
    }

    @Override
    public int getItemCount() {
        return taskItemList != null && taskItemList.size() != 0 ? taskItemList.size() : 0;
    }

    public void setTaskItems(List<TaskTemplate> taskItemList) {
        this.taskItemList = taskItemList;
        notifyDataSetChanged();
    }

    private void setDurationTime(TaskListViewHolder holder, TaskTemplate taskItem) {
        if (taskItem.getDurationTime() != null && taskItem.getDurationTime() != 0) {
            holder.taskDurationIcon.setImageResource(ICON_PLACEHOLDER_TIME_ID);
            holder.taskDurationTime.setText(String.valueOf(taskItem.getDurationTime()));
            holder.taskDurationIcon.setVisibility(View.VISIBLE);
        } else {
            holder.taskDurationTime.setText("");
            holder.taskDurationIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void setSound(TaskListViewHolder holder, TaskTemplate taskItem) {
        if (taskItem.getSound() != null && !taskItem.getSound().getFilename().isEmpty()) {
            holder.taskSoundIcon.setImageResource(ICON_PLACEHOLDER_SOUND_ID);
            holder.taskSoundIcon.setVisibility(View.VISIBLE);
        } else {
            holder.taskSoundIcon.setVisibility(View.GONE);
        }
    }

    private void setPicture(TaskListViewHolder holder, TaskTemplate taskItem) {
        if (taskItem.getPicture() != null && !taskItem.getPicture().getFilename().isEmpty()) {
            String picturePath = assetsHelper.getFileFullPath(taskItem.getPicture());
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            holder.taskPicture.setImageBitmap(bitmap);
        } else {
            holder.taskPicture.setImageResource(ICON_PLACEHOLDER_PICTURE_ID);
        }
    }

    public TaskTemplate getTaskItem(int position) {
        return taskItemList.get(position);
    }

    public void removeListItem(int position){
        taskItemList.remove(position);
        notifyDataSetChanged();
    }

    public interface TaskItemClickListener {

        void onTaskItemClick(int position);
    }

    static class TaskListViewHolder extends RecyclerView.ViewHolder {

        TextView taskName;
        ImageView taskPicture;
        ImageView taskSoundIcon;
        ImageView taskDurationIcon;
        TextView taskDurationTime;

        TaskListViewHolder(View itemView, final TaskItemClickListener taskItemClickListener) {
            super(itemView);
            this.taskName = (TextView) itemView
                    .findViewById(R.id.id_tv_task_name);
            this.taskPicture = (ImageView) itemView
                    .findViewById(R.id.id_iv_task_picture);
            this.taskSoundIcon = (ImageView) itemView
                    .findViewById(R.id.id_iv_task_sound_icon);
            this.taskDurationIcon = (ImageView) itemView
                    .findViewById(R.id.id_iv_task_duration_icon);
            this.taskDurationTime = (TextView) itemView
                    .findViewById(R.id.id_tv_task_duration_time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    taskItemClickListener.onTaskItemClick(getAdapterPosition());
                }
            });
        }
    }
}
