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
import database.entities.PlanTaskTemplate;
import database.entities.TaskTemplate;
import java.util.Collections;
import java.util.List;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.item_touch_helper.ItemTouchHelperAdapter;

public class PlanTaskRecyclerViewAdapter extends
        RecyclerView.Adapter<PlanTaskRecyclerViewAdapter.PlanTaskListViewHolder> implements
        ItemTouchHelperAdapter {

    private static final int ICON_PLACEHOLDER_PICTURE_ID = R.drawable.ic_placeholder;
    private static final int ICON_PLACEHOLDER_SOUND_ID = R.drawable.ic_playing_sound;
    private static final int ICON_PLACEHOLDER_TIME_ID = R.drawable.ic_placeholder_time;
    private List<PlanTaskTemplate> taskItemList;
    private TaskItemClickListener taskItemClickListener;
    private AssetsHelper assetsHelper;

    public PlanTaskRecyclerViewAdapter(TaskItemClickListener taskItemClickListener) {
        this.taskItemClickListener = taskItemClickListener;
        this.taskItemList = Collections.emptyList();
    }

    @Override
    public PlanTaskListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        assetsHelper = new AssetsHelper(parent.getContext());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan_task, parent, false);
        return new PlanTaskListViewHolder(view, taskItemClickListener);
    }

    @Override
    public void onBindViewHolder(PlanTaskRecyclerViewAdapter.PlanTaskListViewHolder holder, int position) {
        if (taskItemList != null && !taskItemList.isEmpty()) {
            PlanTaskTemplate taskItem = taskItemList.get(position);
            holder.taskName.setText(taskItem.getTaskTemplate().getName());
            setPicture(holder, taskItem.getTaskTemplate());
            setSound(holder, taskItem.getTaskTemplate());
            setDurationTime(holder, taskItem.getTaskTemplate());
        }
    }


    @Override
    public int getItemCount() {
        return taskItemList != null && taskItemList.size() != 0 ? taskItemList.size() : 0;
    }

    public void setTaskItems(List<PlanTaskTemplate> taskItemList) {
        this.taskItemList = taskItemList;
        notifyDataSetChanged();
    }

    private void setDurationTime(PlanTaskRecyclerViewAdapter.PlanTaskListViewHolder holder, TaskTemplate taskItem) {
        if (taskItem.getDurationTime() != null && taskItem.getDurationTime() != 0) {
            holder.taskDurationIcon.setImageResource(ICON_PLACEHOLDER_TIME_ID);
            holder.taskDurationTime.setText(String.valueOf(taskItem.getDurationTime()));
            holder.taskDurationIcon.setVisibility(View.VISIBLE);
        } else {
            holder.taskDurationTime.setText("");
            holder.taskDurationIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void setSound(PlanTaskRecyclerViewAdapter.PlanTaskListViewHolder holder, TaskTemplate taskItem) {
        if (taskItem.getSound() != null && !taskItem.getSound().getFilename().isEmpty()) {
            holder.taskSoundIcon.setImageResource(ICON_PLACEHOLDER_SOUND_ID);
            holder.taskSoundIcon.setVisibility(View.VISIBLE);
        } else {
            holder.taskSoundIcon.setVisibility(View.GONE);
        }
    }

    private void setPicture(PlanTaskRecyclerViewAdapter.PlanTaskListViewHolder holder, TaskTemplate taskItem) {
        if (taskItem.getPicture() != null && !taskItem.getPicture().getFilename().isEmpty()) {
            String picturePath = assetsHelper.getFileFullPath(taskItem.getPicture());
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            holder.taskPicture.setImageBitmap(bitmap);
        } else {
            holder.taskPicture.setImageResource(ICON_PLACEHOLDER_PICTURE_ID);
        }
    }

    public PlanTaskTemplate getTaskItem(int position) {
        return taskItemList.get(position);
    }

    public void removeListItem(int position){
        taskItemList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(taskItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(taskItemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        taskItemClickListener.onRemoveTaskClick(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemReleased() {
        taskItemClickListener.onMoveItem();
    }

    public interface TaskItemClickListener {
        void onRemoveTaskClick(int position);
        void onTaskItemClick(int position);
        void onMoveItem();
    }

    static class PlanTaskListViewHolder extends RecyclerView.ViewHolder {

        TextView taskName;
        ImageView taskPicture;
        ImageView taskSoundIcon;
        ImageView taskDurationIcon;
        TextView taskDurationTime;
        ImageButton removeButton;
        ImageView reorder;
        PlanTaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener;

        PlanTaskListViewHolder(View itemView, final PlanTaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener) {
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
                    .findViewById(R.id.id_remove_task);
            this.reorder = (ImageView) itemView.findViewById(R.id.id_task_reorder);
            this.removeButton.setOnClickListener(deleteButtonListener);
            this.taskItemClickListener = taskItemClickListener;
            itemView.setOnClickListener(selectItemListener);

        }


        View.OnClickListener deleteButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskItemClickListener.onRemoveTaskClick(getAdapterPosition());
            }
        };

        View.OnClickListener selectItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskItemClickListener.onTaskItemClick(getAdapterPosition());
            }
        };
    }
}
