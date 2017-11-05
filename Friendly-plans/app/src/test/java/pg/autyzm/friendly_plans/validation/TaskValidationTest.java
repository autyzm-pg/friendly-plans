package pg.autyzm.friendly_plans.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import android.widget.EditText;
import database.repository.TaskTemplateRepository;
import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

@RunWith(MockitoJUnitRunner.class)
public class TaskValidationTest {

    private static final String VALID_TASK_NAME = "Task name";
    private static final String EMPTY_TASK_NAME = "";
    private static final String INVALID_TASK_NAME = "#45%%^3$#@$))";
    private static final String ERROR_MESSAGE_EMPTY = "Error - message empty";
    private static final String ERROR_MESSAGE_ONLY_LETTERS = "Error message - only letters";
    private static final String ERROR_MESSAGE_ONLY_NUMBERS = "Error message - only numbers";
    private static final String ERROR_MESSAGE_NAME_EXIST = "Error message - name exist";
    private static final String INVALID_DURATION = "ERROR NUMBER%6";
    private static final String VALID_DURATION = "3";
    private static final Long TASK_ID = new Random().nextLong();

    @Mock
    public StringsProvider stringsProvider;

    @Mock
    public TaskTemplateRepository taskTemplateRepository;

    @Mock
    public EditText editTextName;

    private TaskValidation taskValidation;

    @Before
    public void setUp() {
        when(stringsProvider.getString(R.string.not_empty_msg)).thenReturn(ERROR_MESSAGE_EMPTY);
        when(stringsProvider.getString(R.string.only_letters_msg))
                .thenReturn(ERROR_MESSAGE_ONLY_LETTERS);
        when(stringsProvider.getString(R.string.only_numbers_msg))
                .thenReturn(ERROR_MESSAGE_ONLY_NUMBERS);
        when(stringsProvider.getString(R.string.name_exist_msg))
                .thenReturn(ERROR_MESSAGE_NAME_EXIST);

        taskValidation = new TaskValidation(stringsProvider, taskTemplateRepository);
    }

    @Test
    public void whenTaskNameToSaveIsValidExpectTaskIsValid() {
        when(taskTemplateRepository.isNameExists(VALID_TASK_NAME)).thenReturn(false);
        ValidationResult validationResult = taskValidation.isNameValid(VALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.VALID));
    }

    @Test
    public void whenTaskNameToSaveIsEmptyExpectTaskIsNotInvalid() {
        ValidationResult validationResult = taskValidation.isNameValid(EMPTY_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_EMPTY));
    }

    @Test
    public void whenTaskNameToSaveIsInvalidExpectTaskIsNotInvalid() {
        ValidationResult validationResult = taskValidation.isNameValid(INVALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_ONLY_LETTERS));
    }

    @Test
    public void whenTaskNameToSaveIsDuplicatedExpectTaskIsNotValid() {
        when(taskTemplateRepository.isNameExists(VALID_TASK_NAME)).thenReturn(true);
        ValidationResult validationResult = taskValidation.isNameValid(VALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_NAME_EXIST));
    }

    @Test
    public void whenTaskNameToUpdateExpectTaskIsValid() {
        when(taskTemplateRepository.isNameExists(TASK_ID, VALID_TASK_NAME)).thenReturn(false);
        ValidationResult validationResult = taskValidation.isNameValid(TASK_ID, VALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.VALID));
    }

    @Test
    public void whenTaskNameToUpdateIsEmptyExpectTaskIsNotValid() {
        ValidationResult validationResult = taskValidation.isNameValid(TASK_ID, EMPTY_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_EMPTY));
    }

    @Test
    public void whenTaskNameToUpdateIsInvalidExpectTaskIsNotValid() {
        ValidationResult validationResult = taskValidation.isNameValid(TASK_ID, INVALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_ONLY_LETTERS));
    }

    @Test
    public void whenTaskNameToUpdateIsDuplicatedExpectTaskIsNotValid() {
        when(taskTemplateRepository.isNameExists(TASK_ID, VALID_TASK_NAME)).thenReturn(true);
        ValidationResult validationResult = taskValidation.isNameValid(TASK_ID, VALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_NAME_EXIST));
    }

    @Test
    public void whenDurationIsANumberExpectIsValid() {
        ValidationResult validationResult = taskValidation.isDurationValid(VALID_DURATION);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.VALID));
    }

    @Test
    public void whenDurationIsNotANumberExpectIsNotValid() {
        ValidationResult validationResult = taskValidation.isDurationValid(INVALID_DURATION);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_ONLY_NUMBERS));
    }
}
