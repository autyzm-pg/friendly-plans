/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer;

import com.example.przyjaznyplan.R;
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
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.dao.ActivityDao;
import com.przyjaznyplan.models.Slide;
import com.przyjaznyplan.utils.BusinessLogic;

import java.util.List;


public class BasicActivityView extends Activity implements View.OnLongClickListener{

    //


    //model variables
    private com.przyjaznyplan.models.Activity activity;
    private List<Slide> slides;


    //other
    private int currentPosition;
    private Handler handler;
    private boolean dataInitialized =false;
    //view variables
    private ImageButton buttonToNextSlide;
    private ImageButton buttonToPrevSlide;
    private ImageView image;
    private ImageButton soundTube;
    private ImageView timerIcon;
    private MediaPlayer mp;
    private EditText slide_id_editText;
    private TextView description;
    private TextView timerText;
    private boolean timerSuspended=false;
    private RelativeLayout time_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initScreen();
        initData();

       if(dataInitialized) {
           initListeners();
           updateAllView();
       }

    }

    //implementacja timera
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
                slides.get(currentPosition).setTime((int) (time / 1000.0));

            }else if(time<=0){

                slides.get(currentPosition).setTime(0);
                play(Globals.GetUser().getPreferences().getTimerSoundPath(), true);

            }

            updateViewAfterTimerHit();


            if(time>0) {
                handler.postDelayed(timer, 1000);
            }else{
                slides.get(currentPosition).setTime(-1);
                updateViewAfterTimerHit();
                timerStop(timer);
            }
        }
    };



    public void timerStop(ExtendedRunnable timer) {
        if(handler!=null){
            timer.isRunning = false;
            handler.removeCallbacks(timer);
        }
    }

    @Override
    protected void onPause() {
        timerStop(timer);
        super.onPause();
    }
    @Override
    protected void onStop() {
        if(mp!=null&&mp.isPlaying()){
            mp.stop();
            mp.reset();
        }
        timerStop(timer);
        super.onStop();
    }


    /***
     * Timer will start if necessery
     * @param timer
     */
    private void timerStart(ExtendedRunnable timer){

        Slide slide = null;
        if(timer.isRunning == true){
            return;
        }
        if((slides!=null && slides.size()>0) && (currentPosition < slides.size())) {

            slide = slides.get(currentPosition);

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




    private void initListeners() {

        //timerIcon.setOnLongClickListener(this);

    }

    private void initScreen() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(Globals.getActivityViewForUser());
    }

    private void checkIsActivityEmpty(){
        if(slides.size()==0 || activity.getStatus().equals(com.przyjaznyplan.models.Activity.ActivityStatus.FINISHED.toString())){
            finishActivity();
            dataInitialized=false;
        }else{
            dataInitialized=true;
        }
    }


    private void initData() {
        currentPosition=0;
        //init data model
        activity = (com.przyjaznyplan.models.Activity) getIntent().getExtras().get("ACTIVITY");
        slides = activity.getSlides();
        checkIsActivityEmpty();

        handler = new Handler();
        mp = new MediaPlayer();
        //init view variables
        if(buttonToNextSlide == null)
            buttonToNextSlide=(ImageButton)findViewById(R.id.basivview_greenbutton);
        if(buttonToPrevSlide == null)
            buttonToPrevSlide=(ImageButton)findViewById(R.id.basivview_redbutton);
        if(image == null)
            image=(ImageView)findViewById(R.id.basicview_image);
        if(soundTube == null)
            soundTube=(ImageButton)findViewById(R.id.basicview_soundTube);
        if(timerIcon == null){
            timerIcon=(ImageView)findViewById(R.id.basicview_czasButton);
            timerIcon.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onTimerLongClick();
                    return true;
                }
            });
        }
        if(description == null)
            description = (TextView)findViewById(R.id.basicview_description);
        if(timerText == null)
            timerText=(TextView)findViewById(R.id.ad_v_timer);
        if(time_str == null)
            time_str = (RelativeLayout) findViewById(R.id.time_str);
    }

    public void onTimerClick(View v){
        Slide slide =slides.get(currentPosition);
        if(slide.getTime()==-1){
            if(mp!=null&&mp.isPlaying()){
                mp.stop();
                mp.reset();
            }
            timerIcon.setVisibility(View.INVISIBLE);
            slide.setTime(0);
            updateViewAfterTimerHit();
        } else {
            timerStart(timer);
        }
    }

    private void updateViewAfterTimerHit() {
        updateArrowsView();
    }


    /***
     * UPDATE CALEGO WIDOKI W ZALEZNOCI OD AKTUALNIE WYSWIETLANEGO SLAJDU
     */
    private void updateAllView(){

        Slide currentSlide = slides.get(currentPosition);

        //aktualizuj caly widok;
        //GLOSNIK
        if(currentSlide.getAudioPath()==null || currentSlide.getAudioPath().equals("")){
            soundTube.setVisibility(View.INVISIBLE);
        }
        else{
            soundTube.setVisibility(View.VISIBLE);
        }

        //TEKST
        if(currentSlide.getText()==null || currentSlide.getText().equals("")){
            description.setVisibility(View.INVISIBLE);
        }else{
            description.setText(currentSlide.getText());
            description.setVisibility(View.VISIBLE);
        }

        //OBRAZEK
        if(currentSlide.getImagePath()==null || currentSlide.getImagePath().equals("")){
            image.setVisibility(View.INVISIBLE);
        }else{
            Display display = getWindowManager().getDefaultDisplay();
            Point screenSize = new Point(display.getWidth(), display.getHeight());
            Bitmap bmp = BitmapFactory.decodeFile(currentSlide.getImagePath());
            image.measure(image.getMeasuredWidth(),image.getMeasuredHeight());
            int ah = 0;
            if(description!=null){
                Rect bounds = new Rect();
                description.getPaint().getTextBounds(description.getText().toString(), 0, description.getText().length(), bounds);
                description.measure(description.getMeasuredWidth(),description.getMeasuredHeight());
                ah+=bounds.height();
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

    public void onTimerLongClick (){
        Slide ac = slides.get(currentPosition);
        if (ac.getTime() != 0) {
            if (timer.isRunning == true){
                timerStop(timer);
            }
            if (mp != null && mp.isPlaying()) {
                mp.stop();
                mp.reset();
            }
            ac.setTime(0);
            updateArrowsView();
        }
    }

    private void updateArrowsView(){
        Slide currentSlide = slides.get(currentPosition);
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
            timerText.setVisibility(View.INVISIBLE);
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
        if (currentPosition == 0 && currentSlide.getTime() > 0) {
            buttonToNextSlide.setVisibility(View.INVISIBLE);
            buttonToPrevSlide.setVisibility(View.INVISIBLE);
        }

        if (currentPosition == 0 && currentSlide.getTime() == 0) {
            buttonToNextSlide.setVisibility(View.VISIBLE);
            buttonToPrevSlide.setVisibility(View.VISIBLE);
        }

        if (currentPosition > 0 && currentPosition < slides.size() && currentSlide.getTime() == 0) {
            buttonToNextSlide.setVisibility(View.VISIBLE);
            buttonToPrevSlide.setVisibility(View.VISIBLE);
        }

        if (currentPosition == slides.size() - 1 && currentSlide.getTime() == 0) {
            buttonToNextSlide.setVisibility(View.VISIBLE);
            buttonToPrevSlide.setVisibility(View.VISIBLE);
        }

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
            if(slides.get(currentPosition).getTime()==-1){
                slides.get(currentPosition).setTime(0);
            }
        }
    }




    public void onClickGreenButton(View view){
        if(slides.get(currentPosition).getTime()==-1){
            if(mp!=null&&mp.isPlaying()){
                mp.stop();
                mp.reset();
            }
            slides.get(currentPosition).setTime(0);

        }
        if(currentPosition==slides.size()-1){
            finishActivity();
            return;
        }

        ++currentPosition;
        buttonToNextSlide.setVisibility(View.INVISIBLE);
        buttonToPrevSlide.setVisibility(View.INVISIBLE);
        updateAllView();
    }

    public void onClickRedButton(View view){
        if(mp!=null&&mp.isPlaying()){
            mp.stop();
            mp.reset();
        }
        if(currentPosition==0){
            super.finish();
            return;
        }
        if(timer.getTime()<=0) {
            com.przyjaznyplan.models.Activity ak= (com.przyjaznyplan.models.Activity) getIntent().getExtras().get("ACTIVITY");
            slides.set(currentPosition,ak.getSlides().get(currentPosition));
            --currentPosition;
            slides.set(currentPosition,ak.getSlides().get(currentPosition));
            updateAllView();
        }
    }

    @Override
    public boolean onLongClick(View view) {

        if(!timerSuspended) {
            timerStop(timer);
            timerSuspended=true;
        }
        else
        {
            timerSuspended=false;
            timerStart(timer);
        }
        return true;
    }

    public void onBasicSoundTubeClick(View view) {
        if(slides.get(currentPosition).getTime()==-1){
            return;
        }
        play(slides.get(currentPosition).getAudioPath(),false);
    }

    private void finishActivity(){



        Intent data = new Intent();
        data.putExtra("STATE", "FINISHED");
        data.putExtra("ID", this.activity.getId());
        setResult(RESULT_OK, data);

        ActivityDao activityDao = new ActivityDao(MySQLiteHelper.getDb());

        if (this.activity.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.ACTIVITY_GALLERY.toString())) {
            com.przyjaznyplan.models.Activity chosen = activityDao.getChosenChildActivity();
            if (chosen != null && chosen.getTypeFlag().equals(com.przyjaznyplan.models.Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString())) {
                activityDao.changeGAToActivity(chosen, BusinessLogic.SYSTEM_CURRENT_PLAN_ID,this.activity);
            } else {
                Toast.makeText(this, "chosen status mismatched", Toast.LENGTH_LONG).show();
            }

            //firstly set number to activity gallery form temp activity gallery(sort by number limit 1)
            //after that - delete temp activity gallery(sort by number limit 1)
            //refresh plan activity



        }
        activityDao.setActivityAsDone(BusinessLogic.SYSTEM_CURRENT_PLAN_ID,this.activity.getNumber(), this.activity.getTypeFlag());
        finish();
    }

    @Override
    protected void onResume() {
        if(buttonToNextSlide!=null){
            ((View)(buttonToNextSlide.getParent())).setBackgroundColor(Color.WHITE);
        }
        super.onResume();
    }
}
