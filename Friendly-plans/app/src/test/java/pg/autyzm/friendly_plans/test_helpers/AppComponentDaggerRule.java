package pg.autyzm.friendly_plans.test_helpers;

import android.content.Context;
import database.repository.DaoSessionModule;
import database.repository.RepositoryModule;
import it.cosenonjaviste.daggermock.DaggerMockRule;
import org.robolectric.RuntimeEnvironment;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.AppComponent;
import pg.autyzm.friendly_plans.file_picker.FilePickerModule;
import pg.autyzm.friendly_plans.string_provider.StringProviderModule;
import pg.autyzm.friendly_plans.validation.ValidationModule;

public class AppComponentDaggerRule extends DaggerMockRule<AppComponent> {

    public AppComponentDaggerRule() {
        super(
                AppComponent.class,
                new FilePickerModule(),
                new RepositoryModule(),
                new ValidationModule(),
                new DaoSessionModule(getAppContext()),
                new StringProviderModule(getAppContext())

        );
        set(new DaggerMockRule.ComponentSetter<AppComponent>() {
            @Override
            public void setComponent(AppComponent component) {
                getApp().setComponent(component);
            }
        });
    }

    private static Context getAppContext() {
        return getApp().getApplicationContext();
    }

    private static App getApp() {
        return (App) RuntimeEnvironment.application;
    }

}