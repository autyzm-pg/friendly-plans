package pg.autyzm.friendly_plans.view.task_create;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.R;

public class TaskCreateActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_management);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Fragment taskCreateFragment = new TaskCreateFragment();
        taskCreateFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.task_container, taskCreateFragment)
                .commit();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.task_menu, new TaskMenuFragment())
                .commit();
    }
}
