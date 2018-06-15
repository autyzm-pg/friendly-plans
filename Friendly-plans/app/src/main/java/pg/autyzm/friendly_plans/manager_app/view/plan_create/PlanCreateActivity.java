package pg.autyzm.friendly_plans.manager_app.view.plan_create;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import pg.autyzm.friendly_plans.R;


public class PlanCreateActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_management);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Fragment planCreateFragment = new PlanCreateFragment();
        planCreateFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.plan_container, planCreateFragment)
                .commit();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.plan_menu, new PlanMenuFragment())
                .commit();
    }
}
