/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer;


import android.app.Activity;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.przyjaznyplan.R;
import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.dao.ActivityDao;
import com.przyjaznyplan.models.Slide;
import com.przyjaznyplan.utils.BusinessLogic;
import com.przyjaznyplanDisplayer.Utils.OnTimerClickInterface;
import com.przyjaznyplanDisplayer.Utils.SlideAdvViewAdapter;


public class AdvancedActivityView extends Activity implements AdapterView.OnItemClickListener, OnTimerClickInterface {

    private SlideAdvViewAdapter adapter;
    private ListView mainList;
    private MediaPlayer player;
    private Handler mHandler;
    private ExtendedRunnable pausedTimer;
    private int currentPosition=0;

    private long then;
    private boolean longpressed=false;
    private Slide longClickedActivity;

    private ExtendedRunnable timer = new ExtendedRunnable() {
        @Override
        public void run() {
            if(currentPosition<0){
                throw new RuntimeException("NullPointerException: Obecna wskaznika slajdu jest błędna wynosi" + currentPosition);
            }
            //aktualizacja czasu
            time-=1000;
            if(time<0){
                time=0;
            }
            if(time!=0) {
                adapter.getItem(currentPosition).setTime((int)(time / 1000.0));

            }else if(time<=0){

                adapter.getItem(currentPosition).setTime(0);
                if(player!=null&&player.isPlaying()){
                    player.stop();
                    player.reset();
                }
                play(Globals.GetUser().getPreferences().getTimerSoundPath(), true);

            }

            adapter.notifyDataSetChanged();

            if(time>0) {
                mHandler.postDelayed(timer, 1000);
            }else{

                timerStop(timer);
                adapter.getItem(currentPosition).setTime(-1);
                adapter.notifyDataSetChanged();
            }
        }
    };

    private void timerStop(){
        if(mHandler!=null && timer!=null) {
            timer.isRunning = false;
            mHandler.removeCallbacks(timer);
        }
    }



    private void timerStart(ExtendedRunnable timer){
        Slide slide = null;
        if(timer.isRunning == true){
            return;
        }
        if(adapter!=null && adapter.getCount()>0 && currentPosition < adapter.getCount()) {
            slide = adapter.getItem(currentPosition);
            if(slide != null && slide.getTime()>0 && currentPosition>=0) {
                timer.setTime(slide.getTime()*1000);
                timer.isRunning = true;
                try{
                    MediaPlayer beepSound = MediaPlayer.create(this, R.raw.beep);
                    beepSound.start();
                }catch (Exception e){
                }
                if(mHandler == null) {
                    mHandler = new Handler();
                }
                mHandler.postDelayed(timer,1000);
            }

        }
        else{
            return;
        }



    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.advancedview);

