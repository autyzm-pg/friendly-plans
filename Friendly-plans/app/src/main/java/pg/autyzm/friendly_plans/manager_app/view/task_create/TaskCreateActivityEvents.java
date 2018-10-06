package pg.autyzm.friendly_plans.manager_app.view.task_create;

import android.view.View;

public interface TaskCreateActivityEvents {

    void eventListStep(View view);

    void eventSelectPicture(View view);

    void eventSelectSound(View view);

    void eventSaveAndFinish(View view);

    void eventClearPicture(View view);

    void eventClearSound(View view);

    void eventClickPreviewPicture(View view);

    void eventChangeButtonStepsVisibility(View view, int id);
}
