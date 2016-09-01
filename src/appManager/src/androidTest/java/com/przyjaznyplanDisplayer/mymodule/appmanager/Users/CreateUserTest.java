package com.przyjaznyplanDisplayer.mymodule.appmanager.Users;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.repositories.DatabaseUtils;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.TestUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class CreateUserTest {

    @Rule
    public ActivityTestRule<EditUserView> activityRule = new ActivityTestRule<>(EditUserView.class, true, false);
    private User expectedUser;

    @Before
    public void setUp() throws Exception {
        DatabaseUtils.rebuildDatabaseWithInitData();
        expectedUser = TestUtils.createUser("NAME", "SURNAME",
                TestUtils.createUserPerferences("/path/to/file", TypyWidokuAktywnosci.small, TypyWidokuCzynnosci.basic, TypyWidokuPlanuAktywnosci.slide));
    }

    @Test
    public void createUserTest(){
        activityRule.launchActivity(new Intent());

        TextView pathToTimer = (TextView) activityRule.getActivity().findViewById(R.id.pathToTimer);
        EditText name = (EditText) activityRule.getActivity().findViewById(R.id.edit_imie);
        EditText surname = (EditText) activityRule.getActivity().findViewById(R.id.edit_nazwisko);
        RadioButton bigView = (RadioButton) activityRule.getActivity().findViewById(R.id.rb_bigView);
        RadioButton advancedView = (RadioButton) activityRule.getActivity().findViewById(R.id.rb_advancedView);
        RadioButton planListView = (RadioButton) activityRule.getActivity().findViewById(R.id.e_rb_planListView);

        assertEquals("Path to timer should be empty", "",pathToTimer.getText().toString());
        assertEquals("Name should be set to default", "Imię", name.getText().toString());
        assertEquals("Surname should be set to default", "Nazwisko", surname.getText().toString());
        assertEquals("Big view should be set as default", true, bigView.isChecked());
        assertEquals("Advanced view should be set as default", true, advancedView.isChecked());
        assertEquals("Plan list view should be set as default", true, planListView.isChecked());
    }

    @Test
    public void editUserTest(){
        Intent intent = new Intent();
        intent.putExtra("user", expectedUser);
        activityRule.launchActivity(new Intent());

        TextView pathToTimer = (TextView) activityRule.getActivity().findViewById(R.id.pathToTimer);
        EditText name = (EditText) activityRule.getActivity().findViewById(R.id.edit_imie);
        EditText surname = (EditText) activityRule.getActivity().findViewById(R.id.edit_nazwisko);
        RadioButton smallView = (RadioButton) activityRule.getActivity().findViewById(R.id.rb_smallView);
        RadioButton basicView = (RadioButton) activityRule.getActivity().findViewById(R.id.rb_basicView);
        RadioButton planSlideView = (RadioButton) activityRule.getActivity().findViewById(R.id.e_rb_planSlideView);

        assertEquals("Path to timer should equal expected", expectedUser.getPreferences().getTimerSoundPath(),pathToTimer.getText().toString());
        assertEquals("Name should be set to expected", expectedUser.getName(), name.getText().toString());
        assertEquals("Surname should be set to expected", expectedUser.getSurname(), surname.getText().toString());
        assertEquals("Small view should be set", true, smallView.isChecked());
        assertEquals("Basic view should be set", true, basicView.isChecked());
        assertEquals("Plan slide view should be set", true, planSlideView.isChecked());
    }



}
