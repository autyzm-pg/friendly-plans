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
public class PlanBreak {
    @Id
    private long id;

    @ToOne(joinProperty = "planBreakTemplateId")
    private PlanBreakTemplate planBreakTemplate;

    @ToOne(joinProperty = "childPlanId")
    private ChildPlan childPlan;

    @ToMany(referencedJoinProperty = "planBreakId")
    private List<PlanBreakStep> planBreakSteps;



}
