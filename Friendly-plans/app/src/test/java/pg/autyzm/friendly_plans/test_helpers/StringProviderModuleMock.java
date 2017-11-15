package pg.autyzm.friendly_plans.test_helpers;

import org.mockito.Mockito;
import pg.autyzm.friendly_plans.string_provider.StringProviderModule;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;


public class StringProviderModuleMock extends StringProviderModule {

    private StringsProvider stringsProvider;

    public StringProviderModuleMock() {
        super(null);
        stringsProvider = Mockito.mock(StringsProvider.class);
    }

    @Override
    public StringsProvider getStringProvider() {
        return stringsProvider;
    }

}
