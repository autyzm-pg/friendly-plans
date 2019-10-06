package pg.autyzm.friendly_plans.manager_app.view.child_settings;


import android.view.View;

public interface ChildSettingsActivityEvents {
    void eventSelectActiveChild(View view);
    void eventSaveSettings(ChildSettingsData childSettingsData);
    void eventSetFontSize(ChildSettingsData childSettingsData, String fontSize);
    void eventSetTasksMode(ChildSettingsData childSettingsData, String tasksMode);
    void eventSetStepsMode(ChildSettingsData childSettingsData, String stepsMode);
    void selectSound();
    void clearSound();
}
