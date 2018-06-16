package pg.autyzm.friendly_plans.manager_app.view.plan_create;

import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import database.entities.PlanTemplate;
import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.databinding.FragmentPlanCreateBinding;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.manager_app.validation.PlanValidation;
import pg.autyzm.friendly_plans.manager_app.validation.ValidationResult;
import pg.autyzm.friendly_plans.manager_app.validation.ValidationStatus;
import pg.autyzm.friendly_plans.manager_app.view.plan_create_task_list.PlanTaskListFragment;
import pg.autyzm.friendly_plans.manager_app.view.view_fragment.CreateFragment;

public class PlanCreateFragment extends CreateFragment implements PlanCreateActivityEvents {

    @Inject
    PlanTemplateRepository planTemplateRepository;

    @Inject
    ToastUserNotifier toastUserNotifier;

    @Inject
    PlanValidation planValidation;

    PlanCreateData planData;
    private Long planId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        FragmentPlanCreateBinding binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_plan_create, container, false);
        View view = binding.getRoot();

        AppComponent appComponent = ((App) getActivity().getApplication()).getAppComponent();
        appComponent.inject(this);

        planData = new PlanCreateData("");
        binding.setPlanData(planData);
        binding.setPlanDataClick(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            Long planId = (Long) arguments.get(ActivityProperties.PLAN_ID);

            if (planId != null) {
                initPlanForm(planId);
            }
        }
    }

    private void initPlanForm(long planId) {
        this.planId = planId;

        PlanTemplate plan = planTemplateRepository.get(planId);
        planData.setPlanName(plan.getName());
    }

    @Override
    public void savePlanData(PlanCreateData planCreateData) {
        Long planId = addPlan(planCreateData.getPlanName());
        if (planId != null) {
            showPlanTaskList();
        }
    }

    private Long addPlan(String planName) {
        if (validateName(planName)) {
            try {
                if(planId != null) {
                    planTemplateRepository.update(planId, planName);
                    showToastMessage(R.string.plan_saved_message);
                    return planId;
                } else {
                    long planId = planTemplateRepository.create(planName);
                    showToastMessage(R.string.plan_saved_message);
                    return planId;
                }
            } catch (RuntimeException exception) {
                Log.e("Plan Create View", "Error saving plan", exception);
                showToastMessage(R.string.save_plan_error_message);
            }
        }
        return null;
    }

    @Override
    protected void setAssetValue(AssetType assetType, String assetName, Long assetId) {
        // Intentionally empty - plan does not includes assets
    }

    private boolean validateName(String taskName) {
        ValidationResult validationResult = planValidation
                .isNewNameValid(taskName);
        if (validationResult.getValidationStatus().equals(ValidationStatus.INVALID)) {
            planData.setNameFieldError(validationResult.getValidationInfo());
            Toast toast = Toast.makeText(
                    getActivity().getApplicationContext(),
                    validationResult.getValidationInfo(),
                    Toast.LENGTH_SHORT
            );
            toast.show();
            return false;
        }
        return true;
    }

    private void showPlanTaskList() {
        PlanTaskListFragment fragment = new PlanTaskListFragment();

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.plan_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
