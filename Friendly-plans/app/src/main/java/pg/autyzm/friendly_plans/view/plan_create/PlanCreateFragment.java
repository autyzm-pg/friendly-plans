package pg.autyzm.friendly_plans.view.plan_create;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;

public class PlanCreateFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ((App) getActivity().getApplication()).getAppComponent().inject(this);
        return inflater.inflate(R.layout.fragment_plan_create, container, false);
    }
}
