package pg.autyzm.friendly_plans.validation;

import android.widget.EditText;
import database.repository.TaskTemplateRepository;
import pg.autyzm.friendly_plans.string_provider.StringsProvider;

public class TaskValidation extends Validation {

    public TaskValidation(StringsProvider stringsProvider, TaskTemplateRepository taskTmplRepo) {
        super(stringsProvider, taskTmplRepo);
    }

    public boolean isValid(EditText name, EditText duration) {
        if (!hasText(name)) {
            return false;
        }
        if (!isNameOk(name, true)) {
            return false;
        }
        if (!isNumber(duration, true)) {
            return false;
        }

        if (taskTmplRepo.isNameExists(name.getText().toString())) {
            showError(name);
            return false;
        }
        return true;
    }
}
