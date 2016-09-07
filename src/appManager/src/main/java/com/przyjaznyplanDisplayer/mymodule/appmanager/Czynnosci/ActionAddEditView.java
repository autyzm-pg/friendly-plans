/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.mymodule.appmanager.Czynnosci;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.przyjaznyplan.models.Slide;
import com.przyjaznyplan.repositories.ActionRepository;
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;
import java.util.List;

import br.com.thinkti.android.filechooser.FileChooser;

public class ActionAddEditView extends Activity {


    private com.przyjaznyplan.models.Activity activity;

    enum ViewType { CREATE, EDIT }
    private ViewType viewType = ViewType.CREATE;
    private Slide slide;
    String pathToPicture="";
    MediaPlayer mp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_edit_view);

        if(getIntent().getExtras().get("ACTIVITY")!=null){
            this.activity = (com.przyjaznyplan.models.Activity)getIntent().getExtras().get("ACTIVITY");
        }

        if(getIntent().getExtras().get("SLIDE")!=null){
            viewType = ViewType.EDIT;
            this.slide=(Slide)getIntent().getExtras().get("SLIDE");
            initEditView();
        } else {
            this.slide = new Slide();
        }
    }

    private void initEditView() {

        if(this.slide.getImagePath()!=null && !this.slide.getImagePath().equals("")){
            setBMP(this.slide.getImagePath());
        }

        initSounds();

        EditText actionTitle = (EditText) findViewById(R.id.editText);
        actionTitle.setText(this.slide.getText());

        EditText actionTime = (EditText) findViewById(R.id.editText2);
        actionTime.setText(String.valueOf(this.slide.getTime()));
    }

    private void initSounds() {
        if(slide.getAudioPath()!=null && !slide.getAudioPath().equals("")) {

            ImageView playSoundIcon = (ImageView) (findViewById(R.id.playSound));
            playSoundIcon.setVisibility(View.VISIBLE);
            ImageView deleteSoundIcon = (ImageView) (findViewById(R.id.deleteSound));
            deleteSoundIcon.setVisibility(View.VISIBLE);
            TextView soundPath = (TextView) findViewById(R.id.soundPath);
            soundPath.setText(this.slide.getAudioPath());
        }
    }

    public void setBMP(String pathToPicture){
        try {
            Bitmap bmp = BitmapFactory.decodeFile(pathToPicture);
            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);
            ImageView activityImage = (ImageView) (findViewById(R.id.picture));
            activityImage.setImageBitmap(scaledBmp);
            slide.setImagePath(pathToPicture);
            ImageView deletePictureIcon = (ImageView) (findViewById(R.id.deletePictureIcon));
            deletePictureIcon.setVisibility(View.VISIBLE);
        }catch (Exception e){

        }
    }

    public void setPicture(View v){
        Intent intent = new Intent(this, FileChooser.class);
        ArrayList<String> extensions = new ArrayList<String>();
        extensions.add(".jpg");
        extensions.add(".jpeg");
        extensions.add(".png");
        intent.putStringArrayListExtra("filterFileExtension", extensions);
        intent.putExtra("fileStartPath", Environment.getExternalStorageDirectory());
        startActivityForResult(intent, RequestCodes.FILE_CHOOSER_PIC);
    }

    public void setSound(View v){
        Intent intent = new Intent(this, FileChooser.class);
        ArrayList<String> extensions = new ArrayList<String>();
        extensions.add(".mp3");
        intent.putStringArrayListExtra("filterFileExtension", extensions);
        intent.putExtra("fileStartPath", Environment.getExternalStorageDirectory());
        startActivityForResult(intent, RequestCodes.FILE_CHOOSER_SOUND);
    }

    public void removePicture(View v) {
        slide.setImagePath(null);
        ImageView activityImage = (ImageView) (findViewById(R.id.picture));
        activityImage.setImageResource(R.drawable.t1);
        ImageView deletePictureIcon = (ImageView) (findViewById(R.id.deletePictureIcon));
        deletePictureIcon.setVisibility(View.INVISIBLE);
    }

    public void removeSound(View v){
        slide.setAudioPath(null);

        ImageView playSoundIcon = (ImageView) (findViewById(R.id.playSound));
        playSoundIcon.setVisibility(View.INVISIBLE);

        TextView soundPath = (TextView) findViewById(R.id.soundPath);
        soundPath.setText("");

        ImageView deleteSoundIcon = (ImageView) (findViewById(R.id.deleteSound));
        deleteSoundIcon.setVisibility(View.INVISIBLE);
    }

    public void playSound(View v){
        if(slide.getAudioPath()!=null && !slide.getAudioPath().equals("")){
            try {
                if(this.mp==null||!this.mp.isPlaying()) {
                    this.mp = MediaPlayer.create(this, Uri.parse(slide.getAudioPath()));
                    mp.start();
                } else {
                    if(this.mp!=null&&this.mp.isPlaying()){
                        mp.stop();
                    }
                }
            }catch(Exception e){
                Toast.makeText(this, R.string.play_timer_error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void saveTemplate(View v){

        try{
            String timeValue;

            EditText etName = (EditText) findViewById(R.id.editText);
            this.slide.setText(etName.getText().toString());

            EditText etTime = (EditText) findViewById(R.id.editText2);
            timeValue = etTime.getText().toString();
            if(timeValue == null || timeValue.equals(""))
                timeValue = "0";

            this.slide.setTime(Integer.parseInt(timeValue));

            assignActionToActivity();

            ActivityRepository.updateWithActions(activity);

            Toast.makeText(this, R.string.save_action_success, Toast.LENGTH_LONG).show();

            super.finish();
        } catch(Exception e){

            Toast.makeText(this, R.string.save_action_error, Toast.LENGTH_LONG).show();
        }
    }

    private void assignActionToActivity() {

        if(this.activity.getSlides() == null)
            this.activity.setSlides(new ArrayList<Slide>());

        if(viewType == viewType.EDIT) {

            for (Slide slide : this.activity.getSlides()) {
                if (slide.getId().equals(this.slide.getId())) {
                    this.activity.getSlides().remove(slide);
                }
            }
        }

        this.activity.getSlides().add(slide);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCodes.FILE_CHOOSER_PIC && resultCode == -1){
            String fileSelected = data.getStringExtra("fileSelected");
            if(!("".equals(fileSelected))){
                try {
                    pathToPicture=fileSelected;
                    setBMP(pathToPicture);
                }catch (Exception e){

                }
            }
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
        }
        if(requestCode == RequestCodes.FILE_CHOOSER_SOUND && resultCode == -1){
            String fileSelected = data.getStringExtra("fileSelected");
            if(!("".equals(fileSelected))){
                try {
                    slide.setAudioPath(fileSelected.toString());
                    initSounds();
                }catch (Exception e){

                }
            }
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
        }
        }
}