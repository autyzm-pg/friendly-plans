package pg.autyzm.friendly_plans.utils;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class StringProviderModule {

    private StringsProvider stringProvider;

    public StringProviderModule(Context context) {
        this.stringProvider = new StringsProvider(context);
    }

    @Provides
    @Singleton
    public StringsProvider getStringProvider() {
        return stringProvider;
    }
}
