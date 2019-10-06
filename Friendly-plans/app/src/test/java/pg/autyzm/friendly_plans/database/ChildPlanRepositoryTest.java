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

import database.entities.ChildPlan;
import database.entities.ChildPlanDao;
import database.entities.DaoSession;
import database.entities.PlanTemplate;
import database.entities.PlanTemplateDao;
import database.repository.ChildPlanRepository;
import pg.autyzm.friendly_plans.BuildConfig;
import pg.autyzm.friendly_plans.test_helpers.DaoSessionResource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ChildPlanRepositoryTest {

    private ChildPlanRepository childPlanRepository;

    private List<ChildPlan> testPlans = new ArrayList<>();

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.application.getApplicationContext();
        DaoSession daoSession = daoSessionResource.getSession(context);
        ChildPlanDao childPlanDao = daoSession.getChildPlanDao();
        PlanTemplateDao planTemplateDao = daoSession.getPlanTemplateDao();
        childPlanRepository = new ChildPlanRepository(daoSession);

        testPlans.add(new ChildPlan());
        testPlans.add(new ChildPlan());
        testPlans.get(0).setIsActive(true);
        testPlans.get(1).setIsActive(false);

        for (int i = 0; i < 2; i++) {
            PlanTemplate planTemplate = new PlanTemplate();
            planTemplate.setName(String.format("TEST PLAN NAME %d", i));
            planTemplateDao.insert(planTemplate);
            testPlans.get(i).setPlanTemplate(planTemplate);
            childPlanDao.insert(testPlans.get(i));
        }
    }

    @After
    public void tearDown() {
        for (ChildPlan childPlan : testPlans) {
            childPlan.delete();
        }
    }

    @Test
    public void whenGettingActivePlansActivePlanIsReturned() {
        ChildPlan activePlan = childPlanRepository.getActivePlan();

        assertThat(activePlan.getPlanTemplate().getName(), equalTo("TEST PLAN NAME 0"));
    }

    @Test
    public void whenGettingActivePlansAndThereIsNoActivePlanNullIsReturned() {
        testPlans.get(0).setIsActive(false);
        childPlanRepository.update(testPlans.get(0));
        ChildPlan activePlan = childPlanRepository.getActivePlan();

        assertThat(activePlan, nullValue());
    }
}
