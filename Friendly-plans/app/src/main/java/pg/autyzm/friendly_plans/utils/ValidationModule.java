package pg.autyzm.friendly_plans.utils;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import pg.autyzm.friendly_plans.validation.TaskValidation;

@Module
public class ValidationModule {

    @Provides
    @Singleton
    TaskValidation getTaskValidation(StringsProvider stringsProvider) {
        return new TaskValidation(stringsProvider);
    }
}
