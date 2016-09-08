/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */

package com.przyjaznyplanDisplayer.mymodule.appmanager.Aktywnosci;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Czynnosci.ActionListView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;
import java.util.MissingResourceException;

import br.com.thinkti.android.filechooser.FileChooser;

public class ActivityEditView extends Activity {
    public static final String EMPTY_VALUE = "";
    private com.przyjaznyplan.models.Activity planActivity;
    private MediaPlayer mp;
    private int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);
        if(getIntent().getExtras()!=null && getIntent().getExtras().get("ACTIVITY")!=null){
            mode = RequestCodes.ACTIVITY_EDITED;
            this.planActivity = (com.przyjaznyplan.models.Activity)getIntent().getExtras().get("ACTIVITY");
            initView();
        }
        else {
            mode = RequestCodes.ACTIVITY_ADDED;
            this.planActivity = new com.przyjaznyplan.models.Activity();
        }
    }

    private void initView() {
        initPicture();

        initSounds();

        EditText activityTitle = (EditText) findViewById(R.id.activityTitle);
        activityTitle.setText(this.planActivity.getTitle());

        EditText timerTime = (EditText) findViewById(R.id.timerTime);
        timerTime.setText(String.valueOf(this.planActivity.getTime()));

        TextView actions = (TextView) findViewById(R.id.activityNumber);
        if(this.planActivity.getSlides()==null){
            actions.setText("0");
        } else {
            actions.setText(this.planActivity.getSlides().size() + "");
        }
    }

    private void initSounds() {
        if(planActivity.getAudioPath()!=null && !planActivity.getAudioPath().equals("")){
            setVisibilityOfElementTo(R.id.removeSoundIcon, View.VISIBLE);
            setVisibilityOfElementTo(R.id.playSoundIcon, View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void initPicture(){
        if(this.planActivity.getIconPath()!=null && !this.planActivity.getIconPath().equals("")){
            initPicture(this.planActivity.getIconPath());
        }
    }

    public void initPicture(String pathToPicture){
        try {
            Bitmap bmp = BitmapFactory.decodeFile(pathToPicture);
            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);

            setImageVisibility(scaledBmp);
            planActivity.setIconPath(pathToPicture);
            setVisibilityOfElementTo(R.id.removePictureIcon, View.VISIBLE);
        }catch (Exception e){
            pathToPicture = "";
            planActivity.setIconPath(pathToPicture);
            Toast.makeText(this, R.string.display_picture_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void setImageVisibility(Bitmap scaledBmp) {
        ImageView activityImage = (ImageView) (findViewById(R.id.picture));
        activityImage.setImageBitmap(scaledBmp);
        activityImage.setVisibility(View.VISIBLE);
    }

    private void setVisibilityOfElementTo(int elementId, int visibleOption) {
        ImageView element = (ImageView) (findViewById(elementId));
        element.setVisibility(visibleOption);
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
        planActivity.setIconPath(null);
        ImageView activityImage = (ImageView) (findViewById(R.id.picture));
        activityImage.setImageResource(R.drawable.t1);
        setVisibilityOfElementTo(R.id.removePictureIcon, View.INVISIBLE);
    }

    public void removeSound(View v){
        planActivity.setAudioPath(null);
        setVisibilityOfElementTo(R.id.removeSoundIcon, View.INVISIBLE);
    }

    public void playSound(View v){
        if(planActivity.getAudioPath()!=null && !planActivity.getAudioPath().equals("")){
            try {
                if(this.mp==null||!this.mp.isPlaying()) {
                    this.mp = MediaPlayer.create(this, Uri.parse(planActivity.getAudioPath()));
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

    public void activityManageClick(View v){

        try {
            saveActivity();
            Intent intent = new Intent(this, ActionListView.class);
            intent.putExtra("ACTIVITY", this.planActivity);
            startActivityForResult(intent, RequestCodes.ACTIVITY_MANAGEMENT);
        } catch(Exception e){
            Toast.makeText(this, R.string.activity_save_error, Toast.LENGTH_SHORT).show();
        }
    }

    public void saveActivity(){

        EditText activityTitleEditText = (EditText) findViewById(R.id.activityTitle);
        String activityTitle = activityTitleEditText.getText().toString();
        if(activityTitle.equals(EMPTY_VALUE))
            throw new RuntimeException();
        this.planActivity.setTitle(activityTitle);

        EditText etTime = (EditText) findViewById(R.id.timerTime);
        String time = etTime.getText().toString();
        if(time.equals(""))
            time = "0";
        this.planActivity.setTime(Integer.parseInt(time));

        this.planActivity.setTypeFlag(com.przyjaznyplan.models.Activity.TypeFlag.ACTIVITY + "");
        this.planActivity.setStatus(com.przyjaznyplan.models.Activity.ActivityStatus.NEW + "");

        if(mode == RequestCodes.ACTIVITY_ADDED){
            ActivityRepository.insertWithActions(planActivity);
            mode = RequestCodes.ACTIVITY_EDITED;
        }
        if(mode == RequestCodes.ACTIVITY_EDITED){
            ActivityRepository.updateWithActions(planActivity);
        }

    }

    public void saveTemplate(View v){

        try{
            saveActivity();
            Intent intent = new Intent();
            intent.putExtra("ACTIVITY", this.planActivity);
            setResult(mode , intent);
            super.finish();
        }catch(RuntimeException e){
            Toast.makeText(this, R.string.missing_title_field, Toast.LENGTH_SHORT).show();

        }catch(Exception e){
            Toast.makeText(this, R.string.activity_save_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCodes.ACTIVITY_MANAGEMENT && resultCode == RequestCodes.ACTIVITY_MANAGEMENT) {
            com.przyjaznyplan.models.Activity result = (com.przyjaznyplan.models.Activity) data.getExtras().get("ACTIVITY");
            refreshView(result.getId());
        }
        else if(requestCode == RequestCodes.FILE_CHOOSER_PIC && resultCode == -1){
            String fileSelected = data.getStringExtra("fileSelected");
            if(!("".equals(fileSelected))){
                 initPicture(fileSelected);
            }
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == RequestCodes.FILE_CHOOSER_SOUND && resultCode == -1){
            String fileSelected = data.getStringExtra("fileSelected");
            if(!("".equals(fileSelected))){
                try {
                    planActivity.setAudioPath(fileSelected.toString());
                    initSounds();
                }catch (Exception e){

                }
            }
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
        }
        else{
            refreshView(this.planActivity.getId());
        }

    }

    private void refreshView(String id) {
        this.planActivity = ActivityRepository.getActivityById(id);
        initActions();
        initPicture();
    }

    private void initActions() {
        TextView actions = (TextView) findViewById(R.id.activityNumber);
        actions.setText(this.planActivity.getSlides().size() + "");
    }

}
