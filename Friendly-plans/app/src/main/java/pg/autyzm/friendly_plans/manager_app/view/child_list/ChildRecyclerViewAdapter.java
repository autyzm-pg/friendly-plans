package pg.autyzm.friendly_plans.manager_app.view.child_list;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import database.entities.Child;
import java.util.Collections;
import java.util.List;
import pg.autyzm.friendly_plans.R;

public class ChildRecyclerViewAdapter extends
        RecyclerView.Adapter<ChildRecyclerViewAdapter.ChildListViewHolder> {

    private List<Child> childItemList;
    private ChildItemClickListener childItemClickListener;
    private Integer selectedChildPosition;

    ChildRecyclerViewAdapter(ChildItemClickListener childItemClickListener) {
        this.childItemClickListener = childItemClickListener;
        this.childItemList = Collections.emptyList();
    }

    interface ChildItemClickListener {

        void onRemoveChildClick(long itemId);

        void onChildItemClick(int position);
    }

    @Override
    public ChildRecyclerViewAdapter.ChildListViewHolder onCreateViewHolder(ViewGroup parent,
            int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_child, parent, false);
        return new ChildRecyclerViewAdapter.ChildListViewHolder(view, childItemClickListener,
                childItemList);
    }

    @Override
    public void onBindViewHolder(ChildRecyclerViewAdapter.ChildListViewHolder holder,
            int position) {
        if (childItemList != null && !childItemList.isEmpty()) {
            Child childItem = childItemList.get(position);
            holder.childName.setText(childItem.getName() + " " + childItem.getSurname());
            if (isPositonActive(position)) {
                holder.itemView.setBackgroundColor(Color.parseColor("#cccccc"));
            } else {
                holder.itemView.setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    public boolean isPositonActive(int position) {
        return (selectedChildPosition != null && selectedChildPosition == position) || (
                selectedChildPosition == null && childItemList.get(position).getIsActive());
    }

    @Override
    public int getItemCount() {
        return childItemList != null && childItemList.size() != 0 ? childItemList.size() : 0;
    }

    Child getChild(int position) {
        return childItemList.get(position);
    }


    void setChildItems(List<Child> childItemList) {
        this.childItemList = childItemList;
        notifyDataSetChanged();
    }

    void setSelectedChildPosition(int selectedChildPosition) {
        this.selectedChildPosition = selectedChildPosition;
        notifyDataSetChanged();
    }

    static class ChildListViewHolder extends RecyclerView.ViewHolder {

        TextView childName;
        ImageButton removeButton;
        ChildItemClickListener childItemClickListener;
        List<Child> childItemList;

        ChildListViewHolder(View itemView, ChildItemClickListener childItemClickListener,
                List<Child> childItemList) {
            super(itemView);
            this.childName = (TextView) itemView
                    .findViewById(R.id.id_tv_child_name);
            this.removeButton = (ImageButton) itemView
                    .findViewById(R.id.id_remove_child);
            this.childItemList = childItemList;
            this.childItemClickListener = childItemClickListener;
            this.removeButton.setOnClickListener(deleteButtonListener);
            itemView.setOnClickListener(selectItemListener);
        }

        View.OnClickListener deleteButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = childItemList.get(getAdapterPosition()).getId();
                childItemClickListener.onRemoveChildClick(id);
            }
        };

        View.OnClickListener selectItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childItemClickListener.onChildItemClick(getAdapterPosition());
            }
        };
    }
}
