package pg.autyzm.friendly_plans.test_helpers;

import database.entities.DaoSession;
import database.repository.AssetRepository;
import database.repository.RepositoryModule;
import database.repository.StepTemplateRepository;
import database.repository.TaskTemplateRepository;
import org.mockito.Mockito;


public class RepositoryModuleMock extends RepositoryModule {

    private TaskTemplateRepository taskTemplateRepository;
    private AssetRepository assetRepository;
    private StepTemplateRepository stepTemplateRepository;

    RepositoryModuleMock() {
        taskTemplateRepository = Mockito.mock(TaskTemplateRepository.class);
        assetRepository = Mockito.mock(AssetRepository.class);
        stepTemplateRepository = Mockito.mock(StepTemplateRepository.class);
    }

    @Override
    public TaskTemplateRepository getTaskTemplateRepository(DaoSession daoSession) {
        return taskTemplateRepository;
    }

    public TaskTemplateRepository getTaskTemplateRepository() {
        return taskTemplateRepository;
    }

    @Override
    public AssetRepository getAssetRepostiory(DaoSession daoSession) {
        return assetRepository;
    }

    public AssetRepository getAssetRepository() {
        return assetRepository;
    }

    @Override
    public StepTemplateRepository getStepTemplateRepository(DaoSession daoSession) {
        return stepTemplateRepository;
    }

    public StepTemplateRepository getStepTemplateRepository() {
        return stepTemplateRepository;
    }
}
