package com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.ActivityAssignmentAdapter;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.List;

public class ActivityAssignmentView extends Activity implements AdapterView.OnItemClickListener {

    private ListView activityAssignmentListView;
    private ActivityAssignmentAdapter activityAssigmentAdapter;
    private List<com.przyjaznyplan.models.Activity> activitiesList;
    private Integer mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_list_view);
        activityAssignmentListView = (ListView) findViewById(R.id.activityAssignmentListView);
        activitiesList = (List<com.przyjaznyplan.models.Activity>) ActivityRepository.getActivities();

        activityAssigmentAdapter = new ActivityAssignmentAdapter(this, R.layout.activity_assignment_row_view, activitiesList);
        activityAssignmentListView.setAdapter(activityAssigmentAdapter);
        activityAssignmentListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);

        mode = (Integer)getIntent().getExtras().get("REQUESTCODE");

    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        com.przyjaznyplan.models.Activity item = activityAssigmentAdapter.getItem(i);

        Intent intent = new Intent(this, ActivityEditView.class);
        intent.putExtra("ACTIVITY",item);
        setResult(mode, intent);
        super.finish();
    }

}