        loadActivity((com.przyjaznyplan.models.Activity) getIntent().getExtras().get("ACTIVITY"));
        initView();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }

    private void initView() {
        mainList = (ListView)findViewById(R.id.adv_v_mainlistView);
        mainList.setOnItemClickListener(this);
        mainList.setAdapter(adapter);
        mainList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    then = System.currentTimeMillis();
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    long now=System.currentTimeMillis();
                    if((now - then) > BusinessLogic.SYSTEM_CLICK_TIMEOUT && longpressed && longClickedActivity!=null ){
                        if(longClickedActivity.getStatus()==Slide.SlideStatus.FINISHED.getValue()) {
                            int index = adapter.getPosition(longClickedActivity);
                            com.przyjaznyplan.models.Activity ak=(com.przyjaznyplan.models.Activity) getIntent().getExtras().get("ACTIVITY");
                            if(ak!=null){
                                adapter.changeChildActivity(longClickedActivity,ak.getSlides().get(index));
                            } else {
                                longClickedActivity.setStatus(Slide.SlideStatus.NEW.getValue());
                            }
                            setCurrentPosition();
                            adapter.notifyDataSetChanged();
                        } else {
                            if(longClickedActivity.getTime()>0){
                                if(timer.isRunning==true){
                                    timerStop(timer);
                                }
                                longClickedActivity.setTime(0);
                            }
                            longClickedActivity.setStatus(Slide.SlideStatus.FINISHED.getValue());
                            setCurrentPosition();
                            adapter.notifyDataSetChanged();
                        }
                        return true;
                    }
                    longpressed=false;
                    longClickedActivity=null;
                }
                return false;
            }
        });

        mainList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                longClickedActivity = (Slide) mainList.getAdapter().getItem(index);
                longpressed=true;
                //Toast.makeText(v.getContext(),"One more sec!", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        ((View)mainList.getParent()).setBackgroundColor(Color.WHITE);

    }


    private void loadActivity(com.przyjaznyplan.models.Activity activity) {
        if(activity.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
            super.finish();
        }
        if(activity.getSlides().size()!=0){
            adapter = new SlideAdvViewAdapter(this,Globals.getActivityViewForUser(), R.id.label,activity.getSlides(), currentPosition ,this);
        }else
        {
            ActivityDao activityDao=new ActivityDao(MySQLiteHelper.getDb());
            if (activity.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.ACTIVITY_GALLERY.toString())) {
                com.przyjaznyplan.models.Activity chosen = activityDao.getChosenChildActivity();
                if (chosen != null && chosen.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString())) {
                    activityDao.changeGAToActivity(chosen, BusinessLogic.SYSTEM_CURRENT_PLAN_ID,activity);
                } else {
                    //Toast.makeText(this, "chosen status mismatched", Toast.LENGTH_LONG).show();
                }
            }
            activityDao.setActivityAsDone(BusinessLogic.SYSTEM_CURRENT_PLAN_ID,activity.getNumber(), activity.getTypeFlag());
            finish();
        }

    }

    @Override
    protected void onResume() {
        if(mainList!=null) {
            ((View) mainList.getParent()).setBackgroundColor(Color.WHITE);
        }
        super.onResume();


    }

    public void onLongTimerClick() {
        Slide ac = adapter.getItem(currentPosition);
        if (ac.getTime() != 0) {
            if (timer.isRunning == true){
                timerStop(timer);
            }
            if (player != null && player.isPlaying()) {
                player.stop();
                player.reset();
            }
            ac.setTime(0);
            adapter.notifyDataSetChanged();
       }
    }



    public void play(String path, boolean reapeatable){

        if(player == null || ((player!=null)&&!player.isPlaying())) {
            Uri uri = Uri.parse(path);
            player = new MediaPlayer();
            try {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(getApplicationContext(), uri);
                player.setLooping(reapeatable);
                player.prepare();
                player.start();
            }catch (Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(player.isPlaying()){
            player.stop();
            player.reset();
            if(reapeatable == true){
                play(path, reapeatable);
                return;
            }
            if(adapter.getItem(currentPosition).getTime()==-1){
                adapter.getItem(currentPosition).setTime(0);
                adapter.notifyDataSetChanged();
            }
        }

    }

    public void onSoundTubeClick(View v){
        LinearLayout layout = (LinearLayout) v.getParent();
        Slide slide =adapter.getItem(mainList.getPositionForView(layout));
        int i = adapter.getPosition(slide);
        if(i!=currentPosition || slide.getTime()==-1){
            return;
        }
        String audioPath=(String)v.getTag();
        play(audioPath, false);
    }


    public void onTimerClick(View v){
        RelativeLayout layout = (RelativeLayout) v.getParent();
        Slide slide =adapter.getItem(mainList.getPositionForView(layout));
        int i = adapter.getPosition(slide);
        if(i==currentPosition){
            if(slide.getTime()==-1){
                if(player!=null&&player.isPlaying()){
                    player.stop();
                    player.reset();
                }
                adapter.getItem(currentPosition).setTime(0);
                adapter.notifyDataSetChanged();
                updatePosition(currentPosition);
            } else {
                timerStart(timer);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        timerStop();
        super.onPause();

    }

    private void timerStop(ExtendedRunnable timer) {
        if(mHandler!=null){
            timer.isRunning = false;
            mHandler.removeCallbacks(timer);
        }
    }

    public void setCurrentPosition(){
        Boolean someActivityIsUnDone = false;
        for(int i=0;i<adapter.getCount();i++){
            if(adapter.getItem(i).getStatus()!= Slide.SlideStatus.FINISHED.getValue()){
                currentPosition=i;
                someActivityIsUnDone = true;
                if(adapter!=null)
                    adapter.currentPosition = currentPosition;
                return;
            }
        }
        if(someActivityIsUnDone == false){
            currentPosition=adapter.getCount();
        }
    }

    private void updatePosition(int i){
        timerStop();
        if(i<currentPosition){
            //Toast.makeText(this,"Już wykonałeś tą czynność",Toast.LENGTH_LONG).show();
            return;
        }
        if(i>currentPosition){
            //Toast.makeText(this,"Wykonaj poprzednią czynność",Toast.LENGTH_LONG).show();
            return;
        }

        adapter.getItem(i).setStatus(Slide.SlideStatus.FINISHED.getValue());


        setCurrentPosition();

        adapter.notifyDataSetChanged();

        if(currentPosition==adapter.getCount()){
            //Toast.makeText(this,"Brawo!",Toast.LENGTH_LONG).show();
            clearFlags();
            finish();
        }
    }

    private void clearFlags() {

        com.przyjaznyplan.models.Activity activity=(com.przyjaznyplan.models.Activity) getIntent().getExtras().get("ACTIVITY");
        for(int i=0;i<adapter.getCount();i++){
            adapter.getItem(i).setStatus(0);

        }
        ActivityDao dao = new ActivityDao(MySQLiteHelper.getDb());

        if (activity.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.ACTIVITY_GALLERY.toString())) {
            com.przyjaznyplan.models.Activity chosen = dao.getChosenChildActivity();
            if (chosen != null && chosen.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString())) {
                dao.changeGAToActivity(chosen, BusinessLogic.SYSTEM_CURRENT_PLAN_ID,activity);
            } else {
               // Toast.makeText(this, "chosen status mismatched", Toast.LENGTH_LONG).show();
            }
            //firstly set number to activity gallery form temp activity gallery(sort by number limit 1)
            //after that - delete temp activity gallery(sort by number limit 1)
            //refresh plan activity

        }

        dao.setActivityAsDone(BusinessLogic.SYSTEM_CURRENT_PLAN_ID,activity.getNumber(), activity.getTypeFlag());



    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if(i<currentPosition){
            //Toast.makeText(this,"Już wykonałeś tą czynność",Toast.LENGTH_LONG).show();
            return;
        }
        if(i>currentPosition){
            //Toast.makeText(this,"Wykonaj poprzednią czynność",Toast.LENGTH_LONG).show();
            return;
        }
        if(adapter.getItem(currentPosition).getTime()==-1){
            adapter.getItem(currentPosition).setTime(0);
            adapter.notifyDataSetChanged();
        }
        if(adapter.getItem(currentPosition).getTime()==0){
            if(player!=null&&player.isPlaying()){
                player.stop();
                player.reset();
            }
            updatePosition(i);
        } else {
            if(timer.isRunning==false){
                timerStart(timer);
            } else {
                return;
            }
        }
    }

    @Override
    public void onLongTimerClickFromAdapter(View position) {
        if(mainList.getPositionForView(position) == currentPosition){
            onLongTimerClick();
            Toast.makeText(this,"Porusz w gore!", Toast.LENGTH_LONG).show();
        }
    }
}
