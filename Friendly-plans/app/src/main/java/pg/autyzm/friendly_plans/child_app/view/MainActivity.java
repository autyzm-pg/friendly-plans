package pg.autyzm.friendly_plans.child_app.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import javax.inject.Inject;

import database.entities.ChildPlan;
import database.repository.ChildPlanRepository;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;

public class MainActivity extends AppCompatActivity {

    @Inject
    ChildPlanRepository childPlanRepository;

    ChildPlan activePlan;

    TextView planNameWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        activePlan = childPlanRepository.getActivePlan();
        if (activePlan != null) {
            setUpView();
        }
        //Todo - else: redirect to finish activity
    }

    private void setUpView() {
        setContentView(R.layout.child_app_activity_main);
    }

}
