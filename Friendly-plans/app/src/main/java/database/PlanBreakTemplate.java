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
public class PlanBreakTemplate {

    @Id
    private long id;

    @ToOne(joinProperty = "planTemplateId")
    private PlanTemplate planTemplate;

    private long planTemplateId;

    @ToOne(joinProperty = "taskTemplateId")
    private TaskTemplate taskTemplate;

    private long taskTemplateId;

    @ToMany(referencedJoinProperty = "planBreakTemplateId")
    private List<PlanBreak> planBreaks;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 851346963)
    private transient PlanBreakTemplateDao myDao;

    @Generated(hash = 773812032)
    public PlanBreakTemplate(long id, long planTemplateId, long taskTemplateId) {
        this.id = id;
        this.planTemplateId = planTemplateId;
        this.taskTemplateId = taskTemplateId;
    }

    @Generated(hash = 383103375)
    public PlanBreakTemplate() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPlanTemplateId() {
        return this.planTemplateId;
    }

    public void setPlanTemplateId(long planTemplateId) {
        this.planTemplateId = planTemplateId;
    }

    public long getTaskTemplateId() {
        return this.taskTemplateId;
    }

    public void setTaskTemplateId(long taskTemplateId) {
        this.taskTemplateId = taskTemplateId;
    }

    @Generated(hash = 917413635)
    private transient Long planTemplate__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1252992241)
    public PlanTemplate getPlanTemplate() {
        long __key = this.planTemplateId;
        if (planTemplate__resolvedKey == null
                || !planTemplate__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanTemplateDao targetDao = daoSession.getPlanTemplateDao();
            PlanTemplate planTemplateNew = targetDao.load(__key);
            synchronized (this) {
                planTemplate = planTemplateNew;
                planTemplate__resolvedKey = __key;
            }
        }
        return planTemplate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1841962759)
    public void setPlanTemplate(@NotNull PlanTemplate planTemplate) {
        if (planTemplate == null) {
            throw new DaoException(
                    "To-one property 'planTemplateId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.planTemplate = planTemplate;
            planTemplateId = planTemplate.getId();
            planTemplate__resolvedKey = planTemplateId;
        }
    }

    @Generated(hash = 309141312)
    private transient Long taskTemplate__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1498914117)
    public TaskTemplate getTaskTemplate() {
        long __key = this.taskTemplateId;
        if (taskTemplate__resolvedKey == null
                || !taskTemplate__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskTemplateDao targetDao = daoSession.getTaskTemplateDao();
            TaskTemplate taskTemplateNew = targetDao.load(__key);
            synchronized (this) {
                taskTemplate = taskTemplateNew;
                taskTemplate__resolvedKey = __key;
            }
        }
        return taskTemplate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 707519624)
    public void setTaskTemplate(@NotNull TaskTemplate taskTemplate) {
        if (taskTemplate == null) {
            throw new DaoException(
                    "To-one property 'taskTemplateId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.taskTemplate = taskTemplate;
            taskTemplateId = taskTemplate.getId();
            taskTemplate__resolvedKey = taskTemplateId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1833070508)
    public List<PlanBreak> getPlanBreaks() {
        if (planBreaks == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanBreakDao targetDao = daoSession.getPlanBreakDao();
            List<PlanBreak> planBreaksNew = targetDao
                    ._queryPlanBreakTemplate_PlanBreaks(id);
            synchronized (this) {
                if (planBreaks == null) {
                    planBreaks = planBreaksNew;
                }
            }
        }
        return planBreaks;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1426024251)
    public synchronized void resetPlanBreaks() {
        planBreaks = null;
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
    @Generated(hash = 1131300768)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlanBreakTemplateDao() : null;
    }
}
