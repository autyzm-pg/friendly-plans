package pg.autyzm.friendly_plans.view.plan_create_task_list;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import database.entities.PlanTaskTemplate;
import java.util.Collections;
import java.util.List;
import pg.autyzm.friendly_plans.R;

public class PlanTaskListRecyclerViewAdapter extends
        RecyclerView.Adapter<PlanTaskListRecyclerViewAdapter.PlanTaskListViewHolder> {

    private List<PlanTaskTemplate> taskItemList;

    PlanTaskListRecyclerViewAdapter() {
        this.taskItemList = Collections.emptyList();
    }

    @Override
    public PlanTaskListRecyclerViewAdapter.PlanTaskListViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new PlanTaskListRecyclerViewAdapter.PlanTaskListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PlanTaskListRecyclerViewAdapter.PlanTaskListViewHolder holder,
            int position) {

    }

    @Override
    public int getItemCount() {
        return taskItemList != null && taskItemList.size() != 0 ? taskItemList.size() : 0;
    }

    static class PlanTaskListViewHolder extends RecyclerView.ViewHolder {

        PlanTaskListViewHolder(View itemView) {
            super(itemView);
        }
    }
}
