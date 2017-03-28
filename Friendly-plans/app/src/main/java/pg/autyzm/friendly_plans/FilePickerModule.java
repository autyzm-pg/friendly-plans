package pg.autyzm.friendly_plans;


import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class FilePickerModule {

    @Provides
    @Singleton
    FilePickerProxy getFilePickerProxy() {
        return new FilePickerProxy();
    }
}
