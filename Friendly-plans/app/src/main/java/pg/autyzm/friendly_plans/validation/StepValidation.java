package pg.autyzm.friendly_plans.validation;

import java.util.List;

import database.entities.StepTemplate;
import database.repository.StepTemplateRepository;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

public class StepValidation extends Validation {

    final StepTemplateRepository stepTemplateRepository;


    public StepValidation(StringsProvider stringsProvider,
                          StepTemplateRepository stepTemplateRepository) {
        super(stringsProvider);
        this.stepTemplateRepository = stepTemplateRepository;
    }

    public ValidationResult isNewNameValid(Long taskId, String name) {
        ValidationResult validationResult = isNameValid(name);
        if (validationResult.getValidationStatus() == ValidationStatus.INVALID) {
            return validationResult;
        }

        if (stepTemplateRepository.get(name, taskId).size() > 0) {
            return new ValidationResult(ValidationStatus.INVALID,
                    stringProvider.getString(R.string.name_exist_msg));
        }

        return new ValidationResult(ValidationStatus.VALID);
    }

    public ValidationResult isUpdateNameValid(Long stepId, Long taskId, String name) {
        ValidationResult validationResult = isNameValid(name);
        if (validationResult.getValidationStatus() == ValidationStatus.INVALID) {
            return validationResult;
        }

        if (!checkUpdateNameDoesNotExist(stepId, taskId, name)) {
            return new ValidationResult(ValidationStatus.INVALID,
                    stringProvider.getString(R.string.name_exist_msg));
        }

        return new ValidationResult(ValidationStatus.VALID);
    }


    private boolean checkUpdateNameDoesNotExist(Long stepId, Long taskId, String name) {
        List<StepTemplate> stepTemplates = stepTemplateRepository.get(name, taskId);
        if (stepTemplates.size() == 0) {
            return true;
        } else if (stepTemplates.size() == 1) {
            return stepTemplates.get(0).getId().equals(stepId);
        }
        return false;
    }

}
