package database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class PlanTemplate {

    @Id
    private long id;

    private String name;

    @ToMany(referencedJoinProperty = "childId")
    private List<ChildPlan> childPlans;

    @ToMany(referencedJoinProperty = "planTemplateId")
    private List<PlanTaskTemplate> planTaskTemplates;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 667849288)
    private transient PlanTemplateDao myDao;

    @Generated(hash = 1799698846)
    public PlanTemplate(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Generated(hash = 623023216)
    public PlanTemplate() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * To-many relationship, resolved on first access (and after reset). Changes to to-many
     * relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 339806248)
    public List<ChildPlan> getChildPlans() {
        if (childPlans == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ChildPlanDao targetDao = daoSession.getChildPlanDao();
            List<ChildPlan> childPlansNew = targetDao
                    ._queryPlanTemplate_ChildPlans(id);
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
     * To-many relationship, resolved on first access (and after reset). Changes to to-many
     * relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 710197037)
    public List<PlanTaskTemplate> getPlanTaskTemplates() {
        if (planTaskTemplates == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PlanTaskTemplateDao targetDao = daoSession.getPlanTaskTemplateDao();
            List<PlanTaskTemplate> planTaskTemplatesNew = targetDao
                    ._queryPlanTemplate_PlanTaskTemplates(id);
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
    @Generated(hash = 1753738323)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getPlanTemplateDao() : null;
    }

}
