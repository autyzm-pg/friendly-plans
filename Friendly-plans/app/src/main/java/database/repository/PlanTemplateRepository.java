package database.repository;

import database.entities.DaoSession;
import database.entities.PlanTemplate;
import database.entities.PlanTemplateDao;
import java.util.List;


public class PlanTemplateRepository {

    private DaoSession daoSession;

    public PlanTemplateRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public long create(String name) {
        PlanTemplate planTemplate = new PlanTemplate();
        planTemplate.setName(name);
        return daoSession.getPlanTemplateDao().insert(planTemplate);
    }

    public PlanTemplate get(Long id) {
        return daoSession.getPlanTemplateDao().load(id);
    }

    public List<PlanTemplate> get(String planName) {
        return daoSession.getPlanTemplateDao()
                .queryBuilder()
                .where(PlanTemplateDao.Properties.Name.eq(planName))
                .list();
    }

    public void delete(Long id) {
        daoSession.getPlanTemplateDao().deleteByKey(id);
    }
}
