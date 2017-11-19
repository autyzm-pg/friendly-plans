package pg.autyzm.friendly_plans.test_helpers;

import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;


public final class AppComponentInjector  {

    private AppComponentInjector() {}

    public static void injectIntoApp(AppComponent appComponent) {
        final App application = (App) RuntimeEnvironment.application;
        Whitebox.setInternalState(application, "appComponent", appComponent);
    }

}
