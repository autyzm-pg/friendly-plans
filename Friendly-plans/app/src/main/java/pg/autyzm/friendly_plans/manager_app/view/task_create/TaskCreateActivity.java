package pg.autyzm.friendly_plans.manager_app.view.task_create;

import android.app.Fragment;
import android.app.FragmentManager.OnBackStackChangedListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import javax.inject.Inject;

import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class TaskCreateActivity extends FragmentActivity {

    @Inject
    ToastUserNotifier toastUserNotifier;

    Fragment taskCreateFragment;
    Fragment taskMenuFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_management);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        taskCreateFragment = new TaskCreateFragment();
        taskCreateFragment.setArguments(bundle);

        getFragmentManager()
                .beginTransaction()
                .add(R.id.task_container, taskCreateFragment)
                .commit();

        taskMenuFragment = new TaskMenuFragment();
        getFragmentManager()
                .beginTransaction()
                .add(R.id.task_menu, taskMenuFragment)
                .commit();

        getFragmentManager().addOnBackStackChangedListener(new OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                boolean isBasicInfo = getFragmentManager().getBackStackEntryCount() == 0;
                setNavigationButtonsEnabled(isBasicInfo);
            }
        });

        AppComponent appComponent = ((App) getApplication()).getAppComponent();
        appComponent.inject(this);
    }

    public void setNavigationButtonsEnabled(boolean isEnabled) {
        taskMenuFragment.getView().findViewById(R.id.id_navigation_steps).setEnabled(!isEnabled);
        taskMenuFragment.getView().findViewById(R.id.id_navigation_basic_info)
                .setEnabled(isEnabled);
    }

    ;

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, String[] permissions,
            int[] grantResults) {
        if (permsRequestCode == 1) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toastUserNotifier.displayNotifications(
                        R.string.storage_permission_granted,
                        getApplicationContext());

            } else {
                toastUserNotifier.displayNotifications(
                        R.string.storage_permission_denied,
                        getApplicationContext());
            }
        }
    }
}
