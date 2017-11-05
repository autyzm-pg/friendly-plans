package pg.autyzm.friendly_plans.validation;

public class ValidationResult {

    private ValidationStatus validationStatus;
    private String validationInfo;

    public ValidationResult(ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
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
