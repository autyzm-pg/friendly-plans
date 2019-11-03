package pg.autyzm.friendly_plans.child_app.utility;

import android.os.Handler;
import android.widget.TextView;

import pg.autyzm.friendly_plans.child_app.view.common.ChildActivityList;

public class ChildActivityExecutor implements Runnable {
    private Integer duration;
    private TextView durationLabel;
    private final Handler timerHandler;
    private ChildActivityList recycyclerView;
    private Integer position;


    public ChildActivityExecutor(Integer position, Integer duration, TextView durationLabel,
                        Handler timerHandler, ChildActivityList recyclerView){
        this.duration = duration;
        this.durationLabel = durationLabel;
        this.timerHandler = timerHandler;
        this.recycyclerView = recyclerView;
        this.position = position;
    }

    @Override
    public void run() {
        if (duration == 0){
            recycyclerView.activityCompleted(position);
            return;
        }

        duration--;
        durationLabel.setText(String.format("%ss", duration.toString()));
        timerHandler.postDelayed(this, 1000);
    }
}