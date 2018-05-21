package pg.autyzm.friendly_plans.view.plan_create;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import database.repository.PlanTemplateRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.FragmentPlanCreateBinding;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.validation.PlanValidation;
import pg.autyzm.friendly_plans.validation.ValidationResult;
import pg.autyzm.friendly_plans.validation.ValidationStatus;
import pg.autyzm.friendly_plans.view.plan_create_task_list.PlanTaskListFragment;

public class PlanCreateFragment extends Fragment implements PlanCreateActivityEvents {


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
        showPlanTaskList();
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
