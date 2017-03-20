package pg.autyzm.friendly_plans.validation;

import android.widget.EditText;

public class TaskValidation extends Validation {

    public static boolean isAllValidationOK(EditText name, EditText duration) {
        if (isFormOk(name, duration)) {
            if (isNameFree(name)) {
                return true;
            }
        }
        return false;
    }

    //every mandatory field in the form is validated here
    private static boolean isFormOk(EditText name, EditText duration) {
        boolean ret = true;

        if (!Validation.hasText(name)) {
            ret = false;
        }
        if (!Validation.isNameOk(name, true)) {
            ret = false;
        }
        if (!Validation.isNumber(duration, false)) {
            ret = false;
        }
        // add validation on other fields if required
        return ret;
    }

    // search name in db
    private static boolean isNameFree(EditText name) {

        boolean isNameOnDb = false; //temporal.  Store db respond in this variable!
        //TODO: implement method that return t/f if record with required name, is on db.

        if (isNameOnDb) {
            showError(name);
            return false;
        }
        return true;
    }

}
