package pg.autyzm.friendly_plans.view.main_screen;

import android.view.View;

public interface MainActivityEvents {

    void eventShowTaskList(View view);

    void eventCreateTask(View view);

    void eventCreatePlan(View view);

    void eventShowPlanList(View view);

    void eventShowChildrenList(View view);
}
