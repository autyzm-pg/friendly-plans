package pg.autyzm.friendly_plans.child_app.view.task_list;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import database.entities.ChildPlan;
import database.entities.TaskTemplate;
import database.repository.AssetRepository;
import database.repository.ChildPlanRepository;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.child_app.utility.ChildActivityExecutor;
import pg.autyzm.friendly_plans.child_app.utility.Consts;
import pg.autyzm.friendly_plans.child_app.utility.ChildActivityState;
import pg.autyzm.friendly_plans.child_app.utility.SoundHelper;
import pg.autyzm.friendly_plans.child_app.utility.StepsDisplayUtils;

public class TaskListActivity extends AppCompatActivity {
    @Inject
    ChildPlanRepository childPlanRepository;
    @Inject
    AssetsHelper assetsHelper;
    @Inject
    AssetRepository assetRepository;
    private TaskRecyclerViewAdapter taskRecyclerViewAdapter;

    TaskRecyclerViewAdapter.TaskItemClickListener taskItemClickListener =
            new TaskRecyclerViewAdapter.TaskItemClickListener() {
                MediaPlayer startSound;
                MediaPlayer endSound;
                AppComponent appComponent;
                @Override
                public void stepsIconListener(int position) {
                    if (position == taskRecyclerViewAdapter.getCurrentTaskPosition()
                            && taskRecyclerViewAdapter.getCurrentTaskState() != ChildActivityState.IN_PROGRESS) {
                        Intent intent = StepsDisplayUtils.getStepsDisplayIntent(
                                TaskListActivity.this,
                                taskRecyclerViewAdapter.getTaskItem(position).getId(),
                                childPlanRepository.getActivePlan().getChild().isDisplayModeSlide()
                        );
                        startActivityForResult(intent, Consts.RETURN_MESSAGE_CODE);
                    }
                }

                @Override
                public void timerIconClickListener(int clickPosition, final TextView durationLabel) {
                    if (clickPosition != taskRecyclerViewAdapter.getCurrentTaskPosition()
                            || taskRecyclerViewAdapter.getCurrentTaskState() == ChildActivityState.IN_PROGRESS)
                        return;

                    if (taskRecyclerViewAdapter.getCurrentTaskState() == ChildActivityState.FINISHED) {
                        SoundHelper.getSoundHelper(((App) getApplication()).getAppComponent()).resetLoopSound(endSound);
                        endSound.stop();
                        SoundHelper.getSoundHelper(appComponent).resetLoopSound(endSound);
                        if (clickPosition < taskRecyclerViewAdapter.getItemCount() - 1) {
                            taskRecyclerViewAdapter.setCurrentTask(clickPosition + 1);
                        }
                        else
                            goToPlanFinishedScreen();
                        return;
                    }
                    startChildActivityExecution(durationLabel);
                }

                @Override
                public void blankChildActivityListener(int clickPosition) {
                    if (clickPosition != taskRecyclerViewAdapter.getCurrentTaskPosition())
                        return;
                    if (clickPosition < taskRecyclerViewAdapter.getItemCount() - 1) {
                        taskRecyclerViewAdapter.moveToNextTask();
                        return;
                    }
                    goToPlanFinishedScreen();
                }

                public void setStartSound(MediaPlayer sound) {
                    startSound = sound;
                }

                public void setEndSound(MediaPlayer sound) {
                    endSound = sound;
                }

                public void setAppComponent(AppComponent appComponent) {
                    this.appComponent = appComponent;
                }

                private void startChildActivityExecution(final TextView durationLabel) {
                    taskRecyclerViewAdapter.setCurrentTaskState(ChildActivityState.IN_PROGRESS);
                    final Handler timerHandler = new Handler();
                    String durationStr = durationLabel.getText().toString();
                    Integer duration = Integer.parseInt(durationStr.replaceAll("[^0-9]", ""));
                    startSound.start();
                    Runnable updater = new ChildActivityExecutor(duration, durationLabel, timerHandler,
                            new ChildActivityExecutor.ActivityCompletedListener() {
                                @Override
                                public void onActivityCompleted() {
                                    taskRecyclerViewAdapter.activityCompleted();
                                    endSound.start();
                                }
                            });
                    timerHandler.post(updater);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.child_app_task_list);
        setRecyclerView();
    }

    void setRecyclerView() {
        AppComponent appComponent = ((App) getApplication()).getAppComponent();
        MediaPlayer startSound = MediaPlayer.create(this, R.raw.beep);
        MediaPlayer endSound = SoundHelper.getSoundHelper(appComponent).prepareLoopSound();
        taskItemClickListener.setStartSound(startSound);
        taskItemClickListener.setEndSound(endSound);
        taskItemClickListener.setAppComponent(appComponent);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_child_app_task_list);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ChildPlan activePlan = childPlanRepository.getActivePlan();
        List<TaskTemplate> tasks = activePlan.getPlanTemplate().getTasksWithThisPlan();
        String filesDirectory = getApplicationContext().getFilesDir().toString();
        taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(tasks, taskItemClickListener, filesDirectory);

        recyclerView.setAdapter(taskRecyclerViewAdapter);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && data.getStringExtra(Consts.RETURN_MESSAGE_KEY) != null)
            if (taskRecyclerViewAdapter.getCurrentTaskPosition() + 1 < taskRecyclerViewAdapter.getItemCount())
                taskRecyclerViewAdapter.moveToNextTask();
            else
                goToPlanFinishedScreen();
    }

    private void goToPlanFinishedScreen() {
        Intent intentWithResult = new Intent();
        intentWithResult.putExtra(Consts.RETURN_MESSAGE_KEY, Consts.MESSAGE_TASKS_COMPLETED);
        setResult(Consts.RETURN_MESSAGE_CODE, intentWithResult);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intentWithResult = new Intent();
        intentWithResult.putExtra(Consts.RETURN_MESSAGE_KEY, Consts.MESSAGE_TASKS_NOT_COMPLETED);
        setResult(Consts.RETURN_MESSAGE_CODE, intentWithResult);
        super.onBackPressed();
    }
}
