package database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by agoncharenko on 1/25/2017.
 */
@Entity
public class PlanTaskStep {

    @Id
    private long id;

    private boolean isDone;

    @ToOne(joinProperty = "stepTemplateId")
    private StepTemplate stepTemplate;

    @ToOne(joinProperty = "planTaskId")
    private PlanTask planTask;

}
