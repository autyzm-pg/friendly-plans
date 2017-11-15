package pg.autyzm.friendly_plans.test_helpers;

import org.mockito.Mockito;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifierModule;

public class ToastUserNotifierModuleMock extends ToastUserNotifierModule {

    private ToastUserNotifier toastUserNotifier;

    public ToastUserNotifierModuleMock() {
        toastUserNotifier = Mockito.mock(ToastUserNotifier.class);
    }

    @Override
    public ToastUserNotifier getToastUserNotifier() {
        return toastUserNotifier;
    }

}
