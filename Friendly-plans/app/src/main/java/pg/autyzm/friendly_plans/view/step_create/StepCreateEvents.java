package pg.autyzm.friendly_plans.view.step_create;


import android.view.View;

public interface StepCreateEvents {

    void onPlayStopSoundClick(View view);

    public interface StepData {

        void saveStepData(StepCreateData stepCreateData);

        void selectStepPicture();

        void cleanStepPicture();

        void showPicture();
    }
}
