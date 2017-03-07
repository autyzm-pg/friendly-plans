package pg.autyzm.friendly_plans;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ExampleTest {
    @Rule
    public ActivityTestRule<TaskCreateActivity> activityRule = new ActivityTestRule<>(TaskCreateActivity.class, true, true);

    @Test
    public void exampleTest() {
        onView(withId(R.id.id_task_create_description))
                .check(matches(withText(R.string.task_create_description)));
    }
}
