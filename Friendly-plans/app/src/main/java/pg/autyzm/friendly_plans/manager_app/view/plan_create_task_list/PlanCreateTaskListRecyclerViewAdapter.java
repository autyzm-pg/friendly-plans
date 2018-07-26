package pg.autyzm.friendly_plans.manager_app.view.plan_create_task_list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.entities.PlanTaskTemplate;
import database.entities.TaskTemplate;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.item_touch_helper.ItemTouchHelperAdapter;

public class PlanCreateTaskListRecyclerViewAdapter extends
        RecyclerView.Adapter<PlanCreateTaskListRecyclerViewAdapter.TaskListViewHolder>  implements ItemTouchHelperAdapter {

    private static final int ICON_PLACEHOLDER_PICTURE_ID = R.drawable.ic_placeholder;
    private static final int ICON_PLACEHOLDER_SOUND_ID = R.drawable.ic_playing_sound;
    private static final int ICON_PLACEHOLDER_TIME_ID = R.drawable.ic_placeholder_time;
    private static List<PlanTaskTemplate> planTaskItemList = new ArrayList<>();
    private TaskItemClickListener taskItemClickListener;
    private AssetsHelper assetsHelper;

    PlanCreateTaskListRecyclerViewAdapter(TaskItemClickListener taskItemClickListener) {
        this.taskItemClickListener = taskItemClickListener;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(planTaskItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(planTaskItemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        taskItemClickListener.onRemoveTaskClick(planTaskItemList.get(position).getId());
        notifyItemRemoved(position);
    }

    @Override
    public void onItemReleased() {
        taskItemClickListener.onMoveItem();
    }

    interface TaskItemClickListener {

        void onRemoveTaskClick(long itemId);

        void onMoveItem();
    }

    @Override
    public TaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        assetsHelper = new AssetsHelper(parent.getContext());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan_task, parent, false);
        return new TaskListViewHolder(view, taskItemClickListener);
    }

    @Override
    public void onBindViewHolder(TaskListViewHolder parent, int position) {
        if (planTaskItemList != null && planTaskItemList.size() != 0) {
            PlanTaskTemplate planTaskTemplate = planTaskItemList.get(position);
            TaskTemplate taskTemplate = planTaskTemplate.getTaskTemplate();
            parent.taskName.setText(taskTemplate.getName());
            setPicture(parent, taskTemplate);
            setSound(parent, taskTemplate);
            setDurationTime(parent, taskTemplate);
        }
    }

    @Override
    public int getItemCount() {
        return planTaskItemList != null && planTaskItemList.size() != 0 ? planTaskItemList.size() : 0;
    }

    public PlanTaskTemplate getPlanTaskTemplate(int id){
        return planTaskItemList.get(id);
    }

    void setTaskItems(List<PlanTaskTemplate> planTaskItemList) {
        this.planTaskItemList = planTaskItemList;
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

    static class TaskListViewHolder extends RecyclerView.ViewHolder {

        TextView taskName;
        ImageButton removeButton;
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
            this.removeButton = (ImageButton) itemView
                    .findViewById(R.id.id_remove_plan_task);
            this.removeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    long id = planTaskItemList.get(getAdapterPosition()).getId();
                    taskItemClickListener.onRemoveTaskClick(id);
                }
            });
        }
    }
}
