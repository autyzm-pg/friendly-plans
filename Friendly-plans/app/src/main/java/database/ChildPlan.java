package database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.List;

/**
 * Created by agoncharenko on 1/25/2017.
 */
@Entity
public class ChildPlan {


    @Id
    private long id;

    private boolean is_active;

    @ToOne(joinProperty = "childId")
    private Child child;

    @ToOne(joinProperty = "planTemplateId")
    private PlanTemplate planTemplate;

    @ToMany(referencedJoinProperty = "childPlanId")
    private List<PlanTask> planTasks;

    @ToMany(referencedJoinProperty = "childPlanId")
    private List<PlanBreak> planBreaks;
}
