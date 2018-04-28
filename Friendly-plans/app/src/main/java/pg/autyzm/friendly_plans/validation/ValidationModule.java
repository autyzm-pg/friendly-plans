package pg.autyzm.friendly_plans.validation;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import database.repository.StepTemplateRepository;
import database.repository.TaskTemplateRepository;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

@Module
public class ValidationModule {

    @Provides
    @Singleton
    public TaskValidation getTaskValidation(
            StringsProvider stringsProvider, TaskTemplateRepository taskTemplateRepository) {
        return new TaskValidation(stringsProvider, taskTemplateRepository);
    }


    @Provides
    @Singleton
    public StepValidation getStepValidation(
            StringsProvider stringsProvider, StepTemplateRepository stepTemplateRepository) {
        return new StepValidation(stringsProvider, stepTemplateRepository);
    }
}
