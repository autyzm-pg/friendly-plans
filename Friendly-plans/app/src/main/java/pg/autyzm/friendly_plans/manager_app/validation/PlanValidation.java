package pg.autyzm.friendly_plans.manager_app.validation;

import java.util.List;

import database.entities.PlanTemplate;
import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

public class PlanValidation extends Validation {

    private PlanTemplateRepository planTemplateRepository;

    public PlanValidation(StringsProvider stringsProvider,
                          PlanTemplateRepository planTemplateRepository) {
        super(stringsProvider);
        this.planTemplateRepository = planTemplateRepository;
    }

    public ValidationResult isNewNameValid(String name) {
        ValidationResult validationResult = isNameValid(name);
        if (validationResult.getValidationStatus() == ValidationStatus.INVALID) {
            return validationResult;
        }

        if (planTemplateRepository.get(name).size() > 0 ){
            return new ValidationResult(ValidationStatus.INVALID,
                    stringProvider.getString(R.string.name_exist_msg));
        }
        return new ValidationResult(ValidationStatus.VALID);
    }

    public ValidationResult isUpdateNameValid(Long planId, String name) {
        ValidationResult validationResult = isNameValid(name);
        if (validationResult.getValidationStatus() == ValidationStatus.INVALID) {
            return validationResult;
        }

        if (!nameExistsWithSameIdOrNotAtAll(planId, name)) {
            return new ValidationResult(ValidationStatus.INVALID,
                    stringProvider.getString(R.string.name_exist_msg));
        }
        return new ValidationResult(ValidationStatus.VALID);
    }

    private boolean nameExistsWithSameIdOrNotAtAll(Long planId, String name) {
        List<PlanTemplate> planTemplates = planTemplateRepository.get(name);
        if (planTemplates.size() == 0) {
            return true;
        } else if (planTemplates.size() == 1) {
            return planTemplates.get(0).getId().equals(planId);
        }

        return false;
    }
}
