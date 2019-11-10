package pg.autyzm.friendly_plans.child_app.view.task_slides;

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

import database.entities.ChildPlan;
import database.entities.TaskTemplate;
import database.repository.ChildPlanRepository;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.child_app.utility.ChildActivityExecutor;
import pg.autyzm.friendly_plans.child_app.utility.Consts;
import pg.autyzm.friendly_plans.child_app.view.common.ChildActivityState;

import static android.view.View.VISIBLE;

public class TaskSlidesActivity extends AppCompatActivity {

    @Inject
    ChildPlanRepository childPlanRepository;

    private List<TaskTemplate> tasks;
    private String imageDirectory;
    private Integer currentTask;
    private ChildActivityState currentTaskStatus;

    TextView taskName;
    ImageView taskImage;
    TextView taskDuration;
    ImageView taskTimerIcon;
    ImageButton backButton;
    ImageButton nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);

        ChildPlan activePlan = childPlanRepository.getActivePlan();
        tasks = activePlan.getPlanTemplate().getTasksWithThisPlan();

        if (tasks.isEmpty())
            finish();

        setUpView();
        setCurrentTask(0);
    }

    private void setCurrentTask(int index){
        currentTask = index;
        TaskTemplate task = tasks.get(index);
        setupTaskView(task);
        currentTaskStatus = task.getDurationTime() == null ?
                ChildActivityState.FINISHED : ChildActivityState.PENDING_START;
    }

    private void setupTaskView(TaskTemplate task){
        taskName.setText(task.getName());

        Integer duration = task.getDurationTime();
        if(duration != null) {
            taskDuration.setText(String.format("%d %s", duration, Consts.DURATION_UNIT_SECONDS));
            displayNavigationControls(false);
            displayTimerControls(true);
        }
        else {
            displayNavigationControls(true);
            displayTimerControls(false);
        }

        if (task.getPicture() != null) {
            taskImage.setVisibility(VISIBLE);
            String imageName = task.getPicture().getFilename();
            Picasso.get()
                    .load(new File(imageDirectory + File.separator + imageName))
                    .into(taskImage);
        } else
            taskImage.setVisibility(View.INVISIBLE);
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
            taskDuration.setVisibility(VISIBLE);
            taskTimerIcon.setVisibility(VISIBLE);
        }
        else {
            taskDuration.setVisibility(View.INVISIBLE);
            taskTimerIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void setUpView() {
        setContentView(R.layout.activity_step_slides);
        imageDirectory = getApplicationContext().getFilesDir().toString();

        taskName = (TextView)findViewById(R.id.id_tv_step_name);
        taskImage = (ImageView)findViewById(R.id.id_iv_step_image);
        taskDuration = (TextView)findViewById(R.id.id_tv_step_duration_time);
        taskTimerIcon = (ImageView)findViewById(R.id.id_iv_step_duration_icon);
        backButton = (ImageButton)findViewById(R.id.id_bv_back_button);
        nextButton = (ImageButton)findViewById(R.id.id_bv_next_button);

        backButton.setOnClickListener(backButtonListener);
        nextButton.setOnClickListener(nextButtonListener);
        taskTimerIcon.setOnClickListener(timerButtonListener);
    }

    View.OnClickListener backButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(currentTask > 0)
                showPreviousTask();
            else
                finish();
        }
    };

    View.OnClickListener nextButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            currentTaskCompleted();
        }
    };

    View.OnClickListener timerButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (currentTaskStatus == ChildActivityState.PENDING_START)
                startChildActivityExecution(taskDuration);
            else if(currentTaskStatus == ChildActivityState.FINISHED)
                displayNavigationControls(true);
        }
    };

    private void goToNextTask(){
        setCurrentTask(++currentTask);
    }

    private void showPreviousTask(){
        setCurrentTask(--currentTask);
    }

    private void currentTaskCompleted(){
        if (currentTask + 1 < tasks.size())
            goToNextTask();
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
                        currentTaskStatus = ChildActivityState.FINISHED;
                    }
                });

        timerHandler.post(updater);
        currentTaskStatus = ChildActivityState.IN_PROGRESS;
    }
}
