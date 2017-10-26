package pg.autyzm.friendly_plans.view.task_create;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import pg.autyzm.friendly_plans.R;

public class TaskCreateActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_management);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.task_container, new TaskCreateFragment())
                .commit();

        getFragmentManager()
                .beginTransaction()
                .add(R.id.task_menu, new TaskMenuFragment())
                .commit();
    }
}
