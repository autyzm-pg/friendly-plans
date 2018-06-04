package pg.autyzm.friendly_plans;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static pg.autyzm.friendly_plans.matcher.RecyclerViewMatcher.withRecyclerView;

import android.content.Intent;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import database.repository.PlanTemplateRepository;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.view.plan_list.PlanListActivity;

@RunWith(AndroidJUnit4.class)
public class PlanListActivityTest {

    private final int numberOfPlans = 10;

    private static final String expectedName = "TEST PLAN ";
    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();
    @Rule
    public ActivityTestRule<PlanListActivity> activityRule = new ActivityTestRule<>(
            PlanListActivity.class, true, true);

    @Before
    public void setUp() {
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

    @Test
    public void searchForASinglePlanExpectThatPlanAtFirstPosition() {
        final int testedPlanPosition = 5;

        onView(withId(R.id.et_search_plan_list)).perform(typeText(expectedName + testedPlanPosition));
        onView(withRecyclerView(R.id.rv_plan_list)
                .atPosition(0))
                .check(matches(hasDescendant(withText(expectedName
                        + testedPlanPosition))));
    }

    @Test
    public void searchForASinglePlanUsingOnlyOneCharacterxpectThatPlanAtFirstPosition() {
        final int testedPlanPosition = 5;

        onView(withId(R.id.et_search_plan_list)).perform(typeText(Integer.toString(testedPlanPosition)));
        onView(withRecyclerView(R.id.rv_plan_list)
                .atPosition(0))
                .check(matches(hasDescendant(withText(expectedName
                        + testedPlanPosition))));
    }

    @Test
    public void searchForEveryPlanExpectEveryPlanToAppear() {
        onView(withId(R.id.et_search_plan_list)).perform(typeText(String.valueOf(expectedName.charAt(0))));
        for (int planNumber = 0; planNumber < numberOfPlans; planNumber++) {
            onView(withId(R.id.rv_plan_list)).perform(scrollToPosition(planNumber));
            onView(withRecyclerView(R.id.rv_plan_list)
                    .atPosition(planNumber))
                    .check(matches(hasDescendant(withText(expectedName
                            + planNumber))));
        }
    }

    @Test
    public void whenPlanIsRemovedExpectPlanIsNotOnTheList() {
        final int testedTaskPosition = 3;
        onView(withId(R.id.rv_plan_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(testedTaskPosition,
                                clickChildViewWithId(R.id.id_remove_plan)));
        onView(withId(R.id.rv_plan_list)).perform(scrollToPosition(testedTaskPosition));
        onView(withRecyclerView(R.id.rv_plan_list)
                .atPosition(testedTaskPosition))
                .check(matches(not(hasDescendant(withText(expectedName
                        + testedTaskPosition)))));
    }

    @Test
    public void whenSearchedPlanIsRemovedExpectNoPlansInSearch(){
        final int testedPlanPosition = 5;

        onView(withId(R.id.et_search_plan_list)).perform(typeText(expectedName + testedPlanPosition));
        onView(withId(R.id.rv_plan_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0,
                                clickChildViewWithId(R.id.id_remove_plan)));
        onView(withRecyclerView(R.id.rv_plan_list)
                .atPosition(0))
                    .check(doesNotExist());
    }

    @Test
    public void whenSearchPlanIsRemovedExpectItToBeRemoved(){
        final int testedPlanPosition = 5;

        onView(withId(R.id.et_search_plan_list)).perform(typeText(expectedName + testedPlanPosition));
        onView(withId(R.id.rv_plan_list))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(0,
                                clickChildViewWithId(R.id.id_remove_plan)));
        onView(withId(R.id.et_search_plan_list)).perform(clearText());
        onView(withId(R.id.rv_plan_list)).perform(scrollToPosition(testedPlanPosition));
        onView(withRecyclerView(R.id.rv_plan_list)
                .atPosition(testedPlanPosition))
                    .check(matches(hasDescendant(withText(expectedName
                        + (testedPlanPosition + 1)))));
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }
}
