package pg.autyzm.friendly_plans;

import android.app.Application;
import dao.DaggerDaoSessionComponent;
import dao.DaoSessionComponent;
import dao.DaoSessionModule;
import dao.RepositoryModule;

public class App extends Application{

    private DaoSessionComponent daoSessionComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        daoSessionComponent = DaggerDaoSessionComponent.builder()
                .daoSessionModule(new DaoSessionModule(this.getApplicationContext()))
                .repositoryModule(new RepositoryModule())
                .build();

    }

    public DaoSessionComponent getDaoSessionComponent() {
        return daoSessionComponent;
    }
}
