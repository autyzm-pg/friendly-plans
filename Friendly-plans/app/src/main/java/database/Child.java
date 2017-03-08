package database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;


@Entity
public class Child {

    @Id(autoincrement = true)
    private Long id;

    private String name;

    private String surname;

    private String fontSize;

    private String pictureSize;

    private String displayMode;

    @ToMany(referencedJoinProperty = "childId")
    private List<ChildPlan> childPlans;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1911343815)
    private transient ChildDao myDao;

    @Generated(hash = 1027982090)
    public Child(Long id, String name, String surname, String font_size, String picture_size,
            String display_mode) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.font_size = font_size;
        this.picture_size = picture_size;
        this.display_mode = display_mode;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getFontSize() {
        return fontSize;
    }

    public void setFontSize(String fontSize) {
        this.fontSize = fontSize;
    }

    public String getPictureSize() {
        return pictureSize;
    }

    public void setPictureSize(String pictureSize) {
        this.pictureSize = pictureSize;
    }

    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    public void setChildPlans(List<ChildPlan> childPlans) {
        this.childPlans = childPlans;
    }

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1911343815)
    private transient ChildDao myDao;

    @Generated(hash = 966481955)
    public Child(long id, String name, String surname, String fontSize, String pictureSize,
            String displayMode) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.fontSize = fontSize;
        this.pictureSize = pictureSize;
        this.displayMode = displayMode;
    }

    @Generated(hash = 891984724)
    public Child() {
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

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 398907355)
    public synchronized void resetChildPlans() {
        childPlans = null;
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
    @Generated(hash = 2128569439)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getChildDao() : null;
    }

}
