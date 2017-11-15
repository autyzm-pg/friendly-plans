package database.repository;

import dagger.Component;
import javax.inject.Singleton;

@Singleton
@Component(modules = DaoSessionModule.class)
public interface DbDaoSessionComponent extends DaoSessionComponent {

}
