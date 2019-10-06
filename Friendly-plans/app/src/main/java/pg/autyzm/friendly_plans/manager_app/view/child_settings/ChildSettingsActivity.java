package pg.autyzm.friendly_plans.manager_app.view.child_settings;

import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import database.repository.ChildRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;

public class ChildSettingsActivity extends FragmentActivity  {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_settings);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();


        Fragment childSettingsFragment = new ChildSettingsFragment();
        childSettingsFragment.setArguments(bundle);

            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.id_layout_child_settings_activity, childSettingsFragment)
                    .commit();

        AppComponent appComponent = ((App) getApplication()).getAppComponent();
        appComponent.inject(this);
    }
}
