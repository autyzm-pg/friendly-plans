package pg.autyzm.friendly_plans.validation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;

import database.entities.PlanTemplate;
import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;
import pg.autyzm.friendly_plans.test_helpers.RandomGenerator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PlanValidationTest {

    private static final String VALID_PLAN_NAME = "Plan name";
    private static final String EMPTY_PLAN_NAME = "";
    private static final String INVALID_PLAN_NAME = "#45%%^3$#@$))";
    private static final String ERROR_MESSAGE_EMPTY = "Error - message empty";
    private static final String ERROR_MESSAGE_NAME_EXIST = "Error message - name exist";
    private static final String ERROR_MESSAGE_ONLY_LETTERS = "Error message - only letters";
    private static final Long PLAN_ID = RandomGenerator.getId();


    @Mock
    private StringsProvider stringsProvider;

    @Mock
    private PlanTemplateRepository planTemplateRepository;

    private PlanTemplate duplicatedPlanTemplate;
    private PlanValidation planValidation;

    @Before
    public void setUp() {
        duplicatedPlanTemplate = new PlanTemplate();
        duplicatedPlanTemplate.setName(VALID_PLAN_NAME);

        when(stringsProvider.getString(R.string.not_empty_msg)).thenReturn(ERROR_MESSAGE_EMPTY);
        when(stringsProvider.getString(R.string.name_exist_msg))
                .thenReturn(ERROR_MESSAGE_NAME_EXIST);
        when(stringsProvider.getString(R.string.only_letters_msg))
                .thenReturn(ERROR_MESSAGE_ONLY_LETTERS);

        planValidation = new PlanValidation(stringsProvider, planTemplateRepository);
    }

    @Test
    public void whenPlanNameIsValidExpectNewNameValidationPasses() {
        when(planTemplateRepository.get(VALID_PLAN_NAME)).thenReturn(Collections.EMPTY_LIST);
        ValidationResult validationResult = planValidation.isNewNameValid(VALID_PLAN_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.VALID));
    }

    @Test
    public void whenPlanNameIsEmptyExpectNewNameValidationFails() {
        ValidationResult validationResult = planValidation.isNewNameValid(EMPTY_PLAN_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_EMPTY));
    }

    @Test
    public void whenPlanNameIsInvalidExpectNewNameValidationFails() {
        ValidationResult validationResult = planValidation.isNewNameValid(INVALID_PLAN_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_ONLY_LETTERS));
    }

    @Test
    public void whenPlanNameIsDuplicatedExpectNewNameValidationFails() {
        when(planTemplateRepository.get(VALID_PLAN_NAME))
                .thenReturn(Collections.singletonList(duplicatedPlanTemplate));
        ValidationResult validationResult = planValidation.isNewNameValid(VALID_PLAN_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_NAME_EXIST));
    }

    @Test
    public void whenPlanNameIsValidExpectUpdateNameValidationPasses() {
        when(planTemplateRepository.get(VALID_PLAN_NAME)).thenReturn(Collections.EMPTY_LIST);
        ValidationResult validationResult = planValidation.isUpdateNameValid(
                PLAN_ID, VALID_PLAN_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.VALID));
    }

    @Test
    public void whenPlanNameIsEmptyExpectUpdateNameValidationFails() {
        ValidationResult validationResult = planValidation.isUpdateNameValid(
                PLAN_ID, EMPTY_PLAN_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_EMPTY));
    }

    @Test
    public void whenPlanNameIsInvalidExpectUpdateNameValidationFails() {
        ValidationResult validationResult = planValidation.isUpdateNameValid(
                PLAN_ID, INVALID_PLAN_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_ONLY_LETTERS));
    }

    @Test
    public void whenPlanNameIsAlreadyInDbWithSameIdExpectUpdateNameValidationPasses() {
        duplicatedPlanTemplate.setId(PLAN_ID);
        when(planTemplateRepository.get(VALID_PLAN_NAME))
                .thenReturn(Collections.singletonList(duplicatedPlanTemplate));
        ValidationResult validationResult = planValidation.isUpdateNameValid(
                PLAN_ID, VALID_PLAN_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.VALID));
    }

    @Test
    public void whenPlanNameIsAlreadyInDbWithDifferentIdExpectUpdateNameValidationPasses() {
        duplicatedPlanTemplate.setId(PLAN_ID + 1);
        when(planTemplateRepository.get(VALID_PLAN_NAME))
                .thenReturn(Collections.singletonList(duplicatedPlanTemplate));
        ValidationResult validationResult = planValidation.isUpdateNameValid(
                PLAN_ID, VALID_PLAN_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_NAME_EXIST));
    }
}
