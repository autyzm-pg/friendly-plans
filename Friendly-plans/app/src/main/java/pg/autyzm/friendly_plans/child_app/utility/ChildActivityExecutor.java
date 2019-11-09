package pg.autyzm.friendly_plans.child_app.utility;

import android.os.Handler;
import android.widget.TextView;

public class ChildActivityExecutor implements Runnable {
    private Integer duration;
    private TextView durationLabel;
    private final Handler timerHandler;
    private ActivityCompletedListener activityCompletedListener;

    public interface ActivityCompletedListener {
        void onActivityCompleted();
    }


    public ChildActivityExecutor(Integer duration, TextView durationLabel, Handler timerHandler,
                                 ActivityCompletedListener activityCompletedListener){
        this.duration = duration;
        this.durationLabel = durationLabel;
        this.timerHandler = timerHandler;
        this.activityCompletedListener = activityCompletedListener;
    }

    @Override
    public void run() {
        if (duration == 0){
            activityCompletedListener.onActivityCompleted();
            return;
        }

        duration--;
        durationLabel.setText(String.format("%d %s", duration, Consts.DURATION_UNIT_SECONDS));
        timerHandler.postDelayed(this, 1000);
    }
}
