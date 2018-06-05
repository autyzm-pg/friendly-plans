package pg.autyzm.friendly_plans.view.child_settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import database.repository.PlanTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;

public class ChildSettingsActivity extends AppCompatActivity {

    @Inject
    PlanTemplateRepository planTemplateRepository;

    private ActivePlanRecyclerViewAdapter planListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_child_settings);
        setUpViews();
        planListAdapter.setPlanItems(planTemplateRepository.getAll());
    }

    private void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_child_settings_plan_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        planListAdapter = new ActivePlanRecyclerViewAdapter();
        recyclerView.setAdapter(planListAdapter);
    }
}
