package pg.autyzm.friendly_plans.view.plan_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import database.repository.PlanTemplateRepository;
import javax.inject.Inject;

import pg.autyzm.friendly_plans.ActivityProperties;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.notifications.ToastUserNotifier;
import pg.autyzm.friendly_plans.view.plan_create.PlanCreateActivity;

public class PlanListActivity extends AppCompatActivity {

    @Inject
    PlanTemplateRepository planTemplateRepository;
    @Inject
    ToastUserNotifier toastUserNotifier;

    EditText searchField;

    private PlanRecyclerViewAdapter planListAdapter;

    PlanRecyclerViewAdapter.PlanItemClickListener planItemClickListener =
            new PlanRecyclerViewAdapter.PlanItemClickListener() {

                @Override
                public void onRemovePlanClick(long itemId) {
                    planTemplateRepository.delete(itemId);
                    searchField.setText(searchField.getText());
                    toastUserNotifier.displayNotifications(
                            R.string.plan_removed_message,
                            getApplicationContext());
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

        searchField = (EditText) findViewById(R.id.et_search_plan_list);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                planListAdapter.setPlanItems(planTemplateRepository.getFilteredByName(s.toString()));
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
                // TextWatcher method
            }
            @Override
            public void afterTextChanged(Editable s) {
                // TextWatcher method
            }
        });
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
}
