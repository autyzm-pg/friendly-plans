package pg.autyzm.friendly_plans.manager_app.view.activate_plan;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import database.entities.PlanTemplate;
import pg.autyzm.friendly_plans.R;

public class ActivatePlanRecyclerViewAdapter extends
        RecyclerView.Adapter<ActivatePlanRecyclerViewAdapter.ActivatePlanListViewHolder> {

    private List<PlanTemplate> planItemList;

    @Override
    public ActivatePlanRecyclerViewAdapter.ActivatePlanListViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_active_plan, parent, false);
        return new ActivatePlanRecyclerViewAdapter.ActivatePlanListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivatePlanRecyclerViewAdapter.ActivatePlanListViewHolder holder,
                                 int position) {
        if (planItemList != null && !planItemList.isEmpty()) {
            PlanTemplate planItem = planItemList.get(position);
            holder.planName.setText(planItem.getName());
        }
    }

    @Override
    public int getItemCount() {
        return planItemList != null && planItemList.size() != 0 ? planItemList.size() : 0;
    }


    class ActivatePlanListViewHolder extends RecyclerView.ViewHolder {
        TextView planName;

        ActivatePlanListViewHolder(View itemView) {
            super(itemView);
            this.planName = (TextView) itemView
                    .findViewById(R.id.id_tv_plan_name);
        }
    }

    void setPlanItems(List<PlanTemplate> planItemList) {
        this.planItemList = planItemList;
        notifyDataSetChanged();
    }
}
