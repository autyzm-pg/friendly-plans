/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer;


import java.util.List;

import com.example.przyjaznyplan.R;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.utils.BusinessLogic;
import com.przyjaznyplanDisplayer.Utils.GridCustomAdapter;
import com.przyjaznyplanDisplayer.Utils.PlanActivityAdapter;
import com.przyjaznyplan.dao.ActivityDao;
import com.przyjaznyplan.DbHelper.MySQLiteHelper;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityGalleryView  extends Activity implements OnItemClickListener{

    private ListView mainListView;
    private GridView grid;
    private GridCustomAdapter gridAdapter;
    private PlanActivityAdapter listAdapter;
    static final int ACTIVITY_CHOSEN = 1;
    static final int ACTIVITY_DONE = 2;
    private Boolean visited = false;
    ActivityDao activityDao;
    List<com.przyjaznyplan.models.Activity> list;
    MediaPlayer mp;
    int selectedActivity = -1;
    private Handler handler;
    private TextView timerText;
    private ImageView timerIcon;
    private RelativeLayout timeRelativeLayout;

    long then;
    com.przyjaznyplan.models.Activity longClickedActivity;
    boolean longpressed;

    private ExtendedRunnable timer = new ExtendedRunnable() {
        @Override
        public void run() {
            if(selectedActivity<0){
                timerStop(timer);
            }
            //aktualizacja czasu
            time-=1000;
            if(time<0){
                time=0;
            }
            if(time!=0) {
                list.get(selectedActivity).setTime((int) (time / 1000.0));

            }else if(time<=0){

                list.get(selectedActivity).setTime(0);
                timerText.setVisibility(View.INVISIBLE);
                play(Globals.GetUser().getPreferences().getTimerSoundPath(), true);

            }

            updateViewAfterTimerHit();


            if(time>0) {
                handler.postDelayed(timer, 1000);
            }else{
                list.get(selectedActivity).setTime(-1);
                timerText.setVisibility(View.INVISIBLE);
                updateViewAfterTimerHit();
                timerStop(timer);
            }
        }
    };

    private void timerStop(ExtendedRunnable timer) {
        if(handler!=null){
            timer.isRunning = false;
            handler.removeCallbacks(timer);
        }
    }

    /***
     * Timer will start if necessery
     * @param timer
     */
    private void timerStart(ExtendedRunnable timer){
        com.przyjaznyplan.models.Activity slide = null;
        if(timer.isRunning == true){
            return;
        }
        if((list!=null && list.size()>0) && (selectedActivity < list.size())) {

            slide = list.get(selectedActivity);

            if(slide != null && slide.getTime()>0 && selectedActivity>=0) {
                timer.setTime(slide.getTime()*1000);
                timer.isRunning = true;
                try{
                    MediaPlayer beepSound = MediaPlayer.create(this, R.raw.beep);
                    beepSound.start();
                }catch (Exception e){

                }
                if(handler == null) {
                    handler = new Handler();
                }
                handler.postDelayed(timer,1000);
            }

        }
        else{
            return;
        }

    }


    @Override
    protected void onPause() {
        timerStop(timer);
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.reset();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (visited) {
            finish();
        }
        visited = true;

        initListOfActivities();
    }

    @Override
    protected void onStop() {
        timerStop(timer);
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.reset();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.breaknormallayout);

        setTitle("Wybierz przerwę");

        grid = (GridView) findViewById(R.id.iconGridNormal);

        selectedActivity = -1;

        timerIcon=(ImageView)findViewById(R.id.basicview_czasButton);
        timerText=(TextView)findViewById(R.id.ad_v_timer);
        timerIcon.setVisibility(View.INVISIBLE);
        timerText.setVisibility(View.INVISIBLE);

        //
        //mainListView = (ListView) findViewById(R.id.listBreakTime);
        activityDao = new ActivityDao(MySQLiteHelper.getDb());
        //list = activityDAO.getAllActivities();

        grid.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    then = System.currentTimeMillis();
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    long now=System.currentTimeMillis();
                    if((now - then) > BusinessLogic.SYSTEM_CLICK_TIMEOUT && longpressed && longClickedActivity!=null ){
                        activityDao.setActivityAsUndone(BusinessLogic.SYSTEM_CURRENT_PLAN_ID,longClickedActivity.getNumber(), longClickedActivity.getTypeFlag());
                        longClickedActivity.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
                        gridAdapter.notifyDataSetChanged();
                        return true;
                    }
                    longpressed=false;
                    longClickedActivity=null;
                }
                return false;
            }
        });

        grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                longClickedActivity = (com.przyjaznyplan.models.Activity) grid.getAdapter().getItem(index);
                longpressed=true;
                Toast.makeText(v.getContext(),"One more sec!", Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }

    public void goToActivity (com.przyjaznyplan.models.Activity item,int position){
        timerStop(timer);
        if (mp != null && mp.isPlaying()) {
            mp.stop();
            mp.reset();
        }
        Intent intent = new Intent(this, BasicActivityView.class);
        User user = Globals.GetUser();

        if(user.getPreferences().getTypWidokuCzynnosci()== TypyWidokuCzynnosci.basic) {
            intent = new Intent(this, BasicActivityView.class);
        }else if(user.getPreferences().getTypWidokuCzynnosci()==TypyWidokuCzynnosci.advanced){
            intent= new Intent(this, AdvancedActivityView.class);
        }
        intent.putExtra("NAME", item.getTitle());
        intent.putExtra("ACTIVITY", item);

        startActivityForResult(intent, ACTIVITY_DONE);
    }

    public void onTimerLongClick (){
        com.przyjaznyplan.models.Activity ac = list.get(selectedActivity);
        if(ac.getTime() == -1){
            goToActivity(ac, 0);
            return;
        }
        if (ac.getTime() != 0) {
            if (timer.isRunning == true){
                timerStop(timer);
            }
            if (mp != null && mp.isPlaying()) {
                mp.stop();
                mp.reset();
            }
            ac.setTime(0);
            timerIcon.setVisibility(View.INVISIBLE);
            timerText.setVisibility(View.INVISIBLE);
            if(timeRelativeLayout == null)
                timeRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
            Display display = getWindowManager().getDefaultDisplay();
            Point screenSize = new Point(display.getWidth(), display.getHeight());
            timeRelativeLayout.measure(timeRelativeLayout.getMeasuredWidth(),timeRelativeLayout.getMeasuredHeight());
            grid.getLayoutParams().height = screenSize.y;
            gridAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        com.przyjaznyplan.models.Activity item = (com.przyjaznyplan.models.Activity) grid.getAdapter().getItem(position);
        if(selectedActivity>-1) {
            if(item.getSlides().size() == 0 && position == selectedActivity && item.getTime() == 0){
                goToActivity(item, position);
            }
            return;
        }
        if(item.getTime()>0){
            if(selectedActivity<0){
                Display display = getWindowManager().getDefaultDisplay();
                Point screenSize = new Point(display.getWidth(), display.getHeight());
                int ah = 0;
                if(timeRelativeLayout == null)
                    timeRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
                timeRelativeLayout.measure(timeRelativeLayout.getMeasuredWidth(),timeRelativeLayout.getMeasuredHeight());
                ah+=timeRelativeLayout.getLayoutParams().height;
                grid.getLayoutParams().height = screenSize.y-ah;
                selectedActivity = position;
                gridAdapter.selectedAc = selectedActivity;
                gridAdapter.notifyDataSetChanged();
                timerIcon.setVisibility(View.VISIBLE);
                timerText.setText(item.getTime()+" s");
                timerText.setVisibility(View.VISIBLE);
                timerIcon.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        onTimerLongClick();
                        return true;
                    }
                });
            }
        } else {
            if(item.getSlides().size() > 0){
                goToActivity(item, position);
            }
            if(selectedActivity<0){
                selectedActivity = position;
                gridAdapter.selectedAc = selectedActivity;
                gridAdapter.notifyDataSetChanged();
            }
        }
    }
    public void onTimerClick(View v){
        com.przyjaznyplan.models.Activity slide =list.get(selectedActivity);
        if(slide.getTime()==-1){
            if(mp!=null&&mp.isPlaying()){
                mp.stop();
                mp.reset();
            }
            timerIcon.setVisibility(View.INVISIBLE);
            slide.setTime(0);
            goToActivity(list.get(selectedActivity),selectedActivity);
        } else {
            timerStart(timer);
        }
    }

    public void updateViewAfterTimerHit(){
        timerText.setText(list.get(selectedActivity).getTime()+" s");
    }

    protected void initListOfActivities() {

        list = activityDao.getActivityGalleryFromPlan(BusinessLogic.SYSTEM_CURRENT_PLAN_ID, Globals.getNumberOfIconsInGridForUser());

        //listAdapter = new PlanActivityAdapter(this, R.layout.rowlistlayout, R.id.label, list);

        gridAdapter = new GridCustomAdapter(this, R.layout.gridiconlayout, R.id.label, list, selectedActivity);

        //mainListView.setAdapter(listAdapter);

        grid.setAdapter(gridAdapter);

        //mainListView.setOnItemClickListener((OnItemClickListener) this);

        grid.setOnItemClickListener((OnItemClickListener)this);

        try{
            (grid).setBackgroundColor(Color.WHITE);
            ((View)((((grid.getParent()))))).setBackgroundColor(Color.WHITE);
            Display display = getWindowManager().getDefaultDisplay();
            Point screenSize = new Point(display.getWidth(), display.getHeight());
            grid.measure(grid.getMeasuredWidth(), grid.getMeasuredHeight());
            grid.getLayoutParams().height = screenSize.y;
        }catch(Exception e){

        }
    }

    public void play(String path, boolean reapeatable){

        if(mp == null || ((mp!=null)&&!mp.isPlaying())) {

            Uri uri = Uri.parse(path);
            mp = new MediaPlayer();
            try {
                mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mp.setDataSource(getApplicationContext(), uri);
                mp.setLooping(reapeatable);
                mp.prepare();
                mp.start();


            }catch (Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(mp.isPlaying()){
            mp.stop();
            mp.reset();
            /*if(slides.get(currentPosition).getTime()==-1){
                slides.get(currentPosition).setTime(0);
            }*/
        }

    }

    public void playSound(View v) {
        String audioPath=(String)v.getTag();
        if(audioPath!=null && !audioPath.equals("")){
            try {
                play(audioPath,false);
            }catch(Exception e){

            }
        }
    }

    @Override
    public void finish(){
        activityDao.changeStatusOfChosenActivity();
        super.finish();
    }
}
