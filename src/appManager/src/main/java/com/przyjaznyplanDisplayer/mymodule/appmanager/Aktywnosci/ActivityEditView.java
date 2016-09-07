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
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.przyjaznyplan.DbHelper.MySQLiteHelper;
import com.przyjaznyplan.dao.ActivityDao;
import com.przyjaznyplan.dto.ActivityDto;
import com.przyjaznyplan.repositories.ActivityRepository;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Czynnosci.ActionListView;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

import br.com.thinkti.android.filechooser.FileChooser;

public class ActivityEditView extends Activity {
    private String pathToPicture;
    private com.przyjaznyplan.models.Activity planActivity;
    private MediaPlayer mp;
    private ActivityDao activityDao;
    private int mode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_view);
        if(getIntent().getExtras()!=null && getIntent().getExtras().get("ACTIVITY")!=null){
            mode = RequestCodes.ACTIVITY_EDITED;
            this.planActivity = (com.przyjaznyplan.models.Activity)getIntent().getExtras().get("ACTIVITY");
            if(this.planActivity.getIconPath()!=null && !this.planActivity.getIconPath().equals("")){
                setBMP(this.planActivity.getIconPath());
            }
            if(planActivity.getAudioPath()!=null && !planActivity.getAudioPath().equals("")){
                ImageView removeSoundIcon = (ImageView) (findViewById(R.id.imageView3));
                removeSoundIcon.setVisibility(View.VISIBLE);
            }
            EditText etName = (EditText) findViewById(R.id.editText);
            etName.setText(this.planActivity.getTitle());
            EditText etTime = (EditText) findViewById(R.id.editText2);
            etTime.setText(String.valueOf(this.planActivity.getTime()));
            TextView tv = (TextView) findViewById(R.id.textView5);
            if(this.planActivity.getSlides()==null){
                tv.setText("0");
            } else {
                tv.setText(this.planActivity.getSlides().size() + "");
            }
        }
        else {
            mode = RequestCodes.ACTIVITY_ADDED;
            this.planActivity = new com.przyjaznyplan.models.Activity();
        }
        try {
            activityDao = new ActivityDao(MySQLiteHelper.getDb());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setBMP(String pathToPicture){
        try {
            Bitmap bmp = BitmapFactory.decodeFile(pathToPicture);
            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);
            ImageView activityImage = (ImageView) (findViewById(R.id.imageView));
            activityImage.setImageBitmap(scaledBmp);
            planActivity.setIconPath(pathToPicture);
            ImageView usunObrazIcon = (ImageView) (findViewById(R.id.imageView4));
            usunObrazIcon.setVisibility(View.VISIBLE);
        }catch (Exception e){
            pathToPicture = "";
            planActivity.setIconPath(pathToPicture);
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
        planActivity.setIconPath(null);
        ImageView activityImage = (ImageView) (findViewById(R.id.imageView));
        activityImage.setImageResource(R.drawable.t1);
        ImageView usunObrazIcon = (ImageView) (findViewById(R.id.imageView4));
        usunObrazIcon.setVisibility(View.INVISIBLE);
    }

    public void removeSound(View v){
        planActivity.setAudioPath(null);
        ImageView usunDzwiekIcon = (ImageView) (findViewById(R.id.imageView3));
        usunDzwiekIcon.setVisibility(View.INVISIBLE);
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

        EditText etName = (EditText) findViewById(R.id.editText);
        this.planActivity.setTitle(etName.getText().toString());

        EditText etTime = (EditText) findViewById(R.id.editText2);
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
            super.finish();
        } catch(Exception e){
            Toast.makeText(this, R.string.activity_save_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RequestCodes.ACTIVITY_MANAGEMENT && resultCode == RequestCodes.ACTIVITY_MANAGEMENT) {
            com.przyjaznyplan.models.Activity result = (com.przyjaznyplan.models.Activity) data.getExtras().get("ACTIVITY");
            this.planActivity = ActivityRepository.getActivityById(result.getId());
            TextView tv = (TextView) findViewById(R.id.textView5);
            tv.setText(this.planActivity.getSlides().size() + "");
        }
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
                    planActivity.setAudioPath(fileSelected.toString());
                    ImageView usunDzwiekIcon = (ImageView) (findViewById(R.id.imageView3));
                    usunDzwiekIcon.setVisibility(View.VISIBLE);
                }catch (Exception e){

                }
            }
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
        }
    }

}
