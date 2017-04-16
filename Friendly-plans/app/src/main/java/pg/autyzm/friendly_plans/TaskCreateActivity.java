package pg.autyzm.friendly_plans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class TaskCreateActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.task_management);
	}

	public void eventCreateTask(View view) {
		showSuccessMessage();
	}

	private void showSuccessMessage() {
		ToastUserNotifier.displayNotifications(R.string.task_saved_message, getApplicationContext());
	}
}
