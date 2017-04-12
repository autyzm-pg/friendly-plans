package pg.autyzm.friendly_plans.file_picker;

import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class FilePickerModule {

    @Provides
    @Singleton
    public FilePickerProxy getFilePickerProxy() {
        return new FilePickerProxy();
    }
}
