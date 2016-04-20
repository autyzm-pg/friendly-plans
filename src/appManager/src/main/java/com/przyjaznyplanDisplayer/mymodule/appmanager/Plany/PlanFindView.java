/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.mymodule.appmanager.Plany;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.dao.PlanDao;
import com.przyjaznyplan.dto.PlanDto;
import com.przyjaznyplan.models.Plan;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;
import java.util.List;

public class PlanFindView extends Activity implements AdapterView.OnItemClickListener {

    private ListView mainListView ;
    private PlanDao planDao;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.plan_find_view);
        try {
            mainListView = (ListView) findViewById(R.id.listView);
            planDao = new PlanDao(MySQLiteHelper.getDb());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        List<Plan> list = planDao.getPlanByTitle("");

        ArrayAdapter<Plan> adapter = new ArrayAdapter<Plan>(this,
                android.R.layout.simple_list_item_1, list);

        mainListView.setAdapter(adapter);

        mainListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        mode = (Integer)getIntent().getExtras().get("REQUESTCODE");
    }

    public void filterClick(View v){
        String name="";
        EditText etName = (EditText) findViewById(R.id.editText);
        try {
            name = etName.getText().toString();
        } catch (Exception e){
        }

        Toast.makeText(v.getContext(), "Searching for: " + name, Toast.LENGTH_LONG).show();

        List<Plan> list = planDao.getPlanByTitle(name);

        ArrayAdapter<Plan> adapter = new ArrayAdapter<Plan>(this,
                R.layout.simple_row_list_view, R.id.label, list);

        mainListView.setAdapter(adapter);

        mainListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private class DialogInterfaceOnClickHelper implements DialogInterface.OnClickListener{
        private Plan planToRemove;
        private Activity actvity;

        public void setActivityToRemoveId(Plan pl, Activity actvity){
            this.planToRemove = pl;
            this.actvity = actvity;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    PlanDto pdto = new PlanDto();
                    pdto.setPlan(planToRemove);
                    planDao.delete(pdto);
                    this.actvity.finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Plan item = (Plan) mainListView.getAdapter().getItem(i);
        if(mode== RequestCodes.PLAN_REMOVE){
            DialogInterfaceOnClickHelper dialogClickListener = new DialogInterfaceOnClickHelper();
            dialogClickListener.setActivityToRemoveId(item, this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Czy na pewno skasować plan \""+ item.getTitle() +"\" ?\n\n\n")
                    .setPositiveButton("Tak", dialogClickListener)
                    .setNegativeButton("Nie", dialogClickListener)
                    .show();
        } else{
            if( mode == RequestCodes.ADD_FROM_PLAN_SEARCH) {
                Intent intent = new Intent(this, PlanyDodajWybraneView.class);
                intent.putExtra("PLAN",item);
                startActivityForResult(intent, RequestCodes.ADD_FROM_PLAN);
            } else {
                Intent intent = new Intent(this, PlanEditView.class);
                intent.putExtra("PLAN",item);
                if(mode==RequestCodes.PLAN_EDIT){
                    startActivityForResult(intent, RequestCodes.PLAN_EDIT);
                }
                if(mode==RequestCodes.ACTIVITY_ADD_FROM_PATTERN){
                    setResult(mode, intent);
                    super.finish();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCodes.ADD_FROM_PLAN && resultCode== 1){
            if(data!= null && data.getExtras()!=null && data.getExtras().get("ACTIVITIES")!=null){
                Intent intent = new Intent();
                intent.putExtra("ACTIVITIES", (ArrayList<com.przyjaznyplan.models.Activity>)data.getExtras().get("ACTIVITIES"));
                intent.putExtra("BREAKACTIVITIES", (ArrayList<com.przyjaznyplan.models.Activity>)data.getExtras().get("BREAKACTIVITIES"));
                intent.putExtra("GAACTIVITIES", (ArrayList<com.przyjaznyplan.models.Activity>)data.getExtras().get("GAACTIVITIES"));
                setResult(requestCode, intent);
            }
        }
        super.finish();
    }
}
