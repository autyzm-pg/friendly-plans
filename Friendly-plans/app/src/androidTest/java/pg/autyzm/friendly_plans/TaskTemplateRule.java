package pg.autyzm.friendly_plans;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import database.repository.StepTemplateRepository;
import database.repository.TaskTemplateRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.rules.ExternalResource;

public class TaskTemplateRule extends ExternalResource {

    private final DaoSessionResource daoSessionResource;
    private final ActivityTestRule activityRule;

    private StepTemplateRepository stepTemplateRepository;
    private TaskTemplateRepository taskTemplateRepository;

    private List<Long> stepIds = new ArrayList<>();
    private List<Long> taskIds = new ArrayList<>();

    public TaskTemplateRule(DaoSessionResource daoSessionResource, ActivityTestRule activityRule) {
        this.daoSessionResource = daoSessionResource;
        this.activityRule = activityRule;
    }

    public long createTaskWithSteps(String taskName1, String stepName1, String stepName2) {
        Context context = activityRule.getActivity().getApplicationContext();
        stepTemplateRepository = new StepTemplateRepository(daoSessionResource.getSession(context));
        taskTemplateRepository = new TaskTemplateRepository(daoSessionResource.getSession(context));

        long taskId = taskTemplateRepository.create(taskName1, 2, null, null);
        taskIds.add(taskId);

        stepIds.add(stepTemplateRepository.create(stepName1, 1, null, null, taskId));
        stepIds.add(stepTemplateRepository.create(stepName2, 2, null, null, taskId));

        return taskId;
    }

    @Override
    protected void after() {
        for (Long stepId : stepIds) {
            stepTemplateRepository.delete(stepId);
        }

        for (Long taskId : taskIds) {
            taskTemplateRepository.delete(taskId);
        }
    }
}
