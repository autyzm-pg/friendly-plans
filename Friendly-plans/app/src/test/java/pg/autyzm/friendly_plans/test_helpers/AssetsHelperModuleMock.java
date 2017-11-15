package pg.autyzm.friendly_plans.test_helpers;

import org.mockito.Mockito;
import pg.autyzm.friendly_plans.asset.AssetsHelper;
import pg.autyzm.friendly_plans.asset.AssetsHelperModule;


public class AssetsHelperModuleMock extends AssetsHelperModule {

    private AssetsHelper assetsHelper;

    public AssetsHelperModuleMock() {
        super(null);
        assetsHelper = Mockito.mock(AssetsHelper.class);
    }

    @Override
    public AssetsHelper getAssetsHelper() {
        return  this.assetsHelper;
    }

}
