package pg.autyzm.friendly_plans;


import dagger.Component;
import database.repository.AssetRepository;
import database.repository.DaoSessionModule;
import database.repository.RepositoryModule;
import database.repository.TaskTemplateRepository;
import javax.inject.Singleton;

@Singleton
@Component(modules = {FilePickerModule.class, DaoSessionModule.class, RepositoryModule.class})
public interface AppComponent {

    @SuppressWarnings("unused")
    FilePickerProxy filePickerProxy();

    @SuppressWarnings("unused")
    TaskTemplateRepository taskTemplateRepository();

    @SuppressWarnings("unused")
    AssetRepository assetRepository();

    void inject(TaskContainerFragment fragment);
}