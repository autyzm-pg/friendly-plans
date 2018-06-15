package pg.autyzm.friendly_plans.manager_app.validation;

public class ValidationResult {

    private final ValidationStatus validationStatus;
    private final String validationInfo;

    public ValidationResult(ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
        this.validationInfo = null;
    }

    public ValidationResult(ValidationStatus validationStatus, String validationInfo) {
        this.validationStatus = validationStatus;
        this.validationInfo = validationInfo;
    }

    public String getValidationInfo() {
        return validationInfo;
    }

    public ValidationStatus getValidationStatus() {
        return validationStatus;
    }
}
