package dao;

import dagger.Component;
import javax.inject.Singleton;
import pg.autyzm.friendly_plans.TaskContainerFragment;

@Singleton
@Component(modules = {DaoSessionModule.class, RepositoryModule.class})
public interface DaoSessionComponent {

    @SuppressWarnings("unused")
    TaskTemplateRepository taskTemplateRepository();

    void inject(TaskContainerFragment activity);
}