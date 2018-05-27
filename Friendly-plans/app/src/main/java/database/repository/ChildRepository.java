package database.repository;

import database.entities.Child;
import database.entities.DaoSession;
import java.util.List;

public class ChildRepository {

    private DaoSession daoSession;

    public ChildRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public long create(String firstName, String lastName) {
        Child childTemplate = new Child();
        childTemplate.setName(firstName);
        childTemplate.setSurname(lastName);
        return daoSession.getChildDao().insert(childTemplate);
    }

    public Child get(Long id) {
        return daoSession.getChildDao().load(id);
    }

    public List<Child> getAll() {
        return daoSession.getChildDao().loadAll();
    }

    public void delete(Long id) {
        daoSession.getChildDao().deleteByKey(id);
    }
}
