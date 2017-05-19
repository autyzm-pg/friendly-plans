package pg.autyzm.friendly_plans.validation;

import android.widget.EditText;
import database.repository.TaskTemplateRepository;
import java.util.regex.Pattern;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

/***
 * Original of validation class is here:
 * https://tausiq.wordpress.com/2013/01/19/android-input-field-validation/
 */
public class Validation {

    private static final String NAME_REGEX = "^[a-zA-Z0-9_. -]*$";
    private static final String NUM_ONLY_REGEX = "^[0-9]*$";
    private final StringsProvider stringProvider;
    protected final TaskTemplateRepository taskTmplRepo;

    public Validation(StringsProvider stringsProvider, TaskTemplateRepository taskTmplRepo) {
        this.stringProvider = stringsProvider;
        this.taskTmplRepo = taskTmplRepo;
    }

    protected boolean isNameOk(EditText editText, boolean required) {
        return isValid(editText, NAME_REGEX, stringProvider.getString(R.string.only_letters_msg),
                required);
    }

    protected boolean isNumber(EditText editText, boolean required) {
        return isValid(editText, NUM_ONLY_REGEX,
                stringProvider.getString(R.string.only_numbers_msg), required);
    }

    private boolean isValid(EditText editText, String regex, String errMsg,
            boolean required) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        if (required && !hasText(editText)) {
            return false;
        }

        if (required && !Pattern.matches(regex, text)) {
            editText.setError(errMsg);
            return false;
        }
        return true;
    }

    protected boolean hasText(EditText editText) {

        String text = editText.getText().toString().trim();
        editText.setError(null);

        if (text.length() == 0) {
            editText.setError(stringProvider.getString(R.string.not_empty_msg));
            return false;
        }
        return true;
    }

    protected void showError(EditText editText) {
        editText.setError(stringProvider.getString(R.string.name_exist_msg));
    }
}
