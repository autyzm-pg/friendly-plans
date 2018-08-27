package pg.autyzm.friendly_plans.test_helpers;

import android.content.Context;

import org.greenrobot.greendao.database.Database;
import org.junit.rules.ExternalResource;

import database.entities.DaoMaster;
import database.entities.DaoSession;

public class DaoSessionResource extends ExternalResource {

    private static final String FRIENDLY_PLANS_DB_NAME = "friendly-plans-db";

    public DaoSession getSession(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(
                context,
                FRIENDLY_PLANS_DB_NAME);
        Database db = helper.getWritableDb();
        return new DaoMaster(db).newSession();
    }
}
