package pg.autyzm.friendly_plans.manager_app.view.activate_plan;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import database.entities.ChildPlan;
import java.util.Collections;
import java.util.List;

import database.entities.PlanTemplate;
import pg.autyzm.friendly_plans.R;

public class ActivatePlanRecyclerViewAdapter extends
        RecyclerView.Adapter<ActivatePlanRecyclerViewAdapter.ActivatePlanListViewHolder> {

    private List<ChildPlan> planItemList;
    private PlanItemClickListener planItemClickListener;
    private Integer selectedPlanPosition;

    ActivatePlanRecyclerViewAdapter(PlanItemClickListener planItemClickListener) {
        this.planItemClickListener = planItemClickListener;
        this.planItemList = Collections.emptyList();
    }

    @Override
    public ActivatePlanRecyclerViewAdapter.ActivatePlanListViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_active_plan, parent, false);
        return new ActivatePlanRecyclerViewAdapter.ActivatePlanListViewHolder(view, planItemClickListener);
    }

    @Override
    public void onBindViewHolder(ActivatePlanRecyclerViewAdapter.ActivatePlanListViewHolder holder,
                                 int position) {
        if (planItemList != null && !planItemList.isEmpty()) {
            ChildPlan planItem = planItemList.get(position);
            holder.planName.setText(planItem.getPlanTemplate().getName());
            if (isPositionActive(position)) {
                holder.itemView.setBackgroundColor(Color.parseColor("#cccccc"));
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public boolean isPositionActive(int position) {
        return (selectedPlanPosition != null && selectedPlanPosition == position) || (
                selectedPlanPosition == null && planItemList.get(position).getIsActive());
    }

    @Override
    public int getItemCount() {
        return planItemList != null && planItemList.size() != 0 ? planItemList.size() : 0;
    }

    interface PlanItemClickListener {

        void onPlanItemClick(int position);
    }

    class ActivatePlanListViewHolder extends RecyclerView.ViewHolder {
        TextView planName;
        PlanItemClickListener planItemClickListener;
        ActivatePlanListViewHolder(View itemView, PlanItemClickListener planItemClickListener) {
            super(itemView);
            this.planName = (TextView) itemView
                    .findViewById(R.id.id_tv_plan_name);
            this.planItemClickListener = planItemClickListener;
            itemView.setOnClickListener(selectItemListener);
        }
        View.OnClickListener selectItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                planItemClickListener.onPlanItemClick(getAdapterPosition());
            }
        };
    }


    void setPlanItems(List<ChildPlan> planItemList) {
        this.planItemList = planItemList;
        notifyDataSetChanged();
    }

    void setSelectedPlanPosition(int selectedChildPosition) {
        this.selectedPlanPosition = selectedChildPosition;
        notifyDataSetChanged();
    }

    Integer getSelectedPlanPosition(){
        return this.selectedPlanPosition;
    }

    ChildPlan getPlanItem(Integer position){
        return this.planItemList.get(position);
    }
}
