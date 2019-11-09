package database.entities;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.ToOne;

@Entity
public class Child {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private String surname;

    private String fontSize;

    private String pictureSize;

    private String tasksDisplayMode;

    private String stepsDisplayMode;

    private Long timerSoundId;

    private boolean isActive;

    @ToMany(referencedJoinProperty = "childId")
    private List<ChildPlan> childPlans;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1911343815)
    private transient ChildDao myDao;

    @ToOne(joinProperty = "timerSoundId")
    private Asset sound;

    @Generated(hash = 1058647110)
    public Child(Long id, String name, String surname, String fontSize, String pictureSize,
            String tasksDisplayMode, String stepsDisplayMode, Long timerSoundId, boolean isActive) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.fontSize = fontSize;
        this.pictureSize = pictureSize;
        this.tasksDisplayMode = tasksDisplayMode;
        this.stepsDisplayMode = stepsDisplayMode;
        this.timerSoundId = timerSoundId;
        this.isActive = isActive;
    }

    @Generated(hash = 891984724)
    public Child() {
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

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getFontSize() {
        return this.fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getPictureSize() {
        return this.pictureSize;
    }

    public void setPictureSize(String pictureSize) {
        this.pictureSize = pictureSize;
    }

    public String getTasksDisplayMode() {
        return tasksDisplayMode;
    }

    public void setTasksDisplayMode(String tasksDisplayMode) {
        this.tasksDisplayMode = tasksDisplayMode;
    }

    public String getStepsDisplayMode() {
        return stepsDisplayMode;
    }

    public boolean isDisplayModeSlide(){
        return stepsDisplayMode.equals("Slide");
    }

    public void setStepsDisplayMode(String stepsDisplayMode) {
        this.stepsDisplayMode = stepsDisplayMode;
    }

    public Long getTimerSoundId() {
        return timerSoundId;
    }

    public void setTimerSoundId(Long timerSoundId) {
        this.timerSoundId = timerSoundId;
    }
    /**
     * To-many relationship, resolved on first access (and after reset). Changes to to-many
     * relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1396703637)
    public List<ChildPlan> getChildPlans() {
        if (childPlans == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChildPlanDao targetDao = daoSession.getChildPlanDao();
            List<ChildPlan> childPlansNew = targetDao._queryChild_ChildPlans(id);
            synchronized (this) {
                if (childPlans == null) {
                    childPlans = childPlansNew;
                }
            }
        }
        return childPlans;
    }

    @Generated(hash = 286221468)
    private transient Long sound__resolvedKey;

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 398907355)
    public synchronized void resetChildPlans() {
        childPlans = null;
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 375251730)
    public Asset getSound() {
        Long __key = this.timerSoundId;
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
    @Generated(hash = 924831495)
    public void setSound(Asset sound) {
        synchronized (this) {
            this.sound = sound;
            timerSoundId = sound == null ? null : sound.getId();
            sound__resolvedKey = timerSoundId;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2128569439)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChildDao() : null;
    }
}
