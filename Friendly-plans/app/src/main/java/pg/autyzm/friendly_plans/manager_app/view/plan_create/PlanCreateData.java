package pg.autyzm.friendly_plans.manager_app.view.plan_create;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import pg.autyzm.friendly_plans.BR;


public class PlanCreateData extends BaseObservable {

    private String planName;
    private String nameFieldError;

    public PlanCreateData(String planName) {
        this.planName = planName;
        nameFieldError = "";
    }

    @Bindable
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
        notifyPropertyChanged(BR.planName);
    }

    @Bindable
    public String getNameFieldError() {
        return nameFieldError;
    }

    public void setNameFieldError(String errorMessage) {
        nameFieldError = errorMessage;
        notifyPropertyChanged(BR.nameFieldError);
    }
}
