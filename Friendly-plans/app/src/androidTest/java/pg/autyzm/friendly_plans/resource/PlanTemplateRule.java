package pg.autyzm.friendly_plans.resource;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;

import org.junit.rules.ExternalResource;

import java.util.ArrayList;
import java.util.List;

import database.entities.PlanTemplate;
import database.repository.PlanTemplateRepository;
import database.repository.TaskTemplateRepository;

public class PlanTemplateRule extends ExternalResource {

    private final DaoSessionResource daoSessionResource;
    private final ActivityTestRule activityRule;

    private PlanTemplateRepository planTemplateRepository;
    private TaskTemplateRepository taskTemplateRepository;

    private List<Long> planIds = new ArrayList<>();
    private List<Long> taskIds = new ArrayList<>();

    public PlanTemplateRule(DaoSessionResource daoSessionResource, ActivityTestRule activityRule) {
        this.daoSessionResource = daoSessionResource;
        this.activityRule = activityRule;
    }

    public long createPlan(String planName) {
        Context context = activityRule.getActivity().getApplicationContext();
        planTemplateRepository = new PlanTemplateRepository(daoSessionResource.getSession(context));

        long planId = planTemplateRepository.create(planName);
        planIds.add(planId);

        return planId;
    }

    public long createPlanWithTasksSteps(String planName, String taskName1, String taskName2) {
        Context context = activityRule.getActivity().getApplicationContext();
        planTemplateRepository = new PlanTemplateRepository(daoSessionResource.getSession(context));
        taskTemplateRepository = new TaskTemplateRepository(daoSessionResource.getSession(context));

        long planId = planTemplateRepository.create(planName);
        planIds.add(planId);

        long taksId1 = taskTemplateRepository.create(taskName1, 2, null, null);
        long taksId2 = taskTemplateRepository.create(taskName2, 2, null, null);
        taskIds.add(taksId1);
        taskIds.add(taksId2);
        planTemplateRepository.setTasksWithThisPlan(planId, taksId1);
        planTemplateRepository.setTasksWithThisPlan(planId, taksId2);

        return planId;
    }

    @Override
    protected void after() {
        for (Long planId : planIds) {
            PlanTemplate planTemplate = planTemplateRepository.get(planId);
            planTemplate.resetTasksWithThisPlan();
            planTemplateRepository.delete(planId);
        }

        for (Long taskId : taskIds) {
            taskTemplateRepository.delete(taskId);
        }
    }
}
