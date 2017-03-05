package database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class PlanTaskTemplate {

    @Id
    private long id;

    private int order;

    private boolean isBreak;

    @ToOne(joinProperty = "planTemplateId")
    private PlanTemplate planTemplate;

    private long planTemplateId;

    @ToOne(joinProperty = "taskTemplateId")
    private TaskTemplate taskTemplate;

    private long taskTemplateId;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1859491700)
    private transient PlanTaskTemplateDao myDao;

    @Generated(hash = 1317694570)
    public PlanTaskTemplate(long id, int order, boolean isBreak, long planTemplateId,
            long taskTemplateId) {
        this.id = id;
        this.order = order;
        this.isBreak = isBreak;
        this.planTemplateId = planTemplateId;
        this.taskTemplateId = taskTemplateId;
    }

    @Generated(hash = 2117299161)
    public PlanTaskTemplate() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
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

    /**
     * To-one relationship, resolved on first access.
     */
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
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

    /**
     * To-one relationship, resolved on first access.
     */
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
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
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}. Entity must
     * attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}. Entity must
     * attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}. Entity must
     * attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    public boolean getIsBreak() {
        return this.isBreak;
    }

    public void setIsBreak(boolean isBreak) {
        this.isBreak = isBreak;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 11680655)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlanTaskTemplateDao() : null;
    }

}
