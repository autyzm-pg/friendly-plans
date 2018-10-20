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

    public void setIsActive(Child child, boolean isActive) {
        child.setIsActive(isActive);
        daoSession.getChildDao().update(child);
    }

    public void setAllInactive (){
        List<Child> children = daoSession.getChildDao().loadAll();
        for (Child child : children){
            child.setIsActive(false);
        }
        daoSession.getChildDao().updateInTx(children);
    };

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

    public List<Child> getByIsActive() {
        return daoSession.getChildDao()
                .queryBuilder()
                .where(Properties.IsActive.eq(true))
                .list();
    }

    public void deleteAll() {
        daoSession.getChildDao().deleteAll();
    }
}
