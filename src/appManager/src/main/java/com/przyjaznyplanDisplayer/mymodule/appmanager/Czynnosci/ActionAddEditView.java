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
import android.widget.Toast;

import com.przyjaznyplan.models.Slide;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

import br.com.thinkti.android.filechooser.FileChooser;

public class ActionAddEditView extends Activity {
    int mode;
    private Slide slide;
    private int position;
    String pathToPicture="";
    MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.action_edit_view);
        if(getIntent().getExtras()!=null && getIntent().getExtras().get("SLIDE")!=null){
            mode= RequestCodes.SLIDE_EDITED;
            this.slide=(Slide)getIntent().getExtras().get("SLIDE");
            this.position = Integer.parseInt(getIntent().getExtras().get("POSITION").toString());
            if(this.slide.getImagePath()!=null && !this.slide.getImagePath().equals("")){
                setBMP(this.slide.getImagePath());
            }
            if(slide.getAudioPath()!=null && !slide.getAudioPath().equals("")){
                ImageView usunDzwiekIcon = (ImageView) (findViewById(R.id.imageView3));
                usunDzwiekIcon.setVisibility(View.VISIBLE);
            }
            EditText etName = (EditText) findViewById(R.id.editText);
            etName.setText(this.slide.getText());
            EditText etTime = (EditText) findViewById(R.id.editText2);
            etTime.setText(String.valueOf(this.slide.getTime()));
        } else {
            mode= RequestCodes.SLIDE_ADDED;
            this.slide = new Slide();
        }
    }

    public void setBMP(String pathToPicture){
        try {
            Bitmap bmp = BitmapFactory.decodeFile(pathToPicture);
            Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, 100, 100, false);
            ImageView activityImage = (ImageView) (findViewById(R.id.imageView));
            activityImage.setImageBitmap(scaledBmp);
            slide.setImagePath(pathToPicture);
            ImageView usunObrazIcon = (ImageView) (findViewById(R.id.imageView4));
            usunObrazIcon.setVisibility(View.VISIBLE);
        }catch (Exception e){

        }
    }

    public void ustawObraz(View v){
        //Intent intent = new Intent(this, FileDialogView.class);
        //startActivityForResult(intent,DODAJ_OBRAZ);
        Intent intent = new Intent(this, FileChooser.class);
        ArrayList<String> extensions = new ArrayList<String>();
        extensions.add(".jpg");
        extensions.add(".jpeg");
        extensions.add(".png");
        intent.putStringArrayListExtra("filterFileExtension", extensions);
        intent.putExtra("fileStartPath", Environment.getExternalStorageDirectory());
        startActivityForResult(intent, RequestCodes.FILE_CHOOSER_PIC);
    }

    public void ustawDzwiek(View v){
        //Intent intent = new Intent(this, FileDialogView.class);
        //startActivityForResult(intent,DODAJ_OBRAZ);
        Intent intent = new Intent(this, FileChooser.class);
        ArrayList<String> extensions = new ArrayList<String>();
        extensions.add(".mp3");
        intent.putStringArrayListExtra("filterFileExtension", extensions);
        intent.putExtra("fileStartPath", Environment.getExternalStorageDirectory());
        startActivityForResult(intent, RequestCodes.FILE_CHOOSER_SOUND);
    }

    public void usunObraz(View v) {
        slide.setImagePath(null);
        ImageView activityImage = (ImageView) (findViewById(R.id.imageView));
        activityImage.setImageResource(R.drawable.t1);
        ImageView usunObrazIcon = (ImageView) (findViewById(R.id.imageView4));
        usunObrazIcon.setVisibility(View.INVISIBLE);
    }

    public void usunDzwiek(View v){
        slide.setAudioPath(null);
        ImageView usunDzwiekIcon = (ImageView) (findViewById(R.id.imageView3));
        usunDzwiekIcon.setVisibility(View.INVISIBLE);
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

            }
        }
    }

    public void zapisz(View v){
        String name="";
        int time=0;
        EditText etName = (EditText) findViewById(R.id.editText);
        try {
            name = etName.getText().toString();
        } catch (Exception e){

        }
        EditText etTime = (EditText) findViewById(R.id.editText2);
        try {
            time = Integer.parseInt(etTime.getText().toString());
        } catch (Exception e){

        }
        try{
            if(name.length()!=0){
                slide.setText(name);
            } else
            {
                slide.setText("");
            }
            slide.setTime(time);
            Intent intent = new Intent();
            intent.putExtra("SLIDE", this.slide);
            if(mode== RequestCodes.SLIDE_EDITED){
                intent.putExtra("POSITION", this.position);
            }
            setResult(mode , intent);
            super.finish();
        } catch(Exception e){

        }
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
                    ImageView usunDzwiekIcon = (ImageView) (findViewById(R.id.imageView3));
                    usunDzwiekIcon.setVisibility(View.VISIBLE);
                }catch (Exception e){

                }
            }
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
        }
        }
}