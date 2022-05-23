package pg.autyzm.friendly_plans.manager_app.view.plan_list;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import database.repository.PlanTemplateRepository;
import javax.inject.Inject;

import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.notifications.DialogUserNotifier;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.manager_app.view.plan_create.PlanCreateActivity;

public class PlanListActivity extends AppCompatActivity {

    @Inject
    PlanTemplateRepository planTemplateRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;

    SearchView searchView;

    private PlanRecyclerViewAdapter planListAdapter;

    PlanRecyclerViewAdapter.PlanItemClickListener planItemClickListener =
            new PlanRecyclerViewAdapter.PlanItemClickListener() {

                @Override
                public void onRemovePlanClick(final long planId) {
                    DialogUserNotifier dialog = new DialogUserNotifier(
                            PlanListActivity.this,
                            getResources().getString(R.string.plan_removal_confirmation_title),
                            getResources().getString(R.string.plan_removal_confirmation_message)
                    );
                    dialog.setPositiveButton(
                            getResources().getString(R.string.plan_removal_confirmation_positive_button),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    removePlan(planId);
                                    dialog.dismiss();
                                }
                            });
                    dialog.setNegativeButton(
                            getResources().getString(R.string.plan_removal_confirmation_negative_button),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    dialog.showDialog();
                }

                @Override
                public void onPlanItemClick(int position) {
                    Bundle bundle = new Bundle();
                    bundle.putLong(ActivityProperties.PLAN_ID,
                    planListAdapter.getPlanItem(position).getId());

                    Intent intent = new Intent(PlanListActivity.this, PlanCreateActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);

        setContentView(R.layout.activity_plan_list);
        setUpViews();
        planListAdapter.setPlanItems(planTemplateRepository.getAll());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.plan_list_menu, menu);
        MenuItem searchViewItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchViewItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                refreshList(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                refreshList(newText);
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        planListAdapter.setPlanItems(planTemplateRepository.getAll());
    }

    private void setUpViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_plan_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        planListAdapter = new PlanRecyclerViewAdapter(planItemClickListener);
        recyclerView.setAdapter(planListAdapter);
    }

    private void removePlan(long itemId){
        planTemplateRepository.delete(itemId);
        refreshList(searchView.getQuery().toString());
        toastUserNotifier.displayNotifications(
                R.string.plan_removed_message,
                getApplicationContext());
    }

    private void refreshList(String searchedValue) {
        planListAdapter.setPlanItems(planTemplateRepository.getFilteredByName(searchedValue));
    }
}
