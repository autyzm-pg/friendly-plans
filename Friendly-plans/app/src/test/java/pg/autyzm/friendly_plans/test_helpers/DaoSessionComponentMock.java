package pg.autyzm.friendly_plans.test_helpers;

import dagger.Component;
import database.repository.DaoSessionComponent;
import javax.inject.Singleton;

@Singleton
@Component(modules = DaoSessionModuleMock.class)
public interface DaoSessionComponentMock extends DaoSessionComponent {

}
