package pg.autyzm.friendly_plans.validation;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

@Module
public class ValidationModule {

    @Provides
    @Singleton
    protected TaskValidation getTaskValidation(StringsProvider stringsProvider) {
        return new TaskValidation(stringsProvider);
    }
}
