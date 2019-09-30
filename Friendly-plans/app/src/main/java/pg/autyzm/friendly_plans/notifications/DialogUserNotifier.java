package pg.autyzm.friendly_plans.notifications;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUserNotifier {
    AlertDialog alertDialog;

    public DialogUserNotifier(Context context, String title, String message){
        alertDialog = new AlertDialog.Builder(
                context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
    }

    public void setPositiveButton(String buttonText, DialogInterface.OnClickListener listener){
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonText, listener);
    }

    public void setNegativeButton(String buttonText, DialogInterface.OnClickListener listener){
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, buttonText, listener);
    }

    public void showDialog() {
        alertDialog.show();
    }
}