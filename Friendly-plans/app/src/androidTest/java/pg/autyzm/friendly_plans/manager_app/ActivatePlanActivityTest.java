package pg.autyzm.friendly_plans.manager_app;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.manager_app.view.activate_plan.ActivatePlanActivity;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pg.autyzm.friendly_plans.matcher.RecyclerViewMatcher.withRecyclerView;

@RunWith(AndroidJUnit4.class)
public class ActivatePlanActivityTest {

    private static final String expectedName = "TEST PLAN ";
    private final int numberOfPlans = 10;
    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();
    @Rule
    public ActivityTestRule<ActivatePlanActivity> activityRule = new ActivityTestRule<>(
            ActivatePlanActivity.class, true, true);

    PlanTemplateRepository planTemplateRepository;

    @Before
    public void setUp() {
        planTemplateRepository = new PlanTemplateRepository(
                daoSessionResource.getSession(activityRule.getActivity().getApplicationContext()));
        planTemplateRepository.deleteAll();
        for (int planNumber = 0; planNumber < numberOfPlans; planNumber++) {
            planTemplateRepository
                    .create(expectedName + planNumber);
        }
        activityRule.launchActivity(new Intent());
    }

    @Test
    public void whenActivatePlanActivityExpectDescriptionFieldAndActivateButton() {
        onView(withId(R.id.id_txt_activate_plan_description))
                .check(matches(withText(R.string.activate_plan_description)));
        onView(withId(R.id.id_btn_activate_plan))
                .check(matches(withText(R.string.activate_plan)));
    }

    @Test
    public void whenPlanIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {
        final int testedPlanPosition = 5;
        onView(withId(R.id.rv_activate_plan_plan_list)).perform(scrollToPosition(testedPlanPosition));
        onView(withRecyclerView(R.id.rv_activate_plan_plan_list)
                .atPosition(testedPlanPosition))
                .check(matches(hasDescendant(withText(expectedName
                        + testedPlanPosition))));
    }
}
