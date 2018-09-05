package pg.autyzm.friendly_plans.manager_app.view.step_create;

public interface StepCreateEvents {

    interface StepData {

        void saveStepData(StepCreateData stepCreateData);

        void selectStepPicture();

        void cleanStepPicture();

        void showPicture();

        void selectStepSound();

        void clearStepSound();
    }
}
