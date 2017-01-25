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
public class StepTemplate {

    @Id
    private long id;

    private String name;

    private String picture;

    private String sound;

    private int order;

    @ToOne(joinProperty = "taskTemplateId")
    private TaskTemplate taskTemplate;

    @ToMany(referencedJoinProperty = "stepTemplateId")
    private List<PlanBreakStep> planBreakSteps;

    @ToMany(referencedJoinProperty = "stepTemplateId")
    private List<PlanTaskStep> planTaskSteps;

}
