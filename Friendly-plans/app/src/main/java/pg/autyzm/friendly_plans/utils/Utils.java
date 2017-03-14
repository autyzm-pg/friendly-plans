package pg.autyzm.friendly_plans.utils;


import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class Utils {

    public static void markFieldtMandatory(TextView tv) {
        String text = tv.getText() + " * ";
        Spannable WordtoSpan = new SpannableString(text);
        WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), text.indexOf('*'),
                text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(WordtoSpan);
    }

}
