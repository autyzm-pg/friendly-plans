package pg.autyzm.friendly_plans.child_app.view.step_list;

import android.content.Intent;
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
import pg.autyzm.friendly_plans.child_app.utility.ChildActivityExecutor;
import pg.autyzm.friendly_plans.child_app.utility.Consts;
import pg.autyzm.friendly_plans.child_app.view.common.ChildActivityState;

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


    StepRecyclerViewAdapter.StepItemClickListener stepListener = new StepRecyclerViewAdapter.StepItemClickListener() {
        @Override
        public void selectStepListener(int clickPosition, final TextView durationLabel) {
            if (clickPosition != stepRecyclerViewAdapter.getCurrentStepPosition()
                    || stepRecyclerViewAdapter.getCurrentStepState() == ChildActivityState.IN_PROGRESS)
                return;

            if (stepRecyclerViewAdapter.getCurrentStepState() == ChildActivityState.FINISHED){
                if (clickPosition < stepRecyclerViewAdapter.getItemCount() - 1) {
                    stepRecyclerViewAdapter.setCurrentStep(clickPosition + 1);
                    return;
                }
                Intent intentWithResult = new Intent();
                intentWithResult.putExtra(Consts.RETURN_MESSAGE_KEY, Consts.MESSAGE_STEPS_COMPLETED);
                setResult(Consts.RETURN_MESSAGE_CODE, intentWithResult);
                finish();
            }

            startChildActivityExecution(durationLabel);
        }

        private void startChildActivityExecution(final TextView durationLabel){
            stepRecyclerViewAdapter.setCurrentStepState(ChildActivityState.IN_PROGRESS);
            final Handler timerHandler = new Handler();
            String durationStr = durationLabel.getText().toString();
            Integer duration = Integer.parseInt(durationStr.replaceAll("[^0-9]", ""));

            Runnable updater = new ChildActivityExecutor(duration, durationLabel, timerHandler, stepRecyclerViewAdapter);
            timerHandler.post(updater);
        }
    };
}
