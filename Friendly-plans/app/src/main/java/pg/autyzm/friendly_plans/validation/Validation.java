package pg.autyzm.friendly_plans.validation;

import database.repository.TaskTemplateRepository;
import java.util.regex.Pattern;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

public class Validation {

    private static final String NAME_REGEX = "^[a-zA-Z0-9_. -]*$";
    private static final String NUM_ONLY_REGEX = "^[0-9]*$";

    protected final StringsProvider stringProvider;
    protected final TaskTemplateRepository taskTemplateRepository;

    public Validation(StringsProvider stringsProvider,
            TaskTemplateRepository taskTemplateRepository) {
        this.stringProvider = stringsProvider;
        this.taskTemplateRepository = taskTemplateRepository;
    }

    protected ValidationResult isNameString(String stringValue, boolean required) {
        return isValid(stringValue, NAME_REGEX, stringProvider.getString(R.string.only_letters_msg),
                required);
    }

    protected ValidationResult isNumber(String stringValue, boolean required) {
        return isValid(stringValue, NUM_ONLY_REGEX,
                stringProvider.getString(R.string.only_numbers_msg), required);
    }

    private ValidationResult isValid(String textValue, String regex, String errMsg,
            boolean required) {
        if (required && textValue.trim().length() == 0) {
            return new ValidationResult(ValidationStatus.INVALID,
                    stringProvider.getString(R.string.not_empty_msg));
        }

        if (required && !Pattern.matches(regex, textValue)) {
            return new ValidationResult(ValidationStatus.INVALID, errMsg);
        }
        return new ValidationResult(ValidationStatus.VALID);
    }
}
