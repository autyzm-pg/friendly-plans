package pg.autyzm.friendly_plans.manager_app.validation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import android.widget.EditText;
import database.entities.TaskTemplate;
import database.repository.TaskTemplateRepository;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;
import pg.autyzm.friendly_plans.test_helpers.RandomGenerator;

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
    private static final String EMPTY_DURATION = "";
    private static final Long TASK_ID = RandomGenerator.getId();

    @Mock
    public StringsProvider stringsProvider;

    @Mock
    public TaskTemplateRepository taskTemplateRepository;

    @Mock
    public EditText editTextName;

    private TaskValidation taskValidation;
    private TaskTemplate duplicatedTaskTemplate = new TaskTemplate();

    @Before
    public void setUp() {
        duplicatedTaskTemplate.setName(VALID_TASK_NAME);
        duplicatedTaskTemplate.setId(RandomGenerator.getId());

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
        when(taskTemplateRepository.get(VALID_TASK_NAME)).thenReturn(Collections.EMPTY_LIST);
        ValidationResult validationResult = taskValidation.isNewNameValid(VALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.VALID));
    }

    @Test
    public void whenTaskNameToSaveIsEmptyExpectTaskIsNotInvalid() {
        ValidationResult validationResult = taskValidation.isNewNameValid(EMPTY_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_EMPTY));
    }

    @Test
    public void whenTaskNameToSaveIsInvalidExpectTaskIsNotInvalid() {
        ValidationResult validationResult = taskValidation.isNewNameValid(INVALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_ONLY_LETTERS));
    }

    @Test
    public void whenTaskNameToSaveIsDuplicatedExpectTaskIsNotValid() {
        when(taskTemplateRepository.get(VALID_TASK_NAME))
                .thenReturn(Collections.singletonList(duplicatedTaskTemplate));
        ValidationResult validationResult = taskValidation.isNewNameValid(VALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_NAME_EXIST));
    }

    @Test
    public void whenTaskNameToUpdateIsValidExpectTaskIsValid() {
        when(taskTemplateRepository.get(VALID_TASK_NAME)).thenReturn(Collections.EMPTY_LIST);
        ValidationResult validationResult = taskValidation
                .isUpdateNameValid(TASK_ID, VALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.VALID));
    }

    @Test
    public void whenTaskNameToUpdateIsEmptyExpectTaskIsNotValid() {
        ValidationResult validationResult = taskValidation
                .isUpdateNameValid(TASK_ID, EMPTY_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_EMPTY));
    }

    @Test
    public void whenTaskNameToUpdateIsInvalidExpectTaskIsNotValid() {
        ValidationResult validationResult = taskValidation
                .isUpdateNameValid(TASK_ID, INVALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_ONLY_LETTERS));
    }

    @Test
    public void whenTaskNameToUpdateIsDuplicatedExpectTaskIsNotValid() {
        when(taskTemplateRepository.get(VALID_TASK_NAME)).thenReturn(Collections.singletonList(
                duplicatedTaskTemplate));

        ValidationResult validationResult = taskValidation
                .isUpdateNameValid(TASK_ID, VALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_NAME_EXIST));
    }

    @Test
    public void whenTaskNameToUpdateIsNotDuplicatedExpectTaskIsValid() {
        duplicatedTaskTemplate.setId(TASK_ID);
        when(taskTemplateRepository.get(VALID_TASK_NAME)).thenReturn(Collections.singletonList(
                duplicatedTaskTemplate));

        ValidationResult validationResult = taskValidation
                .isUpdateNameValid(TASK_ID, VALID_TASK_NAME);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.VALID));
    }

    @Test
    public void whenDurationIsANumberExpectIsValid() {
        ValidationResult validationResult = taskValidation.isDurationValid(VALID_DURATION);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.VALID));
    }

    @Test
    public void whenDurationIsEmptyExpectIsValid() {
        ValidationResult validationResult = taskValidation.isDurationValid(EMPTY_DURATION);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.VALID));
    }

    @Test
    public void whenDurationIsNotANumberExpectIsNotValid() {
        ValidationResult validationResult = taskValidation.isDurationValid(INVALID_DURATION);

        assertThat(validationResult.getValidationStatus(), is(ValidationStatus.INVALID));
        assertThat(validationResult.getValidationInfo(), is(ERROR_MESSAGE_ONLY_NUMBERS));
    }
}
