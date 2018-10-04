package pg.autyzm.friendly_plans.manager_app;


import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.Is.is;
import static pg.autyzm.friendly_plans.matcher.RecyclerViewMatcher.withRecyclerView;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;
import database.entities.Child;
import database.repository.ChildRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.resource.DaoSessionResource;
import pg.autyzm.friendly_plans.manager_app.view.child_list.ChildListActivity;

@RunWith(AndroidJUnit4.class)
public class ChildListActivityTest {

    private static final String EXPECTED_FIRST_NAME = "FIRST NAME";
    private static final String EXPECTED_LAST_NAME = "LAST NAME";

    @ClassRule
    public static DaoSessionResource daoSessionResource = new DaoSessionResource();

    @Rule
    public ActivityTestRule<ChildListActivity> activityRule = new ActivityTestRule<>(
            ChildListActivity.class, true, true);

    private ChildRepository childRepository;
    private List<Long> idsToDelete = new ArrayList<>();

    @Before
    public void setUp() {
        Context context = activityRule.getActivity().getApplicationContext();
        childRepository = new ChildRepository(daoSessionResource.getSession(context));
        final int numberOfChildren = 10;
        childRepository.deleteAll();
        for (int childNumber = 0; childNumber < numberOfChildren; childNumber++) {
            childRepository
                    .create(EXPECTED_FIRST_NAME, EXPECTED_LAST_NAME + childNumber);
        }
    }

    @Before
    public void unlockScreen() {
        final ChildListActivity activity = activityRule.getActivity();
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
            childRepository.delete(id);
        }
    }

    @Test
    public void whenChildListActivityExpectHeaderAndEmptyFields() {
        onView(withId(R.id.id_child_list_description))
                .check(matches(withText(R.string.child_list_description)));
        onView(withId(R.id.id_et_child_first_name))
                .check(matches(withText("")));
        onView(withId(R.id.id_et_child_last_name))
                .check(matches(withText("")));
    }

    @Test
    public void whenAddingNewChildExpectNewChildAddedToDB() {
        childRepository.deleteAll();
        onView(withId(R.id.id_et_child_first_name))
                .perform(replaceText(EXPECTED_FIRST_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.id_et_child_last_name))
                .perform(replaceText(EXPECTED_LAST_NAME));
        closeSoftKeyboard();
        onView(withId(R.id.id_add_child))
                .perform(click());

        List<Child> childTemplates = childRepository.getBySurname(EXPECTED_LAST_NAME);
        idsToDelete.add(childTemplates.get(0).getId());

        assertThat(childTemplates.size(), is(1));
        assertThat(childTemplates.get(0).getName(), is(EXPECTED_FIRST_NAME));
        assertThat(childTemplates.get(0).getSurname(), is(EXPECTED_LAST_NAME));
    }

    @Test
    public void whenChildIsAddedToDBExpectProperlyDisplayedOnRecyclerView() {

        final int testedChildPosition = 6;
        onView(withId(R.id.rv_child_list)).perform(scrollToPosition(testedChildPosition));
        onView(withRecyclerView(R.id.rv_child_list)
                .atPosition(testedChildPosition))
                .check(matches(hasDescendant(withText(EXPECTED_FIRST_NAME + " " + EXPECTED_LAST_NAME
                        + testedChildPosition))));
    }
}
