package pg.autyzm.friendly_plans;

import android.app.Application;
import database.repository.DaoSessionModule;
import database.repository.RepositoryModule;

public class App extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder()
            .daoSessionModule(new DaoSessionModule(this.getApplicationContext()))
            .repositoryModule(new RepositoryModule())
            .filePickerModule(new FilePickerModule())
            .build();

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
