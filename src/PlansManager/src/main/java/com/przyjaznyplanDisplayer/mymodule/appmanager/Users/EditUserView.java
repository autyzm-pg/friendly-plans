/*
 * Copyright (c) 2016. Wydział Elektroniki, Telekomunikacji i Informatyki, Politechnika Gdańska
 * This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or   (at your option) any later version.
 *
 * Copy of GNU General Public License is available at http://www.gnu.org/licenses/gpl-3.0.html
 */
package com.przyjaznyplanDisplayer.mymodule.appmanager.Users;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.przyjaznyplan.models.TypyWidokuAktywnosci;
import com.przyjaznyplan.models.TypyWidokuCzynnosci;
import com.przyjaznyplan.models.TypyWidokuPlanuAktywnosci;
import com.przyjaznyplan.models.User;
import com.przyjaznyplan.models.UserPreferences;
import com.przyjaznyplan.repositories.UserRepository;
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

import br.com.thinkti.android.filechooser.FileChooser;

import static com.przyjaznyplan.models.TypyWidokuAktywnosci.*;

public class EditUserView extends Activity {

    public static final String MP3_FILE_EXTENSION = ".mp3";
    private User user;
    private EditText nameField;
    private EditText surnameField;
    private TextView timerPath;
    private RadioGroup radioGroupPlanActivityTypeView;
    private RadioGroup radioGroupPlanActionTypeView;
    private RadioGroup radioGroupPlanActivityView;
    MediaPlayer player;

    enum ViewType { CREATE, EDIT }
    private ViewType actualViewType = ViewType.EDIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        user = (User)getIntent().getSerializableExtra("user");

        if(user == null) {
            createDefaultUser();
            actualViewType =  ViewType.CREATE;
        }

