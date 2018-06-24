package database.repository;

import android.database.Cursor;

import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import database.entities.Child;
import database.entities.ChildDao.Properties;
import database.entities.DaoSession;
import database.entities.DaoMaster;

import java.lang.reflect.Array;
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

    public List<Child> getBySurname(String surname) {
        return daoSession.getChildDao()
                .queryBuilder()
                .where(Properties.Surname.eq(surname))
                .list();
    }

    public List<Child> getFilteredByFullName(String fullName) {
        String[] splited = fullName.split("\\s+");
        for (int i=0; i<splited.length; i++) {
            splited[i] = "%" + splited[i] + "%";
        }
        if(splited.length == 1 ) {
            String[] whereArguments = {splited[0], splited[0]};
            return daoSession.getChildDao().queryRaw("WHERE name LIKE ? OR surname LIKE ?",
                    whereArguments);
        }
        else {
            String[] whereArguments = {splited[0], splited[1], splited[1], splited[0]};
            return daoSession.getChildDao().queryRaw(
                    "WHERE (name LIKE ? AND surname LIKE ?) OR (name LIKE ? AND surname LIKE ?)",
                    whereArguments);
        }

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
