package database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by agoncharenko on 1/25/2017.
 */
@Entity
public class PlanBreak {
    @Id
    private long id;

    @ToOne(joinProperty = "planBreakTemplateId")
    private PlanBreakTemplate planBreakTemplate;

    private long planBreakTemplateId;


    @ToOne(joinProperty = "childPlanId")
    private ChildPlan childPlan;

    private long childPlanId;

    @ToMany(referencedJoinProperty = "planBreakId")
    private List<PlanBreakStep> planBreakSteps;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1488405976)
    private transient PlanBreakDao myDao;

    @Generated(hash = 1732864271)
    public PlanBreak(long id, long planBreakTemplateId, long childPlanId) {
        this.id = id;
        this.planBreakTemplateId = planBreakTemplateId;
        this.childPlanId = childPlanId;
    }

    @Generated(hash = 290581055)
    public PlanBreak() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlanBreakTemplateId() {
        return this.planBreakTemplateId;
    }

    public void setPlanBreakTemplateId(long planBreakTemplateId) {
        this.planBreakTemplateId = planBreakTemplateId;
    }

    public long getChildPlanId() {
        return this.childPlanId;
    }

    public void setChildPlanId(long childPlanId) {
        this.childPlanId = childPlanId;
    }

    @Generated(hash = 1106904350)
    private transient Long planBreakTemplate__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2083033736)
    public PlanBreakTemplate getPlanBreakTemplate() {
        long __key = this.planBreakTemplateId;
        if (planBreakTemplate__resolvedKey == null
                || !planBreakTemplate__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanBreakTemplateDao targetDao = daoSession.getPlanBreakTemplateDao();
            PlanBreakTemplate planBreakTemplateNew = targetDao.load(__key);
            synchronized (this) {
                planBreakTemplate = planBreakTemplateNew;
                planBreakTemplate__resolvedKey = __key;
            }
        }
        return planBreakTemplate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1262140307)
    public void setPlanBreakTemplate(@NotNull PlanBreakTemplate planBreakTemplate) {
        if (planBreakTemplate == null) {
            throw new DaoException(
                    "To-one property 'planBreakTemplateId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.planBreakTemplate = planBreakTemplate;
            planBreakTemplateId = planBreakTemplate.getId();
            planBreakTemplate__resolvedKey = planBreakTemplateId;
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
    @Generated(hash = 75997108)
    public List<PlanBreakStep> getPlanBreakSteps() {
        if (planBreakSteps == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanBreakStepDao targetDao = daoSession.getPlanBreakStepDao();
            List<PlanBreakStep> planBreakStepsNew = targetDao
                    ._queryPlanBreak_PlanBreakSteps(id);
            synchronized (this) {
                if (planBreakSteps == null) {
                    planBreakSteps = planBreakStepsNew;
                }
            }
        }
        return planBreakSteps;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 922214843)
    public synchronized void resetPlanBreakSteps() {
        planBreakSteps = null;
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
    @Generated(hash = 2133621187)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlanBreakDao() : null;
    }



}
