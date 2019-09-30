package pg.autyzm.friendly_plans.string_provider;

import android.content.Context;

public class StringsProvider {

    private final Context context;

    public StringsProvider(Context context) {
        this.context = context;
    }

    public String getString(int stringId) {
        return context.getString(stringId);
    }
}
