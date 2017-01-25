package database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by agoncharenko on 1/25/2017.
 */
@Entity
public class PlanTemplate {

    @Id
    private long id;

    private String name;

    @ToMany(referencedJoinProperty = "childId")
    private List<ChildPlan> childPlans;

    @ToMany(referencedJoinProperty = "planTemplateId")
    private List<PlanTaskTemplate> planTaskTemplates;

    @ToMany(referencedJoinProperty = "planTemplateId")
    private List<PlanBreakTemplate> planBreakTemplates;

}
