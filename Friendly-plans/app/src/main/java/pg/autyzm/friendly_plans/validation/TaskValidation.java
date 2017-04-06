package pg.autyzm.friendly_plans.validation;

import android.widget.EditText;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

public class TaskValidation extends Validation {

    public TaskValidation(StringsProvider stringsProvider) {
        super(stringsProvider);
    }

    public boolean isValid(EditText name, EditText duration) {
        if (!hasText(name)) {
            return false;
        }
        if (!isNameOk(name, true)) {
            return false;
        }
        if (!isNumber(duration, true)) {
            return false;
        }

        boolean isNameOnDb = false;

        if (isNameOnDb) {
            showError(name);
            return false;
        }
        return true;
    }
}