        initView();
        initHeader();
        initTimer();
        initNameSurnameFields();
        initPlanActivityTypeViewRadioButtons();
        initPlanActionTypeViewRadioButtons();
        initPlanTypeViewRadioButtons();

    }

    private void initHeader() {
        TextView header = (TextView) findViewById(R.id.titleEditView);
        if(actualViewType == ViewType.CREATE)
            header.setText(R.string.create_user_header);
        else
            header.setText(R.string.edit_user_header);
    }

    private void createDefaultUser() {
        user = new User();
        user.setName(getResources().getString(R.string.default_user_name));
        user.setSurname(getResources().getString(R.string.default_user_surname));
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setTimerSoundPath(getResources().getString(R.string.default_empty_timer_path));
        userPreferences.setTypyWidokuAktywnosci(TypyWidokuAktywnosci.big);
        userPreferences.setTypWidokuPlanuAtywnosci(TypyWidokuPlanuAktywnosci.list);
        userPreferences.setTypWidokuCzynnosci(TypyWidokuCzynnosci.advanced);
        user.setPreferences(userPreferences);
    }

    private void initPlanTypeViewRadioButtons(){

        UserPreferences preferences = user.getPreferences();
        TypyWidokuPlanuAktywnosci type = preferences.getTypWidokuPlanuAtywnosci();
        radioGroupPlanActivityView = (RadioGroup)findViewById(R.id.planTypeRadioGroup);
        ((RadioButton)radioGroupPlanActivityView.getChildAt(0)).setChecked(true);

        switch(type){
            case list:
                setRadioButton(radioGroupPlanActivityView.findViewById(R.id.listPlanTypeRadioButton),true);return;
            case slide:
                setRadioButton(radioGroupPlanActivityView.findViewById(R.id.slidePlanTypeRadioButton),true);return;
        }


    }

    private void initView() {
        setContentView(R.layout.edituserview);
    }

    private void initTimer(){
        timerPath = (TextView)findViewById(R.id.pathToTimer);
        String timerSoundPath = user.getPreferences().getTimerSoundPath();

        if(!timerSoundPath.equals(R.string.default_empty_timer_path)){
            Button listenButton = (Button)findViewById(R.id.listenButton);
            listenButton.setVisibility(View.VISIBLE);
            timerPath.setText(timerSoundPath);
        }
    }

    private void initPlanActionTypeViewRadioButtons(){
        UserPreferences preferences = user.getPreferences();
        TypyWidokuCzynnosci type = preferences.getTypWidokuCzynnosci();
        radioGroupPlanActionTypeView = (RadioGroup)findViewById(R.id.planActionTypeRadioGroup);

        switch(type){
            case basic:
                setRadioButton(radioGroupPlanActionTypeView.findViewById(R.id.basicPlanActionTypeRadioButton),true);return;
            case advanced:
                setRadioButton(radioGroupPlanActionTypeView.findViewById(R.id.advancedPlanActionTypeRadioButton),true);return;
        }
    }

    private void initPlanActivityTypeViewRadioButtons() {
        UserPreferences preferences = user.getPreferences();
        TypyWidokuAktywnosci typ = preferences.getTypyWidokuAktywnosci();
        radioGroupPlanActivityTypeView = (RadioGroup)findViewById(R.id.planActivityTypeRadioGroup);

        switch(typ){
            case small:
                setRadioButton(radioGroupPlanActivityTypeView.findViewById(R.id.smallPlanActivityTypeRadioButton),true); return;
            case medium:
                setRadioButton(radioGroupPlanActivityTypeView.findViewById(R.id.mediumPlanActivityTypeRadioButton),true); return;
            case big:
                setRadioButton(radioGroupPlanActivityTypeView.findViewById(R.id.bigPlanActivityTypeRadioButton),true); return;
        }
    }

    private void setRadioButton(View viewById, boolean value) {
        if(viewById instanceof RadioButton){
            RadioButton button = (RadioButton)viewById;
            button.setChecked(value);
        }
    }

    private void initNameSurnameFields() {
        nameField = (EditText)findViewById(R.id.editName);
        surnameField = (EditText)findViewById(R.id.editSurname);
        nameField.setText(user.getName());
        surnameField.setText(user.getSurname());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void chooseTimerPath(View view) {

        Intent intent = new Intent(this, FileChooser.class);
        ArrayList<String> extensions = new ArrayList<String>();
        extensions.add(MP3_FILE_EXTENSION);
        intent.putExtra("fileStartPath", Environment.getExternalStorageDirectory());
        intent.putStringArrayListExtra("filterFileExtension", extensions);
        startActivityForResult(intent, RequestCodes.FILE_CHOOSER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode ==  RequestCodes.FILE_CHOOSER) && (resultCode == -1)) {
            String fileSelected = data.getStringExtra("fileSelected");
            timerPath.setText(fileSelected);
            if(!fileSelected.equals(R.string.default_empty_timer_path)){
                setTimerPath();
                Button listenButton = (Button)findViewById(R.id.listenButton);
                listenButton.setVisibility(View.VISIBLE);
            }
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
        }
    }

    public void saveEdition(View view) {
        setNameSurname();
        setTypeViewsPreferences();
        setTimerPath();

        if(actualViewType == ViewType.EDIT){

              try {
                  user = UserRepository.updateUser(user);
                  Toast.makeText(this, R.string.edit_user_success, Toast.LENGTH_LONG).show();
                  finish();
              }
              catch (Exception ex) {
                  Toast.makeText(this, R.string.edit_user_error, Toast.LENGTH_LONG).show();
              }
        }
        else if(actualViewType == ViewType.CREATE){

            try {
                user  = UserRepository.insertUser(user);
                Toast.makeText(this, getString(R.string.create_user_success) + user.getName() + " " + user.getSurname(), Toast.LENGTH_LONG).show();
                finish();
            }
            catch(Exception ex) {
                Toast.makeText(this, R.string.create_user_error, Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(this, R.string.operation_execute_error, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(player!=null && player.isPlaying()){
            player.stop();
            player.reset();
        }
    }

    private void setTimerPath() {
        if(!timerPath.getText().equals(R.string.default_empty_timer_path)) {
            user.getPreferences().setTimerSoundPath(timerPath.getText().toString());
        }
    }

    private void setTypeViewsPreferences() {

        int activityTypeView = radioGroupPlanActivityTypeView.getCheckedRadioButtonId();

        switch (activityTypeView){
            case R.id.smallPlanActivityTypeRadioButton:
                user.getPreferences().setTypyWidokuAktywnosci(small);break;
            case R.id.mediumPlanActivityTypeRadioButton:
                user.getPreferences().setTypyWidokuAktywnosci(medium);break;
            case R.id.bigPlanActivityTypeRadioButton:
                user.getPreferences().setTypyWidokuAktywnosci(big);break;
        }

        int czynnoscTypeView = radioGroupPlanActionTypeView.getCheckedRadioButtonId();

        switch (czynnoscTypeView){
            case R.id.basicPlanActionTypeRadioButton:
                user.getPreferences().setTypWidokuCzynnosci(TypyWidokuCzynnosci.basic);break;
            case R.id.advancedPlanActionTypeRadioButton:
                user.getPreferences().setTypWidokuCzynnosci(TypyWidokuCzynnosci.advanced);break;
        }

        int planActivityView = radioGroupPlanActivityView.getCheckedRadioButtonId();

        switch(planActivityView){
            case R.id.listPlanTypeRadioButton:
                user.getPreferences().setTypWidokuPlanuAtywnosci(TypyWidokuPlanuAktywnosci.list);
                break;
            case R.id.slidePlanTypeRadioButton:
                user.getPreferences().setTypWidokuPlanuAtywnosci(TypyWidokuPlanuAktywnosci.slide);
                break;
        }

    }

    private void setNameSurname() {
        user.setName(nameField.getText().toString());
        user.setSurname(surnameField.getText().toString());
    }

    public void listenTimer(View view) {

        Button listenButton = (Button)findViewById(R.id.listenButton);

        if(player == null || ((player!=null)&&!player.isPlaying())) {
            Uri uri = Uri.parse(user.getPreferences().getTimerSoundPath());
            player = new MediaPlayer();
            try {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(getApplicationContext(), uri);
                player.prepare();
                player.start();
                listenButton.setText(R.string.stop_timer);

            }catch (Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(player.isPlaying()){
            player.stop();
            player.reset();
            listenButton.setText(R.string.play_timer);
        }

    }
}
