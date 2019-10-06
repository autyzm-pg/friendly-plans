package pg.autyzm.friendly_plans.manager_app.view.child_settings;


import android.databinding.BaseObservable;
import android.databinding.Bindable;
import pg.autyzm.friendly_plans.BR;

public class ChildSettingsData extends BaseObservable {
    private String firstName;
    private String lastName;
    private String fontSize;
    private boolean bigFont;
    private boolean mediumFont;
    private boolean smallFont;
    private String tasksMode;
    private boolean taskModeList;
    private boolean taskModeSlide;
    private String timerSound;
    private String stepsMode;
    private boolean stepsModeList;
    private boolean stepsModeSlide;

    public ChildSettingsData(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Bindable
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        notifyPropertyChanged(BR.firstName);
    }

    @Bindable
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        notifyPropertyChanged(BR.lastName);
    }

    @Bindable
    public String getTimerSound() {
        return timerSound;
    }

    public void setTimerSound(String timerSound) {
        this.timerSound = timerSound;
        notifyPropertyChanged(BR.timerSound);
    }

    @Bindable
    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
        notifyPropertyChanged(BR.fontSize);
    }

    @Bindable
    public boolean isBigFont() {
        return bigFont;
    }

    public void setBigFont(boolean bigFont) {
        this.bigFont = bigFont;
        notifyPropertyChanged(BR.bigFont);
    }

    @Bindable
    public boolean isMediumFont() {
        return mediumFont;
    }

    public void setMediumFont(boolean mediumFont) {
        this.mediumFont = mediumFont;
        notifyPropertyChanged(BR.mediumFont);
    }

    @Bindable
    public boolean isSmallFont() {
        return smallFont;
    }

    public void setSmallFont(boolean smallFont) {
        this.smallFont = smallFont;
        notifyPropertyChanged(BR.smallFont);
    }

    @Bindable
    public boolean isTaskModeList() {
        return taskModeList;
    }

    public void setTaskModeList(boolean taskModeList) {
        this.taskModeList = taskModeList;
        notifyPropertyChanged(BR.taskModeList);
    }

    @Bindable
    public boolean isTaskModeSlide() {
        return taskModeSlide;
    }

    public void setTaskModeSlide(boolean taskModeSlide) {
        this.taskModeSlide = taskModeSlide;
        notifyPropertyChanged(BR.taskModeSlide);
    }

    @Bindable
    public boolean isStepsModeList() {
        return stepsModeList;
    }

    public void setStepsModeList(boolean stepsModeList) {
        this.stepsModeList = stepsModeList;
        notifyPropertyChanged(BR.stepsModeList);
    }

    @Bindable
    public boolean isStepsModeSlide() {
        return stepsModeSlide;
    }

    public void setStepsModeSlide(boolean stepsModeSlide) {
        this.stepsModeSlide = stepsModeSlide;
        notifyPropertyChanged(BR.stepsModeSlide);
    }

    @Bindable
    public String getStepsMode() {
        return stepsMode;
    }

    public void setStepsMode(String stepsMode) {
        this.stepsMode = stepsMode;
        notifyPropertyChanged(BR.stepsMode);
    }

    @Bindable
    public String getTasksMode() {
        return tasksMode;
    }

    public void setTasksMode(String tasksMode) {
        this.tasksMode = tasksMode;
        notifyPropertyChanged(BR.tasksMode);
    }
}
