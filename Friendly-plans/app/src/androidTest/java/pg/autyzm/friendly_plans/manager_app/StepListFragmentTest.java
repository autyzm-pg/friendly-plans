package pg.autyzm.friendly_plans.manager_app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.resource.TaskTemplateRule;
import pg.autyzm.friendly_plans.manager_app.view.step_list.StepListFragment;
import pg.autyzm.friendly_plans.manager_app.view.task_create.TaskCreateActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeLeft;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.assertion.ViewAssertions.selectedDescendantsMatch;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static pg.autyzm.friendly_plans.matcher.RecyclerViewMatcher.withRecyclerView;

@RunWith(AndroidJUnit4.class)
public class StepListFragmentTest {

    private static final String STEP_NAME_1 = "STEP_NAME_1";
    private static final String STEP_NAME_2 = "STEP_NAME_2";
    private static final String TASK_NAME_1 = "TASK_NAME_1";
    private static final String TASK_ID = "TASK_ID";

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<TaskCreateActivity> activityRule = new ActivityTestRule<>(
            TaskCreateActivity.class, true, true);

    @Rule
    public TaskTemplateRule taskTemplateRule = new TaskTemplateRule(daoSessionResource,
            activityRule);

    @Before
    public void setUp() {
        long taskId = taskTemplateRule.createTaskWithSteps(TASK_NAME_1, STEP_NAME_1, STEP_NAME_2);
        openStepsListFragment(taskId);
    }

    @Test
    public void whenTaskHasStepsExpectStepsToBeDisplayed() {
        assertStepDisplayed(0, STEP_NAME_1);
        assertStepDisplayed(1, STEP_NAME_2);
    }

    @Test
    public void whenTaskHasNoStepsExpectNoStepsToBeDisplayed() {
        long taskId = taskTemplateRule.createTask(TASK_NAME_1);
        openStepsListFragment(taskId);

        onView(withId(R.id.rv_step_list)).check(matches(withSize(0)));
    }

    @Test
    public void whenSaveClickedTaskCreateActivityShouldBeDisplayed() {
        onView(withId(R.id.id_btn_next))
            .perform(click());

        onView(withId(R.id.id_btn_steps)).check(matches(isDisplayed()));
    }

    @Test
    public void whenStepIsRemovedExpectStepIsNotOnTheList() {
        onView(withId(R.id.rv_step_list)).perform(
                RecyclerViewActions
                        .actionOnItemAtPosition(0, clickChildViewWithId(R.id.id_remove_step)));
        onView(withText(R.string.step_removal_confirmation_positive_button)).perform(click());

        assertStepDisplayed(0, STEP_NAME_2);
    }

    @Test
    public void whenStepRemoveIconIsClickedButNoConfirmationGivenExpectStepIsOnTheList() {
        onView(withId(R.id.rv_step_list)).perform(
                RecyclerViewActions
                        .actionOnItemAtPosition(0, clickChildViewWithId(R.id.id_remove_step)));
        onView(withText(R.string.step_removal_confirmation_negative_button)).perform(click());

        assertStepDisplayed(0, STEP_NAME_1);
    }

    @Test
    public void whenStepIsRemovedBySwipeExpectStepIsNotOnTheList() {
        onView(withId(R.id.rv_step_list)).perform(
                RecyclerViewActions
                        .actionOnItemAtPosition(0, swipeLeft()));
        onView(withText(R.string.step_removal_confirmation_positive_button)).perform(click());

        assertStepDisplayed(0, STEP_NAME_2);
    }

    @Test
    public void whenStepIsClickedEditStepIsOpened() {
        onView(withId(R.id.rv_step_list)).perform(
                RecyclerViewActions
                        .actionOnItemAtPosition(0, click()));

        onView(withId(R.id.id_et_step_name)).check(matches(withText("STEP_NAME_1")));
    }

     private void openStepsListFragment(long taskId) {
        StepListFragment fragment = new StepListFragment();
        Bundle args = new Bundle();
        args.putLong(TASK_ID, taskId);
        fragment.setArguments(args);

        FragmentManager manager = activityRule.getActivity().getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.task_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void assertStepDisplayed(int position, String stepName) {
        onView(withRecyclerView(R.id.rv_step_list)
                .atPosition(position))
                .check(selectedDescendantsMatch(withId(R.id.id_tv_step_name),
                        withText(stepName)));
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

    static Matcher<View> withSize(final int expectedSize) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(final View view) {
                final int listSize = ((RecyclerView) view).getAdapter().getItemCount();
                return listSize == expectedSize;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("RecyclerView should have " + expectedSize + " items");
            }
        };
    }
}
