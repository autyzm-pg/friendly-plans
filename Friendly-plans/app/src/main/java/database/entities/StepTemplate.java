package database.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
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

    private int order;

    private Long pictureId;

    private Long soundId;

    private Integer durationTime;

    @ToOne(joinProperty = "taskTemplateId")
    private TaskTemplate taskTemplate;

    private long taskTemplateId;

    @ToMany(referencedJoinProperty = "stepTemplateId")
    private List<PlanTaskStep> planTaskSteps;

    @ToOne(joinProperty = "pictureId")
    private Asset picture;

    @ToOne(joinProperty = "soundId")
    private Asset sound;

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

    @Generated(hash = 672659171)
    public StepTemplate(Long id, String name, int order, Long pictureId, Long soundId, Integer durationTime,
            long taskTemplateId) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.pictureId = pictureId;
        this.soundId = soundId;
        this.durationTime = durationTime;
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

    public Long getPictureId() {
        return pictureId;
    }

    public void setPictureId(Long pictureId) {
        this.pictureId = pictureId;
    }

    public Long getSoundId() {
        return soundId;
    }

    public void setSoundId(Long soundId) {
        this.soundId = soundId;
    }

    public Integer getDurationTime() {
        return this.durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
    }

    @Generated(hash = 309141312)
    private transient Long taskTemplate__resolvedKey;

    @Generated(hash = 1986840853)
    private transient Long picture__resolvedKey;

    @Generated(hash = 286221468)
    private transient Long sound__resolvedKey;

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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1909866049)
    public Asset getPicture() {
        Long __key = this.pictureId;
        if (picture__resolvedKey == null || !picture__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AssetDao targetDao = daoSession.getAssetDao();
            Asset pictureNew = targetDao.load(__key);
            synchronized (this) {
                picture = pictureNew;
                picture__resolvedKey = __key;
            }
        }
        return picture;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2132969786)
    public void setPicture(Asset picture) {
        synchronized (this) {
            this.picture = picture;
            pictureId = picture == null ? null : picture.getId();
            picture__resolvedKey = pictureId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2011576312)
    public Asset getSound() {
        Long __key = this.soundId;
        if (sound__resolvedKey == null || !sound__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            AssetDao targetDao = daoSession.getAssetDao();
            Asset soundNew = targetDao.load(__key);
            synchronized (this) {
                sound = soundNew;
                sound__resolvedKey = __key;
            }
        }
        return sound;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 433043017)
    public void setSound(Asset sound) {
        synchronized (this) {
            this.sound = sound;
            soundId = sound == null ? null : sound.getId();
            sound__resolvedKey = soundId;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1579205517)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getStepTemplateDao() : null;
    }

}
