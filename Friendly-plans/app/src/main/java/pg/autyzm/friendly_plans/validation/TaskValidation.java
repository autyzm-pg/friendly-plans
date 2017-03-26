package pg.autyzm.friendly_plans.validation;

import android.widget.EditText;
import pg.autyzm.friendly_plans.utils.StringsProvider;

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
        if (!isNumber(duration, false)) {
            return false;
        }

        //TODO: implement method that return t/f if record with required name, is on db.
        boolean isNameOnDb = false;

        if (isNameOnDb) {
            showError(name);
            return false;
        }
        return true;
    }
}
