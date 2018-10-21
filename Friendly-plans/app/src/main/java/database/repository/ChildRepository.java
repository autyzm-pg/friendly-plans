package database.repository;

import database.entities.Child;
import database.entities.ChildDao.Properties;
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
        childTemplate.setIsActive(false);
        return daoSession.getChildDao().insert(childTemplate);
    }

    public List<Child> getBySurname(String surname) {
        return daoSession.getChildDao()
                .queryBuilder()
                .where(Properties.Surname.eq(surname))
                .list();
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

    public void deleteAll() {
        daoSession.getChildDao().deleteAll();
    }
}
