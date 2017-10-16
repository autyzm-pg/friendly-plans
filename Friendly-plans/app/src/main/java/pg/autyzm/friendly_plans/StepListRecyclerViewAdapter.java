package pg.autyzm.friendly_plans;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import database.entities.StepTemplate;
import java.util.ArrayList;
import java.util.List;

public class StepListRecyclerViewAdapter extends
        RecyclerView.Adapter<StepListRecyclerViewAdapter.StepListViewHolder> {

    private List<StepTemplate> stepItemList;

    public StepListRecyclerViewAdapter() {
        this.stepItemList = new ArrayList<>();
    }

    @Override
    public StepListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step, parent, false);
        return new StepListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StepListViewHolder parent, int position) {
        if (stepItemList != null && stepItemList.size() != 0) {
            StepTemplate stepTemplate = stepItemList.get(position);
            parent.stepName.setText(stepTemplate.getName());
        }
    }

    @Override
    public int getItemCount() {
        return stepItemList != null && stepItemList.size() != 0 ? stepItemList.size() : 0;
    }

    void setStepItemListItems(List<StepTemplate> stepItemList) {
        this.stepItemList = stepItemList;
        notifyDataSetChanged();
    }

    static class StepListViewHolder extends RecyclerView.ViewHolder {

        TextView stepName;

        public StepListViewHolder(View itemView) {
            super(itemView);
            this.stepName = (TextView) itemView
                    .findViewById(R.id.id_tv_step_name);
        }
    }
}
