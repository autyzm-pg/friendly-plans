package pg.autyzm.friendly_plans.utils;

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
    private static final String WRONG_NAME_MSG = "Only letters and numbers are allowed";
    private static final String DUPLICATED_NAME_MSG = "Name already exists";
    private static final String ONLY_NUMBERS_MSG = "Only numbers allowed";
    private static final String NOT_EMPTY_MSG = "Should not be empty";


    // call this method when you need to check XXX validation
    public static boolean isNameOk(EditText editText, boolean required) {
        return isValid(editText, NAME_REGEX, WRONG_NAME_MSG, required);
    }

    // call this method when you need to check XXX number validation
    public static boolean isNumber(EditText editText, boolean required) {
        return isValid(editText, NUM_ONLY_REGEX, ONLY_NUMBERS_MSG, required);
    }

    // return true if the input field is valid, based on the parameter passed
    public static boolean isValid(EditText editText, String regex, String errMsg,
            boolean required) {

        String text = editText.getText().toString().trim();
        // clearing the error, if it was previously set by some other values
        editText.setError(null);

        // text required and editText is blank, so return false
        if (required && !hasText(editText)) {
            return false;
        }

        // pattern doesn't match so returning false
        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        }

        return true;
    }

    // check the input field has any text or not // TODO: for mandatory fields.Find out which ?
    // return true if it contains text otherwise false
    public static boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        // length 0 means there is no text
        if (text.length() == 0) {
            editText.setError(NOT_EMPTY_MSG);
            return false;
        }
        return true;
    }

    public static boolean isNameEmpty(EditText editText, boolean flag) {
        if (!flag) {
            editText.setError(DUPLICATED_NAME_MSG);
            return false;
        } else {
            return true;
        }
    }

}
