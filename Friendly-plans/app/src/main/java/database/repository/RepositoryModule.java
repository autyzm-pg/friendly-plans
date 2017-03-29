package database.repository;

import dagger.Module;
import dagger.Provides;
import database.entities.DaoSession;
import javax.inject.Singleton;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    protected TaskTemplateRepository getTaskTemplateRepository(DaoSession daoSession) {
        return new TaskTemplateRepository(daoSession);
    }
}
