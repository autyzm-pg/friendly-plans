package database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by agoncharenko on 1/25/2017.
 */
@Entity
public class PlanTaskTemplate {

    @Id
    private long id;

    private int order;

    @ToOne(joinProperty = "planTemplateId")
    private PlanTemplate planTemplate;

    @ToOne(joinProperty = "taskTemplateId")
    private TaskTemplate taskTemplate;

}
