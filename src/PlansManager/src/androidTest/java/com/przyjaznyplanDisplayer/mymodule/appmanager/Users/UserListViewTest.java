package com.przyjaznyplanDisplayer.mymodule.appmanager.Users;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.ListView;

import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.TestUtils;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.UserAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserListViewTest {

    public static final int DEFAULT_USER = 1;
    @Rule
    public ActivityTestRule<UserListView> activityRule = new ActivityTestRule<>(UserListView.class, true, false);
    private List<User> expectedUsers;
    private User expectedUser;

    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        expectedUsers = TestUtils.createUsers(3, "NAME", "SURNAME",
                TestUtils.createUserPreferences("empty", TypyWidokuAktywnosci.small, TypyWidokuCzynnosci.basic, TypyWidokuPlanuAktywnosci.list));
        expectedUser = expectedUsers.get(0);
    }

    @Test
    public void testUserListDisplay() {
        runActivity();

        ListView listView = (ListView) activityRule.getActivity().findViewById(R.id.usersListView);

        UserAdapter userAdapter = (UserAdapter) listView.getAdapter();
        assertEquals("Users list should have four elements", userAdapter.getCount(), (expectedUsers.size() + DEFAULT_USER));

        for(User expectedUser : expectedUsers){
            User user = userAdapter.getItem(expectedUsers.indexOf(expectedUser) + DEFAULT_USER);
            assertEquals(expectedUser.getName(), user.getName());
            assertEquals(expectedUser.getSurname(), user.getSurname());
        }

    }

    @Test
    public void testFilterUsersBySurname() {
        runActivity();

        onView(withId(R.id.searchUserInput)).perform(clearText(), typeText(expectedUser.getSurname()));
        ListView listView = (ListView) activityRule.getActivity().findViewById(R.id.usersListView);

        UserAdapter userAdapter = (UserAdapter) listView.getAdapter();
        assertEquals("Users list should have one elements", userAdapter.getCount(), 1);

        User filteredUser = userAdapter.getItem(0);
        assertEquals("Filtered user should have expected surname", expectedUser.getSurname(), filteredUser.getSurname());

    }

    @Test
    public void testFilterUsersByName() {
        runActivity();

        onView(withId(R.id.searchUserInput)).perform(clearText(), typeText(expectedUser.getName()));
        ListView listView = (ListView) activityRule.getActivity().findViewById(R.id.usersListView);

        UserAdapter userAdapter = (UserAdapter) listView.getAdapter();
        assertEquals("Users list should have one elements", userAdapter.getCount(), 1);

        User filteredUser = userAdapter.getItem(0);
        assertEquals("Filtered user should have expected name", expectedUser.getName(), filteredUser.getName());

    }

    private void runActivity() {
        activityRule.launchActivity(new Intent());
    }

    @After
    public void tearDown() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
    }

}
