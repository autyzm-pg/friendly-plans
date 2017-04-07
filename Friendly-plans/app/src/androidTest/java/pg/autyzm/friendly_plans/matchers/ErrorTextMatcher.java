package pg.autyzm.friendly_plans.matchers;

import android.view.View;
import android.widget.EditText;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Use this custom matcher when you need match/assert <b>error text</b> on mandatory(*) EditText
 * fields in xml layout.
 */
public class ErrorTextMatcher {

    public static Matcher<View> hasErrorText(final String expectedErrorText) {
        return new TypeSafeMatcher<View>() {

            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof EditText)) {
                    return false;
                }

                CharSequence error = ((EditText) view).getError();
                if (error == null) {
                    return false;
                }

                String actualError = error.toString();
                return expectedErrorText.equals(actualError);
            }

            @Override
            public void describeTo(Description description) {
            }
        };
    }
}
