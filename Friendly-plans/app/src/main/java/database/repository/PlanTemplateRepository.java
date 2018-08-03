package database.repository;


import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import database.entities.DaoSession;
import database.entities.PlanTaskTemplate;
import database.entities.PlanTaskTemplateDao;
import database.entities.PlanTemplate;
import database.entities.PlanTemplateDao;


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

    public void update(Long planId, String name) {
        PlanTemplate planTemplate = new PlanTemplate();
        planTemplate.setId(planId);
        planTemplate.setName(name);
        daoSession.getPlanTemplateDao().update(planTemplate);
    }

    public void setTasksWithThisPlan(Long planId, Long taskId) {
        PlanTaskTemplate planTaskTemplate = new PlanTaskTemplate();
        planTaskTemplate.setTaskTemplateId(taskId);
        planTaskTemplate.setPlanTemplateId(planId);

        PlanTaskTemplateDao targetDao = daoSession.getPlanTaskTemplateDao();
        targetDao.insert(planTaskTemplate);

        get(planId).resetTasksWithThisPlan();
    }

    public void deleteTaskFromThisPlan(Long planId, Long taskId){
        PlanTaskTemplateDao planTaskTemplateDao = daoSession.getPlanTaskTemplateDao();
        QueryBuilder<PlanTaskTemplate> queryBuilder = planTaskTemplateDao.queryBuilder();

        PlanTaskTemplate planTaskTemplate = queryBuilder.where(
                PlanTaskTemplateDao.Properties.PlanTemplateId.eq(planId),
                PlanTaskTemplateDao.Properties.TaskTemplateId.eq(taskId))
                .uniqueOrThrow();

        planTaskTemplateDao.delete(planTaskTemplate);
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

    public List<PlanTemplate> getAll() {
        return daoSession.getPlanTemplateDao().loadAll();
    }

    public List<PlanTemplate> getFilteredByName(String planName) {
        return daoSession.getPlanTemplateDao()
                .queryBuilder()
                .where(PlanTemplateDao.Properties.Name.like("%" + planName + "%"))
                .list();
    }

    public void delete(Long id) {
        daoSession.getPlanTemplateDao().deleteByKey(id);
    }

    public void deleteAll() {
        daoSession.getPlanTemplateDao().deleteAll();
    }
}
