package pg.autyzm.friendly_plans.manager_app.view.plan_create;

import android.app.Fragment;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.manager_app.view.task_type_enum.TaskType;


public class PlanCreateActivity extends FragmentActivity {

    Fragment planMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_management);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        Fragment planCreateFragment = new PlanCreateFragment();
        planCreateFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.plan_container, planCreateFragment)
                .commit();

        planMenuFragment = new PlanMenuFragment();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.plan_menu, planMenuFragment)
                .commit();

        getFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {

                setDefaultPropertiesForAllButtons();
                if (getFragmentManager().getBackStackEntryCount() == 0) {
                    planMenuFragment.getView().findViewById(R.id.id_navigation_plan_name).setEnabled(true);
                } else {
                    Fragment fragment = getFragmentManager().findFragmentById(R.id.plan_container);
                    Integer typeId = (Integer) fragment.getArguments()
                            .get(ActivityProperties.TYPE_ID);
                    TaskType type = TaskType.getTaskType(typeId);
                    planMenuFragment.getView().findViewById(type.getNavigationButtonId()).setEnabled(true);
                }
            }
        });
    }

    private void  setDefaultPropertiesForAllButtons() {
        planMenuFragment.getView().findViewById(R.id.id_navigation_plan_name).setEnabled(false);
        planMenuFragment.getView().findViewById(R.id.id_navigation_plan_tasks).setEnabled(false);
        planMenuFragment.getView().findViewById(R.id.id_navigation_plan_prizes).setEnabled(false);
        planMenuFragment.getView().findViewById(R.id.id_navigation_plan_interactions).setEnabled(false);
    }
}
