package com.przyjaznyplanDisplayer.mymodule.appmanager.Utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.przyjaznyplan.models.Activity;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;

import java.util.List;


public class ActivityAssignmentAdapter extends ArrayAdapter<Activity> {

    private final List<Activity> activities;
    private final int resourceId;
    private final LayoutInflater mInflater;

    public ActivityAssignmentAdapter(Context context, int resourceId, List<Activity> activities) {
        super(context, resourceId, activities);

        mInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        this.resourceId = resourceId;
        this.activities = activities;
    }

    @Override
    public int getCount() {
        return activities.size();
    }

    @Override
    public Activity getItem(int position) {
        return activities.get(position);
    }

    @Override
    public View getView(int position, View v, ViewGroup parent){
        Activity activity = this.activities.get(position);

        if(v == null )
            v = mInflater.inflate(resourceId, null);

        TextView activityTitle = (TextView) v.findViewById(R.id.activityAssignment);

        activityTitle.setText(activity.getTitle());

        return v;
    }

}
