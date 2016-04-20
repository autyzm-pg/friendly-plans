/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.przyjaznyplan.R;
import com.przyjaznyplan.dao.SlideDao;
import com.przyjaznyplan.models.Activity;
import com.przyjaznyplan.models.Slide;
import com.przyjaznyplan.dao.ActivityDao;

import com.przyjaznyplan.DbHelper.MySQLiteHelper;

import com.przyjaznyplan.utils.BusinessLogic;

import java.util.EventListener;
import java.util.List;


public class
        ActivityView extends android.app.Activity implements EventListener {


    private Activity activity;
    private List<Slide> slides;

    private ActivityDao activityDao = new ActivityDao(MySQLiteHelper.getDb());
    private SlideDao slideDao = new SlideDao(MySQLiteHelper.getDb());

    private int slidePointer;


    private EditText slide_id_editText;
    private EditText description;

    public ImageButton getButtonToNextSlide() {
        return buttonToNextSlide;
    }

    private ImageButton buttonToNextSlide;
    private ImageButton buttonToPrevSlide;
    private ImageView image;
    private ImageButton soundTube;
    private ImageView timerIcon;
    private MediaPlayer mp;
    private Thread timer = null;
    private TimerThread timerThread = null;
    private TimerThread.Types timerType = null;

    //Damian
    private TextView timerText = null;

    @Override
    protected void onResume() {
        super.onResume();


        this.slidePointer = activity.getLastSlideNumber();

        if (activity.getStatus().toString().equals(Activity.ActivityStatus.FINISHED.toString())) {
            Toast.makeText(this, "activity is done", Toast.LENGTH_LONG).show();
            displayLastScreen();
        } else {
            if (activity.getTime() > 0) {
                timerThread = new TimerThread(this, activity.getTime(), TimerThread.Types.ACTIVITY);
                timer = new Thread(timerThread);
                timer.start();
                timerType = TimerThread.Types.ACTIVITY;
            }
            if (this.slides.size() > slidePointer) {
                displaySlide(slides.get(0));

            } else if (this.slides.size() == slidePointer) {
                Toast.makeText(this, "empty activity", Toast.LENGTH_LONG).show();
                displayLastScreen();

            }
        }

    }

    public void displayLastScreen() {
        stopTimer(TimerThread.Types.ALL);
        Intent data = new Intent();
        data.putExtra("STATE", "FINISHED");
        data.putExtra("ID", this.activity.getId());
        setResult(RESULT_OK, data);
        if (this.activity.getTypeFlag().equals(Activity.TypeFlag.ACTIVITY_GALLERY.toString())) {
            Activity chosen = activityDao.getChosenChildActivity();
            if (chosen != null && chosen.getTypeFlag().equals(Activity.TypeFlag.TEMP_ACTIVITY_GALLERY.toString())) {
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

    private void displaySlide(Slide slide) {

        try {
            // slide_id_editText.setText("ID SLAJDU:" + (int) slides.get(this.slidePointer).getId());
            if (slide.getText() == null || slide.getText().equals("")) {
                description.setVisibility(View.INVISIBLE);
            } else {
                description.setVisibility(View.VISIBLE);
                description.setText(slide.getText());
            }

            if (slide.getAudioPath() == null || slide.getAudioPath().equals("")) {

                soundTube.setVisibility(View.INVISIBLE);
            } else {
                soundTube.setVisibility(View.VISIBLE);
                mp = MediaPlayer.create(this, Uri.parse(slide.getAudioPath()));
            }

            if (slide.getTime() == 0) {
                timerIcon.setVisibility(View.INVISIBLE);
            } else {
                timerIcon.setVisibility(View.VISIBLE);
            }

            if (slide.getImagePath() == null || slide.getImagePath().equals("")) {

            } else {


                Display display = getWindowManager().getDefaultDisplay();
                Point screenSize = new Point(display.getWidth(), display.getHeight());
                Point desiredSize = new Point((int) (screenSize.x * 0.6), (int) (screenSize.y * 0.5));
                Bitmap bmp = BitmapFactory.decodeFile(slide.getImagePath());
                //KB:wyswietlenie obrazka w zaleznosci od rodzielczosci ekranu zwazajac na aspect ratio
                Bitmap scaledBmp = scaleBMP(bmp, desiredSize.x, desiredSize.y);
                image.setImageBitmap(scaledBmp);
            }
            updateView();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
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

    @Override
    protected void onStop() {
        super.onStop();
        stopTimer(TimerThread.Types.ALL);


        if (this.mp != null && this.mp.isPlaying()) {
            mp.stop();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activitylayout);


        activity = (Activity) getIntent().getExtras().get("ACTIVITY");
        slides = activity.getSlides();//slideDao.getAllActivitySlides(activity.getId()+"");
        this.slidePointer = activity.getLastSlideNumber();

        String name = getIntent().getExtras().getString("NAME");

        setTitle(name);
        buttonToNextSlide = (ImageButton) findViewById(R.id.button2);
        buttonToPrevSlide = (ImageButton) findViewById(R.id.button1);
        description = (EditText) findViewById(R.id.editText1);
        this.image = (ImageView) findViewById(R.id.imageView1);
        this.timerIcon = (ImageView) findViewById(R.id.czas);
        soundTube = (ImageButton) findViewById(R.id.imageButton1);
        timerText = (TextView) findViewById(R.id.timerText);
        timerText.setTextColor(Color.BLACK);


        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

    }

    //wyłączenie przycisku cofania podczas aktywności
    @Override
    public void onBackPressed() {

    }

    @Override
    public void onAttachedToWindow() {
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        super.onAttachedToWindow();
    }

    public void playSound(View v) {
        mp.start();
    }

    public void nextSlide() {

        stopTimer(TimerThread.Types.SLIDE);

        this.slidePointer++;

        if (this.slidePointer == slides.size()) {
            displayLastScreen();
            return;
        }
        //zapisz ewentualne aktualne dane
        //Pobierz nowe dane do nastepnego slajdu
        Slide slide = slides.get(this.slidePointer);
        displaySlide(slide);
    }


    private void stopTimer(TimerThread.Types type) {
        if (timer != null) {
            if (type.equals(this.timerType) || type.equals(TimerThread.Types.ALL)) {
                timerThread.terminate();
                try {
                    timer.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                timer = null;
                timerThread = null;
                timerType = null;
            }

        }
    }

    public void prevSlide() {

        if (this.slidePointer > 0) {
            this.slidePointer--;

            stopTimer(TimerThread.Types.SLIDE);

            //zapisz ewentualne aktualne dane
            //pobierz dane do poprzedniego slajdu

            Slide slide2 = slides.get(this.slidePointer);
            displaySlide(slide2);
        } else {
            stopTimer(TimerThread.Types.ALL);
            finish();
        }

    }

    public void onClick(View v) {
        final int id = v.getId();
        if (this.mp != null && this.mp.isPlaying()) {
            mp.stop();
        }
        switch (id) {
            case R.id.button2:
                nextSlide();
                break;
            case R.id.button1:
                prevSlide();
                break;
            default:
                break;

        }

    }

    @Override
    public void finish() {
        /*if (!this.activity.getStatus().toString().equals(Activity.ActivityStatus.FINISHED.toString())) {
            activity.setStatus(Activity.ActivityStatus.STARTED.toString());

        }

        activity.setLastSlideNumber(this.slidePointer);

        ActivityDto adto = new ActivityDto();
        adto.setActivity(activity);
        activityDao.update(adto);
        zapisywany progress
        */
        super.finish();
    }

    void updateView() {
        Slide slide = slides.get(this.slidePointer);
        //slide_id_editText.setText("ID SLAJDU:" + (int) slide.getId());

        if (timerThread == null) {
            timerText.setVisibility(View.INVISIBLE);
            if (slide.getTime() > 0) {
                timerText.setVisibility(View.VISIBLE);
                timerThread = new TimerThread(this, slide.getTime(), TimerThread.Types.SLIDE);
                this.timer = new Thread(timerThread);
                this.timerType = TimerThread.Types.SLIDE;

                timer.start();
            }
        }
        if (this.timerType == TimerThread.Types.SLIDE) {
            buttonToNextSlide.setEnabled(false);
            buttonToNextSlide.setVisibility(View.INVISIBLE);
        } else {
            buttonToNextSlide.setEnabled(true);
            buttonToNextSlide.setVisibility(View.VISIBLE);
        }

        if (this.slidePointer == slides.size() - 1) {


            buttonToNextSlide.setImageResource(R.drawable.meta);
            if (this.timerThread != null) {
                buttonToNextSlide.setEnabled(false);
                buttonToNextSlide.setVisibility(View.INVISIBLE);
            } else {
                buttonToNextSlide.setEnabled(true);
                buttonToNextSlide.setVisibility(View.VISIBLE);
            }

        } else {
            buttonToNextSlide.setImageResource(R.drawable.garrow);
            /*TimerThread.Types t = this.timerThread.getType();
             buttonToNextSlide.setImageResource(R.drawable.garrow);
             if(this.timerThread != null && this.timerThread.getType().equals(TimerThread.Types.SLIDE)){
             buttonToNextSlide.setEnabled(false);
             buttonToNextSlide.setVisibility(View.INVISIBLE);

             }
             else{
             buttonToNextSlide.setEnabled(true);
             buttonToNextSlide.setVisibility(View.VISIBLE);
             }*/

        }


    }

    void setTimer(int seconds) {
        if (seconds != 0) {
            timerText.setVisibility(View.VISIBLE);
            timerText.setText(Integer.toString(seconds) + " seconds left");
        } else
            timerText.setVisibility(View.INVISIBLE);
    }


}
