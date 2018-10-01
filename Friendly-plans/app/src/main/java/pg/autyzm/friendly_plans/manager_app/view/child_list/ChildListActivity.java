package pg.autyzm.friendly_plans.manager_app.view.child_list;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import database.repository.ChildRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.ActivityChildListBinding;
import pg.autyzm.friendly_plans.notifications.DialogUserNotifier;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class ChildListActivity extends AppCompatActivity implements ChildListActivityEvents {

    @Inject
    ChildRepository childRepository;

    @Inject
    ToastUserNotifier toastUserNotifier;

    ChildListData childData;

    private ChildRecyclerViewAdapter childListAdapter;

    ChildRecyclerViewAdapter.ChildItemClickListener childItemClickListener =
            new ChildRecyclerViewAdapter.ChildItemClickListener() {

                @Override
                public void onRemoveChildClick(final long childId) {
                    DialogUserNotifier dialog = new DialogUserNotifier(
                            ChildListActivity.this,
                            getResources().getString(R.string.child_removal_confirmation_title),
                            getResources().getString(R.string.child_removal_confirmation_message)
                    );
                    dialog.setPositiveButton(
                            getResources().getString(R.string.child_removal_confirmation_positive_button),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    removeChild(childId);
                                    dialog.dismiss();
                                }
                            });
                    dialog.setNegativeButton(
                            getResources().getString(R.string.child_removal_confirmation_negative_button),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.showDialog();
                }

                @Override
                public void onChildItemClick(int position) {
                    //todo
                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_child_list);

        setUpViews();

    }

    private void setUpViews() {
        ActivityChildListBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_child_list);
        binding.setChildListEvents(this);

        String initialFirstName = "";
        String initialLastName = "";

        childData = new ChildListData(initialFirstName, initialLastName);
        binding.setChildListData(childData);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_child_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        childListAdapter = new ChildRecyclerViewAdapter(childItemClickListener);
        recyclerView.setAdapter(childListAdapter);
        childListAdapter.setChildItems(childRepository.getAll());
    }

    @Override
    public Long saveChildData(ChildListData childListData) {
        Long childId = addChild(childListData.getFirstName(), childListData.getLastName());
        setUpViews();
        return childId;
    }

    private Long addChild(String firstName, String lastName) {
        try {
            long childId = childRepository.create(firstName, lastName);
            showToastMessage(R.string.child_saved_message);
            return childId;
        } catch (RuntimeException exception) {
            return handleSavingError(exception);
        }
    }

    private void removeChild(long itemId){
        childRepository.delete(itemId);
        childListAdapter.setChildItems(childRepository.getAll());
        toastUserNotifier.displayNotifications(
                R.string.child_removed_message,
                getApplicationContext());
    }

    @Nullable
    protected Long handleSavingError(RuntimeException exception) {
        Log.e("Child List View", "Error saving child", exception);
        showToastMessage(R.string.save_child_error_message);
        return null;
    }

    protected void showToastMessage(int resourceStringId) {
        toastUserNotifier.displayNotifications(
                resourceStringId, this);
    }
}
