package pg.autyzm.friendly_plans.validation;

import android.widget.EditText;

public class TaskValidation extends Validation {

    public static boolean isValid(EditText name, EditText duration) {
        if (!Validation.hasText(name)) {
            return false;
        }
        if (!Validation.isNameOk(name, true)) {
            return false;
        }
        if (!Validation.isNumber(duration, false)) {
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
