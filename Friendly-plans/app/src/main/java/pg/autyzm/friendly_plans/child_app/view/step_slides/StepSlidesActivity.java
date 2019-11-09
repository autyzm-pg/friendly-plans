package pg.autyzm.friendly_plans.child_app.view.step_slides;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
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

import static android.view.View.VISIBLE;

public class StepSlidesActivity extends AppCompatActivity {

    @Inject
    StepTemplateRepository stepTemplateRepository;

    private List<StepTemplate> steps;
    private String imageDirectory;
    private Integer currentStep;
    private ChildActivityState currentStepStatus;

    TextView stepName;
    ImageView stepImage;
    TextView stepDuration;
    ImageView stepTimerIcon;
    ImageButton backButton;
    ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);

        steps = stepTemplateRepository.getAll(
                getIntent().getExtras().getLong(ActivityProperties.TASK_ID)
        );
        if (steps.isEmpty())
            finish();

        setUpView();
        setCurrentStep(0);
    }

    private void setCurrentStep(int index){
        currentStep = index;
        StepTemplate step = steps.get(index);
        setupStepView(step);
        currentStepStatus = step.getDuration() == null ?
                ChildActivityState.FINISHED : ChildActivityState.PENDING_START;
    }

    private void setupStepView(StepTemplate step){
        stepName.setText(step.getName());

        Integer duration = step.getDuration();
        if(duration != null) {
            stepDuration.setText(String.format("%d %s", duration, Consts.DURATION_UNIT_SECONDS));
            displayNavigationControls(false);
            displayTimerControls(true);
        }
        else {
            displayNavigationControls(true);
            displayTimerControls(false);
        }

        if (step.getPicture() != null) {
            stepImage.setVisibility(VISIBLE);
            String imageName = step.getPicture().getFilename();
            Picasso.get()
                    .load(new File(imageDirectory + File.separator + imageName))
                    .into(stepImage);
        } else
            stepImage.setVisibility(View.INVISIBLE);
    }

    private void displayNavigationControls(boolean shouldDisplay){
        if(shouldDisplay) {
            nextButton.setVisibility(VISIBLE);
            backButton.setVisibility(VISIBLE);
        }
        else {
            nextButton.setVisibility(View.INVISIBLE);
            backButton.setVisibility(View.INVISIBLE);
        }
    }

    private void displayTimerControls(boolean shouldDisplay){
        if(shouldDisplay) {
            stepDuration.setVisibility(VISIBLE);
            stepTimerIcon.setVisibility(VISIBLE);
        }
        else {
            stepDuration.setVisibility(View.INVISIBLE);
            stepTimerIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void setUpView() {
        setContentView(R.layout.activity_step_slides);
        imageDirectory = getApplicationContext().getFilesDir().toString();

        stepName = (TextView)findViewById(R.id.id_tv_step_name);
        stepImage = (ImageView)findViewById(R.id.id_iv_step_image);
        stepDuration = (TextView)findViewById(R.id.id_tv_step_duration_time);
        stepTimerIcon = (ImageView)findViewById(R.id.id_iv_step_duration_icon);
        backButton = (ImageButton)findViewById(R.id.id_bv_back_button);
        nextButton = (ImageButton)findViewById(R.id.id_bv_next_button);

        backButton.setOnClickListener(backButtonListener);
        nextButton.setOnClickListener(nextButtonListener);
        stepTimerIcon.setOnClickListener(timerButtonListener);
    }

    View.OnClickListener backButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(currentStep > 0)
                showPreviousStep();
            else
                finish();
        }
    };

    View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentStepCompleted();
        }
    };

    View.OnClickListener timerButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentStepStatus == ChildActivityState.PENDING_START)
                startChildActivityExecution(stepDuration);
            else if(currentStepStatus == ChildActivityState.FINISHED)
                displayNavigationControls(true);
        }
    };

    private void goToNextStep(){
        setCurrentStep(++currentStep);
    }

    private void showPreviousStep(){
        setCurrentStep(--currentStep);
    }

    private void currentStepCompleted(){
        if (currentStep + 1 < steps.size())
            goToNextStep();
        else {
            Intent intentWithResult = new Intent();
            intentWithResult.putExtra(Consts.RETURN_MESSAGE_KEY, Consts.MESSAGE_STEPS_COMPLETED);
            setResult(Consts.RETURN_MESSAGE_CODE, intentWithResult);
            finish();
        }
    }

    private void startChildActivityExecution(final TextView durationLabel){
        final Handler timerHandler = new Handler();
        String durationStr = durationLabel.getText().toString();
        Integer duration = Integer.parseInt(durationStr.replaceAll("[^0-9]", ""));

        Runnable updater = new ChildActivityExecutor(duration, durationLabel, timerHandler,
                new ChildActivityExecutor.ActivityCompletedListener(){
                    @Override
                    public void onActivityCompleted() {
                        currentStepStatus = ChildActivityState.FINISHED;
                    }
                });

        timerHandler.post(updater);
        currentStepStatus = ChildActivityState.IN_PROGRESS;
    }
}
