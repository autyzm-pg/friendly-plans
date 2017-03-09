package pg.autyzm.friendly_plans;

import android.app.Application;
import database.repository.DaggerRepositoryComponent;
import database.repository.RepositoryComponent;
import database.repository.DaoSessionModule;
import database.repository.RepositoryModule;

public class App extends Application {

    private RepositoryComponent repositoryComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        repositoryComponent = DaggerRepositoryComponent.builder()
                .daoSessionModule(new DaoSessionModule(this.getApplicationContext()))
                .repositoryModule(new RepositoryModule())
                .build();

    }

    public RepositoryComponent getRepositoryComponent() {
        return repositoryComponent;
    }
}
