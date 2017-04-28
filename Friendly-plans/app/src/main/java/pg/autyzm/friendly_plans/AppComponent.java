package pg.autyzm.friendly_plans;

import dagger.Component;
import database.repository.AssetRepository;
import database.repository.DaoSessionModule;
import database.repository.RepositoryModule;
import database.repository.TaskTemplateRepository;
import javax.inject.Singleton;
import pg.autyzm.friendly_plans.file_picker.FilePickerModule;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.string_provider.StringProviderModule;
import pg.autyzm.friendly_plans.validation.ValidationModule;
import pg.autyzm.friendly_plans.validation.TaskValidation;

@Singleton
@Component(modules = {StringProviderModule.class,
    ValidationModule.class,
    FilePickerModule.class,
    DaoSessionModule.class,
    RepositoryModule.class})
public interface AppComponent {

    @SuppressWarnings("unused")
    TaskValidation taskValidation();

    @SuppressWarnings("unused")
    FilePickerProxy filePickerProxy();

    @SuppressWarnings("unused")
    TaskTemplateRepository taskTemplateRepository();

    @SuppressWarnings("unused")
    AssetRepository assetRepository();

    void inject(TaskContainerFragment activity);
    void inject(TaskListActivity activity);
}
