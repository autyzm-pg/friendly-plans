package pg.autyzm.friendly_plans.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUserNotifier {
    AlertDialog alertDialog;

    public DialogUserNotifier(Context context, String title, String message, String buttonText){
        alertDialog = new AlertDialog.Builder(
                context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                buttonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    public void showDialog() {
        alertDialog.show();
    }
}