package dao;

import dagger.Component;
import javax.inject.Singleton;
import pg.autyzm.friendly_plans.MainActivity;

@Singleton
@Component(modules = {DaoSessionModule.class})
public interface DaoSessionComponent {

    TaskTemplateRepository taskTemplateRepository();

    void inject(MainActivity activity);
}