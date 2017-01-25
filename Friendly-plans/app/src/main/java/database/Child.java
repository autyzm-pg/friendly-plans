package database;

import android.support.annotation.IntDef;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by agoncharenko on 1/25/2017.
 */

@Entity
public class Child {

    @Id
    private long id;

    private String name;

    private String surname;

    private String font_size;

    private String picture_size;

    private String display_mode;

    @ToMany(referencedJoinProperty = "childId")
    private List<ChildPlan> childPlans;

}
