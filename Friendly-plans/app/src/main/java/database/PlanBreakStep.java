package database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

/**
 * Created by agoncharenko on 1/25/2017.
 */
@Entity
public class PlanBreakStep {
    @Id
    private long id;

    private boolean isDone;

    @ToOne(joinProperty = "stepTemplateId")
    private StepTemplate stepTemplate;

    private long stepTemplateId;

    @ToOne(joinProperty = "planBreakId")
    private PlanBreak planBreak;

    private long planBreakId;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1688776373)
    private transient PlanBreakStepDao myDao;

    @Generated(hash = 207782756)
    public PlanBreakStep(long id, boolean isDone, long stepTemplateId,
            long planBreakId) {
        this.id = id;
        this.isDone = isDone;
        this.stepTemplateId = stepTemplateId;
        this.planBreakId = planBreakId;
    }

    @Generated(hash = 935908473)
    public PlanBreakStep() {
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

    public long getPlanBreakId() {
        return this.planBreakId;
    }

    public void setPlanBreakId(long planBreakId) {
        this.planBreakId = planBreakId;
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

    @Generated(hash = 2029887434)
    private transient Long planBreak__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 933483323)
    public PlanBreak getPlanBreak() {
        long __key = this.planBreakId;
        if (planBreak__resolvedKey == null
                || !planBreak__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanBreakDao targetDao = daoSession.getPlanBreakDao();
            PlanBreak planBreakNew = targetDao.load(__key);
            synchronized (this) {
                planBreak = planBreakNew;
                planBreak__resolvedKey = __key;
            }
        }
        return planBreak;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1136240388)
    public void setPlanBreak(@NotNull PlanBreak planBreak) {
        if (planBreak == null) {
            throw new DaoException(
                    "To-one property 'planBreakId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.planBreak = planBreak;
            planBreakId = planBreak.getId();
            planBreak__resolvedKey = planBreakId;
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
    @Generated(hash = 1343324907)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlanBreakStepDao() : null;
    }


}
