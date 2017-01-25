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
public class PlanBreakTemplate {
    @Id
    private long id;

    @ToOne(joinProperty = "planTemplateId")
    private PlanTemplate planTemplate;

    @ToOne(joinProperty = "taskTemplateId")
    private TaskTemplate taskTemplate;

    @ToMany(referencedJoinProperty = "planBreakTemplateId")
    private List<PlanBreak> planBreaks;
}
