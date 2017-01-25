package database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by agoncharenko on 1/25/2017.
 */
@Entity
public class TaskTemplate {

    @Id
    private long id;

    private String name;

    private String picture;

    private String sound;

    private String time;   // TODO: investigate correct format in GreenDao

    @ToMany(referencedJoinProperty = "taskTemplateId")
    private List<StepTemplate> stepTemplates;

    @ToMany(referencedJoinProperty = "taskTemplateId")
    private List<PlanBreakTemplate> planBreakTemplates;

    @ToMany(referencedJoinProperty = "taskTemplateId")
    private List<PlanTaskTemplate> planTaskTemplates;
}
