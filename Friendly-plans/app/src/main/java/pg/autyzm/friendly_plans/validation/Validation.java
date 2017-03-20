package pg.autyzm.friendly_plans.validation;

import android.widget.EditText;
import java.util.regex.Pattern;

/***
 * Original of validation class is here:
 * https://tausiq.wordpress.com/2013/01/19/android-input-field-validation/
 */
public class Validation {

    // Regular Expression
    private static final String NAME_REGEX = "^[a-zA-Z0-9_. -]*$";
    private static final String NUM_ONLY_REGEX = "^[0-9]*$";

    // Error Messages
    private static final String WRONG_NAME_MSG = "@string/only_letters_msg";
    private static final String DUPLICATED_NAME_MSG = "@string/name_exist_msg";
    private static final String ONLY_NUMBERS_MSG = "@string/only_numbers_msg";
    private static final String NOT_EMPTY_MSG = "@string/not_empty_msg";


    protected static boolean isNameOk(EditText editText, boolean required) {
        return isValid(editText, NAME_REGEX, WRONG_NAME_MSG, required);
    }

    protected static boolean isNumber(EditText editText, boolean required) {
        return isValid(editText, NUM_ONLY_REGEX, ONLY_NUMBERS_MSG, required);
    }

    private static boolean isValid(EditText editText, String regex, String errMsg,
            boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required(*) and editText is blank, so return false
        if (required && !hasText(editText)) {
            return false;
        }

        // pattern doesn't match, so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        }

        return true;
    }

    // check the input field has any text or not. Method serve to check mandatory fields(*).
    protected static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        if (text.length() == 0) {
            editText.setError(NOT_EMPTY_MSG);
            return false;
        }
        return true;
    }

    protected static void showError(EditText editText) {
            editText.setError(DUPLICATED_NAME_MSG);
    }
}
