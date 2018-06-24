package pg.autyzm.friendly_plans.manager_app.view.child_list;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import database.repository.ChildRepository;
import javax.inject.Inject;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.databinding.ActivityChildListBinding;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;

public class ChildListActivity extends AppCompatActivity implements ChildListActivityEvents {

    @Inject
    ChildRepository childRepository;

    @Inject
    ToastUserNotifier toastUserNotifier;

    ChildRecyclerViewAdapter childListAdapter;

    ChildListData childData;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.plan_list_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                childListAdapter.setChildItems(childRepository.getFilteredByFullName(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                childListAdapter.setChildItems(childRepository.getFilteredByFullName(newText));
                return false;
            }
        });

        return true;
    }

    private void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_child_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        childListAdapter = new ChildRecyclerViewAdapter();
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
