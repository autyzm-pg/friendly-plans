package pg.autyzm.friendly_plans.validation;

import dagger.Module;
import dagger.Provides;
import database.repository.TaskTemplateRepository;
import javax.inject.Singleton;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

@Module
public class ValidationModule {

    @Provides
    @Singleton
    public TaskValidation getTaskValidation(
            StringsProvider stringsProvider, TaskTemplateRepository taskTemplateRepository) {
        return new TaskValidation(stringsProvider, taskTemplateRepository);
    }
}
