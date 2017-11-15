package pg.autyzm.friendly_plans.test_helpers;

import dagger.Module;
import dagger.Provides;
import database.entities.DaoSession;
import javax.inject.Singleton;
import org.mockito.Mockito;

@Module
public class DaoSessionModuleMock {

    private DaoSession daoSession;

    public DaoSessionModuleMock() {
        daoSession = Mockito.mock(DaoSession.class);
    }


    @Provides
    @Singleton
    public DaoSession getDaoSession() {
        return daoSession;
    }

}
