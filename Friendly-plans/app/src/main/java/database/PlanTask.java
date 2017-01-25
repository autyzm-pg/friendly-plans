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
public class PlanTask {

    @Id
    private long id;

    private boolean isDone;

    @ToOne(joinProperty = "planTaskTemplateId")
    private PlanTaskTemplate planTaskTemplate;

    @ToOne(joinProperty = "childPlanId")
    private ChildPlan childPlan;

    @ToMany(referencedJoinProperty = "planTaskId")
    private List<PlanTaskStep> planTaskSteps;

}
