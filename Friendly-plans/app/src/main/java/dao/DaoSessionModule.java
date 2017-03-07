package dao;

import android.content.Context;
import org.greenrobot.greendao.database.Database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import database.DaoMaster;
import database.DaoSession;

@Module
public class DaoSessionModule {

    public static final String FRIENDLY_PLANS_DB_NAME = "friendly-plans-db";

    private DaoSession daoSession;

    public DaoSessionModule(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(
                context,
                FRIENDLY_PLANS_DB_NAME);
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    @Provides
    @Singleton
    public DaoSession getDaoSession() {
        return daoSession;
    }
}
