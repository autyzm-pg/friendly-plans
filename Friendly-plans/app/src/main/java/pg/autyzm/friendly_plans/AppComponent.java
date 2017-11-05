package pg.autyzm.friendly_plans;

import android.media.MediaPlayer;
import dagger.Component;
import database.repository.AssetRepository;
import database.repository.DaoSessionModule;
import database.repository.RepositoryModule;
import database.repository.StepTemplateRepository;
import database.repository.TaskTemplateRepository;
import javax.inject.Singleton;
import pg.autyzm.friendly_plans.file_picker.FilePickerModule;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.string_provider.StringProviderModule;
import pg.autyzm.friendly_plans.view.step_create.StepCreateFragment;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateFragment;
import pg.autyzm.friendly_plans.validation.TaskValidation;
import pg.autyzm.friendly_plans.validation.ValidationModule;
import pg.autyzm.friendly_plans.view.step_list.StepListFragment;
import pg.autyzm.friendly_plans.view.task_list.TaskListActivity;

@Singleton
@Component(modules = {StringProviderModule.class,
        ValidationModule.class,
        FilePickerModule.class,
        DaoSessionModule.class,
        RepositoryModule.class,
        MediaPlayerModule.class})
public interface AppComponent {

    @SuppressWarnings("unused")
    TaskValidation taskValidation();

    @SuppressWarnings("unused")
    FilePickerProxy filePickerProxy();

    @SuppressWarnings("unused")
    TaskTemplateRepository taskTemplateRepository();

    @SuppressWarnings("unused")
    StepTemplateRepository stepTemplateRepository();

    @SuppressWarnings("unused")
    AssetRepository assetRepository();

    @SuppressWarnings("unused")
    MediaPlayer mediaPlayer();

    void inject(TaskCreateFragment activity);

    void inject(TaskListActivity activity);

    void inject(StepListFragment stepListFragment);

    void inject(StepCreateFragment stepCreateFragment);
}
