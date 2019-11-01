package pg.autyzm.friendly_plans.child_app.view.step_list;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import database.entities.StepTemplate;
import database.repository.StepTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;

public class StepListActivity extends AppCompatActivity {

    private StepRecyclerViewAdapter stepRecyclerViewAdapter;

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
        String filesDirectory = getApplicationContext().getFilesDir().toString();
        stepRecyclerViewAdapter = new StepRecyclerViewAdapter(steps, filesDirectory, stepListener);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_child_app_step_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(stepRecyclerViewAdapter);
    }

    Runnable updater;
    Integer asd = 0;

    StepRecyclerViewAdapter.StepItemClickListener stepListener = new StepRecyclerViewAdapter.StepItemClickListener() {
        @Override
        public void selectStepListener(int position, final TextView stepName) {
//            while (stepRecyclerViewAdapter.currentStepPosition != 3) {
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                stepRecyclerViewAdapter.currentStepPosition++;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        stepRecyclerViewAdapter.notifyDataSetChanged();
//                    }
//                });
//            }
//        }
            final Handler timerHandler = new Handler();

            updater = new Runnable() {
                @Override
                public void run() {
                    stepName.setText(asd.toString());
                    asd++;
                    timerHandler.postDelayed(updater, 1000);
                }
            };
            timerHandler.post(updater);
        }
    };
}
