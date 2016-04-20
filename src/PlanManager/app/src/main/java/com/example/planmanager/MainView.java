package com.example.planmanager;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import DAO.ChildActivityDao;
import model.ChildActivity;
import model.Slide;


public class MainView extends Activity implements AdapterView.OnItemClickListener {

    ListView activityListView;
    ListView slidesLisView;
    private ChildActivityDao activityDAO;
    List<ChildActivity> activityList;
    ChildActivity chosenActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_view);
        activityDAO = new ChildActivityDao(this);
        activityList = activityDAO.getAllActivities();
        activityListView = (ListView)findViewById(R.id.listView);
        slidesLisView = (ListView)findViewById(R.id.listView2);
        displayActivities();
        activityListView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
    }

    private void displayActivities(){
        List<String> values = new ArrayList<String>();
        for (ChildActivity a : activityList) {
            values.add(a.getTitle());
        }
        activityListView.setAdapter(new ArrayAdapter<String>(this, R.layout.rowlistlayout, R.id.label, values));
    }

    private void displaySlidesOfActivity(ChildActivity activity){
        List<String> values = new ArrayList<String>();
        if(activity.getSlides()==null || activity.getSlides().size()==0){
            Toast toast = Toast.makeText(this,"No slideds assigned to activity " + activity.getTitle(),Toast.LENGTH_SHORT);
            slidesLisView.setAdapter(new ArrayAdapter<String>(this, R.layout.rowlistlayout, R.id.label, values));
            toast.show();
            return;
        }
        for (Slide s : activity.getSlides()) {
            values.add("SlajdID:"+s.getId());
        }
        slidesLisView.setAdapter(new ArrayAdapter<String>(this, R.layout.rowlistlayout, R.id.label, values));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId() == R.id.listView){
            chosenActivity = activityList.get(position);
            displaySlidesOfActivity(chosenActivity);
        }

        if(parent.getId() == R.id.listView2){
            showSlideEditOption(chosenActivity.getSlides().get(position));
        }
    }

    private void showSlideEditOption(Slide chosenSlide) {

    }
}
