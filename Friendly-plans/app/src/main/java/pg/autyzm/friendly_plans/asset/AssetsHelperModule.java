package pg.autyzm.friendly_plans.asset;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class AssetsHelperModule {

    private AssetsHelper assetsHelper;

    public AssetsHelperModule(Context context) {
        this.assetsHelper = new AssetsHelper(context);
    }

    @Provides
    @Singleton
    protected AssetsHelper getAssetsHelper() {
        return this.assetsHelper;
    }

}
