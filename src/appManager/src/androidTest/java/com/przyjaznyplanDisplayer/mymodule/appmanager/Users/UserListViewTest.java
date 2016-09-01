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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserListViewTest {

    public static final int DEFAULT_USER = 1;
    @Rule
    public ActivityTestRule<UserListView> activityRule = new ActivityTestRule<>(UserListView.class, true, false);
    private List<User> expectedUsers;

    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        expectedUsers = TestUtils.createUsers(3, "NAME", "SURNAME",
                TestUtils.createUserPerferences("empty", TypyWidokuAktywnosci.small, TypyWidokuCzynnosci.basic, TypyWidokuPlanuAktywnosci.list));
    }

    @Test
    public void testUserListDisplay() {
        runActivity();

        ListView listView = (ListView) activityRule.getActivity().findViewById(R.id.listView2);

        UserAdapter userAdapter = (UserAdapter) listView.getAdapter();
        assertEquals("Users list should have two elements", userAdapter.getCount(), (expectedUsers.size() + DEFAULT_USER));

        for(User expectedUser : expectedUsers){
            User user = userAdapter.getItem(expectedUsers.indexOf(expectedUser) + DEFAULT_USER);
            assertEquals(expectedUser.getName(), user.getName());
            assertEquals(expectedUser.getSurname(), user.getSurname());
        }

    }

    private void runActivity() {
        activityRule.launchActivity(new Intent());
    }

}
