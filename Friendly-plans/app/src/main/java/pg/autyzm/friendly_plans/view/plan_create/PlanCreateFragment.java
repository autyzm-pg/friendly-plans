package pg.autyzm.friendly_plans.view.plan_create;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import database.repository.PlanTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.FragmentPlanCreateBinding;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class PlanCreateFragment extends Fragment implements PlanCreateActivityEvents {


    @Inject
    PlanTemplateRepository planTemplateRepository;

    @Inject
    ToastUserNotifier toastUserNotifier;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        FragmentPlanCreateBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_plan_create, container, false);
        View view = binding.getRoot();

        AppComponent appComponent = ((App) getActivity().getApplication()).getAppComponent();
        appComponent.inject(this);

        PlanCreateData planData = new PlanCreateData("");
        binding.setPlanData(planData);
        binding.setPlanDataClick(this);
        return view;
    }

    private Long addPlan(String planName) {
        try {
            long planId = planTemplateRepository.create(planName);
            showToastMessage(R.string.plan_saved_message);
            return planId;
        } catch (RuntimeException exception) {
            return handleSavingError(exception);
        }
    }

    @Nullable
    private Long handleSavingError(RuntimeException exception) {
        Log.e("Plan Create View", "Error saving plan", exception);
        showToastMessage(R.string.save_plan_error_message);
        return null;
    }

    private void showToastMessage(int resourceStringId) {
        toastUserNotifier.displayNotifications(
                resourceStringId,
                getActivity().getApplicationContext());
    }

    @Override
    public void savePlanData(PlanCreateData planCreateData) {
        addPlan(planCreateData.getPlanName());
    }

}
