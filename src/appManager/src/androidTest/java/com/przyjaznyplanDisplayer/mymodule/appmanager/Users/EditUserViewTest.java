package com.przyjaznyplanDisplayer.mymodule.appmanager.Users;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplan.repositories.UserRepository;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.TestUtils;

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
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditUserViewTest{

    @Rule
    public ActivityTestRule<EditUserView> activityRule = new ActivityTestRule<>(EditUserView.class, true, false);
    private User expectedUser;
    private User expectedNewUser;

    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        expectedUser = TestUtils.createUser("NAME", "SURNAME",
                TestUtils.createUserPerferences("/path/to/file", TypyWidokuAktywnosci.small, TypyWidokuCzynnosci.basic, TypyWidokuPlanuAktywnosci.slide));
        expectedNewUser = new User();
        expectedNewUser.setName("NEW_NAME");
        expectedNewUser.setSurname("NEW SURNAME");
        expectedNewUser.setPreferences(TestUtils.createUserPerferences("/path/to/file", TypyWidokuAktywnosci.small,
                TypyWidokuCzynnosci.basic, TypyWidokuPlanuAktywnosci.slide));
    }

    @Test
    public void createUserShowDefaultDataTest(){
        activityRule.launchActivity(new Intent());
        onView(withId(R.id.titleEditView)).check(matches(withText("TWORZENIE UŻYTKOWNIKA")));

        onView(withId(R.id.pathToTimer)).check(matches(withText("")));
        onView(withId(R.id.edit_imie)).check(matches(withText("Imię")));
        onView(withId(R.id.edit_nazwisko)).check(matches(withText("Nazwisko")));
        onView(withId(R.id.rb_bigView)).check(matches(isChecked()));
        onView(withId(R.id.rb_advancedView)).check(matches(isChecked()));
        onView(withId(R.id.e_rb_planListView)).check(matches(isChecked()));
    }

    @Test
    public void createUserTest(){
        activityRule.launchActivity(new Intent());

        onView(withId(R.id.edit_imie)).perform(clearText(), typeText(expectedNewUser.getName()));
        onView(withId(R.id.edit_nazwisko)).perform(clearText(), typeText(expectedNewUser.getSurname()));
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.rb_smallView)).perform(click());
        onView(withId(R.id.rb_basicView)).perform(click());
        onView(withId(R.id.e_rb_planSlideView)).perform(click());
        onView(withId(R.id.button13)).perform(click());

        List<User> allUsers = UserRepository.getAllUsers();

        User addedUser = null;

        for(User user : allUsers){
            if(user.getName().equals(expectedNewUser.getName()) && user.getSurname().equals(expectedNewUser.getSurname())){
                addedUser = user;
            }
        }

        assertNotEquals("New user should be added", null, addedUser);
        assertEquals("New user should have set activity view to small", TypyWidokuAktywnosci.small , addedUser.getPreferences().getTypyWidokuAktywnosci());
        assertEquals("New user should have set task view to basic", TypyWidokuCzynnosci.basic , addedUser.getPreferences().getTypWidokuCzynnosci());
        assertEquals("New user should have set view to slide", TypyWidokuPlanuAktywnosci.slide , addedUser.getPreferences().getTypWidokuPlanuAtywnosci());

    }

    @Test
    public void editUserShowUsersDataTest(){
        Intent intent = new Intent();
        intent.putExtra("user", expectedUser);
        activityRule.launchActivity(intent);

        onView(withId(R.id.titleEditView)).check(matches(withText("EDYCJA UŻYTKOWNIKA")));
        onView(withId(R.id.pathToTimer)).check(matches(withText(expectedUser.getPreferences().getTimerSoundPath())));
        onView(withId(R.id.edit_imie)).check(matches(withText(expectedUser.getName())));
        onView(withId(R.id.edit_nazwisko)).check(matches(withText(expectedUser.getSurname())));
        onView(withId(R.id.rb_smallView)).check(matches(isChecked()));
        onView(withId(R.id.rb_basicView)).check(matches(isChecked()));
        onView(withId(R.id.e_rb_planSlideView)).check(matches(isChecked()));
    }

    @Test
    public void editUserTest() throws InterruptedException {

        Intent intent = new Intent();
        intent.putExtra("user", expectedUser);
        activityRule.launchActivity(intent);

        onView(withId(R.id.rb_bigView)).perform(click());
        onView(withId(R.id.rb_advancedView)).perform(click());
        onView(withId(R.id.e_rb_planListView)).perform(click());
        onView(withId(R.id.button13)).perform(click());

        Espresso.pressBack();
        onView(withId(R.id.listView2)).check(matches(isDisplayed()));

        Thread.sleep(1500);

        List<User> allUsers = UserRepository.getAllUsers();

        User editedUser = null;

        for(User user : allUsers){
            if(user.getName().equals(expectedUser.getName()) && user.getSurname().equals(expectedUser.getSurname())){
                editedUser = user;
            }
        }

        assertNotEquals("New user should be added", null, editedUser);
        assertEquals("New user should have set activity view to big", TypyWidokuAktywnosci.big , editedUser.getPreferences().getTypyWidokuAktywnosci());
        assertEquals("New user should have set task view to advanced", TypyWidokuCzynnosci.advanced , editedUser.getPreferences().getTypWidokuCzynnosci());
        assertEquals("New user should have set view to list", TypyWidokuPlanuAktywnosci.list , editedUser.getPreferences().getTypWidokuPlanuAtywnosci());


    }

}
