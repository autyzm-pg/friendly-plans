package pg.autyzm.friendly_plans.manager_app.view.plan_create;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pg.autyzm.friendly_plans.R;

public class PlanMenuFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_plan_menu, container, false);
    }
}
