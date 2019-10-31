package database.repository;

import database.entities.Child;
import database.entities.ChildPlan;
import database.entities.ChildPlanDao;
import database.entities.DaoSession;
import java.util.List;

public class ChildPlanRepository {

    private DaoSession daoSession;

    public ChildPlanRepository(DaoSession daoSession) {
        this.daoSession = daoSession;
    }

    public long create(long childId, long planId) {
        ChildPlan childPlanTemplate = new ChildPlan();
        childPlanTemplate.setChildId(childId);
        childPlanTemplate.setPlanTemplateId(planId);
        childPlanTemplate.setIsActive(false);
        return daoSession.getChildPlanDao().insert(childPlanTemplate);
    }

    public void update (ChildPlan plan){
        daoSession.getChildPlanDao().update(plan);
    }

    public void setAllInactive (List<ChildPlan> childPlans ){
        for (ChildPlan childPlan : childPlans){
            childPlan.setIsActive(false);
        }
        daoSession.getChildPlanDao().updateInTx(childPlans);
    }

    public ChildPlan getActivePlan(){
        List<ChildPlan> planList = daoSession.getChildPlanDao()
                .queryBuilder()
                .where(ChildPlanDao.Properties.IsActive.eq(true))
                .list();
        return planList.size() > 0 ? planList.get(0) : null;
    }

    public void deleteAll() {daoSession.getChildPlanDao().deleteAll();}
    public void delete(Long id){
        daoSession.getChildPlanDao().deleteByKey(id);
    }
}