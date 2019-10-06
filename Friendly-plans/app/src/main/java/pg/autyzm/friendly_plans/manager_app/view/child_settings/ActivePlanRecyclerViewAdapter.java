package pg.autyzm.friendly_plans.manager_app.view.child_settings;


import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import database.entities.Child;
import database.entities.ChildPlan;
import database.entities.PlanTemplate;
import database.repository.ChildPlanRepository;
import database.repository.ChildRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.R;

public class ActivePlanRecyclerViewAdapter extends
        RecyclerView.Adapter<ActivePlanRecyclerViewAdapter.ActivePlanListViewHolder> {

    @Inject
    ChildRepository childRepository;

    private List<PlanTemplate> planItemList;
    private PlanItemClickListener planItemClickListener;
    private List<Integer> selectedPlanPositions;
    List<Long> childPlansIds = new ArrayList<>();

    ActivePlanRecyclerViewAdapter(PlanItemClickListener planItemClickListener) {
        this.planItemClickListener = planItemClickListener;
        this.planItemList = new ArrayList<>();
        this.selectedPlanPositions = new ArrayList<>();
    }

    @Override
    public ActivePlanRecyclerViewAdapter.ActivePlanListViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_active_plan, parent, false);
        return new ActivePlanRecyclerViewAdapter.ActivePlanListViewHolder(view,
                planItemClickListener);
    }

    interface PlanItemClickListener {

        void onPlanItemClick(int position);
    }

    @Override
    public void onBindViewHolder(ActivePlanRecyclerViewAdapter.ActivePlanListViewHolder holder,
            int position) {
        if (planItemList != null && !planItemList.isEmpty()) {
            PlanTemplate planItem = planItemList.get(position);
            holder.planName.setText(planItem.getName());
            if (isPositionActive(position)) {
                holder.itemView.setBackgroundColor(Color.parseColor("#cccccc"));
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public boolean isPositionActive(int position) {
        return (selectedPlanPositions.size() > 0 && selectedPlanPositions.contains(position));
    }

    @Override
    public int getItemCount() {
        return planItemList != null && planItemList.size() != 0 ? planItemList.size() : 0;
    }

    PlanTemplate getPlanItem(int position) {
        return planItemList.get(position);
    }

    void setPlanItems(List<PlanTemplate> planItemList) {
        this.planItemList = planItemList;
        notifyDataSetChanged();
    }

    void setChildPlansIds (List<ChildPlan> childPlans) {
        for (ChildPlan plan : childPlans) {
            this.childPlansIds.add(plan.getPlanTemplateId());
        }
        notifyDataSetChanged();
    }

    void setSelectedPlanPosition(List<Integer> positions) {
        this.selectedPlanPositions = positions;
        notifyDataSetChanged();
    }

    class ActivePlanListViewHolder extends RecyclerView.ViewHolder {

        TextView planName;
        PlanItemClickListener planItemClickListener;

        ActivePlanListViewHolder(View itemView, PlanItemClickListener planItemClickListener) {
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
}

