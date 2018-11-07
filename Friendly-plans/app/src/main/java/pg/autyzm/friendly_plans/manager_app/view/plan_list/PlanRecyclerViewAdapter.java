package pg.autyzm.friendly_plans.manager_app.view.plan_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import database.entities.PlanTemplate;
import java.util.ArrayList;
import java.util.List;
import pg.autyzm.friendly_plans.R;

public class PlanRecyclerViewAdapter extends
        RecyclerView.Adapter<PlanRecyclerViewAdapter.PlanListViewHolder> {

    private List<PlanTemplate> planItemList;
    private PlanItemClickListener planItemClickListener;

    PlanRecyclerViewAdapter(List<PlanTemplate> planItemList, PlanItemClickListener planItemClickListener) {
        this.planItemList = planItemList;
        this.planItemClickListener = planItemClickListener;
    }

    PlanTemplate getPlanItem(int position) {
        return planItemList.get(position);
    }

    interface PlanItemClickListener {

        void onRemovePlanClick(long itemId);

        void onPlanItemClick(int position);
    }

    @Override
    public PlanRecyclerViewAdapter.PlanListViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan, parent, false);
        return new PlanRecyclerViewAdapter.PlanListViewHolder(view, planItemClickListener, planItemList);
    }

    @Override
    public void onBindViewHolder(PlanRecyclerViewAdapter.PlanListViewHolder holder, int position) {
        PlanTemplate planItem = planItemList.get(position);
        holder.planName.setText(planItem.getName());
        holder.planItemList = planItemList;
    }

    @Override
    public int getItemCount() {
        return planItemList.size();
    }

    void setPlanItems(List<PlanTemplate> planItemList) {
        this.planItemList = planItemList;
        notifyDataSetChanged();
    }


    static class PlanListViewHolder extends RecyclerView.ViewHolder {

        TextView planName;
        ImageButton removeButton;
        PlanItemClickListener planItemClickListener;
        List<PlanTemplate> planItemList;
        PlanListViewHolder(View itemView, PlanItemClickListener planItemClickListener, List<PlanTemplate> planItemList) {
            super(itemView);
            this.planName = (TextView) itemView
                    .findViewById(R.id.id_tv_plan_name);
            this.removeButton = (ImageButton) itemView
                    .findViewById(R.id.id_remove_plan);
            this.planItemList = planItemList;
            this.planItemClickListener = planItemClickListener;
            this.removeButton.setOnClickListener(deleteButtonListener);
            itemView.setOnClickListener(selectItemListener);
        }

        View.OnClickListener deleteButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = planItemList.get(getAdapterPosition()).getId();
                planItemClickListener.onRemovePlanClick(id);
            }
        };

        View.OnClickListener selectItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planItemClickListener.onPlanItemClick(getAdapterPosition());
            }
        };
    }
}
