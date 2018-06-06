package pg.autyzm.friendly_plans.manager_app.view.child_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import database.entities.Child;
import java.util.Collections;
import java.util.List;
import pg.autyzm.friendly_plans.R;

public class ChildRecyclerViewAdapter extends
        RecyclerView.Adapter<ChildRecyclerViewAdapter.ChildListViewHolder> {

    private List<Child> childItemList;

    ChildRecyclerViewAdapter() {
        this.childItemList = Collections.emptyList();
    }

    @Override
    public ChildRecyclerViewAdapter.ChildListViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_child, parent, false);
        return new ChildRecyclerViewAdapter.ChildListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChildRecyclerViewAdapter.ChildListViewHolder holder,
            int position) {
        if (childItemList != null && !childItemList.isEmpty()) {
            Child childItem = childItemList.get(position);
            holder.childName.setText(childItem.getName() + " " + childItem.getSurname());
        }
    }

    @Override
    public int getItemCount() {
        return childItemList != null && childItemList.size() != 0 ? childItemList.size() : 0;
    }

    void setChildItems(List<Child> childItemList) {
        this.childItemList = childItemList;
        notifyDataSetChanged();
    }

    static class ChildListViewHolder extends RecyclerView.ViewHolder {

        TextView childName;

        ChildListViewHolder(View itemView) {
            super(itemView);
            this.childName = (TextView) itemView
                    .findViewById(R.id.id_tv_child_name);
        }
    }
}
