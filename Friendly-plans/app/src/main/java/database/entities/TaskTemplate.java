package database.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToOne;

@Entity
public class TaskTemplate {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private Integer durationTime;

    private Long pictureId;

    private Long soundId;

    private Integer typeId;

    @ToMany(referencedJoinProperty = "taskTemplateId")
    private List<StepTemplate> stepTemplates;

    @ToMany(referencedJoinProperty = "taskTemplateId")
    private List<PlanTaskTemplate> planTaskTemplates;

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
    @Generated(hash = 404230972)
    private transient TaskTemplateDao myDao;

    @Generated(hash = 1986840853)
    private transient Long picture__resolvedKey;

    @Generated(hash = 286221468)
    private transient Long sound__resolvedKey;

    @Generated(hash = 1896825098)
    public TaskTemplate(Long id, String name, Integer durationTime, Long pictureId, Long soundId,
            Integer typeId) {
        this.id = id;
        this.name = name;
        this.durationTime = durationTime;
        this.pictureId = pictureId;
        this.soundId = soundId;
        this.typeId = typeId;
    }

    @Generated(hash = 2000532247)
    public TaskTemplate() {
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

    public Integer getDurationTime() {
        return this.durationTime;
    }

    public void setDurationTime(Integer durationTime) {
        this.durationTime = durationTime;
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

    public Integer getTypeId() {
        return this.typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * To-many relationship, resolved on first access (and after reset). Changes to to-many
     * relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 856664114)
    public List<StepTemplate> getStepTemplates() {
        if (stepTemplates == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StepTemplateDao targetDao = daoSession.getStepTemplateDao();
            List<StepTemplate> stepTemplatesNew = targetDao
                    ._queryTaskTemplate_StepTemplates(id);
            synchronized (this) {
                if (stepTemplates == null) {
                    stepTemplates = stepTemplatesNew;
                }
            }
        }
        return stepTemplates;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 664303899)
    public synchronized void resetStepTemplates() {
        stepTemplates = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset). Changes to to-many
     * relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 613273513)
    public List<PlanTaskTemplate> getPlanTaskTemplates() {
        if (planTaskTemplates == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanTaskTemplateDao targetDao = daoSession.getPlanTaskTemplateDao();
            List<PlanTaskTemplate> planTaskTemplatesNew = targetDao
                    ._queryTaskTemplate_PlanTaskTemplates(id);
            synchronized (this) {
                if (planTaskTemplates == null) {
                    planTaskTemplates = planTaskTemplatesNew;
                }
            }
        }
        return planTaskTemplates;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 204970657)
    public synchronized void resetPlanTaskTemplates() {
        planTaskTemplates = null;
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

    /**
     * To-one relationship, resolved on first access.
     */
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2132969786)
    public void setPicture(Asset picture) {
        synchronized (this) {
            this.picture = picture;
            pictureId = picture == null ? null : picture.getId();
            picture__resolvedKey = pictureId;
        }
    }

    /**
     * To-one relationship, resolved on first access.
     */
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

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 433043017)
    public void setSound(Asset sound) {
        synchronized (this) {
            this.sound = sound;
            soundId = sound == null ? null : sound.getId();
            sound__resolvedKey = soundId;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1588370016)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskTemplateDao() : null;
    }
}
