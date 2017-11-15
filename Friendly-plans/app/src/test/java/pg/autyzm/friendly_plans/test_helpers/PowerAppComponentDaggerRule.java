package pg.autyzm.friendly_plans.test_helpers;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;


public class PowerAppComponentDaggerRule extends AppComponentDaggerRule implements TestRule {

    @Override
    public Statement apply(Statement base, Description description) {
        final AppComponent appComponent = this.getAppComponent();
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Whitebox.setInternalState(getApp(), "appComponent", appComponent);
            }
        };

    }

    private static App getApp() {
        return (App) RuntimeEnvironment.application;
    }
}
