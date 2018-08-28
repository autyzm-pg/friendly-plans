package pg.autyzm.friendly_plans.database;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import database.entities.DaoSession;
import database.entities.PlanTaskTemplate;
import database.entities.PlanTaskTemplateDao;
import database.entities.PlanTemplate;
import database.entities.PlanTemplateDao;
import database.entities.TaskTemplate;
import database.entities.TaskTemplateDao;
import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.BuildConfig;
import pg.autyzm.friendly_plans.test_helpers.DaoSessionResource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class PlanTemplateRepositoryTest {

    private PlanTemplateRepository planTemplateRepository;

    private List<PlanTemplate> testPlans = new ArrayList<>();

    private TaskTemplate taskAssignedToPlan;
    private TaskTemplate taskNotAssignedToPlan;


    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        DaoSession daoSession = daoSessionResource.getSession(context);
        PlanTemplateDao planTemplateDao = daoSession.getPlanTemplateDao();
        TaskTemplateDao taskTemplateDao = daoSession.getTaskTemplateDao();
        PlanTaskTemplateDao planTaskTemplateDao = daoSession.getPlanTaskTemplateDao();
        planTemplateRepository = new PlanTemplateRepository(daoSession);

        testPlans.add(new PlanTemplate());
        testPlans.add(new PlanTemplate());
        testPlans.get(0).setName("TEST PLAN NAME 0");
        testPlans.get(1).setName("TEST PLAN NAME 1");

        for (PlanTemplate planTemplate : testPlans) {
            planTemplateDao.insert(planTemplate);
        }

        taskAssignedToPlan = new TaskTemplate();
        taskTemplateDao.insert(taskAssignedToPlan);
        PlanTaskTemplate taskToPlanAssignment = new PlanTaskTemplate();
        taskToPlanAssignment.setPlanTemplate(testPlans.get(0));
        taskToPlanAssignment.setTaskTemplate(taskAssignedToPlan);
        planTaskTemplateDao.insert(taskToPlanAssignment);
        taskNotAssignedToPlan = new TaskTemplate();
        taskTemplateDao.insert(taskNotAssignedToPlan);
    }

    @After
    public void tearDown() {
        for (PlanTaskTemplate taskToPlanAssignment : taskAssignedToPlan.getPlanTaskTemplates()) {
            taskToPlanAssignment.delete();
        }
        for (PlanTemplate planTemplate : testPlans) {
            planTemplate.delete();
        }
        taskAssignedToPlan.delete();
        taskNotAssignedToPlan.delete();
    }

    @Test
    public void whenGettingPlansByTaskIdExpectOnlyPlanContainingTaskBeReturned() {
        List<PlanTemplate> plansWithTask = planTemplateRepository.getPlansWithThisTask(
                taskAssignedToPlan.getId());

        assertThat(plansWithTask.size(), equalTo(1));
        assertThat(plansWithTask.get(0).getName(), equalTo("TEST PLAN NAME 0"));
    }

    @Test
    public void whenGettingPlansByTaskIdAndTaskIsNotAssignedToAnyPlanExpectNoPlansBeReturned() {
        List<PlanTemplate> plansWithTask = planTemplateRepository.getPlansWithThisTask(
                taskNotAssignedToPlan.getId());

        assertThat(plansWithTask.size(), equalTo(0));
    }

}
