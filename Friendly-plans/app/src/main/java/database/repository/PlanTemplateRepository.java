package database.repository;

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

    public void setTaskWithPlan(Long planId, Long taskId) {
        List<PlanTaskTemplate> planTaskList = get(planId).getPlanTaskTemplates();
        int order = 0;
        if(planTaskList != null){
            for(PlanTaskTemplate planTaskTemplate : planTaskList){
                if(planTaskTemplate.getOrder() > order) {
                    order = planTaskTemplate.getOrder();
                }
            }
        }
        setTaskWithPlan(planId, taskId, order);
    }

    public void setTaskWithPlan(Long planId, Long taskId, int order) {
        PlanTaskTemplate planTaskTemplate = new PlanTaskTemplate();
        planTaskTemplate.setTaskTemplateId(taskId);
        planTaskTemplate.setPlanTemplateId(planId);
        planTaskTemplate.setOrder(order);

        PlanTaskTemplateDao targetDao = daoSession.getPlanTaskTemplateDao();
        targetDao.insert(planTaskTemplate);

        get(planId).resetPlanTaskTemplates();
    }

    public void removeTaskFromPlan(Long planId, Long planTaskId) {
        PlanTemplate planTemplate = daoSession.getPlanTemplateDao().load(planId);
        List<PlanTaskTemplate> planTaskTemplatesList = planTemplate.getPlanTaskTemplates();
        PlanTaskTemplateDao targetDao = daoSession.getPlanTaskTemplateDao();
        for(PlanTaskTemplate planTaskTemplate : planTaskTemplatesList){
            if(planTaskTemplate.getId() == planTaskId){
                targetDao.delete(planTaskTemplate);
            }
        }
        planTemplate.resetPlanTaskTemplates();
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
