package pg.autyzm.friendly_plans;

import android.app.Application;
import database.repository.DaoSessionModule;
import database.repository.RepositoryModule;
import pg.autyzm.friendly_plans.file_picker.FilePickerModule;
import pg.autyzm.friendly_plans.string_provider.StringProviderModule;
import pg.autyzm.friendly_plans.validation.ValidationModule;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .daoSessionModule(new DaoSessionModule(this.getApplicationContext()))
                .repositoryModule(new RepositoryModule())
                .stringProviderModule(new StringProviderModule(this.getApplicationContext()))
                .validationModule(new ValidationModule())
                .filePickerModule(new FilePickerModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public void setComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }
}
