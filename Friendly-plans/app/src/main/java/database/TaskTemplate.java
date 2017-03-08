package database;

import java.util.List;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class TaskTemplate {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private String picture;

    private String sound;

    private int durationTime;

    @ToMany(referencedJoinProperty = "taskTemplateId")
    private List<StepTemplate> stepTemplates;

    @ToMany(referencedJoinProperty = "taskTemplateId")
    private List<PlanTaskTemplate> planTaskTemplates;

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

    @Generated(hash = 1930893288)
    public TaskTemplate(Long id, String name, String picture, String sound, int durationTime) {
        this.id = id;
        this.name = name;
        this.picture = picture;
        this.sound = sound;
        this.durationTime = durationTime;
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

    public int getDurationTime() {
        return this.durationTime;
    }

    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1588370016)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTaskTemplateDao() : null;
    }
}
