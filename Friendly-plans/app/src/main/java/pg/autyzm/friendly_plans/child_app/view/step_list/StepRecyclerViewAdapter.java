package pg.autyzm.friendly_plans.child_app.view.step_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import database.entities.StepTemplate;
import pg.autyzm.friendly_plans.R;

public class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.StepRecyclerViewHolder> {
    private List<StepTemplate> steps;

    static class StepRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView stepName;

        StepRecyclerViewHolder(View itemView) {
            super(itemView);
            this.stepName = (TextView) itemView.findViewById(R.id.id_tv_step_name);
        }
    }

    StepRecyclerViewAdapter(List<StepTemplate> steps) {
        this.steps = steps;
    }

    @Override
    public StepRecyclerViewAdapter.StepRecyclerViewHolder onCreateViewHolder(ViewGroup parent,
                                                                             int viewType) {
        View stepView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_app_item_step, parent, false);
        return new StepRecyclerViewAdapter.StepRecyclerViewHolder(stepView);
    }

    @Override
    public void onBindViewHolder(StepRecyclerViewAdapter.StepRecyclerViewHolder holder, int position) {
        if (steps != null && !steps.isEmpty()) {
            StepTemplate step = steps.get(position);
            holder.stepName.setText(step.getName());
        }
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }
}
