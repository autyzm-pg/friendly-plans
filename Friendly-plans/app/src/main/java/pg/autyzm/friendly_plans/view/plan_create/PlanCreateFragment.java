package pg.autyzm.friendly_plans.view.plan_create;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.FragmentPlanCreateBinding;

public class PlanCreateFragment extends Fragment implements PlanCreateActivityEvents{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        FragmentPlanCreateBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_plan_create, container, false);

        View view = binding.getRoot();

        PlanCreateData planData = new PlanCreateData("");
        binding.setPlanData(planData);
        binding.setPlanDataClick(this);
        return view;
    }

    @Override
    public void savePlanData(PlanCreateData planCreateData) {
        Log.i("plan name", planCreateData.getPlanName() + " " ); }
}
