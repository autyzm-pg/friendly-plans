package pg.autyzm.friendly_plans.validation;

import database.repository.TaskTemplateRepository;
import java.util.regex.Pattern;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

public class Validation {

    private static final String NAME_REGEX = "^[a-zA-Z0-9_. -]*$";
    private static final String NUM_ONLY_REGEX = "^[0-9]*$";

    final StringsProvider stringProvider;
    final TaskTemplateRepository taskTemplateRepository;

    public Validation(StringsProvider stringsProvider,
            TaskTemplateRepository taskTemplateRepository) {
        this.stringProvider = stringsProvider;
        this.taskTemplateRepository = taskTemplateRepository;
    }

    ValidationResult isStringEmpty(String stringValue) {
        if (stringValue.trim().length() == 0) {
            return new ValidationResult(ValidationStatus.INVALID,
                    stringProvider.getString(R.string.not_empty_msg));
        }
        return new ValidationResult(ValidationStatus.VALID);
    }

    ValidationResult isName(String stringValue) {
        return validatePattern(stringValue, NAME_REGEX,
                stringProvider.getString(R.string.only_letters_msg));
    }

    ValidationResult isNumber(String stringValue) {
        return validatePattern(stringValue, NUM_ONLY_REGEX,
                stringProvider.getString(R.string.only_numbers_msg));
    }

    private ValidationResult validatePattern(String textValue, String regex, String errMsg) {
        if (!Pattern.matches(regex, textValue)) {
            return new ValidationResult(ValidationStatus.INVALID, errMsg);
        }
        return new ValidationResult(ValidationStatus.VALID);
    }
}
