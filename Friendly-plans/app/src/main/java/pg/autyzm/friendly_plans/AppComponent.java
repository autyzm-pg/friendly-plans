package pg.autyzm.friendly_plans;

import android.media.MediaPlayer;
import dagger.Component;
import database.repository.AssetRepository;
import database.repository.ChildPlanRepository;
import database.repository.ChildRepository;
import database.repository.DaoSessionModule;
import database.repository.PlanTemplateRepository;
import database.repository.RepositoryModule;
import database.repository.StepTemplateRepository;
import database.repository.TaskTemplateRepository;
import javax.inject.Singleton;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.asset.AssetsHelperModule;
import pg.autyzm.friendly_plans.child_app.view.MainActivity;
import pg.autyzm.friendly_plans.file_picker.FilePickerModule;
import pg.autyzm.friendly_plans.file_picker.FilePickerProxy;
import pg.autyzm.friendly_plans.manager_app.view.activate_plan.ActivatePlanActivity;
import pg.autyzm.friendly_plans.manager_app.view.child_settings.ChildSettingsFragment;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifierModule;
import pg.autyzm.friendly_plans.manager_app.view.child_list.ChildListActivity;
import pg.autyzm.friendly_plans.manager_app.view.components.SoundComponent;
import pg.autyzm.friendly_plans.manager_app.view.child_settings.ChildSettingsActivity;
import pg.autyzm.friendly_plans.string_provider.StringProviderModule;
import pg.autyzm.friendly_plans.manager_app.view.plan_create.PlanCreateFragment;
import pg.autyzm.friendly_plans.manager_app.view.plan_create_add_tasks.AddTasksToPlanFragment;
import pg.autyzm.friendly_plans.manager_app.view.plan_create_task_list.PlanTaskListFragment;
import pg.autyzm.friendly_plans.manager_app.view.plan_list.PlanListActivity;
import pg.autyzm.friendly_plans.manager_app.view.step_create.StepCreateFragment;
import pg.autyzm.friendly_plans.manager_app.view.task_create.TaskCreateActivity;
import pg.autyzm.friendly_plans.manager_app.view.task_create.TaskCreateFragment;
import pg.autyzm.friendly_plans.manager_app.validation.TaskValidation;
import pg.autyzm.friendly_plans.manager_app.validation.ValidationModule;
import pg.autyzm.friendly_plans.manager_app.view.step_list.StepListFragment;
import pg.autyzm.friendly_plans.manager_app.view.task_list.TaskListActivity;

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
    ChildRepository childRepository();

    @SuppressWarnings("unused")
    ChildPlanRepository childPlanRepository();

    @SuppressWarnings("unused")
    AssetRepository assetRepository();

    @SuppressWarnings("unused")
    MediaPlayer mediaPlayer();

    @SuppressWarnings("unused")
    ToastUserNotifier toastUserNotifier();

    @SuppressWarnings("unused")
    AssetsHelper assetsHelper();


    void inject(ActivatePlanActivity activity);

    void inject(TaskCreateActivity taskCreateActivity);

    void inject(TaskCreateFragment activity);

    void inject(TaskListActivity activity);

    void inject(StepListFragment stepListFragment);

    void inject(PlanCreateFragment activity);

    void inject(PlanListActivity activity);

    void inject(ChildSettingsActivity activity);

    void inject(ChildListActivity childListActivity);

    void inject(PlanTaskListFragment planTaskListFragment);

    void inject(AddTasksToPlanFragment addTasksToPlanFragment);

    void inject(StepCreateFragment stepCreateFragment);

    void inject(ChildSettingsFragment childSettingsFragment);

    void inject(SoundComponent soundComponent);

    void inject(MainActivity activity);
}
