package pg.autyzm.friendly_plans.view.plan_create;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import pg.autyzm.friendly_plans.BR;


public class PlanCreateData extends BaseObservable {

    private String planName;

    public PlanCreateData(String planName) {
        this.planName = planName;
    }

    @Bindable
    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
        notifyPropertyChanged(BR.planName);
    }
}
