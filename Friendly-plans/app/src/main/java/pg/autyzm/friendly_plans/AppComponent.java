package pg.autyzm.friendly_plans;

import android.media.MediaPlayer;
import dagger.Component;
import database.repository.AssetRepository;
import database.repository.DaoSessionModule;
import database.repository.PlanTemplateRepository;
import database.repository.RepositoryModule;
import database.repository.StepTemplateRepository;
import database.repository.TaskTemplateRepository;
import javax.inject.Singleton;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.asset.AssetsHelperModule;
import pg.autyzm.friendly_plans.file_picker.FilePickerModule;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifierModule;
import pg.autyzm.friendly_plans.view.components.SoundComponent;
import pg.autyzm.friendly_plans.string_provider.StringProviderModule;
import pg.autyzm.friendly_plans.view.plan_create.PlanCreateFragment;
import pg.autyzm.friendly_plans.view.plan_create_add_tasks.AddTasksToPlanFragment;
import pg.autyzm.friendly_plans.view.plan_create_task_list.PlanTaskListFragment;
import pg.autyzm.friendly_plans.view.plan_list.PlanListActivity;
import pg.autyzm.friendly_plans.view.step_create.StepCreateFragment;
import pg.autyzm.friendly_plans.view.task_create.TaskCreateFragment;
import pg.autyzm.friendly_plans.validation.TaskValidation;
import pg.autyzm.friendly_plans.validation.ValidationModule;
import pg.autyzm.friendly_plans.view.step_list.StepListFragment;
import pg.autyzm.friendly_plans.view.task_list.TaskListActivity;

@Singleton
@Component(modules = {StringProviderModule.class,
        DaoSessionModule.class,
        ValidationModule.class,
        FilePickerModule.class,
        RepositoryModule.class,
        MediaPlayerModule.class,
        ToastUserNotifierModule.class,
        AssetsHelperModule.class})
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
    PlanTemplateRepository planTemplateRepository();

    @SuppressWarnings("unused")
    AssetRepository assetRepository();

    @SuppressWarnings("unused")
    MediaPlayer mediaPlayer();

    @SuppressWarnings("unused")
    ToastUserNotifier toastUserNotifier();

    @SuppressWarnings("unused")
    AssetsHelper assetsHelper();

    void inject(TaskCreateFragment activity);

    void inject(TaskListActivity activity);

    void inject(StepListFragment stepListFragment);

    void inject(PlanCreateFragment activity);

    void inject(PlanListActivity activity);

    void inject(PlanTaskListFragment planTaskListFragment);

    void inject(AddTasksToPlanFragment addTasksToPlanFragment);

    void inject(StepCreateFragment stepCreateFragment);

    void inject(SoundComponent soundComponent);
}
