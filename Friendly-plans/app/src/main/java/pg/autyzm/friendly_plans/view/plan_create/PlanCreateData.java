package pg.autyzm.friendly_plans.view.plan_create;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

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
