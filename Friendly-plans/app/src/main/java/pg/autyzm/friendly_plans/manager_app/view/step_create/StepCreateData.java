package pg.autyzm.friendly_plans.manager_app.view.step_create;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import database.entities.Asset;
import database.entities.StepTemplate;
import pg.autyzm.friendly_plans.BR;

public class StepCreateData extends BaseObservable {

    private static final String REGEX_TRIM_NAME = "_([\\d]*)(?=\\.)";
    private static final String EMPTY_VALUE = "";

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
        return getAssetFileName(stepTemplate.getPictureId());
    }

    public void setPictureName(String pictureName) {
        this.stepTemplate.setPicture(getAsset(pictureName, this.stepTemplate.getPicture()));
        notifyPropertyChanged(BR.pictureName);
    }

    public void setPicture(Asset picture) {
        this.stepTemplate.setPicture(picture);
        notifyPropertyChanged(BR.pictureName);
    }

    @Bindable
    public String getSoundName() {
        return getAssetFileName(stepTemplate.getSoundId());
    }

    public void setSoundName(String soundName) {
        this.stepTemplate.setSound(getAsset(soundName, this.stepTemplate.getSound()));
        notifyPropertyChanged(BR.soundName);
    }

    public void setSound(Asset sound) {
        this.stepTemplate.setSound(sound);
        notifyPropertyChanged(BR.soundName);
    }

    @Bindable
    public String getStepDuration() {
        if(stepTemplate.getDurationTime() != null) {
            return stepTemplate.getDurationTime().toString();
        }

        return EMPTY_VALUE;
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

    private Asset getAsset(String fileName, Asset asset) {
        if (fileName == null && asset == null) {
            return null;
        }

        asset.setFilename(fileName);

        return asset;
    }

    private String getAssetFileName(Long assetId) {
        if (assetId == null) {
            return EMPTY_VALUE;
        }

        return stepTemplate.getSound().getFilename().replace(REGEX_TRIM_NAME, "");
    }
}
