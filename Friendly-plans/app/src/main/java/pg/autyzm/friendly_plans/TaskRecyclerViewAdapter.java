package pg.autyzm.friendly_plans;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import database.entities.TaskTemplate;
import java.util.ArrayList;
import java.util.List;
import pg.autyzm.friendly_plans.asset.AssetsHelper;

class TaskRecyclerViewAdapter
        extends RecyclerView.Adapter<TaskRecyclerViewAdapter.TaskListViewHolder> {

    private static final int ICON_PLACEHOLDER_PICTURE_ID = R.drawable.ic_placeholder;
    private static final int ICON_PLACEHOLDER_SOUND_ID = R.drawable.ic_playing_sound_2;
    private static final int ICON_PLACEHOLDER_TIME_ID = R.drawable.ic_placeholder_time;
    private List<TaskTemplate> taskItemList;
    private TaskItemClickListener taskItemClickListener;
    private AssetsHelper assetsHelper;

    TaskRecyclerViewAdapter(TaskItemClickListener taskItemClickListener) {
        this.taskItemClickListener = taskItemClickListener;
        this.taskItemList = new ArrayList<TaskTemplate>();
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
        if (taskItemList != null && taskItemList.size() != 0) {
            TaskTemplate taskItem = taskItemList.get(position);
            holder.taskName.setText(taskItem.getName());
            if (taskItem.getPicture() != null && !taskItem.getPicture().getFilename().isEmpty()) {
                String picturePath = assetsHelper.getFileFullPath(taskItem.getPicture());
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                holder.taskPicture.setImageBitmap(bitmap);
            } else {
                holder.taskPicture.setImageResource(ICON_PLACEHOLDER_PICTURE_ID);
            }
            if (taskItem.getSound() != null && !taskItem.getSound().getFilename().isEmpty()) {
                holder.taskSoundIcon.setImageResource(ICON_PLACEHOLDER_SOUND_ID);
                holder.taskSoundIcon.setVisibility(View.VISIBLE);
            } else {
                holder.taskSoundIcon.setVisibility(View.GONE);
            }
            if (taskItem.getDurationTime() != 0) {
                holder.taskDurationIcon.setImageResource(ICON_PLACEHOLDER_TIME_ID);
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
        return taskItemList != null && taskItemList.size() != 0 ? taskItemList.size() : 0;
    }

    void setTaskItems(List<TaskTemplate> taskItemList) {
        this.taskItemList = taskItemList;
        notifyDataSetChanged();
    }

    TaskTemplate getTaskItem(int position) {
        return taskItemList.get(position);
    }

    interface TaskItemClickListener {

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
