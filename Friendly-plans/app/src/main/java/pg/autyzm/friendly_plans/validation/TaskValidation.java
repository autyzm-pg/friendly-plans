package pg.autyzm.friendly_plans.validation;

import database.repository.TaskTemplateRepository;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

public class TaskValidation extends Validation {

    public TaskValidation(StringsProvider stringsProvider,
            TaskTemplateRepository taskTemplateRepository) {
        super(stringsProvider, taskTemplateRepository);
    }

    public ValidationResult isNameValid(String name) {
        ValidationResult validationResult = isNameString(name, true);
        if (validationResult.getValidationStatus() == ValidationStatus.INVALID) {
            return validationResult;
        }

        if (taskTemplateRepository.isNameExists(name)) {
            return new ValidationResult(ValidationStatus.INVALID,
                    stringProvider.getString(R.string.name_exist_msg));
        }

        return new ValidationResult(ValidationStatus.VALID);
    }

    public ValidationResult isNameValid(Long taskId, String name) {
        ValidationResult validationResult = isNameString(name, true);
        if (validationResult.getValidationStatus() == ValidationStatus.INVALID) {
            return validationResult;
        }

        if (taskTemplateRepository.isNameExists(taskId, name)) {
            return new ValidationResult(ValidationStatus.INVALID,
                    stringProvider.getString(R.string.name_exist_msg));
        }

        return new ValidationResult(ValidationStatus.VALID);
    }

    public ValidationResult isDurationValid(String duration) {
        return isNumber(duration, true);
    }
}
