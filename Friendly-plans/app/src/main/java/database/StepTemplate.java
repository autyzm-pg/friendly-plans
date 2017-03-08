package database;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class StepTemplate {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private String picture;

    private String sound;

    private int order;

    @ToOne(joinProperty = "taskTemplateId")
    private TaskTemplate taskTemplate;

    private long taskTemplateId;

    @ToMany(referencedJoinProperty = "stepTemplateId")
    private List<PlanTaskStep> planTaskSteps;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1320587426)
    private transient StepTemplateDao myDao;

    @Generated(hash = 1850431834)
    public StepTemplate(Long id, String name, String picture, String sound, int order, long taskTemplateId) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.sound = sound;
        this.order = order;
        this.taskTemplateId = taskTemplateId;
    }

    @Generated(hash = 33441766)
    public StepTemplate() {
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return this.picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSound() {
        return this.sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public long getTaskTemplateId() {
        return this.taskTemplateId;
    }

    public void setTaskTemplateId(long taskTemplateId) {
        this.taskTemplateId = taskTemplateId;
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
     * To-many relationship, resolved on first access (and after reset). Changes to to-many
     * relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2140032170)
    public List<PlanTaskStep> getPlanTaskSteps() {
        if (planTaskSteps == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanTaskStepDao targetDao = daoSession.getPlanTaskStepDao();
            List<PlanTaskStep> planTaskStepsNew = targetDao
                    ._queryStepTemplate_PlanTaskSteps(id);
            synchronized (this) {
                if (planTaskSteps == null) {
                    planTaskSteps = planTaskStepsNew;
                }
            }
        }
        return planTaskSteps;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1658737863)
    public synchronized void resetPlanTaskSteps() {
        planTaskSteps = null;
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
    @Generated(hash = 1579205517)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStepTemplateDao() : null;
    }

}
