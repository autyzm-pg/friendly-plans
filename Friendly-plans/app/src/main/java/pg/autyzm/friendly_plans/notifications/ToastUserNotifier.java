package pg.autyzm.friendly_plans.notifications;

import android.content.Context;
import android.widget.Toast;

public class ToastUserNotifier {

    private ToastUserNotifier() {
    }

    public static void displayNotifications(int messageCode, Context context) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, messageCode, duration);

        toast.show();
    }
}
