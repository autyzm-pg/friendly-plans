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
import com.przyjaznyplanDisplayer.mymodule.appmanager.R;
import com.przyjaznyplanDisplayer.mymodule.appmanager.Utils.RequestCodes;

import java.util.ArrayList;

import br.com.thinkti.android.filechooser.FileChooser;

import static com.przyjaznyplan.models.TypyWidokuAktywnosci.big;
import static com.przyjaznyplan.models.TypyWidokuAktywnosci.medium;
import static com.przyjaznyplan.models.TypyWidokuAktywnosci.small;

public class CreateUserView extends Activity {

    private TextView timerPath;
    private RadioGroup radioGroupCzynnoscTypeView;
    private RadioGroup radioGroupActivityTypeView;
    private RadioGroup radioGroupPlanActivityView;

    private EditText nameField;
    private EditText surnameField;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        initView();
        initNameSurnameFields();
        initActivityTypeViewRadioButtons();
        initCzynnoscTypeViewRadioButtons();
        initPlanTypewViewRadioButtons();

    }

    private void initView() {
        setContentView(R.layout.createuserview);
        timerPath = (TextView)findViewById(R.id.c_edit_pathToTimer);
    }

    private void initPlanTypewViewRadioButtons(){
        radioGroupPlanActivityView = (RadioGroup)findViewById(R.id.c_rbg_planTypeView);
        ((RadioButton)radioGroupPlanActivityView.getChildAt(0)).setChecked(true);
    }

    private void initCzynnoscTypeViewRadioButtons() {

        radioGroupCzynnoscTypeView = (RadioGroup)findViewById(R.id.c_rbg_czynnoscTypeView);
        ((RadioButton)radioGroupCzynnoscTypeView.getChildAt(0)).setChecked(true);
    }

    private void initActivityTypeViewRadioButtons() {

        radioGroupActivityTypeView = (RadioGroup)findViewById(R.id.c_rb_aktywnoscTypeView);
        ((RadioButton)radioGroupActivityTypeView.getChildAt(0)).setChecked(true);
    }

    private void initNameSurnameFields() {
        nameField = (EditText)findViewById(R.id.c_edit_imie);
        surnameField = (EditText)findViewById(R.id.c_edit_nazwisko);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void addUserDB(View view) {

        User user = new User();
        user.setName(nameField.getText().toString());
        user.setSurname(surnameField.getText().toString());
        UserPreferences preferences = new UserPreferences();
        preferences.setTimerSoundPath(timerPath.getText().toString());
        user.setPreferences(preferences);


        int activityTypeView = radioGroupActivityTypeView.getCheckedRadioButtonId();

        switch (activityTypeView){
            case R.id.c_rb_smallView:
                user.getPreferences().setTypyWidokuAktywnosci(small);break;
            case R.id.c_rb_mediumView:
                user.getPreferences().setTypyWidokuAktywnosci(medium);break;
            case R.id.c_rb_bigView:
                user.getPreferences().setTypyWidokuAktywnosci(big);break;
        }


        int czynnoscTypeView = radioGroupCzynnoscTypeView.getCheckedRadioButtonId();

        switch (czynnoscTypeView){
            case R.id.c_rb_basicView:
                user.getPreferences().setTypWidokuCzynnosci(TypyWidokuCzynnosci.basic);break;
            case R.id.c_rb_advancedView:
                user.getPreferences().setTypWidokuCzynnosci(TypyWidokuCzynnosci.advanced);break;
        }

        int planActivityView = radioGroupPlanActivityView.getCheckedRadioButtonId();

        switch(planActivityView){
            case R.id.c_rb_planListView:
                user.getPreferences().setTypWidokuPlanuAtywnosci(TypyWidokuPlanuAktywnosci.list);
                break;
            case R.id.c_rb_planSlideView:
                user.getPreferences().setTypWidokuPlanuAtywnosci(TypyWidokuPlanuAktywnosci.slide);
                break;
        }


        Intent intent = new Intent();
        intent.putExtra("newUser",user);
        setResult(RESULT_OK, intent);


        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(player!=null && player.isPlaying()){
            player.stop();
            player.reset();
        }
    }

    public void chooseTimerPathCreate(View view) {

        Intent intent = new Intent(this, FileChooser.class);
        ArrayList<String> extensions = new ArrayList<String>();
        extensions.add(".mp3");
        intent.putStringArrayListExtra("filterFileExtension", extensions);
        intent.putExtra("fileStartPath", Environment.getExternalStorageDirectory());
        startActivityForResult(intent, RequestCodes.FILE_CHOOSER);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(player!=null && player.isPlaying()){
            player.stop();
            player.reset();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode ==  RequestCodes.FILE_CHOOSER) && (resultCode == -1)) {
            String fileSelected = data.getStringExtra("fileSelected");
            timerPath.setText(fileSelected);
            if(!("".equals(fileSelected))){

                Button odsluchajBtn = (Button)findViewById(R.id.c_odsluchajBtn);
                odsluchajBtn.setVisibility(View.VISIBLE);
            }
            Toast.makeText(this, fileSelected, Toast.LENGTH_SHORT).show();
        }
    }

    public void listenTimer(View view) {

        Button odsluchajBtn = (Button)findViewById(R.id.c_odsluchajBtn);

        if(player == null || ((player!=null)&&!player.isPlaying())) {
            Uri uri = Uri.parse(timerPath.getText().toString());
            player = new MediaPlayer();
            try {
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDataSource(getApplicationContext(), uri);
                player.prepare();
                player.start();
                odsluchajBtn.setText("Stop");

            }catch (Exception e){
                System.out.println(e.getMessage());
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else if(player.isPlaying()){
            player.stop();
            player.reset();
            odsluchajBtn.setText("Odsłuchaj");
        }

    }
}
