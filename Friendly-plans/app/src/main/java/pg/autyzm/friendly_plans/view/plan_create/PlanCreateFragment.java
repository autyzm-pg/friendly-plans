package pg.autyzm.friendly_plans.view.plan_create;

import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.databinding.FragmentPlanCreateBinding;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.validation.PlanValidation;
import pg.autyzm.friendly_plans.validation.ValidationResult;
import pg.autyzm.friendly_plans.validation.ValidationStatus;
import pg.autyzm.friendly_plans.view.plan_create_task_list.PlanTaskListFragment;
import pg.autyzm.friendly_plans.view.view_fragment.CreateFragment;

public class PlanCreateFragment extends CreateFragment implements PlanCreateActivityEvents {

    @Inject
    PlanTemplateRepository planTemplateRepository;

    @Inject
    ToastUserNotifier toastUserNotifier;

    @Inject
    PlanValidation planValidation;

    PlanCreateData planData;

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
    public void savePlanData(PlanCreateData planCreateData) {
        Long planId = addPlan(planCreateData.getPlanName());
        if (planId != null) {
            showPlanTaskList();
        }
    }

    private Long addPlan(String planName) {
        if (validateName(planName)) {
            try {
                long planId = planTemplateRepository.create(planName);
                showToastMessage(R.string.plan_saved_message);
                return planId;
            } catch (RuntimeException exception) {
                return handleSavingError(exception);
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
