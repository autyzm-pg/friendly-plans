package pg.autyzm.friendly_plans.manager_app.view.child_list;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import database.entities.Child;
import database.repository.ChildRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.ActivityChildListBinding;
import pg.autyzm.friendly_plans.manager_app.view.main_screen.MainActivity;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class ChildListActivity extends AppCompatActivity implements ChildListActivityEvents {

    @Inject
    ChildRepository childRepository;

    @Inject
    ToastUserNotifier toastUserNotifier;

    ChildListData childData;
    private ChildRecyclerViewAdapter childListAdapter;
    private Integer selectedChildPosition;

    ChildRecyclerViewAdapter.ChildItemClickListener childItemClickListener =
            new ChildRecyclerViewAdapter.ChildItemClickListener() {

                @Override
                public void onChildItemClick(int position){
                    childListAdapter.setSelectedChildPosition(position);
                    selectedChildPosition = position;
                }

                @Override
                public void onRemoveChildClick(long itemId){
                    //TODO
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        setContentView(R.layout.activity_child_list);

        ActivityChildListBinding binding = DataBindingUtil
                .setContentView(this, R.layout.activity_child_list);
        binding.setChildListEvents(this);

        String initialFirstName = "";
        String initialLastName = "";

        childData = new ChildListData(initialFirstName, initialLastName);
        binding.setChildListData(childData);

        setUpViews();
    }

    private void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_child_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        childListAdapter = new ChildRecyclerViewAdapter(childItemClickListener);
        recyclerView.setAdapter(childListAdapter);
        childListAdapter.setChildItems(childRepository.getAll());

        Button setActiveChildButton = (Button) findViewById(R.id.id_set_active_child);
        setActiveChildButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setActiveChild();
                Intent intent = new Intent(ChildListActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
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

    private void setActiveChild(){
        List<Child> childList = childRepository.getAll();
        for (Child child : childList)
            if(child.getIsActive())
                child.setIsActive(false);
        childListAdapter.getChild(selectedChildPosition).setIsActive(true);
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
