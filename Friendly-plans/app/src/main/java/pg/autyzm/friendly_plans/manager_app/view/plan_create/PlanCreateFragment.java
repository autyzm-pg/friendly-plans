package pg.autyzm.friendly_plans.manager_app.view.plan_create;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.databinding.DataBindingUtil;

import javax.inject.Inject;

import database.entities.PlanTemplate;
import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.asset.AssetType;
import pg.autyzm.friendly_plans.databinding.FragmentPlanCreateBinding;
import pg.autyzm.friendly_plans.manager_app.validation.PlanValidation;
import pg.autyzm.friendly_plans.manager_app.validation.ValidationResult;
import pg.autyzm.friendly_plans.manager_app.view.plan_create_task_list.PlanTaskListFragment;
import pg.autyzm.friendly_plans.manager_app.view.view_fragment.CreateFragment;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class PlanCreateFragment extends CreateFragment implements PlanCreateActivityEvents {

    @Inject
    PlanTemplateRepository planTemplateRepository;

    @Inject
    ToastUserNotifier toastUserNotifier;

    @Inject
    PlanValidation planValidation;

    PlanCreateData planData;
    private Long planId;
    EditText planNameView;

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
        planNameView = (EditText) view.findViewById(R.id.id_et_plan_name);

    }

    private void initPlanForm(long planId) {
        this.planId = planId;

        PlanTemplate plan = planTemplateRepository.get(planId);
        planData.setPlanName(plan.getName());
    }

    @Override
    public void savePlanData(PlanCreateData planCreateData, Integer typeId) {
        planId = addOrUpdatePlan(planCreateData.getPlanName());
        if (planId != null) {
            showPlanTaskList(typeId);
        }
    }

    private Long addOrUpdatePlan(String planName) {
        try {
            if (planId != null){
                if(validateName(planId, planNameView)) {
                    planTemplateRepository.update(planId, planName);
                    showToastMessage(R.string.plan_saved_message);
                    return planId;
                }
            } else if (validateName(planNameView)) {
                    long planId = planTemplateRepository.create(planName);
                    showToastMessage(R.string.plan_saved_message);
                    return planId;
                }
        } catch (RuntimeException exception) {
            Log.e("Plan Create View", "Error saving plan", exception);
            showToastMessage(R.string.save_plan_error_message);
        }
        return null;
    }

    @Override
    protected void setAssetValue(AssetType assetType, String assetName, Long assetId) {
        // Intentionally empty - plan does not includes assets
    }

    public void onResume() {
        if (planId != null) {
            initPlanForm(planId);
        }
        super.onResume();
    }

    private boolean validateName(Long taskId, EditText taskName) {
        ValidationResult validationResult = planValidation
                .isUpdateNameValid(taskId, taskName.getText().toString());
        return handleInvalidResult(taskName, validationResult);
    }

    private boolean validateName(EditText taskName) {
        ValidationResult validationResult = planValidation
                .isNewNameValid(taskName.getText().toString());
        return handleInvalidResult(taskName, validationResult);
    }

    private void showPlanTaskList(Integer typeId) {
        PlanTaskListFragment fragment = new PlanTaskListFragment();

        Bundle args = new Bundle();
        args.putLong(ActivityProperties.PLAN_ID, planId);
        args.putInt(ActivityProperties.TYPE_ID, typeId);
        fragment.setArguments(args);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.plan_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
