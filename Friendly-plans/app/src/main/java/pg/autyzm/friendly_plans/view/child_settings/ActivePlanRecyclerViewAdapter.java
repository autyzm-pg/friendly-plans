package pg.autyzm.friendly_plans.view.child_settings;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import database.entities.PlanTemplate;
import java.util.ArrayList;
import java.util.List;
import pg.autyzm.friendly_plans.R;

public class ActivePlanRecyclerViewAdapter extends
        RecyclerView.Adapter<ActivePlanRecyclerViewAdapter.ActivePlanListViewHolder> {

    private List<PlanTemplate> planItemList;

    ActivePlanRecyclerViewAdapter() {
        this.planItemList = new ArrayList<>();
    }

    @Override
    public ActivePlanRecyclerViewAdapter.ActivePlanListViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_active_plan, parent, false);
        return new ActivePlanRecyclerViewAdapter.ActivePlanListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ActivePlanRecyclerViewAdapter.ActivePlanListViewHolder holder,
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

    void setPlanItems(List<PlanTemplate> planItemList) {
        this.planItemList = planItemList;
        notifyDataSetChanged();
    }


    class ActivePlanListViewHolder extends RecyclerView.ViewHolder {

        TextView planName;

        ActivePlanListViewHolder(View itemView) {
            super(itemView);
            this.planName = (TextView) itemView
                    .findViewById(R.id.id_tv_plan_name);
        }
    }
}

