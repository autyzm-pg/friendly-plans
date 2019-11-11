package pg.autyzm.friendly_plans.child_app.utility;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.child_app.view.step_list.StepListActivity;
import pg.autyzm.friendly_plans.child_app.view.step_slides.StepSlidesActivity;

public class StepsDisplayUtils {

    public static Intent getStepsDisplayIntent(Context context, long taskId, boolean isDisplayModeSlide){
        Intent intent;
        Bundle bundle = new Bundle();
        bundle.putLong(ActivityProperties.TASK_ID, taskId);

        if (isDisplayModeSlide)
            intent = new Intent(context, StepSlidesActivity.class);
        else
            intent = new Intent(context, StepListActivity.class);

        intent.putExtras(bundle);
        return intent;
    }
}
