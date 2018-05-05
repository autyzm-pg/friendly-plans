package pg.autyzm.friendly_plans.view.plan_list;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import database.repository.PlanTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;

public class PlanListActivity extends AppCompatActivity {

    @Inject
    PlanTemplateRepository planTemplateRepository;

    private PlanRecyclerViewAdapter planListAdapter;

//    PlanRecyclerViewAdapter.PlanItemClickListener planItemClickListener =
//            new PlanRecyclerViewAdapter.PlanItemClickListener() {
//                @Override
//                public void onPlanItemClick(int position) {
//
//                }
//            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_plan_list);
        setUpViews();
        planListAdapter.setPlanItems(planTemplateRepository.getAll());
    }

    private void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_plan_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        planListAdapter = new PlanRecyclerViewAdapter();
        recyclerView.setAdapter(planListAdapter);
    }
}
