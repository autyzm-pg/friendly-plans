package database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class ChildPlan {

    @Id(autoincrement = true)
    private Long id;

    private boolean is_active;

    @ToOne(joinProperty = "childId")
    private Child child;

    private long childId;

    @ToOne(joinProperty = "planTemplateId")
    private PlanTemplate planTemplate;

    private long planTemplateId;

    @ToMany(referencedJoinProperty = "childPlanId")
    private List<PlanTask> planTasks;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1654041837)
    private transient ChildPlanDao myDao;

    @Generated(hash = 395206363)
    public ChildPlan(Long id, boolean is_active, long childId, long planTemplateId) {
        this.id = id;
        this.is_active = is_active;
        this.childId = childId;
        this.planTemplateId = planTemplateId;
    }

    @Generated(hash = 283772284)
    public ChildPlan() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getIs_active() {
        return this.is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public long getChildId() {
        return this.childId;
    }

    public void setChildId(long childId) {
        this.childId = childId;
    }

    public long getPlanTemplateId() {
        return this.planTemplateId;
    }

    public void setPlanTemplateId(long planTemplateId) {
        this.planTemplateId = planTemplateId;
    }

    @Generated(hash = 1042451417)
    private transient Long child__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 587637870)
    public Child getChild() {
        long __key = this.childId;
        if (child__resolvedKey == null || !child__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChildDao targetDao = daoSession.getChildDao();
            Child childNew = targetDao.load(__key);
            synchronized (this) {
                child = childNew;
                child__resolvedKey = __key;
            }
        }
        return child;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1377631279)
    public void setChild(@NotNull Child child) {
        if (child == null) {
            throw new DaoException(
                    "To-one property 'childId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.child = child;
            childId = child.getId();
            child__resolvedKey = childId;
        }
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

    /**
     * To-many relationship, resolved on first access (and after reset). Changes to to-many
     * relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1216552361)
    public List<PlanTask> getPlanTasks() {
        if (planTasks == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanTaskDao targetDao = daoSession.getPlanTaskDao();
            List<PlanTask> planTasksNew = targetDao._queryChildPlan_PlanTasks(id);
            synchronized (this) {
                if (planTasks == null) {
                    planTasks = planTasksNew;
                }
            }
        }
        return planTasks;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1766030107)
    public synchronized void resetPlanTasks() {
        planTasks = null;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 360762169)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChildPlanDao() : null;
    }
}
