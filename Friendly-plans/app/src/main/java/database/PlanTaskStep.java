package database;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by agoncharenko on 1/25/2017.
 */
@Entity
public class PlanTaskStep {

    @Id
    private long id;

    private boolean isDone;

    @ToOne(joinProperty = "stepTemplateId")
    private StepTemplate stepTemplate;

    private long stepTemplateId;

    @ToOne(joinProperty = "planTaskId")
    private PlanTask planTask;

    private long planTaskId;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 449450875)
    private transient PlanTaskStepDao myDao;

    @Generated(hash = 161995468)
    public PlanTaskStep(long id, boolean isDone, long stepTemplateId,
            long planTaskId) {
        this.id = id;
        this.isDone = isDone;
        this.stepTemplateId = stepTemplateId;
        this.planTaskId = planTaskId;
    }

    @Generated(hash = 1229593414)
    public PlanTaskStep() {
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

    public long getStepTemplateId() {
        return this.stepTemplateId;
    }

    public void setStepTemplateId(long stepTemplateId) {
        this.stepTemplateId = stepTemplateId;
    }

    public long getPlanTaskId() {
        return this.planTaskId;
    }

    public void setPlanTaskId(long planTaskId) {
        this.planTaskId = planTaskId;
    }

    @Generated(hash = 112075782)
    private transient Long stepTemplate__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 293884714)
    public StepTemplate getStepTemplate() {
        long __key = this.stepTemplateId;
        if (stepTemplate__resolvedKey == null
                || !stepTemplate__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StepTemplateDao targetDao = daoSession.getStepTemplateDao();
            StepTemplate stepTemplateNew = targetDao.load(__key);
            synchronized (this) {
                stepTemplate = stepTemplateNew;
                stepTemplate__resolvedKey = __key;
            }
        }
        return stepTemplate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 681850098)
    public void setStepTemplate(@NotNull StepTemplate stepTemplate) {
        if (stepTemplate == null) {
            throw new DaoException(
                    "To-one property 'stepTemplateId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.stepTemplate = stepTemplate;
            stepTemplateId = stepTemplate.getId();
            stepTemplate__resolvedKey = stepTemplateId;
        }
    }

    @Generated(hash = 304355203)
    private transient Long planTask__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1599924244)
    public PlanTask getPlanTask() {
        long __key = this.planTaskId;
        if (planTask__resolvedKey == null || !planTask__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanTaskDao targetDao = daoSession.getPlanTaskDao();
            PlanTask planTaskNew = targetDao.load(__key);
            synchronized (this) {
                planTask = planTaskNew;
                planTask__resolvedKey = __key;
            }
        }
        return planTask;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1918367405)
    public void setPlanTask(@NotNull PlanTask planTask) {
        if (planTask == null) {
            throw new DaoException(
                    "To-one property 'planTaskId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.planTask = planTask;
            planTaskId = planTask.getId();
            planTask__resolvedKey = planTaskId;
        }
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
    @Generated(hash = 194617965)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlanTaskStepDao() : null;
    }

}
