package dao;

import dagger.Module;
import dagger.Provides;
import database.DaoSession;
import javax.inject.Singleton;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    TaskTemplateRepository getTaskTemplateRepository(DaoSession daoSession) {
        return new TaskTemplateRepository(daoSession);
    }
}
