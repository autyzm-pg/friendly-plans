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
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.utils.BusinessLogic;
import com.przyjaznyplanDisplayer.Utils.OnTimerClickInterface;
import com.przyjaznyplanDisplayer.Utils.PlanActivityAdapter;
import com.przyjaznyplan.dao.ActivityDao;
import com.przyjaznyplan.DbHelper.MySQLiteHelper;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class PlanActivityView  extends Activity implements OnItemClickListener, OnTimerClickInterface {
	
	static final int ACTIVITY_CHOSEN = 1;
	static final int ACTIVITY_DONE = 2;
	static int LAST_ACTIVITY = 0;
	private ListView mainListView ;
    private Button breakButton;
	private PlanActivityAdapter listAdapter ;
    private int currentActivity = 0;
    private long then;
    private boolean longpressed=false;
    private com.przyjaznyplan.models.Activity longClickedActivity;
    Parcelable state;
    private MediaPlayer mp;
    private MySQLiteHelper helper;
    ActivityDao activityDao;
    List<com.przyjaznyplan.models.Activity> list;
    private boolean breakButtonEnabled = false;
    private RelativeLayout planRelativeLayout;

    private TypyWidokuPlanuAktywnosci typ;

    //for basic view
    private com.przyjaznyplan.models.Activity activity;
    private int currentPosition;
    private Handler handler;
    private boolean dataInitialized =false;
    private ImageButton buttonToNextSlide;
    private ImageButton buttonToPrevSlide;
    private ImageView image;
    private ImageButton soundTube;
    private ImageView timerIcon;
    private TextView timerText;
    private RelativeLayout time_str;

    private EditText slide_id_editText;
    private TextView description;
    private boolean timerSuspended=false;
    //end for basic view

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
                list.get(currentPosition).setTime((int) (time / 1000.0));

            }else if(time<=0){

                list.get(currentPosition).setTime(0);
                play(Globals.GetUser().getPreferences().getTimerSoundPath(), true);

            }

            updateViewAfterTimerHit();


            if(time>0) {
                handler.postDelayed(timer, 1000);
            }else{
                list.get(currentPosition).setTime(-1);
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
        if((list!=null && list.size()>0) && (currentPosition < list.size())) {

            slide = list.get(currentPosition);

            if(slide != null && slide.getTime()>0 && currentPosition>=0) {
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

    private void timerResume(ExtendedRunnable timer){
        handler.postDelayed(timer,1000);
    }


    @Override
    protected void onPause() {
        timerStop(timer);
        if(mp!=null&&mp.isPlaying()){
            mp.stop();
            mp.reset();
        }
        super.onPause();
        if(mainListView!=null){
            state = mainListView.onSaveInstanceState();
        }
    }

    public void onTimerClick(View v){
        com.przyjaznyplan.models.Activity slide;
        if(typ==TypyWidokuPlanuAktywnosci.slide) {
            slide = list.get(currentPosition);
        } else {
            RelativeLayout layout = (RelativeLayout) v.getParent();
            int position = mainListView.getPositionForView(layout);
            if(position!=currentPosition){
                return;
            }
            slide = listAdapter.getItem(position);
        }
        if(slide.getSlides().size()>0){
            goToActivity(slide,currentPosition);
            return;
        }
        if(slide.getTime()==-1){
            if(mp!=null&&mp.isPlaying()){
                mp.stop();
                mp.reset();
                if(typ!=TypyWidokuPlanuAktywnosci.slide) {
                    setActivityAsDone(slide);
                    listAdapter.notifyDataSetChanged();
                }
            }
            if(typ==TypyWidokuPlanuAktywnosci.slide) {
                timerIcon.setVisibility(View.INVISIBLE);
            }
            slide.setTime(0);
            updateViewAfterTimerHit();
        } else {
            timerStart(timer);
        }
    }


    private void updateViewAfterTimerHit() {
        if(typ==TypyWidokuPlanuAktywnosci.slide){
            updateArrowsView();
        } else {
            listAdapter.notifyDataSetChanged();
        }
    }

    private void updateArrowsView() {
        com.przyjaznyplan.models.Activity currentSlide = list.get(currentPosition);
        //STRZALKI

        buttonToNextSlide.setImageResource(R.drawable.garrow);
        if(currentSlide.getTime()== 0){
            timerIcon.setVisibility(View.INVISIBLE);
        } else {
            timerIcon.setVisibility(View.VISIBLE);
        }
        if(currentSlide.getTime()<=0){
            timerText.setVisibility(View.INVISIBLE);
            timerText.setText("");
            timerText.setVisibility(View.VISIBLE);
            timerText.setVisibility(View.INVISIBLE);
        }else{
            timerText.setVisibility(View.INVISIBLE);
            timerText.setTextColor(Color.BLACK);
            timerText.setText(currentSlide.getTime()+" s");
            timerText.setVisibility(View.VISIBLE);
        }
        if(currentSlide.getTime() < 0){
            buttonToNextSlide.setVisibility(View.INVISIBLE);
            buttonToPrevSlide.setVisibility(View.INVISIBLE);
            return;
        }
        if(currentSlide.getTime() > 0){
            buttonToNextSlide.setVisibility(View.INVISIBLE);
            buttonToPrevSlide.setVisibility(View.INVISIBLE);
            return;
        }
        if (currentPosition == 0 && currentSlide.getTime() > 0) {
            buttonToNextSlide.setVisibility(View.INVISIBLE);
            buttonToPrevSlide.setVisibility(View.INVISIBLE);
        }

        if (currentPosition == 0 && currentSlide.getTime() == 0) {
            buttonToNextSlide.setVisibility(View.VISIBLE);
            buttonToPrevSlide.setVisibility(View.INVISIBLE);
        }

        if (currentPosition > 0 && currentPosition < list.size() && currentSlide.getTime() == 0) {
            buttonToNextSlide.setVisibility(View.VISIBLE);
            buttonToPrevSlide.setVisibility(View.VISIBLE);
        }

        if (currentPosition == list.size() - 1 && currentSlide.getTime() == 0) {
            buttonToNextSlide.setVisibility(View.VISIBLE);
            buttonToPrevSlide.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mainListView!=null){
            ((View)(mainListView.getParent())).setBackgroundColor(Color.WHITE);
        }
        initListOfActivities();
        if(state!=null && mainListView!=null){
            mainListView.onRestoreInstanceState(state);
        }
        if(breakButton!=null){
            ((View)(breakButton.getParent())).setBackgroundColor(Color.WHITE);
            diasbleSaveButton();
        }
    }

    @Override
    protected void onStop() {
        if(mp!=null&&mp.isPlaying()){
            mp.stop();
            mp.reset();
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(mp!=null&&mp.isPlaying()){
            mp.stop();
            mp.reset();
        }
        super.onDestroy();
    }

    public void setActivityAsUnDone(com.przyjaznyplan.models.Activity ac){
        if(ac.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString())) {
            activityDao.setActivityGaleryAsUndone(BusinessLogic.SYSTEM_CURRENT_PLAN_ID,ac.getNumber(), ac.getId());
            com.przyjaznyplan.models.Activity newChildGallery=activityDao.getActivityGallery();
            newChildGallery.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
            newChildGallery.setTypeFlag(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString());
            newChildGallery.setNumber(ac.getNumber());
            listAdapter.changeChildActivity(ac,newChildGallery);
        } else{
            activityDao.setActivityAsUndone(BusinessLogic.SYSTEM_CURRENT_PLAN_ID,ac.getNumber(), ac.getTypeFlag());
            ac.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString());
        }
        setCurrentActivityIndex();
        if(typ!=TypyWidokuPlanuAktywnosci.slide) {
            initListOfActivities();
        }
    }

    public void setActivityAsDone(com.przyjaznyplan.models.Activity ac){
        if(ac.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.ACTIVITY_GALLERY.toString())||
                ac.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString())) {
            return;
        } else{
            activityDao.setActivityAsDone(BusinessLogic.SYSTEM_CURRENT_PLAN_ID, ac.getNumber(), ac.getTypeFlag());
            ac.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString());
        }
        if(typ!=TypyWidokuPlanuAktywnosci.slide) {
            setCurrentActivityIndex();
        }
    }

    @Override
	protected void onCreate(Bundle savedInstanceState){
        helper = new MySQLiteHelper(this);
		super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		Log.i("PlanActivity", "ONCREATE ");

        User user = Globals.GetUser();
        typ = user.getPreferences().getTypWidokuPlanuAtywnosci();


        setContentView(Globals.getPlanActivityViewForUser());


        breakButton =(Button) findViewById( R.id.breakButton );
        if(breakButton!=null){
            breakButton.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(breakButtonEnabled==true){
                        return false;
                    }
                    enableSaveButton();
                    return true;
                }
            });
            diasbleSaveButton();
        }
        if(typ!=TypyWidokuPlanuAktywnosci.slide){
            mainListView = (ListView) findViewById( R.id.list );
            mainListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        then = System.currentTimeMillis();
                    }
                    else if(event.getAction() == MotionEvent.ACTION_UP){
                        long now=System.currentTimeMillis();
                        if((now - then) > BusinessLogic.SYSTEM_CLICK_TIMEOUT && longpressed && longClickedActivity!=null ){
                            setActivityAsUnDone(longClickedActivity);
                            listAdapter.notifyDataSetChanged();
                            return true;
                        }
                        longpressed=false;
                        longClickedActivity=null;
                    }
                    return false;
                }
            });

            mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                               int index, long arg3) {
                    longClickedActivity = (com.przyjaznyplan.models.Activity) mainListView.getAdapter().getItem(index);
                    longpressed=true;
                    Toast.makeText(v.getContext(),"One more sec!", Toast.LENGTH_LONG).show();
                    return false;
                }
            });
        }
        try {
            activityDao = new ActivityDao(MySQLiteHelper.getDb());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        if(mainListView!=null){
            ((View)(mainListView.getParent())).setBackgroundColor(Color.WHITE);
        }
	}

    public void initListOfActivities() {
        list = activityDao.getActivitiesAndTempAGFromPlan(BusinessLogic.SYSTEM_CURRENT_PLAN_ID);
        try{
            if(activityDao.getBreaksFromPlan(BusinessLogic.SYSTEM_CURRENT_PLAN_ID, Globals.getNumberOfIconsInGridForUser()).size()>0){
                breakButton.setVisibility(View.VISIBLE);
                if(typ!=TypyWidokuPlanuAktywnosci.slide){
                    Display display = getWindowManager().getDefaultDisplay();
                    Point screenSize = new Point(display.getWidth(), display.getHeight());
                    int ah = 0;
                    breakButton.measure(breakButton.getMeasuredWidth(),breakButton.getMeasuredHeight());
                    ah+=breakButton.getLayoutParams().height;
                    mainListView.getLayoutParams().height = screenSize.y-ah;
                }
            } else {
                breakButton.setVisibility(View.INVISIBLE);
                if(typ!=TypyWidokuPlanuAktywnosci.slide){
                    Display display = getWindowManager().getDefaultDisplay();
                    Point screenSize = new Point(display.getWidth(), display.getHeight());
                    mainListView.getLayoutParams().height = screenSize.y;
                }
            }
        }catch(Exception e){

        }
        if(typ!=TypyWidokuPlanuAktywnosci.slide) {
            if(setCurrentActivityIndex()== false){
                return;
            }
            listAdapter = new PlanActivityAdapter(this, Globals.getListTypeForPlanActivity(), R.id.label, list, currentPosition, this);
            mainListView.setAdapter(listAdapter);
            mainListView.setOnItemClickListener((OnItemClickListener) this);
        } else {
            initData();
        }
    }

    public void onLongTimerClick() {
        com.przyjaznyplan.models.Activity ac = list.get(currentPosition);
        if (ac.getTime() != 0) {
            if (timer.isRunning == true){
                timerStop(timer);
            }
            if (mp != null && mp.isPlaying()) {
                mp.stop();
                mp.reset();
            }
            ac.setTime(0);
            if (typ != TypyWidokuPlanuAktywnosci.slide) {
                listAdapter.notifyDataSetChanged();
            } else {
                updateArrowsView();
            }
        }
    }

    public void diasbleSaveButton(){
        breakButton.setBackgroundColor(Color.RED);
        breakButtonEnabled = false;
    }

    public void enableSaveButton(){
        breakButton.setBackgroundColor(Color.parseColor("#00CC33"));
        breakButtonEnabled = true;
    }

    private void goToActivity(com.przyjaznyplan.models.Activity item, int position){
        Log.i("PlanActivity", "onItemClick: " + position);
        //Toast.makeText(this, item.getTitle() + " selected", Toast.LENGTH_LONG).show();
        com.przyjaznyplan.models.Activity ca = item;
        Intent intent = null;


        User user = Globals.GetUser();


        if(user.getName().equals(Globals.DEFAULT_USER_NAME))
        {

            //Toast.makeText(this,"Ustaw użytkownika w celu zastosowania dedykowanych ustawień. Użyto domyślne ustawienia.",Toast.LENGTH_LONG).show();
            ;
        }

        if(ca.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString())) {
            intent = new Intent(this, ActivityGalleryView.class);
            activityDao.setChosenStatusOnActivityGallery(item,BusinessLogic.SYSTEM_CURRENT_PLAN_ID);
        }else if(ca.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.ACTIVITY.toString()) || ca.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.FINISHED_ACTIVITY_GALLERY.toString())){
            if(user.getPreferences().getTypWidokuCzynnosci()==TypyWidokuCzynnosci.basic) {
                intent = new Intent(this, BasicActivityView.class);
                intent.putExtra("NAME", item.getTitle());
                intent.putExtra("ACTIVITY", item);
            }else if(user.getPreferences().getTypWidokuCzynnosci()==TypyWidokuCzynnosci.advanced){
                intent= new Intent(this, AdvancedActivityView.class);
                intent.putExtra("NAME", item.getTitle());
                intent.putExtra("ACTIVITY", item);
            }
        }
        try {
            startActivityForResult(intent, ACTIVITY_DONE);
        }catch (Exception e){
            Toast.makeText(this,"Nie udało się uruchomić aktywności",Toast.LENGTH_LONG).show();
            System.out.println(e.getMessage());
        }
    }

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        com.przyjaznyplan.models.Activity item = (com.przyjaznyplan.models.Activity) mainListView.getAdapter().getItem(position);
		if(position!=currentPosition) {
            return;
        }
        if(item.getTime()!=0) {
            if(item.getTime()==-1&&mp!=null&&mp.isPlaying()){
                mp.stop();
                mp.reset();
                setActivityAsDone(item);
                listAdapter.notifyDataSetChanged();
            }
            if(item.getTime()>0) {
                timerStart(timer);
            }
            return;
        }
        if(item.getSlides().size()>0 || item.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString())){
            goToActivity(item,position);
        } else {
            setActivityAsDone(item);
            listAdapter.notifyDataSetChanged();
        }

	}

	public void onClick(View v) {
	    final int id = v.getId();
	    switch (id) 
	    {
	    	case R.id.breakButton:
                if(breakButtonEnabled==false){
                    return;
                }
	    		Intent intent = new Intent(this, BreakTimeView.class);
	    		startActivityForResult(intent, ACTIVITY_CHOSEN);
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

    //TODO
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    // Check which request we're responding to
	    if (requestCode == ACTIVITY_CHOSEN) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	            // The user picked a contact.
	            // The Intent's data Uri identifies which contact was selected.
	        	//String reward = data.getExtras().getString("REWARD");
	        	//Log.i("PlanActivity", "onActivityResult: " + reward);
	        	//int size = mainListView.getAdapter().getCount();
	        	//ArrayList<String> list = new ArrayList<String>();
	        	//for(int i=0;i  < size;i++){
	        		
	        	//	list.add(mainListView.getAdapter().getItem(i).toString());
	        	//	if(i==LAST_ACTIVITY){
	        	//		list.add(reward);
	        	//	}
	        	//}
	        	
	        	//listAdapter = new ArrayAdapter<String>(this, R.layout.rowlistlayout,R.id.label, list);
	        	//mainListView.setAdapter(listAdapter);
	        	
	        	
	        	
	        	
	        	//Intent i = new Intent(this, ActivityView.class);
	        	//i.putExtra("ID",LAST_ACTIVITY+1);
	        	//i.putExtra("NAME", reward);
	        	//startActivityForResult(i, ACTIVITY_DONE);
	        	
	        	 //
	            // Do something with the contact here (bigger example below)
	        }
	       
	    } else if(requestCode == ACTIVITY_DONE)
        {
	    	//if(data == null) return;
	    	//if(data.getExtras()==null) return;
	    	//int id = data.getExtras().getInt("ID");
	    	//String stat = data.getExtras().getString("STATE");
	    	//if(stat.equals("FINISHED")){
	    		
	    		//states[id]=true;
	    		
	    		
	    	} else if(requestCode ==12341) {
            super.finish();
        }
	    	
	    	//int size = 	mainListView.getChildCount();
		    //for(int i =0;i<size;i++){
	    	//	if(states[i]){
	    		
	    	//	TextView txtView = (TextView)((LinearLayout)mainListView.getChildAt(i)).getChildAt(2);
	    	//	 txtView.setPaintFlags( txtView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
	    	//	}
	    	//}
    }

    private void showFinalScreen(){
        Intent intent= new Intent(this, FinalView.class);
        startActivityForResult(intent, 12341);
    }

    public boolean setCurrentActivityIndex(){
        Boolean someActivityIsUnDone = false;
        for(int i=0;i<list.size();i++){
            if(list.get(i).getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.NEW.toString())){
                currentPosition=i;
                someActivityIsUnDone = true;
                if(listAdapter!=null)
                    listAdapter.currentPosition=currentPosition;
                return true;
            }
        }
        if(someActivityIsUnDone == false){
            Toast.makeText(this, "Zrobiono wszystkie aktywnosci!!!!!", Toast.LENGTH_LONG).show();
            showFinalScreen();
            return false;
        }
        return true;
    }

    //only for basic view

    private void initData() {
        if(setCurrentActivityIndex()==false){
            return;
        }
        if(handler==null)
            handler = new Handler();
        if(mp==null)
            mp = new MediaPlayer();
        //init view variables
        if(buttonToNextSlide==null)
            buttonToNextSlide=(ImageButton)findViewById(R.id.basivview_greenbutton);
        if(buttonToPrevSlide==null)
            buttonToPrevSlide=(ImageButton)findViewById(R.id.basivview_redbutton);
        if(image==null)
            image=(ImageView)findViewById(R.id.basicview_image);
        if(soundTube==null)
            soundTube=(ImageButton)findViewById(R.id.basicview_soundTube);
        if(timerIcon==null){
            timerIcon=(ImageView)findViewById(R.id.basicview_czasButton);
            timerIcon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onLongTimerClick();
                    return true;
                }
            });
        }
        if(description==null)
            description = (TextView)findViewById(R.id.basicview_description);
        if(timerText==null)
            timerText=(TextView)findViewById(R.id.ad_v_timer);
        if(time_str == null)
            time_str = (RelativeLayout) findViewById(R.id.time_str);
        updateAllView();
    }

    /***
     * UPDATE CALEGO WIDOKI W ZALEZNOCI OD AKTUALNIE WYSWIETLANEGO SLAJDU
     */
    private void updateAllView(){

        com.przyjaznyplan.models.Activity currentSlide = list.get(currentPosition);

        //aktualizuj caly widok;
        //GLOSNIK
        if(currentSlide.getAudioPath()==null || currentSlide.getAudioPath().equals("")){
            soundTube.setVisibility(View.INVISIBLE);
        }
        else{
            soundTube.setVisibility(View.VISIBLE);
        }

        //TEKST
        if(currentSlide.getTitle()==null || currentSlide.getTitle().equals("")){
            description.setVisibility(View.INVISIBLE);
        }else{
            description.setText(currentSlide.getTitle());
            description.setVisibility(View.VISIBLE);
        }

        //OBRAZEK
        if(currentSlide.getIconPath()==null || currentSlide.getIconPath().equals("")){
            image.setVisibility(View.INVISIBLE);
        }else{
            Display display = getWindowManager().getDefaultDisplay();
            Point screenSize = new Point(display.getWidth(), display.getHeight());
            Bitmap bmp = BitmapFactory.decodeFile(currentSlide.getIconPath());
            image.measure(image.getMeasuredWidth(),image.getMeasuredHeight());
            int ah = 0;
            if(description!=null){
                Rect bounds = new Rect();
                description.getPaint().getTextBounds(description.getText().toString(), 0, description.getText().length(), bounds);
                description.measure(description.getMeasuredWidth(),description.getMeasuredHeight());
                ah+=bounds.height();
            }
            if(breakButton!=null){
                breakButton.measure(breakButton.getMeasuredWidth(),breakButton.getMeasuredHeight());
                ah+=breakButton.getMeasuredHeight();
            }
            int aw = 0;
            if(soundTube!=null){
                soundTube.measure(soundTube.getMeasuredWidth(),soundTube.getMeasuredHeight());
                aw+=soundTube.getMeasuredHeight();
            }
            if(time_str!=null){
                time_str.measure(time_str.getMeasuredWidth(),time_str.getMeasuredHeight());
                aw+=time_str.getMeasuredHeight();
            }
            int w = bmp.getWidth();
            int h = bmp.getHeight();
            int ih = screenSize.y-ah;
            int iw = screenSize.x-aw;
            Point desiredSize = null;
            if(h>w){
                desiredSize = new Point((int) (w*ih/h), ih);
            } else {
                desiredSize = new Point( iw, (int) (h*iw/w));
            }
            Bitmap scaledBmp = scaleBMP(bmp, desiredSize.x, desiredSize.y);
            image.setImageBitmap(scaledBmp);
            image.setVisibility(View.VISIBLE);
        }

        //ikonkaTImera
        if(currentSlide.getTime()==0){
            timerIcon.setVisibility(View.INVISIBLE);
        }else{
            timerIcon.setVisibility(View.VISIBLE);
        }

        updateArrowsView();


    }

    //KB Skalowanie bitmapy aspect ratio
    Bitmap scaleBMP(Bitmap originalImage, int newWidth, int newHeight) {
        Bitmap newImage = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        float originalWidth = originalImage.getWidth(), originalHeight = originalImage.getHeight();
        Canvas canvas = new Canvas(newImage);
        float scale = newWidth / originalWidth;
        float xTranslation = 0.0f, yTranslation = (newHeight - originalHeight * scale) / 2.0f;
        Matrix transformation = new Matrix();
        transformation.postTranslate(xTranslation, yTranslation);
        transformation.preScale(scale, scale);
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(originalImage, transformation, paint);
        return newImage;
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
            if(reapeatable == true){
                play(path, reapeatable);
                return;
            }
            if(list.get(currentPosition).getTime()==-1){
                list.get(currentPosition).setTime(0);
            }
        }
    }



    public void onClickGreenButton(View view){
        //++currentPosition;
        buttonToNextSlide.setVisibility(View.INVISIBLE);
        goToActivity(list.get(currentPosition),currentPosition);
    }

    public void onClickRedButton(View view){
        setActivityAsUnDone(list.get(currentPosition));
        --currentPosition;
        setActivityAsUnDone(list.get(currentPosition));
        updateAllView();
    }

    public void onBasicSoundTubeClick(View view) {
        if(list.get(currentPosition).getTime()==-1){
            return;
        }
        play(list.get(currentPosition).getAudioPath(), false);
    }

    @Override
    public void onLongTimerClickFromAdapter( View position) {
        if(typ!=TypyWidokuPlanuAktywnosci.slide){
            if(mainListView.getPositionForView(position) == currentPosition){
                onLongTimerClick();
                Toast.makeText(this,"Porusz w gore!", Toast.LENGTH_LONG).show();
            }
        }
    }

    //end of basic view
	    


}
