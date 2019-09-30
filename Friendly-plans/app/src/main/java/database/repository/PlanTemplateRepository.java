package database.repository;

import database.entities.PlanTask;
import database.entities.PlanTaskDao;
import database.entities.PlanTaskTemplateDao.Properties;
import database.entities.StepTemplateDao;
import database.entities.TaskTemplate;
import database.entities.TaskTemplateDao;
import java.util.ArrayList;
import org.greenrobot.greendao.query.Query;
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

    public void deleteTaskFromThisPlan(PlanTaskTemplate planTaskTemplate) {

        daoSession.getPlanTaskTemplateDao().delete(planTaskTemplate);
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

    public List<TaskTemplate> getTaskWithThisPlanByTypeId(Long planId, Integer typeId) {
        QueryBuilder<TaskTemplate> queryBuilder = daoSession.getTaskTemplateDao().queryBuilder();
        queryBuilder.where(TaskTemplateDao.Properties.TypeId.eq(typeId))
                .join(PlanTaskTemplate.class, PlanTaskTemplateDao.Properties.TaskTemplateId)
                .where(PlanTaskTemplateDao.Properties.PlanTemplateId.eq(planId));
        Query<TaskTemplate> getTaskWithThisPlanByTypeIdQuery = queryBuilder.build();
        return getTaskWithThisPlanByTypeIdQuery.list();
    }

    public List<PlanTemplate> getPlansWithThisTask(Long taskId) {
        QueryBuilder<PlanTemplate> queryBuilder = daoSession.getPlanTemplateDao().queryBuilder();
        queryBuilder.join(PlanTaskTemplate.class, PlanTaskTemplateDao.Properties.PlanTemplateId)
                .where(PlanTaskTemplateDao.Properties.TaskTemplateId.eq(taskId));
        Query<PlanTemplate> getPlansWithThisTaskQuery = queryBuilder.build();
        return getPlansWithThisTaskQuery.list();
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

    public List<PlanTaskTemplate> getPlanTasksByTypeId(Long planId, Integer typeId) {
        List<PlanTaskTemplate> planTask = daoSession.getPlanTaskTemplateDao()
                .queryBuilder()
                .where(PlanTaskTemplateDao.Properties.PlanTemplateId.eq(planId))
                .orderAsc(PlanTaskTemplateDao.Properties.Order)
                .list();
        List<PlanTaskTemplate>  result = new ArrayList<>();
        for(PlanTaskTemplate task : planTask){
            if(task.getTaskTemplate().getTypeId() == typeId){
                result.add(task);
            }
        }
        return result;
    }

    public void updatePlanTask(PlanTaskTemplate planTask){
        daoSession.getPlanTaskTemplateDao().update(planTask);
    }
}
