package database.repository;

import dagger.Module;
import dagger.Provides;
import database.entities.DaoSession;
import javax.inject.Singleton;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public TaskTemplateRepository getTaskTemplateRepository(DaoSession daoSession) {
        return new TaskTemplateRepository(daoSession);
    }

    @Provides
    @Singleton
    public AssetRepository getAssetRepostiory(DaoSession daoSession) {
        return new AssetRepository(daoSession);
    }

    @Provides
    @Singleton
    public StepTemplateRepository getStepTemplateRepository(DaoSession daoSession) {
        return new StepTemplateRepository(daoSession);
    }

    @Provides
    @Singleton
    public PlanTemplateRepository getPlanTemplateRepository(DaoSession daoSession) {
        return new PlanTemplateRepository(daoSession);
    }
}
