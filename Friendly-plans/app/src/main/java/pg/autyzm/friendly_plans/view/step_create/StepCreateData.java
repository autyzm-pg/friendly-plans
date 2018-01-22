package pg.autyzm.friendly_plans.view.step_create;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import pg.autyzm.friendly_plans.BR;

public class StepCreateData extends BaseObservable {

    private String stepName;
    private String pictureName;
    private String soundName;

    public StepCreateData(String stepName, String pictureName, String soundName) {
        this.stepName = stepName;
        this.pictureName = pictureName;
        this.soundName = soundName;
    }

    @Bindable
    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
        notifyPropertyChanged(BR.stepName);
    }

    @Bindable
    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
        notifyPropertyChanged(BR.pictureName);
    }

    @Bindable
    public String getSoundName() {
        return soundName;
    }

    public void setSoundName(String soundName) {
        this.soundName = soundName;
        notifyPropertyChanged(BR.soundName);
    }
}
