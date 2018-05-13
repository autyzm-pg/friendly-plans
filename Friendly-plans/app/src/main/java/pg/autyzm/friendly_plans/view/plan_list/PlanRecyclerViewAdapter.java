package pg.autyzm.friendly_plans.view.plan_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import database.entities.PlanTemplate;
import java.util.Collections;
import java.util.List;
import pg.autyzm.friendly_plans.R;

public class PlanRecyclerViewAdapter extends
        RecyclerView.Adapter<PlanRecyclerViewAdapter.PlanListViewHolder> {

    private static List<PlanTemplate> planItemList;
    private PlanItemClickListener planItemClickListener;

    PlanRecyclerViewAdapter(PlanItemClickListener planItemClickListener) {
        this.planItemClickListener = planItemClickListener;
        this.planItemList = Collections.emptyList();
    }

    interface PlanItemClickListener {

        void onRemovePlanClick(long itemId);
    }

    @Override
    public PlanRecyclerViewAdapter.PlanListViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan, parent, false);
        return new PlanRecyclerViewAdapter.PlanListViewHolder(view, planItemClickListener);
    }

    @Override
    public void onBindViewHolder(PlanRecyclerViewAdapter.PlanListViewHolder holder, int position) {
        if (planItemList != null && !planItemList.isEmpty()) {
            PlanTemplate planItem = planItemList.get(position);
            holder.planName.setText(planItem.getName());
        }
    }

    @Override
    public int getItemCount() {
        return planItemList != null && planItemList.size() != 0 ? planItemList.size() : 0;
    }

    void setPlanItems(List<PlanTemplate> planItemList) {
        this.planItemList = planItemList;
        notifyDataSetChanged();
    }


    static class PlanListViewHolder extends RecyclerView.ViewHolder {

        TextView planName;
        ImageButton removeButton;

        PlanListViewHolder(View itemView, final PlanItemClickListener planItemClickListener) {
            super(itemView);
            this.planName = (TextView) itemView
                    .findViewById(R.id.id_tv_plan_name);
            this.removeButton = (ImageButton) itemView
                    .findViewById(R.id.id_remove_plan);
            this.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long id = planItemList.get(getAdapterPosition()).getId();
                    planItemClickListener.onRemovePlanClick(id);
                }
            });
        }
    }
}
