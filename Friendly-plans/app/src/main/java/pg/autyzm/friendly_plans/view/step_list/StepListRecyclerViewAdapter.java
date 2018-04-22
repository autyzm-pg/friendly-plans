package pg.autyzm.friendly_plans.view.step_list;

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

import database.entities.StepTemplate;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.item_touch_helper.ItemTouchHelperAdapter;
import pg.autyzm.friendly_plans.asset.AssetsHelper;

public class StepListRecyclerViewAdapter extends
        RecyclerView.Adapter<StepListRecyclerViewAdapter.StepListViewHolder>  implements ItemTouchHelperAdapter {

    private static final int ICON_PLACEHOLDER_PICTURE_ID = R.drawable.ic_placeholder;
    private static List<StepTemplate> stepItemList;
    private StepItemClickListener stepItemClickListener;
    private AssetsHelper assetsHelper;

    StepListRecyclerViewAdapter(StepItemClickListener stepItemClickListener) {
        this.stepItemClickListener = stepItemClickListener;
        this.stepItemList = new ArrayList<>();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(stepItemList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(stepItemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        stepItemClickListener.onRemoveStepClick(stepItemList.get(position).getId());
        notifyItemRemoved(position);
    }

    @Override
    public void onItemReleased() {
        stepItemClickListener.onMoveItem();
    }

    interface StepItemClickListener {

        void onRemoveStepClick(long itemId);

        void onMoveItem();

        void onShowStepClick(long itemId);
    }

    @Override
    public StepListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        assetsHelper = new AssetsHelper(parent.getContext());
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);
        return new StepListViewHolder(view, stepItemClickListener);
    }

    @Override
    public void onBindViewHolder(StepListViewHolder parent, int position) {
        if (stepItemList != null && stepItemList.size() != 0) {
            StepTemplate stepTemplate = stepItemList.get(position);
            parent.stepName.setText(stepTemplate.getName());
            setPicture(parent, stepTemplate);
        }
    }

    @Override
    public int getItemCount() {
        return stepItemList != null && stepItemList.size() != 0 ? stepItemList.size() : 0;
    }

    public StepTemplate getStepTemplate(int id){
        return stepItemList.get(id);
    }

    void setStepItemListItems(List<StepTemplate> stepItemList) {
        this.stepItemList = stepItemList;
        notifyDataSetChanged();
    }

    private void setPicture(StepListViewHolder holder, StepTemplate stepItem) {
        if (stepItem.getPicture() != null && !stepItem.getPicture().getFilename().isEmpty()) {
            String picturePath = assetsHelper.getFileFullPath(stepItem.getPicture());
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            holder.stepPicture.setImageBitmap(bitmap);
        } else {
            holder.stepPicture.setImageResource(ICON_PLACEHOLDER_PICTURE_ID);
        }
    }

    static class StepListViewHolder extends RecyclerView.ViewHolder {

        TextView stepName;
        ImageButton removeButton;
        ImageView stepPicture;

        StepListViewHolder(View itemView, final StepItemClickListener stepItemClickListener) {
            super(itemView);
            this.stepName = (TextView) itemView
                    .findViewById(R.id.id_tv_step_name);
            this.stepName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long id = stepItemList.get(getAdapterPosition()).getId();
                    stepItemClickListener.onShowStepClick(id);
                }
            });
            this.stepPicture = (ImageView) itemView
                    .findViewById(R.id.id_iv_step_picture);
            this.removeButton = (ImageButton) itemView
                    .findViewById(R.id.id_remove_step);
            this.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long id = stepItemList.get(getAdapterPosition()).getId();
                    stepItemClickListener.onRemoveStepClick(id);
                }
            });
        }
    }
}
