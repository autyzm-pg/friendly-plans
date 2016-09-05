package com.przyjaznyplanDisplayer.mymodule.appmanager.Users;

import android.content.Intent;
import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplan.repositories.UserRepository;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.TestUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditUserViewTest {

    @Rule
    public ActivityTestRule<EditUserView> activityRule = new ActivityTestRule<>(EditUserView.class, true, false);
    private User expectedUser;
    private User expectedNewUser;
    private final String expectedTimeoutPath = "/path/to/file";

    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        expectedUser = TestUtils.createUser("NAME", "SURNAME",
                TestUtils.createUserPreferences(expectedTimeoutPath, TypyWidokuAktywnosci.small, TypyWidokuCzynnosci.basic, TypyWidokuPlanuAktywnosci.slide));
        expectedNewUser = new User();
        expectedNewUser.setName("NEW_NAME");
        expectedNewUser.setSurname("NEW_SURNAME");
        expectedNewUser.setPreferences(TestUtils.createUserPreferences(expectedTimeoutPath, TypyWidokuAktywnosci.small,
                TypyWidokuCzynnosci.basic, TypyWidokuPlanuAktywnosci.slide));

    }

    @Test
    public void createUserShowDefaultDataTest() {
        activityRule.launchActivity(new Intent());
        Resources resource = activityRule.getActivity().getResources();

        onView(withId(R.id.titleEditView)).check(matches(withText(
                resource.getString(R.string.create_user_header))));

        onView(withId(R.id.pathToTimer)).check(matches(withText(resource.getString(R.string.default_empty_timer_path))));
        onView(withId(R.id.editName)).check(matches(withText(resource.getString(R.string.default_user_name))));
        onView(withId(R.id.editSurname)).check(matches(withText(resource.getString(R.string.default_user_surname))));
        onView(withId(R.id.bigPlanActivityTypeRadioButton)).check(matches(isChecked()));
        onView(withId(R.id.advancedPlanActionTypeRadioButton)).check(matches(isChecked()));
        onView(withId(R.id.listPlanTypeRadioButton)).check(matches(isChecked()));
    }

    @Test
    public void createUserTest() {
        activityRule.launchActivity(new Intent());

        onView(withId(R.id.editName)).perform(clearText(), typeText(expectedNewUser.getName()));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.editSurname)).perform(clearText(), typeText(expectedNewUser.getSurname()));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.smallPlanActivityTypeRadioButton)).perform(click());
        onView(withId(R.id.basicPlanActionTypeRadioButton)).perform(click());
        onView(withId(R.id.slidePlanTypeRadioButton)).perform(click());
        onView(withId(R.id.saveUserButton)).perform(click());

        List<User> allUsers = UserRepository.getAllUsers();

        User addedUser = null;

        for (User user : allUsers) {
            if (user.getName().equals(expectedNewUser.getName()) && user.getSurname().equals(expectedNewUser.getSurname())) {
                addedUser = user;
            }
        }

        assertNotEquals("New user should be added", null, addedUser);
        assertEquals("New user should have set activity view to small", TypyWidokuAktywnosci.small, addedUser.getPreferences().getTypyWidokuAktywnosci());
        assertEquals("New user should have set task view to basic", TypyWidokuCzynnosci.basic, addedUser.getPreferences().getTypWidokuCzynnosci());
        assertEquals("New user should have set view to slide", TypyWidokuPlanuAktywnosci.slide, addedUser.getPreferences().getTypWidokuPlanuAtywnosci());

    }

    @Test
    public void editUserShowUsersDataTest() {
        Intent intent = new Intent();
        intent.putExtra("user", expectedUser);
        activityRule.launchActivity(intent);

        onView(withId(R.id.titleEditView)).check(matches(withText(
                activityRule.getActivity().getResources().getString(R.string.edit_user_header))));
        onView(withId(R.id.pathToTimer)).check(matches(withText(expectedUser.getPreferences().getTimerSoundPath())));
        onView(withId(R.id.editName)).check(matches(withText(expectedUser.getName())));
        onView(withId(R.id.editSurname)).check(matches(withText(expectedUser.getSurname())));
        onView(withId(R.id.smallPlanActivityTypeRadioButton)).check(matches(isChecked()));
        onView(withId(R.id.basicPlanActionTypeRadioButton)).check(matches(isChecked()));
        onView(withId(R.id.slidePlanTypeRadioButton)).check(matches(isChecked()));
    }

    @Test
    public void editUserTest() throws InterruptedException {

        Intent intent = new Intent();
        intent.putExtra("user", expectedUser);
        activityRule.launchActivity(intent);

        onView(withId(R.id.bigPlanActivityTypeRadioButton)).perform(click());
        onView(withId(R.id.advancedPlanActionTypeRadioButton)).perform(click());
        onView(withId(R.id.listPlanTypeRadioButton)).perform(click());
        onView(withId(R.id.saveUserButton)).perform(click());

        List<User> allUsers = UserRepository.getAllUsers();

        User editedUser = null;

        for (User user : allUsers) {
            if (user.getName().equals(expectedUser.getName()) && user.getSurname().equals(expectedUser.getSurname())) {
                editedUser = user;
            }
        }

        assertNotEquals("New user should be added", null, editedUser);
        assertEquals("New user should have set activity view to big", TypyWidokuAktywnosci.big, editedUser.getPreferences().getTypyWidokuAktywnosci());
        assertEquals("New user should have set task view to advanced", TypyWidokuCzynnosci.advanced, editedUser.getPreferences().getTypWidokuCzynnosci());
        assertEquals("New user should have set view to list", TypyWidokuPlanuAktywnosci.list, editedUser.getPreferences().getTypWidokuPlanuAtywnosci());


    }

    @After
    public void tearDown() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
    }


}
