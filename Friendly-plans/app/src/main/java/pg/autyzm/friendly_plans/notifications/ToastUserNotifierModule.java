package pg.autyzm.friendly_plans.notifications;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class ToastUserNotifierModule {

    @Provides
    @Singleton
    protected ToastUserNotifier getToastUserNotifier() {
        return new ToastUserNotifier();
    }

}
