package pg.autyzm.friendly_plans.manager_app.view.activate_plan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import android.widget.TextView;
import database.entities.Child;
import database.entities.ChildPlan;
import database.repository.ChildPlanRepository;
import database.repository.ChildRepository;
import java.util.List;
import javax.inject.Inject;

import database.repository.PlanTemplateRepository;
import pg.autyzm.friendly_plans.App;
import pg.autyzm.friendly_plans.R;
import pg.autyzm.friendly_plans.manager_app.view.child_list.ChildListActivity;
import pg.autyzm.friendly_plans.manager_app.view.main_screen.MainActivity;

public class ActivatePlanActivity extends AppCompatActivity {


    @Inject
    ChildRepository childRepository;

    @Inject
    ChildPlanRepository childPlanRepository;

    ActivatePlanRecyclerViewAdapter planListAdapter;
    Button activateButton;
    protected List<ChildPlan> childPlanList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).getAppComponent().inject(this);
        if(childRepository.getByIsActive().size()>0){
            setContentView(R.layout.activate_plan);
            setUpViews();
        } else {
            setContentView(R.layout.activity_child_settings_alert);
            Button selectChild = (Button) findViewById(R.id.id_btn_child_settings_alert_choose_profile);
            selectChild.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(ActivatePlanActivity.this, ChildListActivity.class);
                    startActivity(intent);
                }
            });

        }

    }

    ActivatePlanRecyclerViewAdapter.PlanItemClickListener planItemClickListener =
            new ActivatePlanRecyclerViewAdapter.PlanItemClickListener() {

                @Override
                public void onPlanItemClick(int position) {
                    planListAdapter.setSelectedPlanPosition(position);
                    activateButton.setEnabled(true);
                }
            };

    private void setUpViews(){
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_activate_plan_plan_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        planListAdapter = new ActivatePlanRecyclerViewAdapter(planItemClickListener);
        recyclerView.setAdapter(planListAdapter);
        Child activeChild = childRepository.getByIsActive().get(0);
        childPlanList = childRepository.getChildPlans(activeChild.getId());
        planListAdapter.setPlanItems(childPlanList);
        TextView tv = (TextView) findViewById(R.id.id_txt_activate_plan_description);
        tv.setText(tv.getText()+" "+activeChild.getName()+" "+activeChild.getSurname());
        activateButton = (Button) findViewById(R.id.id_btn_activate_plan);
        activateButton.setEnabled(false);
        activateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (planListAdapter.getSelectedPlanPosition() != null) {
                    activatePlan();
                }
                Intent intent = new Intent(ActivatePlanActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void activatePlan(){
        Integer position = planListAdapter.getSelectedPlanPosition();
        childPlanRepository.setAllInactive(childPlanList);
        ChildPlan planToUpdate = planListAdapter.getPlanItem(position);
        planToUpdate.setIsActive(true);
        childPlanRepository.update(planToUpdate);
    }
}
