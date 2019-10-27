package pg.autyzm.friendly_plans.child_app.view.step_list;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import database.entities.StepTemplate;
import database.repository.StepTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;

public class StepListActivity extends AppCompatActivity {

    @Inject
    StepTemplateRepository stepTemplateRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.child_app_step_list);
        setRecyclerView();
    }

    void setRecyclerView() {
        List<StepTemplate> steps = stepTemplateRepository.getAll(
                getIntent().getExtras().getLong(ActivityProperties.TASK_ID)
        );
        StepRecyclerViewAdapter stepRecyclerViewAdapter = new StepRecyclerViewAdapter(steps);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_child_app_step_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(stepRecyclerViewAdapter);
    }
}
