package pg.autyzm.friendly_plans.validation;

import database.entities.TaskTemplate;
import database.repository.TaskTemplateRepository;
import java.util.List;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

public class TaskValidation extends Validation {

    final TaskTemplateRepository taskTemplateRepository;

    public TaskValidation(StringsProvider stringsProvider,
            TaskTemplateRepository taskTemplateRepository) {
        super(stringsProvider);
        this.taskTemplateRepository = taskTemplateRepository;
    }

    public ValidationResult isNewNameValid(String name) {
        ValidationResult validationResult = isNameValid(name);
        if (validationResult.getValidationStatus() == ValidationStatus.INVALID) {
            return validationResult;
        }

        if (taskTemplateRepository.get(name).size() > 0) {
            return new ValidationResult(ValidationStatus.INVALID,
                    stringProvider.getString(R.string.name_exist_msg));
        }

        return new ValidationResult(ValidationStatus.VALID);
    }

    public ValidationResult isUpdateNameValid(Long taskId, String name) {
        ValidationResult validationResult = isNameValid(name);
        if (validationResult.getValidationStatus() == ValidationStatus.INVALID) {
            return validationResult;
        }

        if (!checkUpdateNameDoesNotExist(taskId, name)) {
            return new ValidationResult(ValidationStatus.INVALID,
                    stringProvider.getString(R.string.name_exist_msg));
        }

        return new ValidationResult(ValidationStatus.VALID);
    }


    private boolean checkUpdateNameDoesNotExist(Long taskId, String name) {
        List<TaskTemplate> taskTemplates = taskTemplateRepository.get(name);
        if (taskTemplates.size() == 0) {
            return true;
        } else if (taskTemplates.size() == 1) {
            return taskTemplates.get(0).getId().equals(taskId);
        }

        return false;
    }

    public ValidationResult isDurationValid(String duration) {
        ValidationResult validationResult = isStringEmpty(duration);
        if (validationResult.getValidationStatus().equals(ValidationStatus.VALID)) {
            return isNumber(duration);
        }

        return new ValidationResult(ValidationStatus.VALID);
    }
}
