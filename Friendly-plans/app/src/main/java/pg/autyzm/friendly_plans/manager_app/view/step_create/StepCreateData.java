package pg.autyzm.friendly_plans.manager_app.view.step_create;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import database.entities.Asset;
import database.entities.StepTemplate;
import org.apache.commons.lang3.StringUtils;
import pg.autyzm.friendly_plans.BR;

public class StepCreateData extends BaseObservable {

    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";

    private StepTemplate stepTemplate = new StepTemplate();

    public StepCreateData(Long taskId) {
        stepTemplate.setTaskTemplateId(taskId);
    }

    public Long getTaskId() {
        return stepTemplate.getTaskTemplateId();
    }

    @Bindable
    public String getStepName() {
        return stepTemplate.getName();
    }

    public void setStepName(String stepName) {
        this.stepTemplate.setName(stepName);
        notifyPropertyChanged(BR.stepName);
    }

    @Bindable
    public String getPictureName() {
        if (stepTemplate.getPictureId() == null) {
            return StringUtils.EMPTY;
        }

        return getAssetFileName(stepTemplate.getPicture());
    }

    public void setPicture(Asset picture) {
        this.stepTemplate.setPicture(picture);
        notifyPropertyChanged(BR.pictureName);
    }

    @Bindable
    public String getSoundName() {
        if (stepTemplate.getSoundId() == null) {
            return StringUtils.EMPTY;
        }

        return getAssetFileName(stepTemplate.getSound());
    }

    public void setSound(Asset sound) {
        this.stepTemplate.setSound(sound);
        notifyPropertyChanged(BR.soundName);
    }

    @Bindable
    public String getStepDuration() {
        if (stepTemplate.getDurationTime() != null) {
            return stepTemplate.getDurationTime().toString();
        }

        return StringUtils.EMPTY;
    }

    public void setStepDuration(String stepDuration) {
        this.stepTemplate.setDurationTime(Integer.valueOf(stepDuration));
        notifyPropertyChanged(BR.stepDuration);
    }

    public StepTemplate getStepTemplate() {
        return stepTemplate;
    }

    public void setStepTemplate(StepTemplate stepTemplate) {
        this.stepTemplate = stepTemplate;
    }

    private String getAssetFileName(Asset asset) {
        if (asset == null) {
            return StringUtils.EMPTY;
        }

        return asset.getFilename().replace(REGEX_TRIM_NAME, "");
    }
}
