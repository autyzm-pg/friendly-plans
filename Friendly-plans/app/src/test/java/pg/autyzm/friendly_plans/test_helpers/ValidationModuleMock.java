package pg.autyzm.friendly_plans.test_helpers;

import database.repository.TaskTemplateRepository;
import org.mockito.Mockito;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;
import pg.autyzm.friendly_plans.validation.TaskValidation;
import pg.autyzm.friendly_plans.validation.ValidationModule;

public class ValidationModuleMock extends ValidationModule {

    private TaskValidation taskValidation;

    ValidationModuleMock() {
        taskValidation = Mockito.mock(TaskValidation.class);
    }

    @Override
    public TaskValidation getTaskValidation(StringsProvider stringsProvider, TaskTemplateRepository taskTemplateRepository) {
        return taskValidation;
    }

    public TaskValidation getTaskValidation() {
        return taskValidation;
    }
}
