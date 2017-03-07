package database;

import java.util.List;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.DaoException;

/**
 * Created by agoncharenko on 1/25/2017.
 */

@Entity
public class PlanTask {

    @Id
    private long id;

    private boolean isDone;

    @ToOne(joinProperty = "planTaskTemplateId")
    private PlanTaskTemplate planTaskTemplate;

    private long planTaskTemplateId;

    @ToOne(joinProperty = "childPlanId")
    private ChildPlan childPlan;

    private long childPlanId;

    @ToMany(referencedJoinProperty = "planTaskId")
    private List<PlanTaskStep> planTaskSteps;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 749888002)
    private transient PlanTaskDao myDao;

    @Generated(hash = 1735715283)
    public PlanTask(long id, boolean isDone, long planTaskTemplateId,
            long childPlanId) {
        this.id = id;
        this.isDone = isDone;
        this.planTaskTemplateId = planTaskTemplateId;
        this.childPlanId = childPlanId;
    }

    @Generated(hash = 815233186)
    public PlanTask() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean getIsDone() {
        return this.isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public long getPlanTaskTemplateId() {
        return this.planTaskTemplateId;
    }

    public void setPlanTaskTemplateId(long planTaskTemplateId) {
        this.planTaskTemplateId = planTaskTemplateId;
    }

    public long getChildPlanId() {
        return this.childPlanId;
    }

    public void setChildPlanId(long childPlanId) {
        this.childPlanId = childPlanId;
    }

    @Generated(hash = 2013491899)
    private transient Long planTaskTemplate__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1521731984)
    public PlanTaskTemplate getPlanTaskTemplate() {
        long __key = this.planTaskTemplateId;
        if (planTaskTemplate__resolvedKey == null
                || !planTaskTemplate__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanTaskTemplateDao targetDao = daoSession.getPlanTaskTemplateDao();
            PlanTaskTemplate planTaskTemplateNew = targetDao.load(__key);
            synchronized (this) {
                planTaskTemplate = planTaskTemplateNew;
                planTaskTemplate__resolvedKey = __key;
            }
        }
        return planTaskTemplate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1383196550)
    public void setPlanTaskTemplate(@NotNull PlanTaskTemplate planTaskTemplate) {
        if (planTaskTemplate == null) {
            throw new DaoException(
                    "To-one property 'planTaskTemplateId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.planTaskTemplate = planTaskTemplate;
            planTaskTemplateId = planTaskTemplate.getId();
            planTaskTemplate__resolvedKey = planTaskTemplateId;
        }
    }

    @Generated(hash = 674687381)
    private transient Long childPlan__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 464786722)
    public ChildPlan getChildPlan() {
        long __key = this.childPlanId;
        if (childPlan__resolvedKey == null
                || !childPlan__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChildPlanDao targetDao = daoSession.getChildPlanDao();
            ChildPlan childPlanNew = targetDao.load(__key);
            synchronized (this) {
                childPlan = childPlanNew;
                childPlan__resolvedKey = __key;
            }
        }
        return childPlan;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 105128079)
    public void setChildPlan(@NotNull ChildPlan childPlan) {
        if (childPlan == null) {
            throw new DaoException(
                    "To-one property 'childPlanId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.childPlan = childPlan;
            childPlanId = childPlan.getId();
            childPlan__resolvedKey = childPlanId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1347595177)
    public List<PlanTaskStep> getPlanTaskSteps() {
        if (planTaskSteps == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanTaskStepDao targetDao = daoSession.getPlanTaskStepDao();
            List<PlanTaskStep> planTaskStepsNew = targetDao
                    ._queryPlanTask_PlanTaskSteps(id);
            synchronized (this) {
                if (planTaskSteps == null) {
                    planTaskSteps = planTaskStepsNew;
                }
            }
        }
        return planTaskSteps;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1658737863)
    public synchronized void resetPlanTaskSteps() {
        planTaskSteps = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 15905679)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlanTaskDao() : null;
    }

}
