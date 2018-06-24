package pg.autyzm.friendly_plans.manager_app.validation;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public final class Utils {

    private static final String ASTERISK = " * ";

    public static void markFieldMandatory(TextView tv) {
        String text = tv.getText() + ASTERISK;
        Spannable wordToSpan = new SpannableString(text);
        wordToSpan.setSpan(new ForegroundColorSpan(Color.RED), text.indexOf('*'),
                text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(wordToSpan);
    }

    private Utils() {

    }
}
