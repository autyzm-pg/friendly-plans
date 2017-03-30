package pg.autyzm.friendly_plans;

import android.app.Application;
import database.repository.DaoSessionModule;
import database.repository.RepositoryModule;
import pg.autyzm.friendly_plans.utils.StringProviderModule;
import pg.autyzm.friendly_plans.utils.ValidationModule;

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
                .build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
