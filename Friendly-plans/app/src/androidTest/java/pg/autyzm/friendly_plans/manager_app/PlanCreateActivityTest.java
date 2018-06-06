package pg.autyzm.friendly_plans.manager_app;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;

import android.content.Context;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;
import database.entities.PlanTemplate;
import database.repository.PlanTemplateRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.matcher.ToastMatcher;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.manager_app.view.plan_create.PlanCreateActivity;

@RunWith(AndroidJUnit4.class)
public class PlanCreateActivityTest {

    private static final String EXPECTED_NAME = "PLANS NAME";
    private static final String DUPLICATED_NAME = "DUPLICATED NAME";

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<PlanCreateActivity> activityRule = new ActivityTestRule<>(
            PlanCreateActivity.class, true, true);

    private PlanTemplateRepository planTemplateRepository;
    private List<Long> idsToDelete = new ArrayList<>();

    @Before
    public void setUp() {
        Context context = activityRule.getActivity().getApplicationContext();
        planTemplateRepository = new PlanTemplateRepository(daoSessionResource.getSession(context));

        long id = planTemplateRepository.create(DUPLICATED_NAME);
        idsToDelete.add(id);
    }

    @Before
    public void unlockScreen() {
        final PlanCreateActivity activity = activityRule.getActivity();
        Runnable wakeUpDevice = new Runnable() {
            public void run() {
                activity.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        };
        activity.runOnUiThread(wakeUpDevice);
    }

    @After
    public void tearDown() {
        for (Long id : idsToDelete) {
            planTemplateRepository.delete(id);
        }
    }

    @Test
    public void whenPlanCreateActivityExpectHeaderAndEmptyField() {
        onView(withId(R.id.id_plan_create_description))
                .check(matches(withText(R.string.plan_create_description)));
        onView(withId(R.id.id_et_plan_name))
                .check(matches(withText("")));
    }

    @Test
    public void whenAddingNewPlanExpectNewPlanAddedToDB() {
        onView(withId(R.id.id_et_plan_name))
                .perform(replaceText(EXPECTED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_plan_next))
                .perform(click());

        List<PlanTemplate> planTemplates = planTemplateRepository.get(EXPECTED_NAME);
        idsToDelete.add(planTemplates.get(0).getId());

        assertThat(planTemplates.size(), is(1));
        assertThat(planTemplates.get(0).getName(), is(EXPECTED_NAME));
    }

    @Test
    public void whenAddingNewPlanAndPlanAlreadyExistsExpectPlanNotAdded() {
        onView(withId(R.id.id_et_plan_name))
                .perform(replaceText(DUPLICATED_NAME));
        closeSoftKeyboard();

        onView(withId(R.id.id_btn_plan_next))
                .perform(click());

        closeSoftKeyboard();

        onView(withText(R.string.name_exist_msg)).inRoot(new ToastMatcher())
                .check(matches(isDisplayed()));

        List<PlanTemplate> planTemplates = planTemplateRepository.get(DUPLICATED_NAME);
        assertThat(planTemplates.size(), is(1));
    }
}
