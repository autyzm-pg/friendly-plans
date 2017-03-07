package pg.autyzm.friendly_plans;

import android.content.Context;
import database.DaoMaster;
import database.DaoSession;
import org.greenrobot.greendao.database.Database;
import org.junit.rules.ExternalResource;

public class DaoSessionResource extends ExternalResource {

    public static final String FRIENDLY_PLANS_DB_NAME = "friendly-plans-db";

    public DaoSession getSession(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(
                context,
                FRIENDLY_PLANS_DB_NAME);
        Database db = helper.getWritableDb();
        return new DaoMaster(db).newSession();
    }
}
