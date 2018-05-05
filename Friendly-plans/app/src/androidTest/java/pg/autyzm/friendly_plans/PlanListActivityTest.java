package pg.autyzm.friendly_plans;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pg.autyzm.friendly_plans.matcher.RecyclerViewMatcher.withRecyclerView;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import database.repository.PlanTemplateRepository;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.view.plan_list.PlanListActivity;

@RunWith(AndroidJUnit4.class)
public class PlanListActivityTest {

    private static final String expectedName = "TEST PLAN ";
    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();
    @Rule
    public ActivityTestRule<PlanListActivity> activityRule = new ActivityTestRule<>(
            PlanListActivity.class, true, true);

    @Before
    public void setUp() {
        final int numberOfPlans = 10;
        PlanTemplateRepository planTemplateRepository = new PlanTemplateRepository(
                daoSessionResource.getSession(activityRule.getActivity().getApplicationContext()));
        planTemplateRepository.deleteAll();
        for (int planNumber = 0; planNumber < numberOfPlans; planNumber++) {
            planTemplateRepository
                    .create(expectedName + planNumber);
        }
        activityRule.launchActivity(new Intent());
    }

    @Test
    public void whenPlanIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {
        final int testedPlanPosition = 5;
        onView(withId(R.id.rv_plan_list)).perform(scrollToPosition(testedPlanPosition));
        onView(withRecyclerView(R.id.rv_plan_list)
                .atPosition(testedPlanPosition))
                .check(matches(hasDescendant(withText(expectedName
                        + testedPlanPosition))));
    }
}
