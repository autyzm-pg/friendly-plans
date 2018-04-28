package pg.autyzm.friendly_plans.view.plan_create;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import pg.autyzm.friendly_plans.R;


public class PlanCreateActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_management);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.plan_container, new PlanCreateFragment())
                .commit();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.plan_menu, new PlanMenuFragment())
                .commit();
    }
}
